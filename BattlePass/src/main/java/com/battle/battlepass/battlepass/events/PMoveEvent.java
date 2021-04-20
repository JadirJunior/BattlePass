package com.battle.battlepass.battlepass.events;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PMoveEvent implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Location loc1 = e.getFrom();
        Location loc2 = e.getTo();
        double distance = loc1.distance(loc2);
    }

}
