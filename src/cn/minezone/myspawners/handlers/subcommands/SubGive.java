package cn.minezone.myspawners.handlers.subcommands;

import cn.minezone.myspawners.MySpawnersPlugin;
import cn.minezone.myspawners.api.Spawner;
import cn.minezone.myspawners.handlers.LanguageHandler;
import cn.minezone.myspawners.handlers.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mcard
 */
public class SubGive implements SubCommand {

    private MySpawnersPlugin main;

    public SubGive(MySpawnersPlugin main) {

        this.main = main;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) throws CommandException {
        if (!main.getManager().checkName(args[1])) {
            throw new CommandException(LanguageHandler.getLanguage("sub.command.give.uncraete"));
        }
        int amount = 1;
        if (args.length == 3 && !isInt(args[2])) {
            throw new CommandException(LanguageHandler.getLanguage("arguments.is.not.int"));
        }
        if (args.length == 3) {
            amount = Integer.parseInt(args[2]);
        }
        if (args[0].equalsIgnoreCase("@a")) {
            ItemStack item = main.getManager().getSpawnerItem(main.getManager().getSpawner(args[1]), amount);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.getInventory().addItem(item);
            }
            throw new CommandException(LanguageHandler.getLanguage("sub.command.give.success"));
        } else {
            Player p = Bukkit.getPlayerExact(args[0]);
            if (p == null || !p.isOnline()) {
                throw new CommandException(LanguageHandler.getLanguage("arguments.player.no.online"));
            }
            p.getInventory().addItem(main.getManager().getSpawnerItem(main.getManager().getSpawner(args[1]), amount));
            throw new CommandException(LanguageHandler.getLanguage("sub.command.give.success"));
        }
    }

    private boolean isInt(String num) {
        try {
            Integer.parseInt(num);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] args) {
        if (args == null || args.length == 0) {
            return null;
        }
        if (args.length == 1) {
            List<String> s = new ArrayList<>();
            for (Spawner spawner : main.getManager().getSpawners()) {
                s.add(spawner.getName());
            }
            return s;
        }
        return null;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(getPermission());
    }

    @Override
    public String getPermission() {
        return "myspawners.command.give";
    }

    @Override
    public int getMinimumArguments() {
        return 2;
    }

    @Override
    public int getMaximumArguments() {
        return 3;
    }

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public String getHelp() {
        return LanguageHandler.getLanguage("sub.command.give.gethelp");
    }

    @Override
    public List<String> getHelps() {
        return LanguageHandler.getLanguages("sub.command.give.gethelps");
    }

    @Override
    public SubCommand clone() {
        try {
            return (SubCommand) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
