package cn.minezone.myspawners.listener;

import cn.minezone.myspawners.handlers.LanguageHandler;
import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;


/**
 * @author mcard
 */
public class InventoryListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player) && !(e.getInventory().getHolder() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getWhoClicked();
        //保证点击者和容器所有者为同一人
        if (!p.getName().equals(((Player) e.getInventory().getHolder()).getName())) {
            return;
        }
        String title = LanguageHandler.getLanguage("inventory.gui.title");
        title = title.replace("{page}/{max-page}", "");
        if (!e.getInventory().getTitle().contains(title)) {
            return;
        }
        e.setCancelled(true);
        if (!e.getInventory().equals(e.getClickedInventory())) {
            return;
        }
        ItemStack item = e.getCurrentItem();
        if (item.getType().equals(Material.AIR)) {
            return;
        }
        NBTItem nbt = new NBTItem(item);
        Bukkit.dispatchCommand(p,"myspawner give " + p.getName() + " " + nbt.getString("spawner") + " ");
    }


}
