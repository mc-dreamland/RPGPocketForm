package shallow.ai.rpgpocketform.form;

import com.ayou.peformapi.AbstractGui;
import com.ayou.peformapi.builder.AbstractFormBuilder;
import com.ayou.peformapi.builder.ComplexFormBuilder;
import com.ayou.peformapi.complexform.ComplexFormElement;
import com.ayou.peformapi.complexform.ComplexFormInit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.geysermc.cumulus.component.Component;
import org.geysermc.cumulus.component.util.ComponentType;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.form.Form;
import org.geysermc.cumulus.form.ModalForm;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.cumulus.response.FormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.cumulus.util.glue.CustomFormGlue;
import protocolsupport.libs.com.google.gson.JsonArray;
import protocolsupportpocketstuff.api.modals.callback.ComplexFormCallback;
import protocolsupportpocketstuff.api.modals.elements.complex.ModalLabel;
import protocolsupportpocketstuff.api.modals.elements.complex.ModalToggle;
import shallow.ai.rpgpocketform.config.ConfigHandler;
import su.nightexpress.quantumrpg.QuantumRPG;
import su.nightexpress.quantumrpg.modules.sell.SellManager;

import java.util.ArrayList;
import java.util.List;

public class SellGui implements CustomForm {

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public @Nullable FormImage icon() {
        return null;
    }

    @Override
    public @NonNull List<@Nullable Component> content() {
        return null;
    }

    @Override
    public @NonNull String title() {
        return null;
    }

    Player player;

    public CustomForm buildForm(Player player){
        this.player = player;
       CustomForm.Builder customForm = CustomForm.builder().title(ConfigHandler.getConfig().getString("sell.title", ""));
       customForm.label(ConfigHandler.getConfig().getString("sell.context", ""));
       addToggle(customForm);
       addResponseHandler(customForm);
        return customForm.build();
    }


    public void addToggle(CustomForm.Builder form) {
        for (ItemStack itemStack : getPlayerItem(player)){
            form.toggle(itemStack.getItemMeta().getDisplayName());
        }
    }


    public void addResponseHandler(CustomForm.Builder form){
        List<ItemStack> sellItemStacks = new ArrayList<>();
        form.(customFormResponse -> {
            System.out.println(customFormResponse.next().toString());
            }
        );
    }
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
                                new ModalLabel(ConfigHandler.getConfig().getString("sell.context", "")))
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
                    }
                };
            }
        });
    }

}
