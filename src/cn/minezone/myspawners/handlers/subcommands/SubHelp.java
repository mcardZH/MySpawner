package cn.minezone.myspawners.handlers.subcommands;

import cn.minezone.myspawners.handlers.SubCommand;
import cn.minezone.myspawners.handlers.CommandHandler;
import cn.minezone.myspawners.handlers.LanguageHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mcard
 */
public class SubHelp implements SubCommand {

    private CommandHandler dad;

    public SubHelp(CommandHandler commandHandler) {
        this.dad = commandHandler;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) throws CommandException {
        if (dad.getSubCommands() == null || dad.getSubCommands().isEmpty()) {
            commandSender.sendMessage(LanguageHandler.getLanguage("command.cant.find.any.sub.commands"));
        }
        for (SubCommand sub : dad.getSubCommands()) {
            if (!sub.getName().equalsIgnoreCase(args[0])) {
                continue;
            }
            if (!sub.hasPermission(commandSender)) {
                commandSender.sendMessage(LanguageHandler.getLanguage("command.dont.have.permissions.get.help")
                        .replace("{sub-command-name}", sub.getName()));
                continue;
            }
            for (String temp : LanguageHandler.getConfig().getStringList("commands-settings.fully-help.start")) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', temp));
            }
            for (String help : sub.getHelps()) {
                commandSender.sendMessage(help);
            }
            for (String temp : LanguageHandler.getConfig().getStringList("commands-settings.fully-help.end")) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', temp));
            }
            return true;
        }
        commandSender.sendMessage(LanguageHandler.getLanguage("command.cant.find.this.sub.command"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] args) {
        List<String> sb = new ArrayList<>();
        for (SubCommand sub : dad.getSubCommands()) {
            if (sub.hasPermission(commandSender)) {
                sb.add(sub.getName());
            }
        }
        return sb;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return true;
    }

    @Override
    public String getPermission() {
        return "";
    }

    @Override
    public int getMinimumArguments() {
        return 1;
    }

    @Override
    public int getMaximumArguments() {
        return 1;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return LanguageHandler.getLanguage("sub.command.help.gethelp");
    }

    @Override
    public List<String> getHelps() {
        return LanguageHandler.getLanguages("sub.command.help.gethelps");
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
