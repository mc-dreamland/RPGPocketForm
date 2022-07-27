package shallow.ai.rpgpocketform.hook;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {
    private static Economy econ = null;
    public static boolean setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static double getBalance(Player player){
        return getEconomy().getBalance(player);
    }

    public static void withdrawBalance(Player player, double v) {
        getEconomy().withdrawPlayer(player, v);
    }

    public static void depositBalance(Player player, double v) {
        getEconomy().depositPlayer(player, v);
    }
}
