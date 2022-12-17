package fr.sisig48.pl.Utils;


import org.bukkit.configuration.file.FileConfiguration;

import fr.sisig48.pl.Main;

public class Uconfig {
	
	static FileConfiguration config;
	public Uconfig(Main main) {
		config = main.getConfig();
	}


	//public static final
	public static String getConfig(String configPath) {
		return config.getString(configPath);
	}
	
	
	public static void setConfig(String configPath, String it) {
		config.set(configPath, it);
		return;
	}
}
