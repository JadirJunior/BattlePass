package com.battle.battlepass.battlepass;

import com.battle.battlepass.battlepass.commands.CreateQuestCommand;
import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.events.*;
import com.battle.battlepass.battlepass.manager.CommandManager;
import com.battle.battlepass.battlepass.manager.CreateQuestManager;
import com.battle.battlepass.battlepass.missions.KillMission;
import com.battle.battlepass.battlepass.references.Mission;
import com.battle.battlepass.battlepass.references.PlayerModel;
import com.battle.battlepass.battlepass.references.Tier;
import com.battle.battlepass.battlepass.utils.InventoryBuilder;
import com.battle.battlepass.battlepass.utils.ItemBuilder;
import com.battle.battlepass.battlepass.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class BattlePass extends JavaPlugin {

    public static CreateQuestManager quest = new CreateQuestManager();

    public static HashMap<String, InventoryBuilder> inventories = new HashMap<>();
    public static HashMap<String, ItemBuilder> itemsActions= new HashMap<>();
    public static HashMap<String, Tier> tiers = new HashMap<>();
    public static HashMap<String, PlayerModel> players = new HashMap<>();
    private static JavaPlugin plugin;

    public static HashMap<Integer, Mission> missions = new HashMap<>();
    public static HashMap<Integer, Mission> seasonMissions = new HashMap<>();

    public static void registerInventoriesBase() {
        BattlePass.inventories.put("BattlePass", new InventoryBuilder(9*3, "BattlePass"));
        BattlePass.inventories.put("Missions", new InventoryBuilder(9*3, "Missions"));
    }

    @Override
    public void onEnable() {
        plugin = this;
        getCommand("bp").setExecutor(new CommandManager());
        getCommand("cq").setExecutor(new CreateQuestCommand());
        getCommand("startquest").setExecutor(new CommandManager());

        Bukkit.getPluginManager().registerEvents(new PInventoryClick(), this);
        Bukkit.getPluginManager().registerEvents(new PJoinEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PSendChatMessage(), this);
        Bukkit.getPluginManager().registerEvents(new BlockInteract(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDeath(), this);


        registerInventoriesBase();
        Utils.initialize();
    }

    @Override
    public void onDisable() {
        BattlePass.players.forEach((uuid, model) -> {
            Utils.configPlayers.getConfig().set("players."+uuid+".exp", model.getExp());
            Utils.configPlayers.getConfig().set("players."+uuid+".tier", model.getTier());

            List<Mission> dailyMissions = model.getMissionsDaily();
            List<Mission> seasonMissions = model.getSeasonMissions();

            List<String> activeDailyMissions = new ArrayList<>();
            List<String> completedDailyMissions = new ArrayList<>();

            List<String> activeSeasonMissions = new ArrayList<>();
            List<String> completedSeasonMissions = new ArrayList<>();

            dailyMissions.forEach(mission -> {
                if (mission.getStat() == Mission.StatsMission.ACTIVE) {
                    activeDailyMissions.add(mission.getId()+"");
                }
                else if (mission.getStat() == Mission.StatsMission.COMPLETED) {
                    completedDailyMissions.add(mission.getId()+"");
                }
            });

            seasonMissions.forEach(mission -> {
                if (mission.getStat() == Mission.StatsMission.ACTIVE) {
                    activeSeasonMissions.add(mission.getId()+"");
                } else if (mission.getStat() == Mission.StatsMission.COMPLETED) {
                    completedSeasonMissions.add(mission.getId()+"");
                }
            });

            Utils.configPlayers.getConfig().set("players."+uuid+".active_missions.daily", activeDailyMissions);
            Utils.configPlayers.getConfig().set("players."+uuid+".active_missions.season", activeSeasonMissions);

            Utils.configPlayers.getConfig().set("players."+uuid+".completed_missions.daily", completedDailyMissions);
            Utils.configPlayers.getConfig().set("players."+uuid+".completed_missions.season", completedSeasonMissions);
        });

        Utils.configPlayers.saveConfig();
    }

    public static JavaPlugin getPlugin() { return plugin; }
}
