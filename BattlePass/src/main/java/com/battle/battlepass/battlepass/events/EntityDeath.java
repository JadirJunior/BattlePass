package com.battle.battlepass.battlepass.events;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.missions.BreakBlockMission;
import com.battle.battlepass.battlepass.missions.KillMission;
import com.battle.battlepass.battlepass.references.Mission;
import com.battle.battlepass.battlepass.references.PlayerModel;
import com.battle.battlepass.battlepass.reflection.Title;
import com.battle.battlepass.battlepass.utils.ChatUtil;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeath implements Listener {

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        Player player = e.getEntity().getKiller();
        LivingEntity entity = e.getEntity();
        if (player != null) {
            PlayerModel playerModel = BattlePass.players.get(player.getUniqueId().toString());

            playerModel.getActiveQuests().forEach(quest -> {
                if (quest.getType() == MissionType.KILL) {
                    KillMission killMission = null;
                    if (quest.getTypeQuest() == TypeQuest.DAILY) {
                        for (Mission m : playerModel.getMissionsDaily()) {
                            if (m.getId() == quest.getId()) {
                                killMission = (KillMission) m;
                            }
                        }
                    } else if (quest.getTypeQuest() == TypeQuest.SEASON) {
                        for (Mission m : playerModel.getSeasonMissions()) {
                            if (m.getId() == quest.getId()) {
                                killMission = (KillMission) m;
                            }
                        }
                    }

                    if (killMission != null && entity.getName().equalsIgnoreCase(killMission.getEntityName())) {
                        int entitiesRemaining = killMission.getEntitiesToKill()-1;
                        killMission.setEntitiesToKill(entitiesRemaining);
                        if (entitiesRemaining == 0) {
                            new Title().send(player, 2, 3, 2, ChatUtil.format("&b&lParabéns!"), ChatUtil.format("&8Você concluiu a missão '&2"+killMission.getName()+"&8'"));
                            playerModel.completeQuest(killMission);
                        }

                    }
                }
            });
        }

    }

}
