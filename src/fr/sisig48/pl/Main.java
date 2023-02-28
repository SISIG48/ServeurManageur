package fr.sisig48.pl;



import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.ServeurManageur.Updater.ServeurManageurUpdate;
import fr.sisig48.pl.Automating.Mine;
import fr.sisig48.pl.Command.CommandBug;
import fr.sisig48.pl.Command.CommandConfig;
import fr.sisig48.pl.Command.CommandFriends;
import fr.sisig48.pl.Command.CommandJobs;
import fr.sisig48.pl.Command.CommandMenu;
import fr.sisig48.pl.Command.CommandeMine;
import fr.sisig48.pl.Command.CommandeRe;
import fr.sisig48.pl.Command.CommandeSpawn;
import fr.sisig48.pl.Sociale.Friends;
import fr.sisig48.pl.Sociale.PlayerJobs;
import fr.sisig48.pl.State.JobsPNJ;
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
		getCommand("jobs").setExecutor(new CommandJobs());
		getCommand("menu").setExecutor(new CommandMenu());
		new Uconfig(this);
		ServeurManageurUpdate.SendMaj();
		ServeurManageurUpdate.Note();
		Friends.load();
		PlayerJobs.load();
		StartsendInfo();
		String uuid = "location.pnj.jobs.uuid";
		if(Uconfig.getConfig(uuid) != null) {
			Entity e = Bukkit.getEntity(UUID.fromString(Uconfig.getConfig(uuid)));
			e.teleport(JobsPNJ.getLoc());
			e.setCustomName(Uconfig.getConfig("location.pnj.jobs.name"));
		}
		for(Player player : Bukkit.getOnlinePlayers()) if(!new PlayerJobs(player.getPlayer()).get().isEnable()) player.getPlayer().sendMessage("§4Attention votre métier est vérouillé : §6Aucune action n'est possible pour votre jobs : §a" + new PlayerJobs(player.getPlayer()).get().getName() + " §6nous vous avons attribué un jobs fictif : Chaumage");
		Mine.AutoFill.start();

	}

	@Override
	public void onDisable() {
		Mine.AutoFill.interrupt();
		reloadConfig();
		saveConfig();
		Friends.saveAll();
		PlayerJobs.saveAll();
		for(Player e : Bukkit.getOnlinePlayers()) {
			
			if(!(e.getName().equals("SISIG48") || e.getName().equals("indylynx") ||  e.getName().equals("Heldorus_"))) {
				e.kickPlayer("§4Serveur error §aPlease WAIT");
			} else {
				e.sendMessage("§6Attention certain bug son lié a ce reload,");
				e.sendMessage("§6nous vous avons garder la conection car vous ête sur la liste");
			}
		}
		logs.add("Plugin Stoping");
	}
	
	@SuppressWarnings("deprecation")
	private static void StartsendInfo(){
		try {
			ArrayList<String> note = new ArrayList<String>();
			for(String e : logs.ReadFile("note.txt")) note.add(e);
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(!p.isOp()) break;
				for(String n : note) p.sendMessage(n);
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
