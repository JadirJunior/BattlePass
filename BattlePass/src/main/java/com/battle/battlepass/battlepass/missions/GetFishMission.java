package com.battle.battlepass.battlepass.missions;

import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.references.Mission;

import java.util.List;

public class GetFishMission extends Mission {

    private int quant;

    public GetFishMission(MissionType type, String name, int id, int quant, int exp, TypeQuest typeQuest, List<String> description, boolean isPremium) {
        super(type, name, id, exp, typeQuest, description, isPremium);
        this.quant = quant;
    }

    public int getQuant() { return quant; }


    public void setQuant(int quant) { this.quant = quant; }


}
