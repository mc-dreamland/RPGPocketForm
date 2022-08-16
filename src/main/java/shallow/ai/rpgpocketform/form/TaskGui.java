package shallow.ai.rpgpocketform.form;

import com.handy.playertask.PlayerTask;
import com.handy.playertask.api.PlayerTaskApi;
import com.handy.playertask.entity.TaskList;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.form.ModalForm;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.floodgate.api.FloodgateApi;
import shallow.ai.rpgpocketform.handler.ConfigHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskGui {

    Player player;

    public SimpleForm buildForm(Player player){
        this.player = player;
        SimpleForm.Builder simpleForm = SimpleForm.builder().title(ConfigHandler.getConfig().getString("task.title", ""));
        simpleForm.content(ConfigHandler.getConfig().getString("task.content", ""));
        for (int id : ConfigHandler.getConfig().getIntegerList("task.id")){
            TaskList taskList = PlayerTaskApi.getInstance().findDetailByTaskId(id);
            simpleForm.button(taskList.getTaskName() + taskList.getTaskDemand()
                    + "\n"
                    + taskList.getTaskRewards()
            );
        }
        return simpleForm.build();
    }

    public void addResponseHandler(CustomForm.Builder form){
        List<ItemStack> sellItemStacks = new ArrayList<>();
        AtomicInteger i = new AtomicInteger();
        form.validResultHandler(response -> {

        });
    }
}
