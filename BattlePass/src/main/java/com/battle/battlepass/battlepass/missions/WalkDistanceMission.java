package com.battle.battlepass.battlepass.missions;

import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.references.Mission;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class WalkDistanceMission extends Mission {

    private final int distanceTotal;
    private double progress;



    public WalkDistanceMission(MissionType type, String name, int id, int exp, TypeQuest typeQuest, List<String> description, boolean isPremium, int distance) {
        super(type, name, id, exp, typeQuest, description, isPremium);
        this.distanceTotal = distance;
        this.progress = 0;
    }

    public int getDistance() { return distanceTotal; }

    public double getProgress() { return progress; }

    public void setProgress(double distance) { this.progress = distance; }
}
