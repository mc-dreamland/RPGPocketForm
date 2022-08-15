package shallow.ai.rpgpocketform.form;

import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.ModalForm;
import shallow.ai.rpgpocketform.handler.ConfigHandler;

@Deprecated
public class QuestGui {
    private ModalForm.Builder formBuilder;
    private Player player;


    public QuestGui(Player player, String content, String title, String button1, String button2) {
        this.player = player;
        formBuilder = ModalForm.builder()
                .title(title)
                .content(content)
                .button1(button1)
                .button2(button2);
    }

    public ModalForm.Builder getFormBuilder() {
        return formBuilder;
    }
}
