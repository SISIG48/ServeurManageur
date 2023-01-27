package fr.sisig48.pl;



import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.ServeurManageur.Updater.ServeurManageurUpdate;
import fr.sisig48.pl.Command.CommandBug;
import fr.sisig48.pl.Command.CommandConfig;
import fr.sisig48.pl.Command.CommandFriends;
import fr.sisig48.pl.Command.CommandeMine;
import fr.sisig48.pl.Command.CommandeRe;
import fr.sisig48.pl.Command.CommandeSpawn;
import fr.sisig48.pl.Sociale.Friends;
import fr.sisig48.pl.Utils.Uconfig;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;



public class Main extends JavaPlugin {
	
	protected FileConfiguration config = getConfig();
	@Override
	public void onEnable() {
		logs.add("Plugin Starting");
		if(ServeurManageurUpdate.CheckUpdate()) {
			System.err.println("--------- You need do UPDATE ---------");
			logs.add("End Tchecking Update : Maj detetect");
			ServeurManageurUpdate.NeedUpdate = 1;
		} else {
			System.err.println("--------- You dont need UPDATE ---------");
			logs.add("End Tchecking Update : No Maj detetect");
			ServeurManageurUpdate.NeedUpdate = 0;
		}
		/*
		try {
			OnlinePlayer.ReadUsercache();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		
		getServer().getPluginManager().registerEvents(new Listner(this), this);
		reloadConfig();
		getCommand("mine").setExecutor(new CommandeMine());
		getCommand("spawn").setExecutor(new CommandeSpawn());
		getCommand("re").setExecutor(new CommandeRe());
		getCommand("config").setExecutor(new CommandConfig());
		getCommand("bug").setExecutor(new CommandBug());
		getCommand("friends").setExecutor(new CommandFriends());
		new Uconfig(this);
		ServeurManageurUpdate.SendMaj();
		ServeurManageurUpdate.Note();
		Friends.load();
		StartsendInfo();
	}

	@Override
	public void onDisable() {
		reloadConfig();
		saveConfig();
		Friends.saveAll();
		
		for(Player e : Bukkit.getOnlinePlayers()) {
			
			if(!e.isOp()) {
				e.kickPlayer("§4Serveur do a rapid-restart §aPlease WAIT");
			}
		}
		logs.add("Plugin Stoping");
	}
	
	@SuppressWarnings("deprecation")
	private static void StartsendInfo(){
		try {
			
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(!p.isOp()) break;
				for(String e : logs.ReadFile("note.txt")) {
					p.sendMessage(e);
				}
				TextComponent msgl = new TextComponent("§e[§e§lCLIQUE POUR REJOIDRE§e]");
				msgl.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eREJOIDRE").create()));
				msgl.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/5RwetHwxsS"));
				p.spigot().sendMessage(msgl);
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
