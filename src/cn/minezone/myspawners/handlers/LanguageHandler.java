package cn.minezone.myspawners.handlers;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LanguageHandler {

    private static YamlConfiguration config;
    private static YamlConfiguration main;

    public static String getLanguage(String key) {
        return getLanguage(key, null);
    }

    public static String getLanguage(String key, OfflinePlayer p) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(Bukkit.getPluginManager().getPlugin("MySpawners").getDataFolder(), "config.yml"));
        String lang = config.getString("lang", "zh_CN") + ".yml";
        main = config;
        if (LanguageHandler.config == null) {
            File f = new File(Bukkit.getPluginManager().getPlugin("MySpawners").getDataFolder(), "languages/" + lang);
            if (!f.exists()) {
                Bukkit.getPluginManager().getPlugin("MySpawners").saveResource("languages/" + lang, true);
            }
            LanguageHandler.config = YamlConfiguration.loadConfiguration(f);
        }
        String langs = LanguageHandler.config.getString(key, "");
        if (langs.equals("")) {
            langs = YamlConfiguration.loadConfiguration(Bukkit.getPluginManager().getPlugin("MySpawners")
                    .getResource("languages/" + lang)).getString(key, key);//硬盘内配置文件没有就从jar内部读取
        }
        return ChatColor.translateAlternateColorCodes('&', langs);
    }

    public static List<String> getLanguages(String key) {
        return getLanguages(key, null);
    }

    public static List<String> getLanguages(String key, OfflinePlayer p) {
        if (config == null) {
            config = YamlConfiguration.loadConfiguration(new File(Bukkit.getPluginManager().getPlugin("MySpawners").getDataFolder(), "config.yml"));
            String lang = config.getString("lang", "zh_CN") + ".yml";
            File f = new File(Bukkit.getPluginManager().getPlugin("MySpawners").getDataFolder(), "languages/" + lang);
            if (!f.exists()) {
                Bukkit.getPluginManager().getPlugin("MySpawners").saveResource("languages/" + lang, true);
            }
            config = YamlConfiguration.loadConfiguration(f);
        }
        List<String> list = new ArrayList<>();
        for (String msg : config.getStringList(key)) {
            list.add(ChatColor.translateAlternateColorCodes('&', msg));
        }
        return list;
    }

    private static String placeholderAPI(OfflinePlayer p, String before) {
        if (!main.getBoolean("API.placeholderAPI", false)) {
            return before;
        }
        if (PlaceholderAPI.containsPlaceholders(before)) {
            return PlaceholderAPI.setPlaceholders(p, before);
        } else {
            return before;
        }
    }

    private static List<String> placeholderAPI(OfflinePlayer p, List<String> before) {
        if (!main.getBoolean("API.placeholderAPI", false)) {
            return before;
        }
        return PlaceholderAPI.setPlaceholders(p, before);
    }

    /**
     * 返回config.yml
     *
     * @return 返回YamlConfiguration
     */
    public static YamlConfiguration getConfig() {
        return main;
    }

    public static void reloadLanguage() {
        config = null;
    }
}
