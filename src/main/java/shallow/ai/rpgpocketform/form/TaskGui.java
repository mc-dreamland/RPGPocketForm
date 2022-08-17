package shallow.ai.rpgpocketform.form;

import com.handy.playertask.api.PlayerTaskApi;
import com.handy.playertask.entity.TaskList;
import com.handy.playertask.entity.TaskPlayer;
import com.handy.playertask.lib.core.DateUtil;
import com.handy.playertask.lib.util.BaseUtil;
import com.handy.playertask.param.PlayerTaskParam;
import com.handy.playertask.service.TaskPlayerService;
import com.handy.playertask.util.TaskUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.form.SimpleForm;
import shallow.ai.rpgpocketform.handler.ConfigHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskGui {

    Player player;

    public TaskGui() {
    }

    public SimpleForm buildForm(Player player){
        this.player = player;
        SimpleForm.Builder simpleForm = SimpleForm.builder().title(ConfigHandler.getConfig().getString("task.title", ""));
        simpleForm.content(ConfigHandler.getConfig().getString("task.content", ""));
            List<TaskPlayer> taskPlayers = TaskPlayerService.getInstance().findByPlayer(player.getName(), DateUtil.getToday());
            if (!taskPlayers.isEmpty()) {
                int todayTaskNum = TaskUtil.getTodayTaskNum();
                if (todayTaskNum >= 1) {
                    List<Long> todayTaskIndexList = TaskUtil.getTodayTaskIndexList();
                    if (todayTaskIndexList.size() >= 1 && todayTaskIndexList.size() == todayTaskNum) {
                        for(int i = 0; i < todayTaskNum; ++i) {
                            TaskPlayer taskPlayer = taskPlayers.get(i);
                            simpleForm.button(ChatColor.translateAlternateColorCodes('&', taskPlayer.getTaskName()) + (taskPlayer.getStatus() ? " §a已完成" : " §c未完成")
                                    + "\n"
                                    + ChatColor.translateAlternateColorCodes('&', taskPlayer.getDescription() == null ? "" : taskPlayer.getDescription())
                            );
                        }
                    }
                }
        }
        addResponseHandler(simpleForm);
        return simpleForm.build();
    }

    public void addResponseHandler(SimpleForm.Builder form){
        form.validResultHandler(response -> {
            Bukkit.dispatchCommand(player, "plk open");
        });
    }
}
