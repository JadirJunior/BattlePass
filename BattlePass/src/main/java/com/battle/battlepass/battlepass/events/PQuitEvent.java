package com.battle.battlepass.battlepass.events;

import com.battle.battlepass.battlepass.BattlePass;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PQuitEvent implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {

        BattlePass.dailyInvPlayer.remove(e.getPlayer().getUniqueId().toString());
        BattlePass.seasonInvPlayer.remove(e.getPlayer().getUniqueId().toString());
        BattlePass.tierInvPlayer.remove(e.getPlayer().getUniqueId().toString());

    }


}
