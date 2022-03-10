package com.battle.battlepass.battlepass.events;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.missions.MilkCowMission;
import com.battle.battlepass.battlepass.missions.WalkDistanceMission;
import com.battle.battlepass.battlepass.references.Mission;
import com.battle.battlepass.battlepass.references.PlayerModel;
import com.battle.battlepass.battlepass.reflection.Title;
import com.battle.battlepass.battlepass.utils.ChatUtil;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class PInteractEntity implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getItemInHand();
        Entity entity = e.getRightClicked();
        if (item==null) { return; }

        if (item.getType() == Material.BUCKET && entity.getType() == EntityType.COW) {
            PlayerModel playerModel = BattlePass.players.get(player.getUniqueId().toString());

            playerModel.getActiveQuests().forEach(quest -> {
                if (quest.getType() == MissionType.MILK) {

                    MilkCowMission milkCowMission = null;
                    if (quest.getTypeQuest() == TypeQuest.DAILY) {
                        for (Mission m : playerModel.getMissionsDaily()) {
                            if (m.getId() == quest.getId()) {
                                milkCowMission = (MilkCowMission) m;
                            }
                        }
                    } else if (quest.getTypeQuest() == TypeQuest.SEASON) {
                        for (Mission m : playerModel.getSeasonMissions()) {
                            if (m.getId() == quest.getId()) {
                                milkCowMission = (MilkCowMission) m;
                            }
                        }
                    }

                    if (milkCowMission != null) {

                        int progress = milkCowMission.getProgress()+1;
                        milkCowMission.setProgress(progress);
                        player.sendMessage(progress+"");

                        if (progress >= milkCowMission.getTimes()) {
                            new Title().send(player, 2, 3, 2, ChatUtil.format("&b&lParabéns!"), ChatUtil.format("&8Você concluiu a missão '&2"+milkCowMission.getName()+"&8'"));
                            playerModel.completeQuest(milkCowMission);
                        }

                    }

                }
            });
        }
    }

}
