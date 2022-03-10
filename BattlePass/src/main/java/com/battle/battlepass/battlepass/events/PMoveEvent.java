package com.battle.battlepass.battlepass.events;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.missions.KillMission;
import com.battle.battlepass.battlepass.missions.WalkDistanceMission;
import com.battle.battlepass.battlepass.references.Mission;
import com.battle.battlepass.battlepass.references.PlayerModel;
import com.battle.battlepass.battlepass.reflection.Title;
import com.battle.battlepass.battlepass.utils.ChatUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PMoveEvent implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Location loc1 = e.getFrom();
        Location loc2 = e.getTo();
        double distance = loc1.distance(loc2);
        if (loc1.getBlockX() == loc2.getBlockX() && loc1.getBlockZ()==loc2.getBlockZ()) {
            return;
        }

        PlayerModel playerModel = BattlePass.players.get(player.getUniqueId().toString());

        playerModel.getActiveQuests().forEach(quest -> {
            if (quest.getType() == MissionType.WALK) {

                WalkDistanceMission walkDistanceMission = null;
                if (quest.getTypeQuest() == TypeQuest.DAILY) {
                    for (Mission m : playerModel.getMissionsDaily()) {
                        if (m.getId() == quest.getId()) {
                            walkDistanceMission = (WalkDistanceMission) m;
                        }
                    }
                } else if (quest.getTypeQuest() == TypeQuest.SEASON) {
                    for (Mission m : playerModel.getSeasonMissions()) {
                        if (m.getId() == quest.getId()) {
                            walkDistanceMission = (WalkDistanceMission) m;
                        }
                    }
                }

                if (walkDistanceMission != null) {

                    double progress = walkDistanceMission.getProgress()+distance;
                    walkDistanceMission.setProgress(progress);

                    if (progress >= walkDistanceMission.getDistance()) {
                        new Title().send(player, 2, 3, 2, ChatUtil.format("&b&lParabéns!"), ChatUtil.format("&8Você concluiu a missão '&2"+walkDistanceMission.getName()+"&8'"));
                        playerModel.completeQuest(walkDistanceMission);
                    }

                }

            }
        });
    }

}
