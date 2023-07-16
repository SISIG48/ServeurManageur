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

	public static String getConfig(String path) {
		if(!isSet(path)) setConfig(path, config.getDefaults().getString(path));
		return config.getString(path);
	}
	
	
	public static void setConfig(String Path, String it) {
		config.set(Path, it);
		logs.add("Config : " + Path + " have set to \"" + it + "\"");
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
	
	private static boolean isSet(String path) {
		return config.isSet(path);
	}
	
}
