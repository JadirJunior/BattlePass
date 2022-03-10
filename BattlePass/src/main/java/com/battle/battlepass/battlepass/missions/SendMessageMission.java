package com.battle.battlepass.battlepass.missions;

import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.references.Mission;

import java.util.List;

public class SendMessageMission extends Mission {

    private String message;

    public SendMessageMission(MissionType type, String name, int id, int exp, TypeQuest typeQuest, List<String> description, String message, boolean isPremium) {
        super(type, name, id, exp, typeQuest, description, isPremium);
        this.message = message;
    }

    public String getMessage() { return this.message; }
}
