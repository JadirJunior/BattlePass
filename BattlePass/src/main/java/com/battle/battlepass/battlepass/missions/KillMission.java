package com.battle.battlepass.battlepass.missions;

import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.references.Mission;

public class KillMission extends Mission {
    private int entities;
    private String entityName;

    public KillMission(MissionType type, String name, int id, int entities, String entityName, int exp, TypeQuest typeQuest) {
        super(type, name, id, exp, typeQuest);
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
