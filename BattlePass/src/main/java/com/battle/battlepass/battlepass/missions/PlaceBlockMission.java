package com.battle.battlepass.battlepass.missions;

import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.references.Mission;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class PlaceBlockMission extends Mission {

    private int blocks;
    private ItemStack block;

    public PlaceBlockMission(MissionType type, String name, int id, int blocks, ItemStack block, int exp, TypeQuest typeQuest) {
        super(type, name, id, exp, typeQuest);
        this.blocks = blocks;
        this.block = block;
    }

    public int getBlocks() {return blocks;}

    public ItemStack getBlock() { return block; }

    public void setBlocks(int blocks) {
        this.blocks = blocks;
    }
}
