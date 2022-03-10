package com.battle.battlepass.battlepass.references;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.missions.KillMission;

import java.util.ArrayList;
import java.util.List;

public class Mission {
    private MissionType type;
    private String name;
    private int id;
    private int exp;
    private StatsMission stat;
    private TypeQuest typeQuest;
    private String season = null;
    private List<String> description = new ArrayList<>();
    private boolean isPremium;

    public Mission() {}

    public Mission(MissionType type, String name, int id, int exp) {
        this.name = name;
        this.type = type;
        this.id = id;
        this.exp = exp;
        this.stat = StatsMission.ACTIVE;
    }

    public Mission(MissionType type, String name, int id, int exp, TypeQuest typeQuest, List<String> description) {
        this.name = name;
        this.type = type;
        this.id = id;
        this.exp = exp;
        this.stat = StatsMission.ACTIVE;
        this.typeQuest = typeQuest;
        this.season = typeQuest == TypeQuest.SEASON ? BattlePass.season : "";
        this.description = description;
    }

    public Mission(MissionType type, String name, int id, int exp, TypeQuest typeQuest, List<String> description, boolean isPremium) {
        this.name = name;
        this.type = type;
        this.id = id;
        this.exp = exp;
        this.stat = StatsMission.ACTIVE;
        this.typeQuest = typeQuest;
        this.season = typeQuest == TypeQuest.SEASON ? BattlePass.season : "";
        this.description = description;
        this.isPremium = isPremium;
    }

    public Mission(MissionType type, String name, int id, int exp, StatsMission stat, TypeQuest typeQuest, List<String> description) {
        this.name = name;
        this.type = type;
        this.id = id;
        this.exp = exp;
        this.stat = stat;
        this.typeQuest = typeQuest;
        this.season = typeQuest == TypeQuest.SEASON ? BattlePass.season : "";
        this.description = description;
    }

    public boolean isPremium() { return isPremium; }

    public void setIsPremium(boolean isPremium) { this.isPremium = isPremium; }

    public String getSeason() { return season; }

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


    public List<String> getDescription() { return description; }

    public void setStats(StatsMission stat) { this.stat = stat; }
}
