package fr.sisig48.pl;


import org.bukkit.plugin.java.JavaPlugin;

import fr.sisig48.pl.Command.CommandeMine;



public class Main extends JavaPlugin {
	
	
	@Override
	public void onEnable() {
		

		System.out.println("Le plugin MAIN c'est allume");
		getServer().getPluginManager().registerEvents(new Listner(), this);
		LoadConfig();
		getCommand("mine").setExecutor(new CommandeMine());
		getCommand("spawn").setExecutor(new CommandeSpawn());
		
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
