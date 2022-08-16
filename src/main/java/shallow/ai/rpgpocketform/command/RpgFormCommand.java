package shallow.ai.rpgpocketform.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.ModalForm;
import org.geysermc.floodgate.api.FloodgateApi;
import shallow.ai.rpgpocketform.RPGPocketForm;
import shallow.ai.rpgpocketform.form.QuestGui;
import shallow.ai.rpgpocketform.form.SellGui;
import shallow.ai.rpgpocketform.form.TaskGui;
import shallow.ai.rpgpocketform.handler.AttrHandler;
import shallow.ai.rpgpocketform.handler.ConfigHandler;

public class RpgFormCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) return false;
        if (sender instanceof Player || sender instanceof ConsoleCommandSender){
            switch (args[0]) {
                case "sell":
                    SellGui s = new SellGui();
                    FloodgateApi.getInstance().sendForm(((Player) sender).getUniqueId(), s.buildForm(((Player) sender).getPlayer()));
                    return true;
                case "addattr":
                    if (args.length == 2){
                            if (AttrHandler.getAttributePoints((Player) sender) > 0){
                                // 执行加点
                                AttrHandler.upAttribute(args[1], (Player) sender);

                                sender.sendMessage("§a加点成功");
                                return true;
                            }
                            sender.sendMessage("点数不足");
                            return false;
                    }
                    return false;
                case "reload":
                    if (sender instanceof ConsoleCommandSender || sender.isOp()) {
                        RPGPocketForm.getInstance().reloadConfig();
                        sender.sendMessage(ConfigHandler.getConfig().getString("general.reloaded"));
                        return true;
                    }
                    return false;
            }
        }
        return false;
    }
}
