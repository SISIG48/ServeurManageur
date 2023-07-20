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
import fr.sisig48.pl.Automating.AutoReload;
import fr.sisig48.pl.Automating.Mine;
import fr.sisig48.pl.Automating.PayPal;
import fr.sisig48.pl.Command.CommandBug;
import fr.sisig48.pl.Command.CommandConfig;
import fr.sisig48.pl.Command.CommandFriends;
import fr.sisig48.pl.Command.CommandHouse;
import fr.sisig48.pl.Command.CommandJobs;
import fr.sisig48.pl.Command.CommandLootBox;
import fr.sisig48.pl.Command.CommandMenu;
import fr.sisig48.pl.Command.CommandMine;
import fr.sisig48.pl.Command.CommandRe;
import fr.sisig48.pl.Command.CommandShop;
import fr.sisig48.pl.Command.CommandSpawn;
import fr.sisig48.pl.Sociale.Friends;
import fr.sisig48.pl.Sociale.PlayerJobs;
import fr.sisig48.pl.State.JobsPNJ;
import fr.sisig48.pl.State.ShopPNJ;
import fr.sisig48.pl.Utils.Uconfig;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

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
				for(int i = 0; Uconfig.getConfig("location.pnj.jobs." + i) != null; i++) {
					if((uuid = Uconfig.getConfig("location.pnj.jobs." + i + ".uuid")) == null) continue;
					try {
						e = Bukkit.getEntity(UUID.fromString(uuid));
						e.teleport(JobsPNJ.getLoc(i));
						e.setCustomName(Uconfig.getConfig("location.pnj.jobs." + i + ".name"));
						JobsPNJ.SetType(e);
						JobsPNJ.addUUID(e.getUniqueId());
					} catch (IllegalArgumentException ex) {sec.sendMessage("§dErr : §e" + ex.getMessage() + " §duuid miss : §e" + uuid);}
				}
				
				//shop PNJ
				for(int i = 0; Uconfig.getConfig("location.pnj.shop." + i) != null; i++) {
					if((uuid = Uconfig.getConfig("location.pnj.shop." + i + ".uuid")) == null) continue;
					try {
						e = Bukkit.getEntity(UUID.fromString(uuid));
						e.teleport(ShopPNJ.getLoc(i));
						ShopPNJ.addUUID(e.getUniqueId());
						e.setCustomName(Uconfig.getConfig("location.pnj.shop." + i + ".name"));
						ShopPNJ.SetType(e);
					} catch (IllegalArgumentException ex) {sec.sendMessage("§dErr : §e" + ex.getMessage() + " §duuid miss : §e" + uuid);}
				}
				sec.sendMessage("§8end load PNJ - Diff");
				
			});
			
			sec.sendMessage("§8Sending start info");
			StartsendInfo();
			
			for(Player player : Bukkit.getOnlinePlayers()) if(!new PlayerJobs(player.getPlayer()).get().isEnable()) player.getPlayer().sendMessage("§4Attention votre métier est vérouillé : §6Aucune action n'est possible pour votre jobs : §a" + new PlayerJobs(player.getPlayer()).get().getName() + " §6nous vous avons attribué un jobs fictif : Chaumage");
			sec.sendMessage("§8Starting Mine");
			Mine.AutoFill.start();
			
			sec.sendMessage("§8Start Payment");
			for(Player p : Bukkit.getOnlinePlayers()) new PayPal(p);
			sec.sendMessage("§daddon loading sucess");
		}
	}, "init SM addon");
	
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
		getCommand("lootbox").setExecutor(new CommandLootBox());
		Uconfig.intit(this);
		AutoReload.initiate();
		loadThread.start();
		for(Player p : Bukkit.getOnlinePlayers()) new PayPal(p);
		
	}
	
	@SuppressWarnings("removal")
	@Override
	public void onDisable() {
		Mine.AutoFill.stop();
		loadThread.stop();
		reloadConfig();
		saveConfig();
		Friends.saveAll();
		PlayerJobs.saveAll();
		for(Player e : Bukkit.getOnlinePlayers()) {
			
			if(!(e.getName().equals("SISIG48"))) {
				e.kickPlayer("§dErreur serveur, veuillez patienter. \n"
						+ "§4réessayer plus tard \n"
						+ "\n"
						+ "§dServer error, please wait. \n"
						+ "§4try again later");
			} else {
				e.sendMessage("§6Attention certain bug son lié a ce reload,");
				e.sendMessage("§6nous vous avons garder la conection car vous ête sur la liste");
			}
		}
		PayPal.thread.stop();
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
