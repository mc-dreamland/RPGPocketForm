package shallow.ai.rpgpocketform.form;

import com.ayou.peformapi.AbstractGui;
import com.ayou.peformapi.builder.AbstractFormBuilder;
import com.ayou.peformapi.builder.WindowFormBuilder;
import com.ayou.peformapi.windowform.IModalWindow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import protocolsupportpocketstuff.api.modals.callback.ModalWindowCallback;
import shallow.ai.rpgpocketform.RPGPocketForm;
import shallow.ai.rpgpocketform.config.ConfigHandler;
import shallow.ai.rpgpocketform.hook.VaultHook;
import su.nightexpress.quantumrpg.QuantumRPG;
import su.nightexpress.quantumrpg.api.ItemAPI;
import su.nightexpress.quantumrpg.modules.repair.RepairManager;
import su.nightexpress.quantumrpg.modules.sell.SellManager;

public class RepairGui extends AbstractGui {

    private final RepairManager repairManager;

    public RepairGui() {
        repairManager = QuantumRPG.getInstance().getModule(RepairManager.class);
    }

    @Override
    public AbstractFormBuilder builder(Player player) {
        return WindowFormBuilder.builder().init(new IModalWindow() {
            @Override
            public String title() {
                return ConfigHandler.getConfig().getString("repair.title", "");
            }

            @Override
            public String context() {
                return ConfigHandler.getConfig().getString("repair.context", "");
            }

            @Override
            public String trueButtonText() {
                return ConfigHandler.getConfig().getString("repair.confirm", "");
            }

            @Override
            public String falseButtonText() {
                return ConfigHandler.getConfig().getString("repair.cancel", "");
            }

            @Override
            public ModalWindowCallback callback() {
                return new ModalWindowCallback() {
                    @Override
                    public void onModalWindowResponse(Player player, String s, boolean quit, boolean btn) {
                        if (quit) return;
                        if (!btn) return;
                        // true 按钮执行逻辑
                        ItemStack itemStack = player.getInventory().getItemInMainHand();
                        if (itemStack == null || !itemStack.hasItemMeta() || !itemStack.getItemMeta().hasLore()) return;
                        if (!ItemAPI.isDamaged(itemStack)) return;
                        double repairPrice = repairManager.calcCost(itemStack, RepairManager.RepairType.VAULT);
                        // 修复不足没钱
                        if (VaultHook.getBalance(player) < repairPrice) {
                            player.sendMessage(ConfigHandler.getConfig().getString("general.no_money", ""));
                            return;
                        }
                        // 扣除
                        VaultHook.withdrawBalance(player, repairPrice);
                        int max = ItemAPI.getDurabilityMinOrMax(itemStack, 1);
                        // 修复
                        ItemAPI.setDurability(itemStack, max, max);

                    }
                };
            }
        });
    }
}
