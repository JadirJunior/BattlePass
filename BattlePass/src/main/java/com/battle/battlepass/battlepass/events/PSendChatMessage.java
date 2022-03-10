package com.battle.battlepass.battlepass.events;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.missions.SendMessageMission;
import com.battle.battlepass.battlepass.references.Mission;
import com.battle.battlepass.battlepass.references.PlayerModel;
import com.battle.battlepass.battlepass.reflection.Title;
import com.battle.battlepass.battlepass.utils.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;

public class PSendChatMessage implements Listener {

    List<String> mobs = new ArrayList<>();


    @EventHandler
    public void playerSendMessage(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();

        PlayerModel model = BattlePass.players.get(player.getUniqueId().toString());

        if (model != null) {
            model.getActiveQuests().forEach(quest -> {
                if (quest.getType() == MissionType.CHAT) {
                    SendMessageMission sendMessageMission = null;

                    if (quest.getTypeQuest() == TypeQuest.DAILY) {
                        for (Mission m : model.getMissionsDaily()) {
                            if (m.getId() == quest.getId()) {
                                sendMessageMission = (SendMessageMission) m;
                            }
                        }
                    } else if (quest.getTypeQuest() == TypeQuest.SEASON) {
                        for (Mission m : model.getSeasonMissions()) {
                            if (m.getId() == quest.getId()) {
                                sendMessageMission = (SendMessageMission) m;
                            }
                        }
                    }

                    if (sendMessageMission != null && message.equalsIgnoreCase(sendMessageMission.getMessage())) {
                        new Title().send(player, 2, 3, 2, ChatUtil.format("&b&lParabéns!"), ChatUtil.format("&8Você concluiu a missão '&2"+sendMessageMission.getName()+"&8'"));
                        model.completeQuest(sendMessageMission);
                    }
                }
            });
        }

    }
}
