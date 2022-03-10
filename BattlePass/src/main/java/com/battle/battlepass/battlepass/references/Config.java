package com.battle.battlepass.battlepass.references;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.battle.battlepass.battlepass.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {
	
	private  JavaPlugin plugin;
	private File file;
	private YamlConfiguration config;
	private String name;

	public Config(JavaPlugin plugin, String nameFile) {
		this.plugin = plugin;
		setName(nameFile);
		reloadConfig();
	}
	
	
	public YamlConfiguration getConfig() {
		return this.config;
	}
	
	public void saveConfig() {
		try {
		getConfig().save(getFile());
		} catch (IOException e) {
		e.printStackTrace();
	}
	}

	public ConfigurationSection getSection(String path) {
		return getConfig().getConfigurationSection(path);
	}
	
	
	public String getString(String path) {
		return getConfig().getString(path);
	}
	
	
	public void saveDefaultConfig() {
		getPlugin().saveResource(name, false);
	}
	
	public JavaPlugin getPlugin() {
		return this.plugin;
	}
	
	private void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void reloadConfig() {
		file = new File(getPlugin().getDataFolder(), name);
		if (!file.exists()) {
			InputStream inputStream = getPlugin().getResource(getName());
			InputStreamReader reader = new InputStreamReader(inputStream);
			config = YamlConfiguration.loadConfiguration(reader);
			saveConfig();
		} else {
			config = YamlConfiguration.loadConfiguration(getFile());
		}
	}
	
	public File getFile() {
		return this.file;
	}
	
	protected List<String> getListString(String path) {
		return getConfig().getStringList(path);
	}
	
}
