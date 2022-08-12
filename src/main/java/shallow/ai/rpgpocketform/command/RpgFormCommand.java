package shallow.ai.rpgpocketform.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;
import shallow.ai.rpgpocketform.form.QuestGui;
import shallow.ai.rpgpocketform.form.SellGui;

public class RpgFormCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) return false;
        if (sender instanceof Player){
            switch (args[0]) {
                case "sell":
                    SellGui s = new SellGui();
                    FloodgateApi.getInstance().sendForm(((Player) sender).getUniqueId(), s.buildForm(((Player) sender).getPlayer()));
                    return true;
                case "repair":
                    System.out.println("sender = " + sender);
                    return true;
                case "attr":
                    System.out.println("label = " + label);
                    return true;
                case "quest":
                    QuestGui q = new QuestGui("","","","");
                    return true;
            }
        }
        return false;
    }
}
