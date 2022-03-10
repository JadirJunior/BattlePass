package com.battle.battlepass.battlepass.missions;

import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.references.Mission;

import java.util.List;

public class KillMission extends Mission {
    private int entities;
    private String entityName;

    public KillMission(MissionType type, String name, int id, int entities, String entityName, int exp, TypeQuest typeQuest, List<String> description, boolean isPremium) {
        super(type, name, id, exp, typeQuest, description, isPremium);
        this.entities = entities;
        this.entityName = entityName;
    }

    public int getEntitiesToKill() {
        return entities;
    }

    public String getEntityName() { return entityName; }

    public void setEntitiesToKill(int entities) {
        this.entities = entities;
    }
}
