package cn.minezone.myspawners.util;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerInputUtils implements Listener {

    private static Map<String, PlayerInputHook> chat;

    public PlayerInputUtils() {
        chat = new HashMap<>();
    }

    public static void getPlayerNextChat(Player p, PlayerInputHook hook) {
        if (chat.containsKey(p.getName())) {
            return;
        }
        chat.put(p.getName(), hook);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)//最高优先并忽略所有已经被取消的事件
    public void onChat(PlayerChatEvent e) {
        if (chat.containsKey(e.getPlayer().getName())) {
            e.setCancelled(true);
            chat.get(e.getPlayer().getName()).receiveChat(e.getPlayer(), e.getMessage());
            chat.remove(e.getPlayer().getName());//执行HOOK并删除相关对象
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        chat.remove(e.getPlayer().getName());//玩家退出游戏则取消获取操作
    }

}
