package shallow.ai.rpgpocketform.form;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.floodgate.api.FloodgateApi;
import shallow.ai.rpgpocketform.handler.ConfigHandler;
import su.nightexpress.quantumrpg.QuantumRPG;
import su.nightexpress.quantumrpg.modules.sell.SellManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SellGui {


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
        AtomicInteger i = new AtomicInteger();
        form.validResultHandler(response -> {
            if (!response.hasNext()) {
                return;
            }
            i.getAndIncrement();

            if ((Boolean)response.next()){
                sellItemStacks.add(getPlayerItem(player).get(i.get()));
                FloodgateApi.getInstance().sendForm(player.getUniqueId(), new SellConfirmGui(player, sellItemStacks).buildForm(player));
            }
        });
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

}
