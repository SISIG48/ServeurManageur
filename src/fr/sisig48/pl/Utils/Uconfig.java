package fr.sisig48.pl.Utils;


import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.sisig48.pl.Main;
import fr.sisig48.pl.logs;
import fr.sisig48.pl.Automating.Mine;

public class Uconfig {
	
	public FileConfiguration conf;
	public File file;
	static FileConfiguration config;
	protected static Main main;
	public Uconfig(File config) {
		conf = YamlConfiguration.loadConfiguration(config);
		file = config;
	}
	
	//

	public String get(String path) {
		return conf.getString(path);
	}
	
	final public FileConfiguration getConfig() {
		return conf;
	}
	
	public void set(String Path, String it) {
		conf.set(Path, it);
		logs.add("Config : " + Path + " have set to \"" + it + "\"");
		saving();
		return;
	}
	
	
	public void reload() {
		logs.add("Config Reloading");
		Mine.isChange = true;
		main.reloadConfig();
		return;
	}
	
	public void saving() {
		logs.add("Config Saving");
		try {
			conf.save(file.getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean AlreadySet(String path) {
		return conf.isSet(path);
	}
	
	
	//Static = base config file
	public static void intit(Main main) {
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
