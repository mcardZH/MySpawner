package cn.minezone.myspawners.handlers.subcommands;

import cn.minezone.myspawners.MySpawnersPlugin;
import cn.minezone.myspawners.api.Spawner;
import cn.minezone.myspawners.handlers.LanguageHandler;
import cn.minezone.myspawners.handlers.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @author mcard
 */
public class SubList implements SubCommand {

    private MySpawnersPlugin plugin;

    public SubList(MySpawnersPlugin plugin) {

        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) throws CommandException {
        /*
         * 启用
         */
        int en = 0;
        /*
         * 禁用
         */
        int dis = 0;
        for (Spawner sp : plugin.getManager().getSpawners()) {
            /*
             * 计数
             */
            if (sp.isEnable()) {
                en += 1;
            } else {
                dis += 1;
            }
            commandSender.sendMessage(sp.getName() + ChatColor.GREEN + "(" + ChatColor.RESET +
                    sp.getDisplayName()
                    + ChatColor.RESET + "" + ChatColor.GREEN + ")  " +
                    (sp.isEnable() ? ChatColor.GREEN + "" + sp.isEnable() : ChatColor.RED + "" + sp.isEnable()));
        }
        commandSender.sendMessage(LanguageHandler.getLanguage("sub.command.list.all")
                .replace("{num1}", en + dis + "")
                .replace("{num2}", en + "").replace("{num3}", dis + ""));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] args) {
        return null;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(getPermission());
    }

    @Override
    public String getPermission() {
        return "myspawners.command.list";
    }

    @Override
    public int getMinimumArguments() {
        return 0;
    }

    @Override
    public int getMaximumArguments() {
        return 0;
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getHelp() {
        return LanguageHandler.getLanguage("sub.command.list.gethelp");
    }

    @Override
    public List<String> getHelps() {
        return LanguageHandler.getLanguages("sub.command.list.gethelps");
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
