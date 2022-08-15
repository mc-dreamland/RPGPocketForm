package shallow.ai.rpgpocketform.handler;

import org.bukkit.configuration.file.FileConfiguration;
import shallow.ai.rpgpocketform.RPGPocketForm;

public class ConfigHandler {
    public static FileConfiguration getConfig(){
        return RPGPocketForm.getInstance().getConfig();
    }
}
