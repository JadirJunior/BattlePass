package com.battle.battlepass.battlepass.events;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.references.Mission;
import com.battle.battlepass.battlepass.references.PlayerModel;
import com.battle.battlepass.battlepass.utils.InventoryBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class PJoinEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!BattlePass.players.containsKey(e.getPlayer().getUniqueId().toString())) {
            String uuid = e.getPlayer().getUniqueId().toString();
            BattlePass.players.put(uuid, new PlayerModel(uuid, 0, "0"));
        }

        if (!BattlePass.dailyInvPlayer.containsKey(e.getPlayer().getUniqueId().toString())){
            BattlePass.dailyInvPlayer.put(e.getPlayer().getUniqueId().toString(), new InventoryBuilder(9*5, "Missoes Diarias", TypeQuest.DAILY));
        }

        if (!BattlePass.seasonInvPlayer.containsKey(e.getPlayer().getUniqueId().toString())){
            BattlePass.seasonInvPlayer.put(e.getPlayer().getUniqueId().toString(), new InventoryBuilder(9*5, "Missoes da Temporada", TypeQuest.SEASON));
        }

        if (!BattlePass.tierInvPlayer.containsKey(e.getPlayer().getUniqueId().toString()))  {
            BattlePass.tierInvPlayer.put(e.getPlayer().getUniqueId().toString(), new InventoryBuilder(9*5, "Tiers"));
        }

    }
}
