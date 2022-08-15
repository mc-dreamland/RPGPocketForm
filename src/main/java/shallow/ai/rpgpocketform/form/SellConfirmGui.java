package shallow.ai.rpgpocketform.form;

import com.ayou.peformapi.builder.AbstractFormBuilder;
import com.ayou.peformapi.builder.WindowFormBuilder;
import com.ayou.peformapi.windowform.ModalWindow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.form.ModalForm;
import org.geysermc.floodgate.api.FloodgateApi;
import protocolsupportpocketstuff.api.modals.callback.ModalWindowCallback;
import shallow.ai.rpgpocketform.config.ConfigHandler;
import shallow.ai.rpgpocketform.hook.VaultHook;
import su.nightexpress.quantumrpg.QuantumRPG;
import su.nightexpress.quantumrpg.modules.sell.SellManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SellConfirmGui {

    public SellConfirmGui(Player player, List<ItemStack> sellItemStacks) {
        this.player = player;
        this.sellItemStacks = sellItemStacks;
    }

    Player player;

    public ModalForm buildForm(Player player){
        this.player = player;
        ModalForm.Builder customForm = ModalForm.builder().title(ConfigHandler.getConfig().getString("sell.confirm.title", ""));
        customForm.content(ConfigHandler.getConfig().getString("sell.confirm.content", ""));
        customForm.button1(ConfigHandler.getConfig().getString("sell.confirm.true", ""));
        customForm.button2(ConfigHandler.getConfig().getString("sell.confirm.false", ""));
        return customForm.build();
    }

    public void addResponseHandler(CustomForm.Builder form){
        AtomicInteger i = new AtomicInteger();
        form.validResultHandler(response -> {
            if (!response.hasNext()) {
                return;
            }
            i.getAndIncrement();
            SellManager sellManager = QuantumRPG.getInstance().getModule(SellManager.class);
            double sellPrice = 0.0;
            for (ItemStack itemStack : sellItemStacks){
                if (player.getInventory().contains(itemStack)) {
                    sellPrice += sellManager.calcCost(itemStack);
                    player.getInventory().remove(itemStack);
                    System.out.println("sellPrice = " + sellPrice);
                }
                else {
                    player.sendMessage(ConfigHandler.getConfig().getString("general.illegal"));
                    return;
                }
            }
            // 给了
            VaultHook.depositBalance(player, sellPrice);
        });
    }

    private final List<ItemStack> sellItemStacks;

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

}
