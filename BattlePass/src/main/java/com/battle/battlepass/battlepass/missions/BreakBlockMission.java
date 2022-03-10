package com.battle.battlepass.battlepass.missions;

import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.references.Mission;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BreakBlockMission extends Mission {

    private int blocks;
    private ItemStack block;

    public BreakBlockMission(MissionType type, String name, int id, int blocks, ItemStack block, int exp, TypeQuest typeQuest, List<String> description, boolean isPremium) {
        super(type, name, id, exp, typeQuest, description, isPremium);
        this.blocks = blocks;
        this.block = block;
    }

    public int getBlocks() { return blocks; }

    public ItemStack getBlock() { return block; }

    public void setBlocks(int blocks) { this.blocks = blocks; }
}
