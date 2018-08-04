package cn.minezone.myspawners;

import cn.minezone.myspawners.api.Spawner;
import cn.minezone.myspawners.api.SpawnerManager;
import cn.minezone.myspawners.handlers.CommandHandler;
import cn.minezone.myspawners.listener.InventoryListener;
import cn.minezone.myspawners.util.PlayerInputUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

/**
 * @author mcard
 */
public class MySpawnersPlugin extends JavaPlugin {
    private final String[] ad = new String[]{
            randomColor() + "┏━━━━━━━━━━━━━━━━━━━━━━━━━┓",
            randomColor() + "┃  MySpawners 1.0Snapshot ┃",
            randomColor() + "┃      Power by mcard     ┃",
            randomColor() + "┗━━━━━━━━━━━━━━━━━━━━━━━━━┛",
            randomColor() + " 接插件定制，QQ:1459974942",
            randomColor() + " 省时、省钱、安全放心。"
    };

    private SpawnerManager manager;
    private CommandHandler handler;

    @Override
    public void onEnable() {
        //初始化
        //注册可序列化的类
        ConfigurationSerialization.registerClass(Spawner.class);
        //保存默认配置
        saveDefaultConfig();

        //初始化插件数据统计
        new Metrics(this);

        if (getConfig().getBoolean("start-ad", true)) {
            Bukkit.getConsoleSender().sendMessage(ad);
        }
        saveConfig();

        manager = SpawnerManager.getManagerInstance(this);

        handler = new CommandHandler(this, getConfig());

        Bukkit.getPluginCommand("myspawner").setExecutor(handler);
        Bukkit.getPluginCommand("myspawner").setTabCompleter(handler);

        //注册监听器
        Bukkit.getPluginManager().registerEvents(new PlayerInputUtils(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
    }


    @Override
    public void onDisable() {
        manager.saveConfig(false);
    }

    public static String randomColor() {
        String[] color = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "e", "f"};
        return ChatColor.COLOR_CHAR + color[new Random().nextInt(color.length - 1)];
    }

    public SpawnerManager getManager() {
        return manager;
    }

    public CommandHandler getHandler() {
        return handler;
    }
}
