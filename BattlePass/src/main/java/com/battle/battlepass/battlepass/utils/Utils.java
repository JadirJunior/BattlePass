package com.battle.battlepass.battlepass.utils;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.config.*;
import com.battle.battlepass.battlepass.database.DatabaseConnection;
import com.battle.battlepass.battlepass.database.SavePlayerData;
import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import com.battle.battlepass.battlepass.missions.*;
import com.battle.battlepass.battlepass.references.Config;
import com.battle.battlepass.battlepass.references.Mission;
import com.battle.battlepass.battlepass.references.PlayerModel;
import com.battle.battlepass.battlepass.references.Tier;
import com.battle.battlepass.battlepass.tasks.AutoSavePlayers;
import com.battle.battlepass.battlepass.tasks.ResetDailyMissions;
import com.battle.battlepass.battlepass.tasks.SeasonUpdate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Utils {

    public static ConfigMenus configMenus;
    public static TierConfig configTier;
    public static ConfigPlayers configPlayers;
    public static ConfigDailyMissions configDailyMissions;
    public static ConfigSeasonMissions configSeasonMissions;
    public static Config config;

    public static void initialize() {
        initializeConfigMenus();
        initializeConfigTier();
        initializeConfigDailyMissions();
        initializeConfigSeasonMissions();
        initializeConfigPlayers();
        initializeConfig();
        initializeMySQL();

        if (BattlePass.autoUpdateSeason) {

            Bukkit.getScheduler().runTaskTimer(BattlePass.getPlugin(), new SeasonUpdate(),0, 20*500);

        }
    }

    public static void reloadConfigs() {
        Bukkit.getScheduler().cancelAllTasks();
        BattlePass.missions.clear();
        BattlePass.seasonMissions.clear();
        BattlePass.tiers.clear();
        BattlePass.itemsActions.clear();
        initialize();
        Bukkit.getScheduler().runTaskTimer(BattlePass.getPlugin(), new SeasonUpdate(),0, 20*500);
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
                        boolean headItem = configMenus.getConfig().getBoolean("menus."+menu+"."+item+".head");
                        String name = configMenus.getConfig().getString("menus."+menu+"."+item+".name");
                        int slot = (int) configMenus.getConfig().get("menus."+menu+"."+item+".slot");
                        List<String> lore = configMenus.getConfig().getStringList("menus."+menu+"."+item+".item.lore");
                        String target;

                        if (configMenus.getConfig().get("menus."+menu+"."+item+".target") == null || configMenus.getConfig().get("menus."+menu+"."+item+".target").equals("")) {
                            target = "";
                        } else {
                            target = configMenus.getConfig().getString("menus."+menu+"."+item+".target");
                        }

                        if (headItem) {
                            String headPlayer = configMenus.getString("menus."+menu+"."+item+".headPlayer");
                            ItemBuilder builder = new ItemBuilder(getPlayerHead(headPlayer), target == null ? "" : target, ChatUtil.format(name));
                            builder.setLore(lore);
                            BattlePass.itemsActions.put(ChatUtil.format(name), builder);
                            invMain.setItem(slot, builder.getItem());

                        }
                        else {

                            int idItem = (int) configMenus.getConfig().get("menus."+menu+"."+item+".item.id");
                            int dataItem = (int) configMenus.getConfig().get("menus."+menu+"."+item+".item.data");

                            ItemBuilder builder = new ItemBuilder(name.replace("&", "§"), idItem, dataItem, target == null ? "" : target);
                            builder.setLore(lore);
                            BattlePass.itemsActions.put(name.replace("&", "§"), builder);
                            invMain.setItem(slot, builder.getItem());

                        }

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
                boolean isPremium = configTier.getConfig().getBoolean("tier."+tier+".isPremium");
                String name = configTier.getString("tier."+tier+".name");
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

                BattlePass.tiers.put(tier, new Tier(exp, builder == null ? null : builder.getItem(), commands, amount, Integer.parseInt(tier), name, isPremium));

            });

            InventoryBuilder invTiers = new InventoryBuilder(9*5, "Tier");

            BattlePass.tiers.forEach((nameTier, tier) -> {
                int data = 7;
                invTiers.getInventory().addItem(new ItemBuilder(nameTier, 160, (byte) data).getItem());
            });

            BattlePass.inventories.put("Tier", invTiers);
        } catch (Exception err) {err.printStackTrace();}
    }

    private static void initializeConfigPlayers() {
        configPlayers = new ConfigPlayers(BattlePass.getPlugin(), "players.yml");
        configPlayers.saveConfig();
        try {
            Object has = configPlayers.getConfig().get("players");
            if (has == null) return;

            Set<String> playersUUID = configPlayers.getSection("players").getKeys(false);

            playersUUID.forEach(uuid -> {
                boolean isPremium = configPlayers.getConfig().getBoolean("players."+uuid+".isPremium");
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

                BattlePass.players.put(uuid, new PlayerModel(uuid, exp, tier, playerDailyMissions, playerSeasonMissions, isPremium));
            });

        } catch (Exception err) { err.printStackTrace(); }
    }

    private static void initializeConfigDailyMissions() {
        configDailyMissions = new ConfigDailyMissions(BattlePass.getPlugin(), "daily-missions.yml");
        configDailyMissions.saveConfig();
        try {
            Set<String> missions = configDailyMissions.getSection("missions").getKeys(false);
            if (missions != null) {

                missions.forEach(id -> {
                    boolean isPremium = configDailyMissions.getConfig().getBoolean("missions."+id+".isPremium");
                    int idNumber = Integer.parseInt(id);
                    String name = configDailyMissions.getString("missions."+id+".name");
                    String type = configDailyMissions.getString("missions."+id+".type");
                    int exp = configDailyMissions.getConfig().getInt("missions."+id+".exp");

                    List<String> description = configDailyMissions.getConfig().getStringList("missions."+id+".description");

                    if (type.equalsIgnoreCase("kill")) {
                        int entities = configDailyMissions.getConfig().getInt("missions."+id+".entities");
                        String name_entity = configDailyMissions.getString("missions."+id+".name_entity");
                        BattlePass.missions.put(idNumber, new KillMission(MissionType.KILL, name, idNumber, entities, name_entity, exp, TypeQuest.DAILY, description, isPremium));
                    }

                    else if (type.equalsIgnoreCase("place") || type.equalsIgnoreCase("break")) {
                        int blocks = configDailyMissions.getConfig().getInt("missions."+id+".blocks");
                        int idBlock = configDailyMissions.getConfig().getInt("missions."+id+".block.id");
                        int data = configDailyMissions.getConfig().getInt("missions."+id+".block.data");

                        ItemStack builder = new ItemBuilder(idBlock, data).getItem();
                        if (builder.getType().isBlock()) {
                            if (type.equalsIgnoreCase("place")) {
                                BattlePass.missions.put(idNumber, new PlaceBlockMission(MissionType.PLACE, name, idNumber, blocks, builder, exp, TypeQuest.DAILY, description, isPremium));
                            } else if (type.equalsIgnoreCase("break")) {
                                BattlePass.missions.put(idNumber, new BreakBlockMission(MissionType.BREAK, name, idNumber, blocks, builder, exp, TypeQuest.DAILY, description, isPremium));
                            }
                        }
                        else {
                            BattlePass.getPlugin().getLogger().warning("O item informado na quest " + id + " nao e um bloco.");
                        }

                    } else if (type.equalsIgnoreCase("chat")) {
                        String message = configDailyMissions.getString("missions."+id+".message");
                        BattlePass.missions.put(idNumber, new SendMessageMission(MissionType.CHAT, name, idNumber, exp, TypeQuest.DAILY, description, message, isPremium));
                    } else if (type.equalsIgnoreCase("fish")) {
                        int quant = configDailyMissions.getConfig().getInt("missions."+id+".fishes");

                        BattlePass.missions.put(idNumber, new GetFishMission(MissionType.FISH, name, idNumber, quant, exp, TypeQuest.DAILY, description, isPremium));
                    } else if (type.equalsIgnoreCase("consume")) {
                        int quant = configDailyMissions.getConfig().getInt("missions."+id+".quant");
                        int idItem = configDailyMissions.getConfig().getInt("missions."+id+".item.id");
                        int data = configDailyMissions.getConfig().getInt("missions."+id+".item.data");

                        ItemStack builder = new ItemBuilder(idItem, data).getItem();

                        if (builder.getType().isBlock()) {
                            BattlePass.getPlugin().getLogger().warning("O item informado na quest " + id + " e um bloco.");
                            return;
                        }

                        BattlePass.missions.put(idNumber, new ConsumeItemMission(MissionType.CONSUME, name, idNumber, quant, exp, TypeQuest.DAILY, description, builder, isPremium));
                    } else if (type.equalsIgnoreCase("walk")) {
                        int distance = configDailyMissions.getConfig().getInt("missions."+id+".distance");

                        BattlePass.missions.put(idNumber, new WalkDistanceMission(MissionType.WALK, name, idNumber, exp, TypeQuest.DAILY, description, isPremium, distance));
                    } else if (type.equalsIgnoreCase("milk")) {
                        int times = configDailyMissions.getConfig().getInt("missions."+id+".times");

                        BattlePass.missions.put(idNumber, new MilkCowMission(MissionType.MILK, name, idNumber, exp, TypeQuest.DAILY, description, isPremium, times));
                    } else if (type.equalsIgnoreCase("exp")) {
                        int value = configDailyMissions.getConfig().getInt("missions."+id+".value");

                        BattlePass.missions.put(idNumber, new GetExpMission(MissionType.EXP, name, idNumber, exp, TypeQuest.DAILY, description, isPremium, value));
                    } else if (type.equalsIgnoreCase("craft")) {
                        int idBlock = configDailyMissions.getConfig().getInt("missions."+id+".item.id");
                        int data = configDailyMissions.getConfig().getInt("missions."+id+".item.data");

                        ItemStack builder = new ItemBuilder(idBlock, data).getItem();

                        BattlePass.missions.put(idNumber, new CraftItemMission(MissionType.CRAFT, name, idNumber, exp, TypeQuest.DAILY, description, isPremium, builder));
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
                BattlePass.season = seasonName;
                BattlePass.autoUpdateSeason = configSeasonMissions.getConfig().getBoolean("update");
                if (BattlePass.autoUpdateSeason) {

                    BattlePass.durationSeason = configSeasonMissions.getConfig().getInt("duration");
                    BattlePass.dataStartSeason = configSeasonMissions.getString("start");
                }

                Set<String> missions = configSeasonMissions.getSection("seasons."+seasonName+".missions").getKeys(false);

                if (missions != null) {
                    missions.forEach(id -> {
                        boolean isPremium = configSeasonMissions.getConfig().getBoolean("seasons."+seasonName+".missions."+id+".isPremium");
                        int idNumber = Integer.parseInt(id);
                        String name = configSeasonMissions.getString("seasons."+seasonName+".missions."+id+".name");
                        String type = configSeasonMissions.getString("seasons."+seasonName+".missions."+id+".type");
                        int exp = configSeasonMissions.getConfig().getInt("seasons."+seasonName+".missions."+id+".exp");

                        List<String> description = configSeasonMissions.getConfig().getStringList("seasons."+seasonName+".missions."+id+".description");

                        if (type.equalsIgnoreCase("kill")) {
                            int entities = configSeasonMissions.getConfig().getInt("seasons."+seasonName+".missions."+id+".entities");
                            String name_entity = configSeasonMissions.getString("seasons."+seasonName+".missions."+id+".name_entity");
                            BattlePass.seasonMissions.put(idNumber, new KillMission(MissionType.KILL, name, idNumber, entities, name_entity, exp, TypeQuest.SEASON, description, isPremium));
                        }

                        else if (type.equalsIgnoreCase("place") || type.equalsIgnoreCase("break")) {
                            int blocks = configSeasonMissions.getConfig().getInt("seasons."+seasonName+".missions."+id+".blocks");
                            int idBlock = configSeasonMissions.getConfig().getInt("seasons."+seasonName+".missions."+id+".block.id");
                            int data = configSeasonMissions.getConfig().getInt("seasons."+seasonName+".missions."+id+".block.data");

                            ItemStack builder = new ItemBuilder(idBlock, data).getItem();

                            if (builder.getType().isBlock()) {
                                if (type.equalsIgnoreCase("place")) {
                                    BattlePass.seasonMissions.put(idNumber, new PlaceBlockMission(MissionType.PLACE, name, idNumber, blocks, builder, exp, TypeQuest.SEASON, description, isPremium));
                                } else if (type.equalsIgnoreCase("break")) {
                                    BattlePass.seasonMissions.put(idNumber, new BreakBlockMission(MissionType.BREAK, name, idNumber, blocks, builder, exp, TypeQuest.SEASON, description, isPremium));
                                }
                            }
                            else {
                                BattlePass.getPlugin().getLogger().warning("O item informado na quest " + id + " não é um bloco.");
                            }
                        }

                        else if (type.equalsIgnoreCase("chat")) {

                            String message = configSeasonMissions.getString("seasons."+seasonName+".missions."+id+".message");
                            BattlePass.seasonMissions.put(idNumber, new SendMessageMission(MissionType.CHAT, name, idNumber, exp, TypeQuest.SEASON, description, message, isPremium));

                        } else if (type.equalsIgnoreCase("fish")) {
                            int quant = configSeasonMissions.getConfig().getInt("seasons."+seasonName+".missions."+id+".fishes");

                            BattlePass.seasonMissions.put(idNumber, new GetFishMission(MissionType.FISH, name, idNumber, quant, exp, TypeQuest.SEASON, description, isPremium));
                        } else if (type.equalsIgnoreCase("consume")) {
                            int quant = configSeasonMissions.getConfig().getInt("seasons."+seasonName+".missions."+id+".quant");
                            int idItem = configSeasonMissions.getConfig().getInt("seasons."+seasonName+".missions."+id+".item.id");
                            int data = configSeasonMissions.getConfig().getInt("seasons."+seasonName+".missions."+id+".item.data");

                            ItemStack builder = new ItemBuilder(idItem, data).getItem();

                            if (builder.getType().isBlock()) {
                                BattlePass.getPlugin().getLogger().warning("O item informado na quest " + id + " e um bloco.");
                                return;
                            }

                            BattlePass.seasonMissions.put(idNumber, new ConsumeItemMission(MissionType.CONSUME, name, idNumber, quant, exp, TypeQuest.SEASON, description, builder, isPremium));
                        } else if (type.equalsIgnoreCase("walk")) {
                            int distance = configSeasonMissions.getConfig().getInt("seasons."+seasonName+".missions."+id+".distance");

                            BattlePass.seasonMissions.put(idNumber, new WalkDistanceMission(MissionType.WALK, name, idNumber, exp, TypeQuest.SEASON, description, isPremium, distance));
                        } else if (type.equalsIgnoreCase("milk")) {
                            int times = configSeasonMissions.getConfig().getInt("seasons."+seasonName+".missions."+id+".times");

                            BattlePass.seasonMissions.put(idNumber, new MilkCowMission(MissionType.MILK, name, idNumber, exp, TypeQuest.SEASON, description, isPremium, times));
                        } else if (type.equalsIgnoreCase("exp")) {
                            int value = configSeasonMissions.getConfig().getInt("seasons."+seasonName+".missions."+id+".value");

                            BattlePass.seasonMissions.put(idNumber, new GetExpMission(MissionType.EXP, name, idNumber, exp, TypeQuest.SEASON, description, isPremium, value));
                        } else if (type.equalsIgnoreCase("craft")) {
                            int idBlock = configSeasonMissions.getConfig().getInt("seasons."+seasonName+".missions."+id+".item.id");
                            int data = configSeasonMissions.getConfig().getInt("seasons."+seasonName+".missions."+id+".item.data");

                            ItemStack builder = new ItemBuilder(idBlock, data).getItem();

                            BattlePass.seasonMissions.put(idNumber, new CraftItemMission(MissionType.CRAFT, name, idNumber, exp, TypeQuest.SEASON, description, isPremium, builder));
                        }

                    });

                }
            }

        } catch (Exception err) {
            BattlePass.getPlugin().getLogger().warning("Ocorreu um erro ao carregar a config de missões de season");
        }


    }

    private static void initializeConfig() {
        config = new Config(BattlePass.getPlugin(), "config.yml");
        config.saveConfig();
        try {
            BattlePass.last_update = config.getString("last_update");
            BattlePass.previuousPageSlot = config.getConfig().getInt("page_items.previous_page");
            BattlePass.pageSlot = config.getConfig().getInt("page_items.page");
            BattlePass.nextPageSlot = config.getConfig().getInt("page_items.next_page");

            if (config.getConfig().getBoolean("auto_save")) {
                int period = config.getConfig().getInt("temp_auto_save");
                Bukkit.getScheduler().runTaskTimer(BattlePass.getPlugin(), new AutoSavePlayers(), period*20L, period* 20L);
            }
            Bukkit.getScheduler().runTaskTimer(BattlePass.getPlugin(), new ResetDailyMissions(), 20*10, 20*100); //A cada 2000 segundos.
        } catch(Exception err) {}

    }

    private static void initializeMySQL() {
        if (config.getConfig().getBoolean("my-sql")) {
            BattlePass.useMySQL = true;
            DatabaseConnection.database = config.getString("database");
            DatabaseConnection.host = config.getString("host");
            DatabaseConnection.port = config.getString("port");
            DatabaseConnection.password = config.getString("password");
            DatabaseConnection.username = config.getString("username");
        }
    }

    public static void savePlayers() {
        if (BattlePass.players == null || BattlePass.players.size() == 0) { return; }
        BattlePass.players.forEach((uuid, model) -> {
            Utils.configPlayers.getConfig().set("players."+uuid+".exp", model.getExp());
            Utils.configPlayers.getConfig().set("players."+uuid+".tier", model.getTier());
            Utils.configPlayers.getConfig().set("players."+uuid+".isPremium", model.isPremium());

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

        if (BattlePass.useMySQL) {
            new SavePlayerData().save();
        }
    }

    @SuppressWarnings("decreption")
    public static ItemStack getPlayerHead(String player) {

        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());

        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();

        skullMeta.setOwner(player);
        skullMeta.setDisplayName(player);

        item.setItemMeta(skullMeta);

        return item;
    }


}