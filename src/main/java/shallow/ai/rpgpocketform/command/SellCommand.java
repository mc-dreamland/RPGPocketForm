package shallow.ai.rpgpocketform.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import shallow.ai.rpgpocketform.form.SellGui;

public class SellCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) return false;
        if (sender instanceof Player){
            if (args[0].equals("sell")){
                new SellGui().sendModal((Player) sender);
            }
        }
        return false;
    }
}
