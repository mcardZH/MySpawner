package cn.minezone.myspawners.handlers;


import cn.minezone.myspawners.MySpawnersPlugin;
import cn.minezone.myspawners.handlers.subcommands.*;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author mcard
 */
public class CommandHandler implements TabExecutor {

    private List<SubCommand> subCommands = new ArrayList<>();
    private MySpawnersPlugin plugin;
    private FileConfiguration config;

    public CommandHandler(MySpawnersPlugin plugin, FileConfiguration config) {
        this.plugin = plugin;
        this.config = config;
        regSubCommand(new SubHelp(this));
        regSubCommand(new SubList(plugin));
        regSubCommand(new SubGuiList(plugin));
        regSubCommand(new SubAddSpawner(plugin));
        regSubCommand(new SubEditSpawner(plugin));
        regSubCommand(new SubGive(plugin));


    }

    public void regSubCommand(SubCommand sub) {
        subCommands.add(sub);
    }

    public List<SubCommand> getSubCommands() {
        //将所有对象进行深拷贝，防止外界直接进行修改
        List<SubCommand> clone = new ArrayList<>();

        for (SubCommand sub : subCommands) {
            clone.add(sub.clone());
        }
        return clone;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            for (SubCommand sub : subCommands) {
                if (sub.hasPermission(sender)) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            config.getString("commands-settings.clearly-help")
                                    .replace("{sub-command-name}", sub.getName())
                                    .replace("{sub-command-help}", sub.getHelp())));
                }
            }
            return true;
        }
        for (SubCommand sub : subCommands) {
            if (!sub.getName().equalsIgnoreCase(args[0])) {
                continue;
            }
            if (sub.hasPermission(sender)) {
                if (sub.getMinimumArguments() > args.length - 1) {
                    sender.sendMessage(LanguageHandler.getLanguage("command.dont.have.enough.arguments")
                            .replace("{arguments}", args.length - 1 + "")
                            .replace("{min-arguments}", sub.getMinimumArguments() + ""));
                    return true;
                }
                if (sub.getMaximumArguments() < args.length - 1) {
                    sender.sendMessage(LanguageHandler.getLanguage("command.too.many.arguments")
                            .replace("{arguments}", args.length - 1 + "")
                            .replace("{max-arguments}", sub.getMaximumArguments() + ""));
                    return true;
                }
                String[] arg = new String[args.length - 1];

                IntStream.range(1, args.length).forEach(i -> arg[i - 1] = args[i]);
                try {
                    if (sub.execute(sender, arg)) {
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                config.getString("commands-settings.clearly-help")
                                        .replace("{sub-command-name}", sub.getName())
                                        .replace("{sub-command-help}", sub.getHelp())));
                    }
                    return true;
                } catch (CommandException e) {
                    sender.sendMessage(e.getMessage());
                    return true;
                }
            } else {
                sender.sendMessage(LanguageHandler.getLanguage("command.dont.have.permission"));
                return true;
            }
        }
        sender.sendMessage(LanguageHandler.getLanguage("command.cant.find.this.sub.command"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> subs = new ArrayList<>();
            StringBuilder tip = new StringBuilder();
            for (SubCommand sub : subCommands) {
                if (sub.hasPermission(sender)) {
                    tip.append(!sub.equals(subCommands.get(subCommands.size() - 1)) ? sub.getName() + "," : sub.getName());
                    subs.add(sub.getName());
                }
            }
            sender.sendMessage(tip.toString());
            return subs;
        }
        for (SubCommand sub : subCommands) {
            if (sub.hasPermission(sender)) {
                if (!sub.getName().equalsIgnoreCase(args[0])) {
                    continue;
                }
                if (args.length <= 2) {
                    return sub.onTabComplete(sender, null);
                    //调用tab完成方法;
                }
                String[] arg = new String[args.length - 2];

                System.arraycopy(args, 1, arg, 0, args.length - 1 - 1);
                return sub.onTabComplete(sender, arg);
                //调用tab完成方法
            }
        }
        return null;
    }

}