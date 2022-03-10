package com.battle.battlepass.battlepass.missions;

import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.references.Mission;

import java.util.List;

public class GetExpMission extends Mission {

    private final int value;
    private int progress;

    public GetExpMission(MissionType type, String name, int id, int exp, TypeQuest typeQuest, List<String> description, boolean isPremium, int value) {
        super(type, name, id, exp, typeQuest, description, isPremium);
        this.value = value;
        this.progress = 0;
    }

    public int getValue() { return value; }

    public int getProgress() { return progress; }

    public void setProgress(int progress) { this.progress = progress; }

}
