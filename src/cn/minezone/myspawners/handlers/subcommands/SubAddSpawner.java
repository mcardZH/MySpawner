package cn.minezone.myspawners.handlers.subcommands;

import cn.minezone.myspawners.MySpawnersPlugin;
import cn.minezone.myspawners.api.Spawner;
import cn.minezone.myspawners.api.SpawnerManager;
import cn.minezone.myspawners.handlers.LanguageHandler;
import cn.minezone.myspawners.handlers.SubCommand;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @author mcard
 */
public class SubAddSpawner implements SubCommand {

    private MySpawnersPlugin plugin;

    public SubAddSpawner(MySpawnersPlugin plugin) {

        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) throws CommandException {
        if (plugin.getManager().checkName(args[0])) {
            throw new CommandException(LanguageHandler.getLanguage("sub.command.addspawner.repetition"));
            //重复名称
        }
        if (plugin.getManager().addSpawner(new Spawner(args[0]))) {
            throw new CommandException(LanguageHandler.getLanguage("sub.command.addspawner.create.success"));
            //创建成功
        } else {
            throw new CommandException(LanguageHandler.getLanguage("sub.command.addspawner.create.fail"));
            //创建失败
        }
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
        return "myspawners.command.addspawner";
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
        return "as";
    }

    @Override
    public String getHelp() {
        return LanguageHandler.getLanguage("sub.command.addspawner.gethelp");
    }

    @Override
    public List<String> getHelps() {
        return LanguageHandler.getLanguages("sub.command.addspawner.gethelps");
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
