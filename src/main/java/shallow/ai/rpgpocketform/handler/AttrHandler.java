package shallow.ai.rpgpocketform.handler;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;
import org.bukkit.entity.Player;

public class AttrHandler {
    public static int getAttributePoints(Player player){
        return getPlayerData(player).getAttributePoints();
    }

    public static void upAttribute(String attribute, Player player){
        getPlayerData(player).upAttribute(attribute);
    }

    public static PlayerData getPlayerData(Player player){
        return SkillAPI.getPlayerData(player);
    }
}
