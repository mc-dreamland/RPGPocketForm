package shallow.ai.rpgpocketform;

import org.bukkit.plugin.java.JavaPlugin;
import shallow.ai.rpgpocketform.command.RpgFormCommand;
import shallow.ai.rpgpocketform.hook.VaultHook;

public final class RPGPocketForm extends JavaPlugin {
    private static RPGPocketForm instance;

    public static RPGPocketForm getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        if (!VaultHook.setupEconomy() ) {
            getLogger().severe("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        saveDefaultConfig();
        reloadConfig();
        this.getServer().getPluginCommand("rpgform").setExecutor(new RpgFormCommand());
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
