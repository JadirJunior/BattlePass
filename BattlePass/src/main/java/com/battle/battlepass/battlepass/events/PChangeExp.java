package com.battle.battlepass.battlepass.events;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.missions.GetExpMission;
import com.battle.battlepass.battlepass.missions.WalkDistanceMission;
import com.battle.battlepass.battlepass.references.Mission;
import com.battle.battlepass.battlepass.references.PlayerModel;
import com.battle.battlepass.battlepass.reflection.Title;
import com.battle.battlepass.battlepass.utils.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class PChangeExp implements Listener {

    @EventHandler
    public void onChange(PlayerExpChangeEvent e) {
        Player player = e.getPlayer();

        PlayerModel playerModel = BattlePass.players.get(player.getUniqueId().toString());

        playerModel.getActiveQuests().forEach(quest -> {
            if (quest.getType() == MissionType.EXP) {

                GetExpMission getExpMission = null;
                if (quest.getTypeQuest() == TypeQuest.DAILY) {
                    for (Mission m : playerModel.getMissionsDaily()) {
                        if (m.getId() == quest.getId()) {
                            getExpMission = (GetExpMission) m;
                        }
                    }
                } else if (quest.getTypeQuest() == TypeQuest.SEASON) {
                    for (Mission m : playerModel.getSeasonMissions()) {
                        if (m.getId() == quest.getId()) {
                            getExpMission = (GetExpMission) m;
                        }
                    }
                }

                if (getExpMission != null) {

                    int progress = getExpMission.getProgress()+e.getAmount();
                    getExpMission.setProgress(progress);

                    if (progress >= getExpMission.getValue()) {
                        new Title().send(player, 2, 3, 2, ChatUtil.format("&b&lParabéns!"), ChatUtil.format("&8Você concluiu a missão '&2"+getExpMission.getName()+"&8'"));
                        playerModel.completeQuest(getExpMission);
                    }

                }

            }
        });
    }

}
