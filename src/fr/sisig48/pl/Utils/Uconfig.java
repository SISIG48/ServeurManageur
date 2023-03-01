package fr.sisig48.pl.Utils;


import org.bukkit.configuration.file.FileConfiguration;

import fr.sisig48.pl.Main;
import fr.sisig48.pl.logs;
import fr.sisig48.pl.Automating.Mine;

public class Uconfig {
	
	static FileConfiguration config;
	protected static Main main;
	public Uconfig(Main main) {
		config = main.getConfig();
		Uconfig.main = main;
	}

	public static String getConfig(String configPath) {
		return config.getString(configPath);
	}
	
	
	public static void setConfig(String configPath, String it) {
		config.set(configPath, it);
		logs.add("Config : " + configPath + " have set to \"" + it + "\"");
		save();
		return;
	}
	
	public static void saveConfig() {
		save();
		return;
	}
	
	public static void reloadConfig() {
		logs.add("Config Reloading");
		Mine.isChange = true;
		main.reloadConfig();
		return;
	}
	
	private static void save() {
		logs.add("Config Saving");
		main.saveConfig();
	}
	
	
	
}
