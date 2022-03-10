package com.battle.battlepass.battlepass;

import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.events.*;
import com.battle.battlepass.battlepass.manager.CommandManager;
import com.battle.battlepass.battlepass.references.Mission;
import com.battle.battlepass.battlepass.references.PlayerModel;
import com.battle.battlepass.battlepass.references.Tier;
import com.battle.battlepass.battlepass.utils.InventoryBuilder;
import com.battle.battlepass.battlepass.utils.ItemBuilder;
import com.battle.battlepass.battlepass.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class BattlePass extends JavaPlugin {

    public static String season;
    public static String dataStartSeason;
    public static int durationSeason;

    public static HashMap<String, InventoryBuilder> inventories = new HashMap<>();
    public static HashMap<String, ItemBuilder> itemsActions = new HashMap<>();
    public static HashMap<String, Tier> tiers = new HashMap<>();
    public static HashMap<String, PlayerModel> players = new HashMap<>();


    public static HashMap<String, InventoryBuilder> dailyInvPlayer = new HashMap<>();
    public static HashMap<String, InventoryBuilder> seasonInvPlayer = new HashMap<>();
    public static HashMap<String, InventoryBuilder> tierInvPlayer = new HashMap<>();
    public static HashMap<String, InventoryBuilder> mainInvPlayer = new HashMap<>();


    public static String last_update;
    public static int nextPageSlot;
    public static int pageSlot;
    public static int previuousPageSlot;
    public static boolean useMySQL;
    public static boolean autoUpdateSeason;



    private static JavaPlugin plugin;

    public static HashMap<Integer, Mission> missions = new HashMap<>();
    public static HashMap<Integer, Mission> seasonMissions = new HashMap<>();

    public static void registerInventoriesBase() {
        BattlePass.inventories.put("BattlePass", new InventoryBuilder(9*3, "BattlePass"));
        BattlePass.inventories.put("Missions", new InventoryBuilder(9*3, "Missions"));
        
        BattlePass.inventories.put("Missoes Diarias", new InventoryBuilder(9*5, "Missoes Diarias", TypeQuest.DAILY));
        BattlePass.inventories.put("Missoes da Temporada", new InventoryBuilder(9*5, "Missoes da Temporada", TypeQuest.SEASON));
        BattlePass.inventories.put("Tiers", new InventoryBuilder(9*5, "Tiers"));
    }

    @Override
    public void onEnable() {
        plugin = this;
        getCommand("bp").setExecutor(new CommandManager());
        getCommand("startquest").setExecutor(new CommandManager());
        getCommand("bpadmin").setExecutor(new CommandManager());

        Bukkit.getPluginManager().registerEvents(new PInventoryClick(), this);
        Bukkit.getPluginManager().registerEvents(new PJoinEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PSendChatMessage(), this);
        Bukkit.getPluginManager().registerEvents(new BlockInteract(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDeath(), this);
        Bukkit.getPluginManager().registerEvents(new PQuitEvent(), this);
        Bukkit.getPluginManager().registerEvents(new FishEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PConsume(), this);
        Bukkit.getPluginManager().registerEvents(new PMoveEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PInteractEntity(), this);
        Bukkit.getPluginManager().registerEvents(new PChangeExp(), this);
        Bukkit.getPluginManager().registerEvents(new PCraftEvent(), this);

        registerInventoriesBase();
        Utils.initialize();
    }

    @Override
    public void onDisable() {
        Utils.savePlayers();
    }

    public static JavaPlugin getPlugin() { return plugin; }
}
