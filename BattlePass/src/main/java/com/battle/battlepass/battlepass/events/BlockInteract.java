package com.battle.battlepass.battlepass.events;

import com.avaje.ebean.annotation.EnumValue;
import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.missions.BreakBlockMission;
import com.battle.battlepass.battlepass.missions.KillMission;
import com.battle.battlepass.battlepass.missions.PlaceBlockMission;
import com.battle.battlepass.battlepass.references.Mission;
import com.battle.battlepass.battlepass.references.PlayerModel;
import com.battle.battlepass.battlepass.reflection.Title;
import com.battle.battlepass.battlepass.utils.ChatUtil;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;


public class BlockInteract implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block blockBreak = e.getBlock();
        PlayerModel playerModel = BattlePass.players.get(player.getUniqueId().toString());
        playerModel.getActiveQuests().forEach(quest -> {
            if (quest.getType() == MissionType.BREAK) {
                BreakBlockMission breakBlockMission = null;
                if (quest.getTypeQuest() == TypeQuest.DAILY) {
                    for (Mission m : playerModel.getMissionsDaily()) {
                        if (m.getId() == quest.getId()) {
                            breakBlockMission = (BreakBlockMission) m;
                        }
                    }
                }
                else if (quest.getTypeQuest() == TypeQuest.SEASON) {
                    for (Mission m : playerModel.getSeasonMissions()) {
                        if (m.getId() == quest.getId() && m.getSeason().equalsIgnoreCase(quest.getSeason())) {
                            breakBlockMission = (BreakBlockMission) m;
                        }
                    }
                }
                if (breakBlockMission != null) {
                    if (breakBlockMission.getBlock().getTypeId() == blockBreak.getTypeId() && breakBlockMission.getBlock().getData().getData() == blockBreak.getData()) {
                        int blocksRemaining = breakBlockMission.getBlocks()-1;
                        breakBlockMission.setBlocks(blocksRemaining);
                        if (blocksRemaining == 0) {
                            new Title().send(player, 2, 3, 2, ChatUtil.format("&b&lParabéns!"), ChatUtil.format("&8Você concluiu a missão '&2"+breakBlockMission.getName()+"&8'"));
                            playerModel.completeQuest(breakBlockMission);
                        }
                    }
                }
            }
        });
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Block blockBreak = e.getBlock();
        PlayerModel playerModel = BattlePass.players.get(player.getUniqueId().toString());
        playerModel.getActiveQuests().forEach(quest -> {
            if (quest.getType() == MissionType.PLACE) {
                PlaceBlockMission placeBlockMission = null;

                if (quest.getTypeQuest() == TypeQuest.DAILY) {
                    for (Mission m : playerModel.getMissionsDaily()) {
                        if (m.getId() == quest.getId()) {
                            placeBlockMission = (PlaceBlockMission) m;
                        }
                    }
                }
                else if (quest.getTypeQuest() == TypeQuest.SEASON) {
                    for (Mission m : playerModel.getSeasonMissions()) {
                        if (m.getId() == quest.getId() && m.getSeason().equalsIgnoreCase(quest.getSeason())) {
                            placeBlockMission = (PlaceBlockMission) m;
                        }
                    }
                }
                if (placeBlockMission != null) {
                    if (placeBlockMission.getBlock().getTypeId() == blockBreak.getTypeId() && placeBlockMission.getBlock().getData().getData() == blockBreak.getData()) {
                        int blocksRemaining = placeBlockMission.getBlocks()-1;
                        placeBlockMission.setBlocks(blocksRemaining);
                        if (blocksRemaining == 0) {
                            new Title().send(player, 2, 3, 2, ChatUtil.format("&b&lParabéns!"), ChatUtil.format("&8Você concluiu a missão '&2"+placeBlockMission.getName()+"&8'"));
                            playerModel.completeQuest(placeBlockMission);
                        }
                    }
                }
            }
        });
    }

}
