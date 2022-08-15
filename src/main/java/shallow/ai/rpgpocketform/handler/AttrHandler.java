package shallow.ai.rpgpocketform.handler;

import com.sucy.skill.SkillAPI;
import org.bukkit.entity.Player;

public class AttrHandler {
    public static int getAttributePoints(Player player){
        return SkillAPI.getPlayerData(player).getAttributePoints();
    }

    public static void giveAttribute(String attribute, Player player){
        SkillAPI.getPlayerData(player).giveAttribute(attribute,1);
    }
}
