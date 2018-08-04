package cn.minezone.myspawners.api;

import org.bukkit.Location;

/**
 * @author mcard
 */
public class SpawnerBlock {

    private Spawner spawner;
    private Location location;


    public SpawnerBlock(Spawner s, Location location) {
        this.spawner = s;
        this.location = location;
    }

    public Spawner getSpawner() {
        return spawner;
    }

    public Location getLocation() {
        return location;
    }

    public void setSpawner(Spawner spawner) {
        this.spawner = spawner;
    }
}
