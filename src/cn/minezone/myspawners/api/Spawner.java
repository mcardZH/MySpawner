package cn.minezone.myspawners.api;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author mcard
 */
public class Spawner implements ConfigurationSerializable, Cloneable {

    private ItemStack icon;
    private EntityType type;
    private String name;
    private String displayName;
    private List<EntityType> spawns;
    private List<String> commands;
    private List<ItemStack> itemStack;
    private int ticks;
    private int playerRadius;
    private int radius;
    private boolean enable;
    /**
     *  最后一次生成时间
     */
    private int lastTick;
    private List<Location> locs;

    /**
     * 最简构造方法
     *
     * @param name 名称
     */
    public Spawner(String name) {
        this.icon = new ItemStack(Material.STONE);
        this.name = name;
        this.displayName = "";
        this.spawns = new ArrayList<>();
        this.commands = new ArrayList<>();
        this.itemStack = new ArrayList<>();
        this.ticks = 20;
        this.radius = 5;
        this.playerRadius = 5;
        this.enable = false;
        this.lastTick = 0;
        this.type = EntityType.PIG;
    }

    /**
     * 自定义刷怪笼
     *
     * @param icon         图标（在商店中显示）
     * @param name         名称（存储在配置中）
     * @param displayName  显示的名称（在/ms list中显示）
     * @param spawns       刷新的生物
     * @param commands     执行的命令
     * @param itemStacks   物品
     * @param type         对应刷怪笼的怪物类型
     * @param ticks        刷新间隔
     * @param playerRadius 激活刷怪笼玩家所需要在的半径
     * @param radius       刷怪半径
     * @param enable       启用
     */
    public Spawner(ItemStack icon, String name, String displayName, List<EntityType> spawns, List<String> commands, List<ItemStack> itemStacks,
                   EntityType type, int ticks, int playerRadius, int radius, boolean enable) {
        this.icon = icon == null ? new ItemStack(Material.STONE) : icon;
        //确保所有项目正确
        this.type = type;
        if (name == null || "".equals(name)) {
            throw new Error("刷怪笼名称必须存在");
        }
        this.name = name;
        this.displayName = displayName == null ? "" : displayName;
        this.spawns = spawns == null ? new ArrayList<>() : spawns;
        this.commands = commands == null ? new ArrayList<>() : commands;
        this.itemStack = itemStacks == null ? new ArrayList<>() : itemStacks;
        this.ticks = ticks <= 0 ? 20 : ticks;
        this.playerRadius = playerRadius <= 0 ? 5 : playerRadius;
        this.radius = radius <= 0 ? 5 : radius;
        this.enable = enable;
        this.lastTick = 0;
        this.locs = new ArrayList<>();
        //ArmorStand
    }


    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("icon", getIcon());
        map.put("name", getName());
        map.put("displayName", getDisplayName());
        List<String> et = new ArrayList<>();
        for (EntityType e : getSpawns()) {
            et.add(e.name());
        }
        map.put("spawns", et);
        map.put("commands", getCommands());
        map.put("itemStack", getItemStack());
        map.put("ticks", ticks);
        map.put("playerRadius", playerRadius);
        map.put("radius", radius);
        map.put("enable", isEnable());
        map.put("locations", locs);
        map.put("entity", type.name());
        return map;
    }

    public static Spawner deserialize(Map<String, Object> map) {//反序列化
        List<String> en = map.get("spawns") instanceof List ? (List<String>) map.get("spawns") : new ArrayList<>();
        List<EntityType> et = new ArrayList<>();
        for (String e : en) {
            et.add(EntityType.valueOf(e));
        }
        Spawner spawner = new Spawner((ItemStack) map.get("icon"), (String) map.get("name"),
                (String) map.get("displayName"), et,
                map.get("commands") instanceof List ? (List<String>) map.get("commands") : new ArrayList<>(),
                map.get("itemStack") instanceof List ? (List<ItemStack>) map.get("itemStack") : new ArrayList<>(),
                EntityType.valueOf((String) map.get("entity")), (int) map.get("ticks"),
                (int) map.get("playerRadius"), (int) map.get("radius"), (Boolean) map.get("enable"));
        spawner.setLocs((List<Location>) map.get("locations"));
        return spawner;
    }

    //Getter and Setter
    //==============================================

    public ItemStack getIcon() {
        return icon;
    }

    public int getTicks() {
        return ticks;
    }

    public List<EntityType> getSpawns() {
        return spawns;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getCommands() {
        return commands;
    }

    public List<ItemStack> getItemStack() {
        return itemStack;
    }

    public boolean isEnable() {
        return enable;
    }

    public String getName() {
        return name;
    }

    public int getPlayerRadius() {
        return playerRadius;
    }

    public int getRadius() {
        return radius;
    }

    public int getLastTick() {
        return lastTick;
    }

    public List<Location> getLocs() {
        return locs;
    }

    public EntityType getType() {
        return type;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpawns(List<EntityType> spawns) {
        this.spawns = spawns;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public void setLastTick(int lastTick) {
        this.lastTick = lastTick;
    }

    public void setPlayerRadius(int playerRadius) {
        this.playerRadius = playerRadius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setItemStack(List<ItemStack> itemStack) {
        this.itemStack = itemStack;
    }

    public void setLocs(List<Location> locs) {
        this.locs = locs;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public void addItemStack(ItemStack itemStack) {
        this.itemStack.add(itemStack);
    }

    public void addSpawns(EntityType entityType) {
        this.spawns.add(entityType);
    }

    public void addCommand(String command) {
        this.commands.add(command);
    }

    public void addLastTick() {
        lastTick += 1;
    }

    public void addLocation(Location loc) {
        locs.add(loc);
    }

    @Override
    public Spawner clone() {
        try {
            return (Spawner) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
