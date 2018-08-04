package cn.minezone.myspawners.api;

import cn.minezone.myspawners.MySpawnersPlugin;
import cn.minezone.myspawners.handlers.LanguageHandler;
import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mcard
 */
public class SpawnerManager {

    private static SpawnerManager sm;

    private MySpawnersPlugin main;
    private Set<Spawner> spawners = new HashSet<>();
    private YamlConfiguration config;
    private File f;


    public static SpawnerManager getManagerInstance(MySpawnersPlugin main) {//单例模式
        synchronized (SpawnerManager.class) {
            //多线程安全
            if (sm == null && main != null) {
                sm = new SpawnerManager(main);
            }
            return sm;
        }
    }

    private SpawnerManager(MySpawnersPlugin main) {
        this.main = main;
        //load spawners
        f = new File(main.getDataFolder(), "spawners.yml");
        if (!f.exists()) {
            main.saveResource("spawners.yml", true);
        }
        config = YamlConfiguration.loadConfiguration(f);
        for (String key : config.getKeys(false)) {
            if (config.get(key, null) == null) {
                continue;
            }
            spawners.add((Spawner) config.get(key));
        }
        Bukkit.getConsoleSender().sendMessage(LanguageHandler.getLanguage("loading.load.spawner")
                .replace("{num}", spawners.size() + ""));
    }

    public Set<Spawner> getSpawners() {
        return spawners;
    }

    public void reloadAllSpawners() {//重载所有刷怪笼
        //load spawners
        f = new File(main.getDataFolder(), "spawners.yml");
        if (!f.exists()) {
            main.saveResource("spawners.yml", true);
        }
        config = YamlConfiguration.loadConfiguration(f);

        for (String key : config.getValues(false).keySet()) {
            if (config.get(key, null) == null) {
                continue;
            }
            spawners.add((Spawner) config.get(key));
        }
        Bukkit.getConsoleSender().sendMessage(LanguageHandler.getLanguage("loading.load.spawner")
                .replace("{num}", spawners.size() + ""));
    }

    public boolean addSpawner(Spawner spawner) {
        if (checkName(spawner.getName())) {
            return false;
        }
        boolean s = spawners.add(spawner);
        if (s) {
            config.set(spawner.getName(), spawner);
            saveConfig(true);
        }
        return s;
    }

    public ItemStack getSpawnerItem(Spawner s) {
        return getSpawnerItem(s, 1);
    }

    public ItemStack getSpawnerItem(Spawner s, int amount) {
        ItemStack item = new ItemStack(Material.MOB_SPAWNER);
        ItemMeta im = s.getIcon().getItemMeta();
        item.setAmount(amount);
        im.setDisplayName(s.getDisplayName());
        item.setItemMeta(im);
        NBTItem nbti = new NBTItem(item);
        nbti.setString("spawner", s.getName());
        //为物品增加NBT标签，记录其对应的名称
        return nbti.getItem().clone();
    }

    public Spawner getSpawner(String name) {
        if (!checkName(name)) {
            return null;
        }
        for (Spawner spawner : spawners) {
            if (spawner.getName().equals(name)) {
                return spawner;
            }
        }
        return null;
    }

    /**
     * 检查名称是否被占用
     *
     * @param name 名称
     * @return 结果
     */
    public boolean checkName(String name) {
        for (Spawner sp : getSpawners()) {
            if (sp.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean removeSpawner(Spawner spawner) {
        boolean s = getSpawners().remove(spawner);
        if (s) {
            config.set(spawner.getName(), null);
        }
        return s;
    }

    public void saveAllSpawner() {
        for (Spawner s : spawners) {
            config.set(s.getName(), s);
        }
        saveConfig(true);
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public void saveConfig(boolean asy) {//为了保证性能，异步进行
        if (asy) {
            Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
                try {
                    config.save(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            try {
                config.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 判断是否为Spawner放置事件
     * @return
     */
    public boolean isSpawnerPlace() {
        return false;
    }
}
