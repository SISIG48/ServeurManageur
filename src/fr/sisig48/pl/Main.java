package fr.sisig48.pl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.ServeurManageur.Updater.ServeurManageurUpdate;
import fr.sisig48.pl.JobsHouse.MainHouse;
import fr.sisig48.pl.Automating.AutoSave;
import fr.sisig48.pl.Automating.Mine;
import fr.sisig48.pl.Automating.PayPal;
import fr.sisig48.pl.Automating.WebAuto;
import fr.sisig48.pl.Command.CommandAccount;
import fr.sisig48.pl.Command.CommandBug;
import fr.sisig48.pl.Command.CommandConfig;
import fr.sisig48.pl.Command.CommandFriends;
import fr.sisig48.pl.Command.CommandHouse;
import fr.sisig48.pl.Command.CommandJobs;
import fr.sisig48.pl.Command.CommandMenu;
import fr.sisig48.pl.Command.CommandMine;
import fr.sisig48.pl.Command.CommandRe;
import fr.sisig48.pl.Command.CommandSave;
import fr.sisig48.pl.Command.CommandShop;
import fr.sisig48.pl.Command.CommandSpawn;
import fr.sisig48.pl.Command.CommandWeb;
import fr.sisig48.pl.Economie.XpCounter;
import fr.sisig48.pl.Sociale.Friends;
import fr.sisig48.pl.Sociale.PlayerJobs;
import fr.sisig48.pl.State.JobsPNJ;
import fr.sisig48.pl.State.ShopPNJ;
import fr.sisig48.pl.Utils.Uconfig;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.sisig48.web.WebServer;


public class Main extends JavaPlugin {
	public static JavaPlugin Plug;
	public static ConsoleCommandSender sec = Bukkit.getConsoleSender();
	protected FileConfiguration config = getConfig();
	private static Thread loadThread = new Thread(new Runnable() {
		
		@Override
		public void run() {
			sec.sendMessage("§8Loading addon...");
			sec.sendMessage("§8Loading updater system");
			if(ServeurManageurUpdate.CheckUpdate()) {
				sec.sendMessage(("  §4--------- You need do UPDATE ---------"));
				logs.add("End Tchecking Update : Maj detetect");
				ServeurManageurUpdate.NeedUpdate = 1;
			} else {
				sec.sendMessage(("  §8--------- You don't need UPDATE ---------"));
				logs.add("End Tchecking Update : No Maj detetect");
				ServeurManageurUpdate.NeedUpdate = 0;
			}
			sec.sendMessage("§8  SendMaj");
			ServeurManageurUpdate.SendMaj();
			sec.sendMessage("§8  Load note upgrade");
			ServeurManageurUpdate.Note();
				sec.sendMessage("§8end load");
				
				sec.sendMessage("§8Loading friendly addon");
			Friends.load();
				sec.sendMessage("§8end load");
				
				sec.sendMessage("§8Loading jobs addon");
			PlayerJobs.load();
			MainHouse.init();
				sec.sendMessage("§8Loading Jobs Office PNJ");
			
			
			Bukkit.getScheduler().runTask(Plug, () -> {
				Entity e;
				String uuid = " ";
				
				
				//jobs PNJ
				int saves = -1; 
				String path = "location.pnj.jobs.";
				for(int i = 0; Uconfig.getConfig(path + i) != null; i++) {
					saves++;
					if((uuid = Uconfig.getConfig(path + i + ".uuid")) == null) continue;
					try {
						e = Bukkit.getEntity(UUID.fromString(uuid));
						e.teleport(JobsPNJ.getLoc(i));
						e.setCustomName(Uconfig.getConfig(path + i + ".name"));
						JobsPNJ.SetType(e);
						JobsPNJ.addUUID(e.getUniqueId());
					} catch (IllegalArgumentException ex) {sec.sendMessage("§dErr : §e" + ex.getMessage() + " §duuid miss : §e" + uuid);}
				}
				
				PNJSave(path, saves);
				
				//shop PNJ
				path = "location.pnj.shop.";
				saves = -1; 
				for(int i = 0; Uconfig.getConfig(path + i) != null; i++) {
					saves++;
					//Vérification de l'existance complete de la save
					if((uuid = Uconfig.getConfig(path + i + ".uuid")) == null) continue;
					try {
						//Modfification PJN
						e = Bukkit.getEntity(UUID.fromString(uuid));
						e.teleport(ShopPNJ.getLoc(i));
						ShopPNJ.addUUID(e.getUniqueId());
						e.setCustomName(Uconfig.getConfig(path + i + ".name"));
						ShopPNJ.SetType(e);
						
						
					} catch (IllegalArgumentException ex) {sec.sendMessage("§dErr : §e" + ex.getMessage() + " §duuid miss : §e" + uuid);}
				}
				
				PNJSave(path, saves);
				
				sec.sendMessage("§8end load PNJ - Diff");
				
			});
			
			sec.sendMessage("§8Sending start info");
			StartsendInfo();
			
			for(Player player : Bukkit.getOnlinePlayers()) if(!new PlayerJobs(player.getPlayer()).get().isEnable()) player.getPlayer().sendMessage("§4Attention votre métier est vérouillé : §6Aucune action n'est possible pour votre jobs : §a" + new PlayerJobs(player.getPlayer()).get().getName() + " §6nous vous avons attribué un jobs fictif : Chaumage");
			sec.sendMessage("§8Starting Mine");
			Mine.AutoFill.start();
			
			sec.sendMessage("§8Start Payment");
			for(Player p : Bukkit.getOnlinePlayers()) new PayPal(p);
			
			sec.sendMessage("§8Start xp");
			XpCounter.Count();
			
			webStart();
			
	        sec.sendMessage("§daddon loading sucess");
	        AutoSave.initiate();
		}
	}, "init SM addon");
	
	private static void webStart() {
		WebServer.setPath(Main.Plug.getDataFolder().getPath().replace("\\", "/") + "/web");
		WebAuto.SaveChange();
		WebServer web = new WebServer(443);
		web.setMaxUsers(50);
		web.start();
	}
	
	private static void PNJSave(String path, int max) {
		String[] saveList = {"x", "y", "z", "w", "yaw", "uuid", "name"};
		
		//Save réorganisation
		boolean press = false;
		for(int i = max; i >= 0; i--) {
			if(!press) Uconfig.setConfig(path + (i + 1), null);
			if(Uconfig.getConfig(path + i) == null) {
				press = false;
				continue;
			}
			if(Uconfig.getConfig(path + i).equals("void")) {
				if(press) {
					Uconfig.setConfig(path + (i), null);
					for(String tag : saveList) Uconfig.setConfig(path + i + "." + tag, Uconfig.getConfig(path + (i+1) + "." + tag));
					Uconfig.setConfig(path + (i + 1), null);
					press = true;
				} else {
					Uconfig.setConfig(path + (i), null);
					press = false;
				}
			} else press = true;
		}
	}
	
	@Override
	public void onEnable() {
		logs.add("Plugin Starting");
		Plug = getProvidingPlugin(getClass());
		getServer().getPluginManager().registerEvents(new Listner(this), this);
		reloadConfig();
		getCommand("mine").setExecutor(new CommandMine());
		getCommand("spawn").setExecutor(new CommandSpawn());
		getCommand("re").setExecutor(new CommandRe());
		getCommand("config").setExecutor(new CommandConfig());
		getCommand("bug").setExecutor(new CommandBug());
		getCommand("friends").setExecutor(new CommandFriends());
		getCommand("jobs").setExecutor(new CommandJobs());
		getCommand("menu").setExecutor(new CommandMenu());
		getCommand("house").setExecutor(new CommandHouse());
		getCommand("shop").setExecutor(new CommandShop());
		getCommand("save").setExecutor(new CommandSave());
		getCommand("web").setExecutor(new CommandWeb());
		getCommand("account").setExecutor(new CommandAccount());
		Uconfig.intit(this);
		loadThread.start();
		for(Player p : Bukkit.getOnlinePlayers()) new PayPal(p);
	}
	
	@SuppressWarnings("removal")
	@Override
	public void onDisable() {
		//Fermeture des threads
		try {WebServer.stopAll();} catch (Exception e) {e.printStackTrace();}
		try {Mine.AutoFill.stop();} catch (Exception e) {e.printStackTrace();}
		try {loadThread.stop();} catch (Exception e) {e.printStackTrace();}
		try {PayPal.thread.stop();} catch (Exception e) {e.printStackTrace();}
		try {AutoSave.del();} catch (Exception e) {e.printStackTrace();}
		
		// Sauvegarde
		try {AutoSave.save();} catch (Exception e) {e.printStackTrace();}
		try {saveConfig();} catch (Exception e) {e.printStackTrace();}
		
		for(Player e : Bukkit.getOnlinePlayers()) {
			e.kickPlayer("§dErreur serveur, veuillez patienter. \n"
						+ "§4réessayer plus tard \n"
						+ "\n"
						+ "§dServer error, please wait. \n"
						+ "§4try again later");

		}
		logs.add("Plugin Stoping");
	}
	
	@SuppressWarnings("deprecation")
	private static void StartsendInfo(){
		try {
			ArrayList<String> note = new ArrayList<String>();
			for(String e : logs.ReadFile("note.txt", "UTF-8")) note.add(e);
			TextComponent msgl = new TextComponent("§e[§e§lCLIQUE POUR REJOIDRE§e]");
			msgl.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eREJOIDRE").create()));
			msgl.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/5RwetHwxsS"));
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(!p.isOp()) break;
				for(String n : note) p.sendMessage(n);
				p.spigot().sendMessage(msgl);
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
