package cn.minezone.myspawners.handlers.subcommands;

import cn.minezone.myspawners.MySpawnersPlugin;
import cn.minezone.myspawners.api.Spawner;
import cn.minezone.myspawners.handlers.LanguageHandler;
import cn.minezone.myspawners.handlers.SubCommand;
import cn.minezone.myspawners.util.PlayerInputHook;
import cn.minezone.myspawners.util.PlayerInputUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mcard
 */
public class SubEditSpawner implements SubCommand, PlayerInputHook {


    private MySpawnersPlugin main;

    public SubEditSpawner(MySpawnersPlugin main) {

        this.main = main;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) throws CommandException {
        if (!(commandSender instanceof Player)) {
            throw new CommandException(LanguageHandler.getLanguage("command.sender.must.be.a.player"));
            //检查指令发送者是否为玩家
        }
        Player p = (Player) commandSender;
        if (!main.getManager().checkName(args[0])) {
            throw new CommandException(LanguageHandler.getLanguage("sub.command.editspawner.uncraete"));
            //检查此刷怪笼是否已创建
        }
        if ("icon".equalsIgnoreCase(args[1]) && args.length == 2) {
            if (p.getItemInHand() == null) {
                throw new CommandException(LanguageHandler.getLanguage("sub.command.editspawner.no.item"));
                //检查手里是否有物品
            }
            for (Spawner spawner : main.getManager().getSpawners()) {
                if (spawner.getName().equals(args[0])) {
                    ItemStack item = p.getItemInHand().clone();
                    item.setAmount(1);
                    spawner.setIcon(item);
                    //设置物品
                    main.getManager().saveAllSpawner();
                    throw new CommandException(LanguageHandler.getLanguage("sub.command.editspawner.set.success"));
                    //设置成功
                }
            }
        }
        if ("name".equalsIgnoreCase(args[1]) && args.length == 3) {
            for (Spawner spawner : main.getManager().getSpawners()) {
                if (spawner.getName().equals(args[0])) {
                    main.getManager().getConfig().set(spawner.getName(), null);
                    main.getManager().saveConfig(true);
                    spawner.setName(args[2]);
                    main.getManager().saveAllSpawner();
                    throw new CommandException(LanguageHandler.getLanguage("sub.command.editspawner.set.success"));
                    //设置成功
                }
            }
        }
        if ("dn".equalsIgnoreCase(args[1]) && args.length == 3) {
            for (Spawner spawner : main.getManager().getSpawners()) {
                if (spawner.getName().equals(args[0])) {
                    spawner.setDisplayName(ChatColor.translateAlternateColorCodes('&', args[2]));
                    main.getManager().saveAllSpawner();
                    throw new CommandException(LanguageHandler.getLanguage("sub.command.editspawner.set.success"));
                    //设置成功
                }
            }
        }
        if ("addspawn".equalsIgnoreCase(args[1]) && args.length == 3) {
            try {
                EntityType.valueOf(args[2].toUpperCase());
            } catch (Exception e) {
                throw new CommandException(LanguageHandler.getLanguage("sub.command.editspawner.entitytype.error"));
                //找不到此生物
            }
            for (Spawner spawner : main.getManager().getSpawners()) {
                if (spawner.getName().equals(args[0])) {
                    spawner.addSpawns(EntityType.valueOf(args[2].toUpperCase()));
                    main.getManager().saveAllSpawner();
                    throw new CommandException(LanguageHandler.getLanguage("sub.command.editspawner.set.success"));
                    //设置成功
                }
            }
        }
        if ("addcmd".equalsIgnoreCase(args[1]) && args.length == 2) {
            for (Spawner spawner : main.getManager().getSpawners()) {
                if (spawner.getName().equals(args[0])) {
                    PlayerInputUtils.getPlayerNextChat(p, this);
                    throw new CommandException(LanguageHandler.getLanguage("sub.command.editspawner.input.tip")
                            .replace("{name}", args[0]));
                }
            }
        }

        if ("additem".equalsIgnoreCase(args[1]) && args.length == 2) {
            if (p.getItemInHand() == null) {
                throw new CommandException(LanguageHandler.getLanguage("sub.command.editspawner.no.item"));
                //检查手里是否有物品
            }
            for (Spawner spawner : main.getManager().getSpawners()) {
                if (spawner.getName().equals(args[0])) {
                    spawner.addItemStack(p.getItemInHand());
                    main.getManager().saveAllSpawner();
                    throw new CommandException(LanguageHandler.getLanguage("sub.command.editspawner.set.success"));
                    //设置成功
                }
            }
        }
        if ("settick".equalsIgnoreCase(args[1]) && args.length == 3) {
            if (!isInt(args[2])) {
                throw new CommandException(LanguageHandler.getLanguage("arguments.is.not.int"));
            }
            if (Integer.parseInt(args[2]) < 1) {
                throw new CommandException(LanguageHandler.getLanguage("arguments.must.bigger.than.x")
                        .replace("{x}", "1"));
            }
            for (Spawner spawner : main.getManager().getSpawners()) {
                if (spawner.getName().equals(args[0])) {
                    spawner.setTicks(Integer.parseInt(args[2]));
                    main.getManager().saveAllSpawner();
                    throw new CommandException(LanguageHandler.getLanguage("sub.command.editspawner.set.success"));
                    //设置成功
                }
            }
        }
        if ("setpr".equalsIgnoreCase(args[1]) && args.length == 3) {
            if (!isInt(args[2])) {
                throw new CommandException(LanguageHandler.getLanguage("arguments.is.not.int"));
            }
            if (Integer.parseInt(args[2]) < 1) {
                throw new CommandException(LanguageHandler.getLanguage("arguments.must.bigger.than.x")
                        .replace("{x}", "1"));
            }
            for (Spawner spawner : main.getManager().getSpawners()) {
                if (spawner.getName().equals(args[0])) {
                    spawner.setPlayerRadius(Integer.parseInt(args[2]));
                    main.getManager().saveAllSpawner();
                    throw new CommandException(LanguageHandler.getLanguage("sub.command.editspawner.set.success"));
                    //设置成功
                }
            }
        }
        if ("setr".equalsIgnoreCase(args[1]) && args.length == 3) {
            if (!isInt(args[2])) {
                throw new CommandException(LanguageHandler.getLanguage("arguments.is.not.int"));
            }
            if (Integer.parseInt(args[2]) < 1) {
                throw new CommandException(LanguageHandler.getLanguage("arguments.must.bigger.than.x")
                        .replace("{x}", "1"));
            }
            for (Spawner spawner : main.getManager().getSpawners()) {
                if (spawner.getName().equals(args[0])) {
                    spawner.setRadius(Integer.parseInt(args[2]));
                    main.getManager().saveAllSpawner();
                    throw new CommandException(LanguageHandler.getLanguage("sub.command.editspawner.set.success"));
                    //设置成功
                }
            }
        }
        if ("enable".equalsIgnoreCase(args[1]) && args.length == 2) {
            for (Spawner spawner : main.getManager().getSpawners()) {
                if (spawner.getName().equals(args[0])) {
                    spawner.setEnable(true);
                    main.getManager().saveAllSpawner();
                    throw new CommandException(LanguageHandler.getLanguage("sub.command.editspawner.set.success"));
                    //设置成功
                }
            }
        }
        if ("disable".equalsIgnoreCase(args[1]) && args.length == 2) {
            for (Spawner spawner : main.getManager().getSpawners()) {
                if (spawner.getName().equals(args[0])) {
                    spawner.setEnable(false);
                    main.getManager().saveAllSpawner();
                    throw new CommandException(LanguageHandler.getLanguage("sub.command.editspawner.set.success"));
                    //设置成功
                }
            }
        }
        if (args[1].equalsIgnoreCase("type") && args.length == 3) {
            try {
                EntityType.valueOf(args[2].toUpperCase());
            } catch (Exception e) {
                throw new CommandException(LanguageHandler.getLanguage("sub.command.editspawner.entitytype.error"));
                //找不到此生物
            }
            for (Spawner spawner : main.getManager().getSpawners()) {
                if (spawner.getName().equals(args[0])) {
                    spawner.setType(EntityType.valueOf(args[2].toUpperCase()));
                    main.getManager().saveAllSpawner();
                    throw new CommandException(LanguageHandler.getLanguage("sub.command.editspawner.set.success"));
                    //设置成功
                }
            }
        }
        throw new CommandException(LanguageHandler.getLanguage("arguments.error"));
//
//        PlayerInputUtils.getPlayerNextChat(p, this);
//        commandSender.sendMessage("请随便输入一句话，无需“/”");
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
            List<String> r = new ArrayList<>();
            for (Spawner spawner : main.getManager().getSpawners()) {
                r.add(spawner.getName());
            }
            return r;
        }

        if (args.length == 1) {
            return Arrays.asList("icon", "name", "dn", "addspawn", "addcmd", "additem", "settick", "setpr", "setr", "enable", "disable", "type");
        }
        if (args.length == 2 && "addspawn".equalsIgnoreCase(args[1])) {
            List<String> entitys = new ArrayList<>();
            for (EntityType value : EntityType.values()) {
                if (value.isSpawnable()) {
                    entitys.add(value.name());
                }
            }
            return entitys;
        }
        return null;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(getPermission());
    }

    @Override
    public String getPermission() {
        return "myspawners.command.editspawner";
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
        return "es";
    }

    @Override
    public String getHelp() {
        return LanguageHandler.getLanguage("sub.command.editspawner.gethelp");
    }

    @Override
    public List<String> getHelps() {
        return LanguageHandler.getLanguages("sub.command.editspawner.gethelps");
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

    @Override
    public void receiveChat(Player p, String msg) {
        for (Spawner spawner : main.getManager().getSpawners()) {
            if (msg.startsWith(spawner.getName() + " ")) {
                msg = msg.replace(spawner.getName() + " ", "");
                spawner.addCommand(msg);
                main.getManager().saveAllSpawner();
                p.sendMessage(LanguageHandler.getLanguage("sub.command.editspawner.set.success"));
                return;
            }
        }
        p.sendMessage(LanguageHandler.getLanguage("sub.command.editspawner.uncraete"));
    }
}
