package com.cubepalace.marry.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.cubepalace.marry.Marry;

public class CouplesFile {

	private Marry plugin;
	private File file;
	private FileConfiguration config;

	public CouplesFile(Marry plugin, String fileName) {
		this.plugin = plugin;
		file = new File(plugin.getDataFolder(), fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				plugin.getLogger().warning("Could not create file " + file.getName());
				e.printStackTrace();
			}
		}
		config = YamlConfiguration.loadConfiguration(file);
	}

	public void save() {
		try {
			config.save(file);
		} catch (IOException e) {
			plugin.getLogger().warning("Could not save file " + file.getName());
			e.printStackTrace();
		}
	}

	public void reload() {
		config = YamlConfiguration.loadConfiguration(file);
		save();
	}

	public List<Couple> loadCouples() {
		List<Couple> couples = new ArrayList<>();

		try {
			for (String player1UUID : config.getConfigurationSection("couples").getKeys(false)) {
				UUID player1 = UUID.fromString(player1UUID);
				UUID player2 = UUID.fromString(config.getString("couples." + player1UUID));
				couples.add(new Couple(player1, player2));
			}
		} catch (NullPointerException e) {
			// This is in case "couples" hasn't been generated yet, we don't need to do
			// anything
		}

		return couples;
	}

	public void saveCouples(List<Couple> couples) {
		Map<String, String> strCouples = new HashMap<>();

		for (Couple couple : couples) {
			strCouples.put(couple.getPlayer1().toString(), couple.getPlayer2().toString());
		}

		config.set("couples", strCouples);
		save();
	}
}
