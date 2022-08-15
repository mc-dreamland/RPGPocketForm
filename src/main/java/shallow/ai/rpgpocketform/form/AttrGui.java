package shallow.ai.rpgpocketform.form;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.manager.AttributeManager;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.Form;
import org.geysermc.cumulus.form.SimpleForm;
import shallow.ai.rpgpocketform.config.ConfigHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AttrGui {
    private final Player p;
    private final HashMap<String, AttributeManager.Attribute> attr;

    public AttrGui(Player p) {
        this.attr = SkillAPI.getAttributeManager().getAttributes();
        this.p = p;
    }

    public Form build(){
        return SimpleForm.builder()
                .title(ConfigHandler.getConfig().getString("attr.title"))
                .content(ConfigHandler.getConfig().getString("attr.content") + getAttributePoints(p)).build();

    }

    public void initButtons(SimpleForm.Builder builder){
        for (String attrName : attr.keySet()){
            builder.button(attr.get(attrName).getName()
                    + "\n§a"
                    + SkillAPI.getPlayerData(p).getAttribute(attr.get(attrName).getName()));
        }
    }

    public int getAttributePoints(Player player){
        return SkillAPI.getPlayerData(player).getAttributePoints();
    }

    public void giveAttribute(String attribute, Player player){
        SkillAPI.getPlayerData(player).giveAttribute(attribute,1);
    }
}
