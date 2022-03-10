package com.battle.battlepass.battlepass.references;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Tier {
    private int exp;
    private List<String> commands = new ArrayList<>();
    private ItemStack item;
    private int amount;
    private String name;
    private int tier;
    private boolean isPremium;

    public Tier(int exp, ItemStack item, List<String> commands, int amount, int tier,String name, boolean isPremium) {
        this.exp = exp;
        this.commands= commands;
        this.item = item;
        this.amount = amount;
        this.name = name;
        this.tier = tier;
        if (item != null) {
            this.item.setAmount(amount);
        }
        this.isPremium = isPremium;

    }

    public boolean isPremium() { return isPremium; }

    public int getExp() {
        return exp;
    }

    public int getLevel() { return tier; }

    public String getName() { return name; }

    public void setLevel(int level) { this.tier = level; }

    public List<String> getCommands() { return commands; }

    public ItemStack getItem() { return item; }

}
