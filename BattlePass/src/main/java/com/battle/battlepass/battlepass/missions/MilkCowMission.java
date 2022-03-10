package com.battle.battlepass.battlepass.missions;

import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.references.Mission;

import java.util.List;

public class MilkCowMission extends Mission {

    private final int times;
    private int progress;


    public MilkCowMission(MissionType type, String name, int id, int exp, TypeQuest typeQuest, List<String> description, boolean isPremium, int times) {
        super(type, name, id, exp, typeQuest, description, isPremium);
        this.times = times;
        this.progress = 0;
    }

    public int getTimes() { return times; }

    public int getProgress() { return progress; }

    public void setProgress(int progress) { this.progress = progress; }

}
