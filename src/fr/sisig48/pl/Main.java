package fr.sisig48.pl;




import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import fr.ServeurManageur.Updater.ServeurManageurUpdate;
import fr.sisig48.pl.Command.CommandeMine;
import fr.sisig48.pl.Command.CommandeRe;
import fr.sisig48.pl.Command.CommandeSpawn;
import fr.sisig48.pl.Utils.Uconfig;



public class Main extends JavaPlugin {
	
	protected FileConfiguration config = getConfig();
	
	@Override
	public void onEnable() {
		
		
		if(ServeurManageurUpdate.CheckUpdate()) {
			System.err.println("--------- You need do UPDATE ---------");
		}
		System.out.println("Le plugin MAIN c'est allume");
		getServer().getPluginManager().registerEvents(new Listner(this), this);
		LoadConfig();
		getCommand("mine").setExecutor(new CommandeMine());
		getCommand("spawn").setExecutor(new CommandeSpawn());
		getCommand("re").setExecutor(new CommandeRe());
		new Uconfig(this);
		
	}

	@Override
	public void onDisable() {
		System.out.println("Le plugin MAIN c'est coupe");
		saveConfig();
	}
	
	private void LoadConfig() {
		saveDefaultConfig();
		//getConfig().options().copyDefaults(true);
		//getServer().getPluginManager().registerEvents(this , this);
		//saveConfig();
	}
	
	
}
