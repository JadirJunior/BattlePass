package com.battle.battlepass.battlepass.references;

import com.battle.battlepass.battlepass.BattlePass;
import com.battle.battlepass.battlepass.enums.TypeQuest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerModel {
    private Player player;
    private int exp;
    private String uuid;
    private String tier;
    private boolean isPremium;

    private List<Mission> seasonMissions = new ArrayList<>();
    private List<Mission> missionsDaily = new ArrayList<>();

    public PlayerModel(Player player, int exp, String tier, List<Mission> dailyMissions, List<Mission> seasonMissions) {
        this.player= player;
        this.exp = exp;
        this.missionsDaily = dailyMissions;
        this.tier = tier;
        this.seasonMissions = seasonMissions;
    }

    public PlayerModel(String uuid, int exp, String tier, List<Mission> dailyMissions, List<Mission> seasonMissions) {
        this.uuid = uuid;
        this.exp = exp;
        this.missionsDaily = dailyMissions;
        this.seasonMissions = seasonMissions;
        this.tier = tier;
    }

    public PlayerModel(String uuid, int exp, String tier, List<Mission> dailyMissions, List<Mission> seasonMissions, boolean isPremium) {
        this.uuid = uuid;
        this.exp = exp;
        this.missionsDaily = dailyMissions;
        this.seasonMissions = seasonMissions;
        this.tier = tier;
        this.isPremium = isPremium;
    }

    public PlayerModel(String uuid, int exp, String tier) {
        this.uuid = uuid;
        this.exp = exp;
        this.tier = tier;
    }

    public int getExp() {
        return exp;
    }

    public Player getPlayer() {
        if (player == null) {
            try {
                return Bukkit.getPlayer(uuid);
            } catch (Exception err) {
                return null;
            }
        }
        return player;
    }

    public List<Mission> getMissionsDaily() {
        return missionsDaily;
    }

    public List<Mission> getSeasonMissions() { return seasonMissions; }

    public void setIsPremium(boolean isPremium) { this.isPremium = isPremium; }

    public boolean isPremium() { return isPremium; }

    public String getTier() {
        return tier;
    }

    public Serializable addMission(Mission mission) {

        if (mission.getTypeQuest() == TypeQuest.DAILY) {
            if (getMissionsDaily().contains(mission)) {
                return false;
            }
            getMissionsDaily().add(mission);
            return true;
        } else if (mission.getTypeQuest() ==TypeQuest.SEASON) {
            if (getSeasonMissions().contains(mission)) {
                return false;
            }
            getSeasonMissions().add(mission);
            return true;
        }


        return null;
    }

    public Serializable startQuest(int id, TypeQuest typeQuest) {
        if (typeQuest == null) {
            return null;
        }

        if (typeQuest == TypeQuest.DAILY) {
            Mission daily = BattlePass.missions.get(id);
            if (daily != null){
                return addMission(daily);
            }
            return null;
        } else if (typeQuest == TypeQuest.SEASON) {
            Mission season = BattlePass.seasonMissions.get(id);
            if (season != null) {
                return addMission(season);
            }
            return null;
        }
        return null;
    }


    public List<Mission> getActiveQuests() {
        List<Mission> season = getActiveSeasonMissions();
        List<Mission> daily = getActiveDailyMissions();


        List<Mission> allActives = new ArrayList<>();
        allActives.addAll(season);
        allActives.addAll(daily);
        return allActives;
    }

    public List<Mission> getActiveSeasonMissions() {
        List<Mission> seasonActivesMissions = new ArrayList<>();
        getSeasonMissions().forEach(season -> {
            if (season.getStat() == Mission.StatsMission.ACTIVE) {
                seasonActivesMissions.add(season);
            }
        });
        return seasonActivesMissions;
    }

    public List<Mission> getActiveDailyMissions() {
        List<Mission> dailyActivesMissions = new ArrayList<>();
        getMissionsDaily().forEach(daily -> {
            if (daily.getStat() == Mission.StatsMission.ACTIVE) {
                dailyActivesMissions.add(daily);
            }
        });
        return dailyActivesMissions;
    }

    public void completeQuest(Mission mission) {
        if (mission.getTypeQuest() == TypeQuest.DAILY) {
            getMissionsDaily().get(getMissionsDaily().indexOf(mission)).setStats(Mission.StatsMission.COMPLETED);
            this.setExp(getExp()+mission.getExp());
        }
        else if (mission.getTypeQuest() == TypeQuest.SEASON) {
            getSeasonMissions().get(getSeasonMissions().indexOf(mission)).setStats(Mission.StatsMission.COMPLETED);
            this.setExp(getExp()+mission.getExp());
        }
    }

    public void setExp(int exp) {
        this.exp = exp < 0 ? 0 : exp;
    }

    public void setTier(int tier) {
        this.tier = tier<0 ? "0" : tier+"";
    }

}
