package shallow.ai.rpgpocketform.form;

import com.ayou.peformapi.AbstractGui;
import com.ayou.peformapi.builder.AbstractFormBuilder;
import com.ayou.peformapi.builder.ComplexFormBuilder;
import com.ayou.peformapi.complexform.ComplexFormElement;
import com.ayou.peformapi.complexform.ComplexFormInit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import protocolsupport.libs.com.google.gson.JsonArray;
import protocolsupportpocketstuff.api.modals.callback.ComplexFormCallback;
import protocolsupportpocketstuff.api.modals.elements.complex.ModalLabel;
import protocolsupportpocketstuff.api.modals.elements.complex.ModalToggle;
import shallow.ai.rpgpocketform.config.ConfigHandler;
import su.nightexpress.quantumrpg.QuantumRPG;
import su.nightexpress.quantumrpg.modules.sell.SellManager;

import java.util.ArrayList;
import java.util.List;

public class SellGui extends AbstractGui {
    private final SellManager sellManager;

    public SellGui() {
        sellManager = QuantumRPG.getInstance().getModule(SellManager.class);
    }

    public List<ItemStack> getPlayerItem(Player player){
        List<ItemStack> itemStacks = new ArrayList<>();
        PlayerInventory inv = player.getInventory();
        for (int i = 0; i <= 35; i++){
            if (inv.getItem(i) == null) continue;
            if (!inv.getItem(i).hasItemMeta()) continue;
            if (!inv.getItem(i).getItemMeta().hasLore()) continue;
            if (sellManager.calcCost(inv.getItem(i)) <= 0) continue;
            itemStacks.add(inv.getItem(i));
        }
        return itemStacks;
    }

    @Override
    public AbstractFormBuilder builder(Player player) {
        return ComplexFormBuilder.builder().init(new ComplexFormInit() {
            @Override
            public String title() {
                return ConfigHandler.getConfig().getString("sell.title", "");
            }

            @Override
            public List<ComplexFormElement> initElements() {
                ArrayList<ComplexFormElement> elementArrayList = new ArrayList<>();
                elementArrayList.add(
                        new ComplexFormElement(()->
                                new ModalLabel(ConfigHandler.getConfig().getString("sell.description", "")))
                );

                for (ItemStack itemStack : getPlayerItem(player)){
                    elementArrayList.add(
                            new ComplexFormElement(()->
                                    new ModalToggle(itemStack.getItemMeta().getDisplayName()))
                            );
                }

                return elementArrayList;
            }

            @Override
            public ComplexFormCallback callback() {
                return new ComplexFormCallback() {
                    @Override
                    public void onComplexFormResponse(Player player, String s, boolean quit, JsonArray jsonArray) {
                        if (quit) return;
                        if (jsonArray.size() == 1) {
                            player.sendMessage(ConfigHandler.getConfig().getString("general.no_item", ""));
                            return;
                        }
                        List<ItemStack> sellItemStacks = new ArrayList<>();
                        for (int i = 0; i < jsonArray.size(); i++){
                            if (jsonArray.get(i).isJsonNull()) continue;
                            if (jsonArray.get(i).getAsBoolean()){
                                sellItemStacks.add(getPlayerItem(player).get(i - 1));
                            }
                        }
                        new SellConfirmGui(sellItemStacks).sendModal(player);
                    }
                };
            }
        });
    }

}
