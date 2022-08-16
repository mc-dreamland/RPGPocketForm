package shallow.ai.rpgpocketform.form;

import com.handy.playertask.api.PlayerTaskApi;
import com.handy.playertask.entity.TaskList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.form.SimpleForm;
import shallow.ai.rpgpocketform.handler.ConfigHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Deprecated
public class TaskGui {

    Player player;

    public TaskGui() {
    }

    public SimpleForm buildForm(Player player){
        this.player = player;
        SimpleForm.Builder simpleForm = SimpleForm.builder().title(ConfigHandler.getConfig().getString("task.title", ""));
        simpleForm.content(ConfigHandler.getConfig().getString("task.content", ""));
        for (int id : ConfigHandler.getConfig().getIntegerList("task.id")){
            TaskList taskList = PlayerTaskApi.getInstance().findDetailByTaskId(id);
            if (taskList != null ) {
                simpleForm.button(ChatColor.translateAlternateColorCodes('&', taskList.getTaskName()) + (taskList.getStatus() ? " §a未完成" : " §c已完成")
                        + "\n"
                        + ChatColor.translateAlternateColorCodes('&', taskList.getDescription() == null ? "" : taskList.getDescription())
                );
            }
        }
        addResponseHandler(simpleForm);
        return simpleForm.build();
    }

    public void addResponseHandler(SimpleForm.Builder form){
        form.validResultHandler(response -> {

        });
    }
}
