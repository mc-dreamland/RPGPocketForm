package shallow.ai.rpgpocketform.form;

import com.ayou.peformapi.AbstractGui;
import com.ayou.peformapi.builder.AbstractFormBuilder;
import com.ayou.peformapi.builder.SimpleFormBuilder;
import com.ayou.peformapi.simpleform.ISimpleFormInit;
import com.ayou.peformapi.simpleform.SimpleFormButton;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.manager.AttributeManager;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.cumulus.form.Form;
import org.geysermc.cumulus.form.ModalForm;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;
import protocolsupportpocketstuff.api.modals.elements.simple.ModalButton;
import shallow.ai.rpgpocketform.config.ConfigHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AttrGui {

    public Form build(){
        return SimpleForm.builder()
                .title("")
                .content("test").build();

    }

    public AbstractFormBuilder builder(Player player) {
        return SimpleFormBuilder.builder().init(new ISimpleFormInit() {
            @Override
            public String title() {
                return ConfigHandler.getConfig().getString("attr.title");
            }

            @Override
            public String context() {
                return ConfigHandler.getConfig().getString("attr.context") + "\n" + ConfigHandler.getConfig().getString("attr.context_value") + getAttributePoints(player);
            }

            @Override
            public List<SimpleFormButton> initButtons() {
                HashMap<String, AttributeManager.Attribute> attr = SkillAPI.getAttributeManager().getAttributes();
                ArrayList<SimpleFormButton> simpleFormButtons = new ArrayList<>();
                for (String attrName : attr.keySet()){
                    simpleFormButtons.add(
                            new SimpleFormButton(
                                    new ModalButton(attr.get(attrName).getName()), p->{
                                        if (getAttributePoints(p) < 1) {
                                            player.sendMessage(ConfigHandler.getConfig().getString("attr.deny"));
                                            return;
                                        }
                                        giveAttribute(attrName,p);
                    }));
                }
                return simpleFormButtons;
            }
        });
    }

    public int getAttributePoints(Player player){
        return SkillAPI.getPlayerData(player).getAttributePoints();
    }

    public void giveAttribute(String attribute, Player player){
        SkillAPI.getPlayerData(player).giveAttribute(attribute,1);
    }

    public @NonNull String title() {
        return null;
    }
}
