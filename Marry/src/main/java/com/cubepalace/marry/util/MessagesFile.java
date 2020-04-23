package com.cubepalace.marry.util;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.cubepalace.marry.Marry;

public class MessagesFile {

	private File file;
	private FileConfiguration config;

	public MessagesFile(Marry plugin, String fileName) {
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

	public void reload() {
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public FileConfiguration getConfig() {
		return config;
	}
}
