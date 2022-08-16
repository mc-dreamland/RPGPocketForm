package shallow.ai.rpgpocketform.form;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.form.ModalForm;
import shallow.ai.rpgpocketform.RPGPocketForm;
import shallow.ai.rpgpocketform.handler.ConfigHandler;
import shallow.ai.rpgpocketform.hook.VaultHook;
import su.nightexpress.quantumrpg.QuantumRPG;
import su.nightexpress.quantumrpg.modules.sell.SellManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SellConfirmGui {

    private Player player;
    private final List<ItemStack> sellItemStacks;
    private final SellManager sellManager;

    public SellConfirmGui(Player player, List<ItemStack> sellItemStacks) {
        this.player = player;
        this.sellItemStacks = sellItemStacks;
        sellManager = QuantumRPG.getInstance().getModule(SellManager.class);
    }



    public ModalForm buildForm(Player player){
        this.player = player;
        ModalForm.Builder modalform = ModalForm.builder().title(ConfigHandler.getConfig().getString("sell.confirm.title", ""));
        modalform.button1(ConfigHandler.getConfig().getString("sell.confirm.true", ""));
        modalform.button2(ConfigHandler.getConfig().getString("sell.confirm.false", ""));
        double sellPrice = 0.0;
        for (ItemStack itemStack : sellItemStacks){
            if (player.getInventory().contains(itemStack)) {
                sellPrice += sellManager.calcCost(itemStack);
            }
        }
        modalform.content(ConfigHandler.getConfig().getString("sell.confirm.content", "") + " " + sellPrice);
        addResponseHandler(modalform);
        return modalform.build();
    }

    public void addResponseHandler(ModalForm.Builder form){
        AtomicInteger i = new AtomicInteger();
        form.validResultHandler(response -> {
            i.getAndIncrement();
            double sellPrice = 0D;
            for (ItemStack itemStack : sellItemStacks){
                if (player.getInventory().contains(itemStack)) {
                    sellPrice += sellManager.calcCost(itemStack);
                    player.getInventory().remove(itemStack);
                }
                else {
                    player.sendMessage(ConfigHandler.getConfig().getString("general.illegal"));
                    sellPrice = 0D;
                    return;
                }
            }
            // 给了
            player.sendMessage(ConfigHandler.getConfig().getString("sell.success") + sellPrice + " 金币");
            VaultHook.depositBalance(player, sellPrice);
        });
    }


    public List<ItemStack> getPlayerItem(Player player){
        List<ItemStack> itemStacks = new ArrayList<>();
        PlayerInventory inv = player.getInventory();
        for (int i = 0; i <= 35; i++){
            if (inv.getItem(i) == null) continue;
            if (!inv.getItem(i).hasItemMeta()) continue;
            if (!inv.getItem(i).getItemMeta().hasLore()) continue;
            if (sellManager.calcCost(inv.getItem(i)) <= 0) continue;
            if (inv.getItem(i).getItemMeta().getLore().contains("§c不可出售")) continue;
            itemStacks.add(inv.getItem(i));
        }
        return itemStacks;
    }

}
