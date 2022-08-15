package shallow.ai.rpgpocketform.form;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.geysermc.cumulus.component.ButtonComponent;
import org.geysermc.cumulus.form.ModalForm;
import org.geysermc.cumulus.form.SimpleForm;

import java.util.List;

public class QuestGui implements ModalForm {

    private String content,title,button1,button2;

    public QuestGui(String content, String title, String button1, String button2) {
        this.content = content;
        this.title = title;
        this.button1 = button1;
        this.button2 = button2;
    }


    @Override
    public @NonNull String content() {
        return content;
    }

    @Override
    public @NonNull String button1() {
        return button1;
    }

    @Override
    public @NonNull String button2() {
        return button2;
    }

    @Override
    public @NonNull String title() {
        return title;
    }

}
