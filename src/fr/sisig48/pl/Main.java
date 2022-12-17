package fr.sisig48.pl;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import fr.sisig48.pl.Command.CommandeMine;
import fr.sisig48.pl.Utils.Uconfig;



public class Main extends JavaPlugin {
	
	protected FileConfiguration config = getConfig();
	
	@Override
	public void onEnable() {
		

		System.out.println("Le plugin MAIN c'est allume");
		getServer().getPluginManager().registerEvents(new Listner(this), this);
		LoadConfig();
		getCommand("mine").setExecutor(new CommandeMine());
		getCommand("spawn").setExecutor(new CommandeSpawn());
		new Uconfig(this);
		
	}

	@Override
	public void onDisable() {
		System.out.println("Le plugin MAIN c'est coupe");
	}
	
	private void LoadConfig() {
		saveDefaultConfig();
		//getConfig().options().copyDefaults(true);
		//getServer().getPluginManager().registerEvents(this , this);
		//saveConfig();
	}
	
	
}
