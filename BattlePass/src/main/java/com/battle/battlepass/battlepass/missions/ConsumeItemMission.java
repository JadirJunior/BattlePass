package com.battle.battlepass.battlepass.missions;

import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.references.Mission;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ConsumeItemMission extends Mission {

    private int quant;
    private ItemStack item;


    public ConsumeItemMission(MissionType type, String name, int id, int quant, int exp, TypeQuest typeQuest, List<String> description, ItemStack item, boolean isPremium) {
        super(type, name, id, exp, typeQuest, description, isPremium);
        this.quant = quant;
        this.item = item;
    }

    public int getQuant() {
        return quant;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }
}
