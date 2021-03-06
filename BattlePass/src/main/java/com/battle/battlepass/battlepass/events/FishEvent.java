package com.battle.battlepass.battlepass.events;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.missions.GetFishMission;
import com.battle.battlepass.battlepass.missions.KillMission;
import com.battle.battlepass.battlepass.references.Mission;
import com.battle.battlepass.battlepass.references.PlayerModel;
import com.battle.battlepass.battlepass.reflection.Title;
import com.battle.battlepass.battlepass.utils.ChatUtil;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

public class FishEvent implements Listener {

    @EventHandler
    public void onFish(PlayerFishEvent e) {

        Player player = e.getPlayer();
        if (e.getCaught() instanceof ItemStack) {
            ItemStack item = (ItemStack) e.getCaught();
            if (item.getType() == Material.RAW_FISH) {
                if (player != null) {
                    PlayerModel playerModel = BattlePass.players.get(player.getUniqueId().toString());

                    playerModel.getActiveQuests().forEach(quest -> {
                        if (quest.getType() == MissionType.FISH) {
                            GetFishMission getFishMission = null;
                            if (quest.getTypeQuest() == TypeQuest.DAILY) {
                                for (Mission m : playerModel.getMissionsDaily()) {
                                    if (m.getId() == quest.getId()) {
                                        getFishMission = (GetFishMission) m;
                                    }
                                }
                            } else if (quest.getTypeQuest() == TypeQuest.SEASON) {
                                for (Mission m : playerModel.getSeasonMissions()) {
                                    if (m.getId() == quest.getId()) {
                                        getFishMission = (GetFishMission) m;
                                    }
                                }
                            }

                            if (getFishMission != null) {
                                int fishesToGet = getFishMission.getQuant()-1;
                                getFishMission.setQuant(fishesToGet);
                                if (fishesToGet == 0) {
                                    new Title().send(player, 2, 3, 2, ChatUtil.format("&b&lParab??ns!"), ChatUtil.format("&8Voc?? concluiu a miss??o '&2"+getFishMission.getName()+"&8'"));
                                    playerModel.completeQuest(getFishMission);
                                }

                            }
                        }
                    });
                }
            }

        }



    }

}
