package com.battle.battlepass.battlepass.references;

import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.missions.KillMission;

public class Mission {
    private MissionType type;
    private String name;
    private int id;
    private int exp;
    private StatsMission stat;
    private TypeQuest typeQuest;

    public Mission() {}

    public Mission(MissionType type, String name, int id, int exp) {
        this.name = name;
        this.type = type;
        this.id = id;
        this.exp = exp;
        this.stat = StatsMission.ACTIVE;
    }

    public Mission(MissionType type, String name, int id, int exp, TypeQuest typeQuest) {
        this.name = name;
        this.type = type;
        this.id = id;
        this.exp = exp;
        this.stat = StatsMission.ACTIVE;
        this.typeQuest = typeQuest;
    }

    public Mission(MissionType type, String name, int id, int exp, StatsMission stat, TypeQuest typeQuest) {
        this.name = name;
        this.type = type;
        this.id = id;
        this.exp = exp;
        this.stat = stat;
        this.typeQuest = typeQuest;
    }

    public TypeQuest getTypeQuest() { return typeQuest; }

    public String getName() {
        return name;
    }

    public MissionType getType() {
        return type;
    }

    public int getId() { return id; }

    public int getExp() { return exp; }

    public enum StatsMission {
        COMPLETED, ACTIVE
    }

    public StatsMission getStat() {
        return stat;
    }

    public void setStats(StatsMission stat) { this.stat = stat; }
}
