package com.battle.battlepass.battlepass.events;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.missions.BreakBlockMission;
import com.battle.battlepass.battlepass.missions.ConsumeItemMission;
import com.battle.battlepass.battlepass.references.Mission;
import com.battle.battlepass.battlepass.references.PlayerModel;
import com.battle.battlepass.battlepass.reflection.Title;
import com.battle.battlepass.battlepass.utils.ChatUtil;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class PConsume implements Listener {

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        Player player = e.getPlayer();
        ItemStack itemConsumed = e.getItem();
        PlayerModel playerModel = BattlePass.players.get(player.getUniqueId().toString());

        playerModel.getActiveQuests().forEach(quest -> {
            if (quest.getType() == MissionType.CONSUME) {
                ConsumeItemMission consumeItemMission = null;
                if (quest.getTypeQuest() == TypeQuest.DAILY) {
                    for (Mission m : playerModel.getMissionsDaily()) {
                        if (m.getId() == quest.getId()) {
                            consumeItemMission = (ConsumeItemMission) m;
                        }
                    }
                }
                else if (quest.getTypeQuest() == TypeQuest.SEASON) {
                    for (Mission m : playerModel.getSeasonMissions()) {
                        if (m.getId() == quest.getId() && m.getSeason().equalsIgnoreCase(quest.getSeason())) {
                            consumeItemMission = (ConsumeItemMission) m;
                        }
                    }
                }

                if (consumeItemMission != null) {
                    if (consumeItemMission.getItem().getTypeId() == itemConsumed.getTypeId() && consumeItemMission.getItem().getData().getData() == itemConsumed.getData().getData()) {
                        int items = consumeItemMission.getQuant()-1;
                        consumeItemMission.setQuant(items);
                        if (items == 0) {
                            new Title().send(player, 2, 3, 2, ChatUtil.format("&b&lParabéns!"), ChatUtil.format("&8Você concluiu a missão '&2"+consumeItemMission.getName()+"&8'"));
                            playerModel.completeQuest(consumeItemMission);
                        }
                    }
                }
            }
        });
    }


}
