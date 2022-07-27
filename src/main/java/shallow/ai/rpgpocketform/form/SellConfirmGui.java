package shallow.ai.rpgpocketform.form;

import com.ayou.peformapi.AbstractGui;
import com.ayou.peformapi.builder.AbstractFormBuilder;
import com.ayou.peformapi.builder.WindowFormBuilder;
import com.ayou.peformapi.complexform.ComplexFormElement;
import com.ayou.peformapi.windowform.ModalWindow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import protocolsupportpocketstuff.api.modals.callback.ModalWindowCallback;
import protocolsupportpocketstuff.api.modals.elements.complex.ModalToggle;
import shallow.ai.rpgpocketform.config.ConfigHandler;
import shallow.ai.rpgpocketform.hook.VaultHook;
import su.nightexpress.quantumrpg.QuantumRPG;
import su.nightexpress.quantumrpg.modules.sell.SellManager;

import java.util.ArrayList;
import java.util.List;

public class SellConfirmGui extends AbstractGui {
    private final List<ItemStack> sellItemStacks;

    public SellConfirmGui(List<ItemStack> sellItemStacks) {
        this.sellItemStacks = sellItemStacks;
    }

    public List<ItemStack> getPlayerItem(Player player){
        List<ItemStack> itemStacks = new ArrayList<>();
        PlayerInventory inv = player.getInventory();
        for (int i = 0; i <= 35; i++){
            if (inv.getItem(i) == null) continue;
            if (!inv.getItem(i).hasItemMeta()) continue;
            if (!inv.getItem(i).getItemMeta().hasLore()) continue;
            itemStacks.add(inv.getItem(i));
        }
        return itemStacks;
    }

    @Override
    public AbstractFormBuilder builder(Player player) {
        return WindowFormBuilder.builder().init(new ModalWindow() {
            @Override
            public String[] contexts() {
                return ConfigHandler.getConfig().getStringList("sell.confirm.contexts").toArray(new String[0]);
            }

            @Override
            public String title() {
                return ConfigHandler.getConfig().getString("sell.confirm.title");
            }

            @Override
            public String trueButtonText() {
                return ConfigHandler.getConfig().getString("sell.confirm.true");
            }

            @Override
            public String falseButtonText() {
                return ConfigHandler.getConfig().getString("sell.confirm.false");
            }

            @Override
            public ModalWindowCallback callback() {
                return new ModalWindowCallback() {
                    @Override
                    public void onModalWindowResponse(Player player, String s, boolean b, boolean b1) {
                        if (b) return;
                        if (!b1) return;
                        SellManager sellManager = QuantumRPG.getInstance().getModule(SellManager.class);
                        double sellPrice = 0.0;
                        for (ItemStack itemStack : sellItemStacks){
                            if (player.getInventory().contains(itemStack)) {
                                sellPrice += sellManager.calcCost(itemStack);
                                player.getInventory().remove(itemStack);
                                // TODO: 给钱
                                System.out.println("sellPrice = " + sellPrice);
                            }
                            else {
                                player.sendMessage(ConfigHandler.getConfig().getString("general.illegal"));
                                return;
                            }
                        }
                        // 给予
                        VaultHook.depositBalance(player, sellPrice);

                    }
                };
            }
        });
    }
}
