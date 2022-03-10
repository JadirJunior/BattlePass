package com.battle.battlepass.battlepass.missions;

import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.references.Mission;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CraftItemMission extends Mission {

    private final ItemStack item;

    public CraftItemMission(MissionType type, String name, int id, int exp, TypeQuest typeQuest, List<String> description, boolean isPremium, ItemStack item) {
        super(type, name, id, exp, typeQuest, description, isPremium);
        this.item = item;
    }

    public ItemStack getItem() { return item; }

}
