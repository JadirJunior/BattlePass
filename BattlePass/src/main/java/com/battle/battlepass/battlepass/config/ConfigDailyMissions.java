package com.battle.battlepass.battlepass.config;

import com.battle.battlepass.battlepass.enums.MissionType;
import com.battle.battlepass.battlepass.references.Config;
import com.battle.battlepass.battlepass.references.Mission;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigDailyMissions extends Config {


    public ConfigDailyMissions(JavaPlugin plugin, String nameFile) {
        super(plugin, nameFile);
    }
}
