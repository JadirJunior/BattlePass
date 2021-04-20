package com.battle.battlepass.battlepass.utils;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.config.*;
import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.missions.BreakBlockMission;
import com.battle.battlepass.battlepass.missions.KillMission;
import com.battle.battlepass.battlepass.missions.PlaceBlockMission;
import com.battle.battlepass.battlepass.references.Mission;
import com.battle.battlepass.battlepass.references.PlayerModel;
import com.battle.battlepass.battlepass.references.Tier;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Utils {

    public static ConfigMenus configMenus;
    public static TierConfig configTier;
    public static ConfigPlayers configPlayers;
    public static ConfigDailyMissions configDailyMissions;
    public static ConfigSeasonMissions configSeasonMissions;

    public static void initialize() {
        initializeConfigMenus();
        initializeConfigTier();
        initializeConfigDailyMissions();
        initializeConfigSeasonMissions();
        initializeConfigPlayers();

        BattlePass.missions.forEach((id, mission) -> {
            Bukkit.getConsoleSender().sendMessage("DAILY -> id -> " + id + " name:"+mission.getName()+" type:"+mission.getType());
        });

        BattlePass.seasonMissions.forEach((id, mission) -> {
            Bukkit.getConsoleSender().sendMessage("SEASON -> id -> " + id + " name:"+mission.getName()+" type:"+mission.getType());
        });

    }

    private static void initializeConfigMenus() {
        configMenus = new ConfigMenus(BattlePass.getPlugin(), "menus.yml");
        configMenus.saveConfig();
        try {
            Set<String> menus = configMenus.getSection("menus").getKeys(false);
            menus.forEach(menu -> {
                if (BattlePass.inventories.get(menu) != null) {
                    Inventory invMain = BattlePass.inventories.get(menu).getInventory();
                    Set<String> items = configMenus.getSection("menus."+menu).getKeys(false);
                    items.forEach(item -> {
                        String name = configMenus.getConfig().getString("menus."+menu+"."+item+".name");
                        int slot = (int) configMenus.getConfig().get("menus."+menu+"."+item+".slot");
                        int idItem = (int) configMenus.getConfig().get("menus."+menu+"."+item+".item.id");
                        int dataItem = (int) configMenus.getConfig().get("menus."+menu+"."+item+".item.data");
                        List<String> lore = configMenus.getConfig().getStringList("menus."+menu+"."+item+".item.lore");
                        String target;

                        if (configMenus.getConfig().get("menus."+menu+"."+item+".target") == null || configMenus.getConfig().get("menus."+menu+"."+item+".target").equals("")) {
                            target = "";
                        } else {
                            target = configMenus.getConfig().getString("menus."+menu+"."+item+".target");
                        }
                        ItemBuilder builder = new ItemBuilder(name.replace("&", "§"), idItem, dataItem, target == null ? "" : target);
                        builder.setLore(lore);
                        BattlePass.itemsActions.put(name.replace("&", "§"), builder);
                        invMain.setItem(slot, builder.getItem());
                    });
                }
            });
            configMenus.saveConfig();
        } catch(Exception err) {}
    }

    private static void initializeConfigTier() {
        configTier = new TierConfig(BattlePass.getPlugin(), "tier.yml");
        configTier.saveConfig();
        try {
            Set<String> tiers = configTier.getSection("tier").getKeys(false);
            tiers.forEach(tier -> {
                int exp = (int) configTier.getConfig().get("tier."+tier+".exp");
                boolean useCommands = configTier.getConfig().getBoolean("tier."+tier+".recompensas.useCommands");
                boolean useItem = configTier.getConfig().getBoolean("tier."+tier+".recompensas.useItem");
                List<String> commands = null;
                String itemName;
                int amount = 0;
                int idItem;
                byte data;
                List<String> lore;
                ItemBuilder builder = null;
                if (useCommands) {
                    commands = configTier.getConfig().getStringList("tier."+tier+".recompensas.commands");
                }
                if (useItem) {
                    itemName = configTier.getConfig().getString("tier."+tier+".recompensas.item.name");
                    amount = configTier.getConfig().getInt("tier."+tier+".recompensas.item.amount");
                    idItem = configTier.getConfig().getInt("tier."+tier+".recompensas.item.id");
                    data = (byte) configTier.getConfig().getInt("tier."+tier+".recompensas.item.data");
                    lore = configTier.getConfig().getStringList("tier."+tier+".recompensas.item.lore");
                    builder = new ItemBuilder(itemName, idItem, data, "");
                    builder.setLore(lore);
                }

                BattlePass.tiers.put(tier, new Tier(exp, builder == null ? null : builder.getItem(), commands, amount));
            });
            InventoryBuilder invTiers = new InventoryBuilder(9*5, "Tier");
            BattlePass.tiers.forEach((nameTier, tier) -> {
                int data = 7;
                invTiers.getInventory().addItem(new ItemBuilder("Tier "+nameTier, 160, (byte) data).getItem());
            });
            BattlePass.inventories.put("Tier", invTiers);
        } catch (Exception err) {}
    }

    private static void initializeConfigPlayers() {
        configPlayers = new ConfigPlayers(BattlePass.getPlugin(), "players.yml");
        configPlayers.saveConfig();
        try {
            Set<String> playersUUID = configPlayers.getSection("players").getKeys(false);
            playersUUID.forEach(uuid -> {

                int exp =configPlayers.getConfig().getInt("players."+uuid+".exp");
                String tier =configPlayers.getConfig().getString("players."+uuid+".tier");

                List<String> active_daily_missions = configPlayers.getConfig().getStringList("players."+uuid+".active_missions.daily");
                List<String> active_season_missions = configPlayers.getConfig().getStringList("players."+uuid+".active_missions.season");

                List<String> completed_daily_missions = configPlayers.getConfig().getStringList("players."+uuid+".completed_missions.daily");
                List<String> completed_season_missions = configPlayers.getConfig().getStringList("players."+uuid+".completed_missions.season");
                
                
                List<Mission> dailyMissionsActive = new ArrayList<>();
                List<Mission> seasonMissionsActive = new ArrayList<>();

                List<Mission> dailyMissionsCompleted = new ArrayList<>();
                List<Mission> seasonMissionsCompleted = new ArrayList<>();
                
                if (BattlePass.missions.size() != 0 && BattlePass.seasonMissions.size() != 0) {
                    Mission.StatsMission statsActive = Mission.StatsMission.ACTIVE;
                    Mission.StatsMission statsCompleted = Mission.StatsMission.COMPLETED;

                    active_daily_missions.forEach(id -> {
                        try {
                            Mission mission = BattlePass.missions.get(Integer.parseInt(id));
                            mission.setStats(statsActive);
                            dailyMissionsActive.add(mission);
                        } catch (Exception err) {
                            BattlePass.getPlugin().getLogger().warning("A missao do player " +uuid+" com id "+id+" nao existe mais.");
                        }

                    });

                    active_season_missions.forEach(id -> {
                        try {
                            Mission mission = BattlePass.seasonMissions.get(Integer.parseInt(id));
                            mission.setStats(statsActive);
                            seasonMissionsActive.add(mission);
                        } catch (Exception err) {
                            BattlePass.getPlugin().getLogger().warning("A missao do player " +uuid+" com id "+id+" nao existe mais.");
                        }

                    });

                    completed_daily_missions.forEach(id -> {
                        try {
                            Mission mission = BattlePass.missions.get(Integer.parseInt(id));
                            mission.setStats(statsCompleted);
                            dailyMissionsCompleted.add(mission);
                        } catch (Exception err) {
                            BattlePass.getPlugin().getLogger().warning("A missao do player " +uuid+" com id "+id+" nao existe mais.");
                        }
                    });
                    
                    completed_season_missions.forEach(id -> {
                        try {
                            Mission mission = BattlePass.seasonMissions.get(Integer.parseInt(id));
                            mission.setStats(statsCompleted);
                            seasonMissionsCompleted.add(mission);
                        } catch (Exception err) {
                            BattlePass.getPlugin().getLogger().warning("A missao do player " +uuid+" com id "+id+" nao existe mais.");
                        }

                    });
                }
                seasonMissionsActive.addAll(seasonMissionsCompleted);
                dailyMissionsActive.addAll(dailyMissionsCompleted);

                List<Mission> playerDailyMissions = dailyMissionsActive;
                List<Mission> playerSeasonMissions = seasonMissionsActive;

                playerSeasonMissions.forEach(mission -> {
                    Bukkit.getConsoleSender().sendMessage(mission.getName() + " - " + mission.getTypeQuest() + " - " + mission.getStat());
                });

                Bukkit.getConsoleSender().sendMessage("Iniciando missoes diarias");

                playerDailyMissions.forEach(mission -> {
                    Bukkit.getConsoleSender().sendMessage(mission.getName() + " - " + mission.getTypeQuest() + " - " + mission.getStat());
                });

                BattlePass.players.put(uuid, new PlayerModel(uuid, exp, tier, playerDailyMissions, playerSeasonMissions));
            });

        } catch (Exception err) {
            BattlePass.getPlugin().getLogger().warning("Ocorreu um erro inesperado");
        }
    }

    private static void initializeConfigDailyMissions() {
        configDailyMissions = new ConfigDailyMissions(BattlePass.getPlugin(), "daily-missions.yml");
        configDailyMissions.saveConfig();

        try {
            Set<String> missions = configDailyMissions.getSection("missions").getKeys(false);
            if (missions != null) {

                missions.forEach(id -> {
                    int idNumber = Integer.parseInt(id);
                    String name = configDailyMissions.getString("missions."+id+".name");
                    String type = configDailyMissions.getString("missions."+id+".type");
                    int exp = configDailyMissions.getConfig().getInt("missions."+id+".exp");

                    if (type.equalsIgnoreCase("kill")) {
                        int entities = configDailyMissions.getConfig().getInt("missions."+id+".entities");
                        String name_entity = configDailyMissions.getString("missions."+id+".name_entity");
                        BattlePass.missions.put(idNumber, new KillMission(MissionType.KILL, name, idNumber, entities, name_entity, exp, TypeQuest.DAILY));
                    }

                    else if (type.equalsIgnoreCase("place") || type.equalsIgnoreCase("break")) {
                        int blocks = configDailyMissions.getConfig().getInt("missions."+id+".blocks");
                        int idBlock = configDailyMissions.getConfig().getInt("missions."+id+".block.id");
                        int data = configDailyMissions.getConfig().getInt("missions."+id+".block.data");

                        ItemStack builder = new ItemBuilder(idBlock, data).getItem();
                        if (builder.getType().isBlock()) {
                            if (type.equalsIgnoreCase("place")) {
                                BattlePass.missions.put(idNumber, new PlaceBlockMission(MissionType.PLACE, name, idNumber, blocks, builder, exp, TypeQuest.DAILY));
                            } else if (type.equalsIgnoreCase("break")) {
                                BattlePass.missions.put(idNumber, new BreakBlockMission(MissionType.BREAK, name, idNumber, blocks, builder, exp, TypeQuest.DAILY));
                            }
                        }
                        else {
                            BattlePass.getPlugin().getLogger().warning("O item informado na quest " + id + " nao e um bloco.");
                        }

                    }
                });

            }

        } catch (Exception err) {
            BattlePass.getPlugin().getLogger().warning("Ocorreu um erro ao carregar as missões diárias");
        }

    }

    private static void initializeConfigSeasonMissions() {
        configSeasonMissions = new ConfigSeasonMissions(BattlePass.getPlugin(), "seasons.yml");
        configSeasonMissions.saveConfig();

        try {
            String seasonName = configSeasonMissions.getString("season");
            if (seasonName != null) {
                Set<String> missions = configSeasonMissions.getSection("seasons."+seasonName+".missions").getKeys(false);

                if (missions != null) {
                    missions.forEach(id -> {
                        int idNumber = Integer.parseInt(id);
                        String name = configSeasonMissions.getString("seasons."+seasonName+".missions."+id+".name");
                        String type = configSeasonMissions.getString("seasons."+seasonName+".missions."+id+".type");
                        int exp = configSeasonMissions.getConfig().getInt("seasons."+seasonName+".missions."+id+".exp");

                        if (type.equalsIgnoreCase("kill")) {
                            int entities = configSeasonMissions.getConfig().getInt("seasons."+seasonName+".missions."+id+".entities");
                            String name_entity = configSeasonMissions.getString("seasons."+seasonName+".missions."+id+".name_entity");
                            BattlePass.seasonMissions.put(idNumber, new KillMission(MissionType.KILL, name, idNumber, entities, name_entity, exp, TypeQuest.SEASON));
                        }

                        else if (type.equalsIgnoreCase("place") || type.equalsIgnoreCase("break")) {
                            int blocks = configSeasonMissions.getConfig().getInt("seasons."+seasonName+".missions."+id+".blocks");
                            int idBlock = configSeasonMissions.getConfig().getInt("seasons."+seasonName+".missions."+id+".block.id");
                            int data = configSeasonMissions.getConfig().getInt("seasons."+seasonName+".missions."+id+".block.data");

                            ItemStack builder = new ItemBuilder(idBlock, data).getItem();

                            if (builder.getType().isBlock()) {
                                if (type.equalsIgnoreCase("place")) {
                                    BattlePass.seasonMissions.put(idNumber, new PlaceBlockMission(MissionType.PLACE, name, idNumber, blocks, builder, exp, TypeQuest.SEASON));
                                } else if (type.equalsIgnoreCase("break")) {
                                    BattlePass.seasonMissions.put(idNumber, new BreakBlockMission(MissionType.BREAK, name, idNumber, blocks, builder, exp, TypeQuest.SEASON));
                                }
                            }
                            else {
                                BattlePass.getPlugin().getLogger().warning("O item informado na quest " + id + " não é um bloco.");
                            }
                        }
                    });

                }
            }
        } catch (Exception err) {
            BattlePass.getPlugin().getLogger().warning("Ocorreu um erro ao carregar a config de missões de season");
        }


    }

}
