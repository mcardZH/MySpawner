package cn.minezone.myspawners.handlers.subcommands;

import cn.minezone.myspawners.MySpawnersPlugin;
import cn.minezone.myspawners.api.Spawner;
import cn.minezone.myspawners.handlers.SubCommand;
import cn.minezone.myspawners.handlers.LanguageHandler;
import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mcard
 */
public class SubGuiList implements SubCommand {

    private MySpawnersPlugin plugin;

    public SubGuiList(MySpawnersPlugin plugin) {

        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) throws CommandException {
        if (!(commandSender instanceof Player)) {
            throw new CommandException(LanguageHandler.getLanguage("command.sender.must.be.a.player"));
        }
        int page = 1;
        if (args.length != 0) {
            if (!isInt(args[0])) {
                throw new CommandException(LanguageHandler.getLanguage("arguments.is.not.int"));
            }
            page = Integer.parseInt(args[0]);
            if (page <= 0) {
                throw new CommandException(LanguageHandler.getLanguage("arguments.must.bigger.than.x")
                        .replace("{x}", "0"));
            }
        }
        int enables = 0;
        for (Spawner s : plugin.getManager().getSpawners()) {
            if (s.isEnable()) {
                enables += 1;
            }
        }
        int maxPage = (int) Math.ceil(((double) enables / 54));
        if (maxPage < page) {
            throw new CommandException(LanguageHandler.getLanguage("arguments.must.less.than.x")
                    .replace("{x}", maxPage + ""));
        }
        /*
            最大容器尺寸
         */
        int maxInventorySize = 54;
        Inventory inv = Bukkit.createInventory((Player) commandSender, maxInventorySize, LanguageHandler.getLanguage("inventory.gui.title")
                .replace("{page}", page + "").replace("{max-page}", maxPage + ""));
        int t = (page - 1) * 54;

        List<Spawner> spawners = new ArrayList<>(plugin.getManager().getSpawners());
        for (int i = 0; i <= maxInventorySize && (i + t) < plugin.getManager().getSpawners().size(); i++) {
            NBTItem ni = new NBTItem(spawners.get(i + t).getIcon());
            ni.setString("spawner", spawners.get(i + t).getName());
            inv.setItem(i, ni.getItem());
        }
        ((Player) commandSender).openInventory(inv);
        return true;
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
        return null;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(getPermission());
    }

    @Override
    public String getPermission() {
        return "myspawners.command.gui";
    }

    @Override
    public int getMinimumArguments() {
        return 0;
    }

    @Override
    public int getMaximumArguments() {
        return 1;
    }

    @Override
    public String getName() {
        return "gui";
    }

    @Override
    public String getHelp() {
        return LanguageHandler.getLanguage("sub.command.gui.gethelp");
    }

    @Override
    public List<String> getHelps() {
        return LanguageHandler.getLanguages("sub.command.gui.gethelps");
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
