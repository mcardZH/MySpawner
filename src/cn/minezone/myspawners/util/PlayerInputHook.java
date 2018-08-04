package cn.minezone.myspawners.util;

import org.bukkit.entity.Player;

public interface PlayerInputHook {
    void receiveChat(Player p, String msg);
}
