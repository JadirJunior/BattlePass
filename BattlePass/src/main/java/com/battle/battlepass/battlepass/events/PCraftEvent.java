package com.battle.battlepass.battlepass.events;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.missions.CraftItemMission;
import com.battle.battlepass.battlepass.references.Mission;
import com.battle.battlepass.battlepass.references.PlayerModel;
import com.battle.battlepass.battlepass.reflection.Title;
import com.battle.battlepass.battlepass.utils.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class PCraftEvent implements Listener {

    @EventHandler
    public void onCraft(CraftItemEvent e) {

        if (e.getWhoClicked() instanceof Player) {
            Player player = (Player) e.getWhoClicked();
            ItemStack result = e.getRecipe().getResult();

            PlayerModel playerModel = BattlePass.players.get(player.getUniqueId().toString());

            playerModel.getActiveQuests().forEach(quest -> {
                if (quest.getType() == MissionType.CRAFT) {

                    CraftItemMission craftItemMission = null;
                    if (quest.getTypeQuest() == TypeQuest.DAILY) {
                        for (Mission m : playerModel.getMissionsDaily()) {
                            if (m.getId() == quest.getId()) {
                                craftItemMission = (CraftItemMission) m;
                            }
                        }
                    } else if (quest.getTypeQuest() == TypeQuest.SEASON) {
                        for (Mission m : playerModel.getSeasonMissions()) {
                            if (m.getId() == quest.getId()) {
                                craftItemMission = (CraftItemMission) m;
                            }
                        }
                    }

                    if (craftItemMission != null) {

                        if (result.getTypeId() == craftItemMission.getItem().getTypeId() &&
                                result.getData().getData() == craftItemMission.getItem().getData().getData()) {
                            new Title().send(player, 2, 3, 2, ChatUtil.format("&b&lParabéns!"), ChatUtil.format("&8Você concluiu a missão '&2"+craftItemMission.getName()+"&8'"));
                            playerModel.completeQuest(craftItemMission);
                        }

                    }

                }
            });

        }
    }

}
