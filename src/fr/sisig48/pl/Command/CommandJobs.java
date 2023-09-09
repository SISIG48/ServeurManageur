package fr.sisig48.pl.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import fr.sisig48.pl.logs;
import fr.sisig48.pl.Economie.XpCounter;
import fr.sisig48.pl.Sociale.Jobs;
import fr.sisig48.pl.Sociale.PlayerJobs;
import fr.sisig48.pl.State.JobsPNJ;
import fr.sisig48.pl.Utils.Uconfig;

public class CommandJobs extends JobsPNJ implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg) {
		if(!(sender instanceof Player)) {
			if(arg.length < 1) return false;
			switch(arg[0]) {
			case "rl" :
				PlayerJobs.reload();
				return true;
			case "save" :
				PlayerJobs.saveAll();
				return true;
			case "xp" :
				for(Material m : Material.values()) if(XpCounter.getXp(m, Jobs.NOT) != 0) sender.sendMessage("§aXp pour §4" + m.name() + " §a: §d" + XpCounter.getXp(m, Jobs.NOT));
				return true;
			case "reset" :
				for(Jobs jo : Jobs.All) jo.getFile().delete();
				Bukkit.dispatchCommand(sender, "rl");
				return true;
			case "statue" :
				for(Jobs j : Jobs.All) sender.sendMessage("§eJobs : §4" + j.getName() + " §eis : §4" + j.isEnable());
				return true;
			case "setE" :
				if(arg.length > 1) {
					Jobs j = Jobs.valueOf(arg[1]);
					j.enable = true;
					j.saveFile();
					return true;
				}
				for(Jobs j : Jobs.All) {
					j.enable = true;
					sender.sendMessage("§eJobs : §4" + j.getName() + " §eis : §4" + j.isEnable());
					j.saveFile();
				}
				return true;
			case "setD" :
				if(arg.length > 1) {
					Jobs j = Jobs.valueOf(arg[1]);
					j.enable = false;
					j.saveFile();
					return true;
				}
				for(Jobs j : Jobs.All) {
					j.enable = false;
					sender.sendMessage("§eJobs : §4" + j.getName() + " §eis : §4" + j.isEnable());
					j.saveFile();
				}
				return true;
			}
			logs.send("§4Invalide : §ecommande disponible pour non joueur >");
			logs.send("§ejobs rl");
			logs.send("§ejobs save");
			logs.send("§ejobs xp");
			logs.send("§ejobs reset");
			logs.send("§ejobs statue");
			logs.send("§ejobs setE");
			logs.send("§ejobs setD");
			return true;
		}
		if(arg.length >= 1) {
			PlayerJobs p = new PlayerJobs((OfflinePlayer) sender);
			switch(arg[0]) {
			case "set" :
				p.add(Jobs.valueOf(arg[1]));
				sender.sendMessage("§aVous avez définie sur : §4" + p.get().getName());
				p.save();
				return true;
			case "xp" : 
				switch(arg[1]){
				case "set" :
					p.setXp(Integer.valueOf(arg[2]));
					return true;
				case "get" :
					sender.sendMessage("§aVotre xp est de : §4" + p.getXp());
					return true;
				case "info" :
					for(Material m : Material.values()) if(XpCounter.getXp(m, Jobs.NOT) != 0) sender.sendMessage("§aXp pour §4" + m.name() + " §a: §d" + XpCounter.getXp(m, Jobs.NOT));
					return true;
				}
			case "get" :
				@SuppressWarnings("deprecation") OfflinePlayer playerGet = Bukkit.getOfflinePlayer(arg[1]);
				if(playerGet == null || !playerGet.hasPlayedBefore()) sender.sendMessage("§4Player : §6" + arg[1] + " §4n'existe pas");
				else sender.sendMessage("§6Jobs de §4" + arg[1] + " §6est §4" + new PlayerJobs(playerGet).get().getName());
				return true;
			case "rl" :
				PlayerJobs.reload();
				return true;
			case "save" :
				PlayerJobs.saveAll();
				return true;
			case "reset" :
				for(Jobs jo : Jobs.All) jo.getFile().delete();
				Bukkit.dispatchCommand(sender, "rl");
				return true;
			case "test" :
				sender.sendMessage(String.valueOf(new PlayerJobs(Bukkit.getPlayer(sender.getName())).getXp()));
				return true;
			case "statu" :
				for(Jobs j : Jobs.All) sender.sendMessage("§eJobs : §4" + j.getName() + " §eis : §4" + j.isEnable());
				return true;
			case "setE" :
				if(arg.length > 1) {
					Jobs j = Jobs.valueOf(arg[1]);
					j.enable = true;
					j.saveFile();
					return true;
				}
				for(Jobs j : Jobs.All) {
					j.enable = true;
					sender.sendMessage("§eJobs : §4" + j.getName() + " §eis : §4" + j.isEnable());
					j.saveFile();
				}
				return true;
			case "setD" :
				if(arg.length > 1) {
					Jobs j = Jobs.valueOf(arg[1]);
					j.enable = false;
					j.saveFile();
					return true;
				}
				for(Jobs j : Jobs.All) {
					j.enable = false;
					sender.sendMessage("§eJobs : §4" + j.getName() + " §eis : §4" + j.isEnable());
					j.saveFile();
				}
				return true;
			}
			
		}
		
		String r = "-";
		int n = 0;
		if(arg.length == 1) {
			try {
				String uuids;
				for(int i = 0; (r = Uconfig.getConfig("location.pnj.jobs."+ i)) != null && (uuids = Uconfig.getConfig("location.pnj.jobs."+ r +".uuid")) != null && !uuids.equals(arg[0]); n = i++);
				r = Uconfig.getConfig("location.pnj.jobs."+ n +".uuid");
				if(r != null && r.equals(arg[0])) {
					UUID uuid = UUID.fromString(arg[0]);
					Bukkit.getEntity(uuid).remove();
					sender.sendMessage("§a§l" + arg[0] + " §esuprimé §8§l(§d" + Uconfig.getConfig("location.pnj.jobs."+ n + ".name") + "§8§l)");
					if(n != -1) Uconfig.setConfig("location.pnj.jobs."+ n, "void");
					return true;
				} else {
					sender.sendMessage("§4§lUUID non trouvé");
					return true;
				}
			} catch (IllegalArgumentException e) {}
		}
				
		for(int i = 0; r != null && !r.equals("void") ; n = i++) r = Uconfig.getConfig("location.pnj.jobs."+ i);
		Location loc = Bukkit.getPlayer(sender.getName()).getLocation();
		JobsPNJ.setLoc(loc, n);
		Villager pnj = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
		Uconfig.setConfig("location.pnj.jobs." + n + ".uuid", String.valueOf(pnj.getUniqueId()));
		JobsPNJ.addUUID(pnj.getUniqueId());
		pnj.setAI(false);
		pnj.setCanPickupItems(false);
		pnj.setCollidable(false);
		JobsPNJ.SetType(pnj);
		String name = "§ajobs";
		if(arg.length >= 1) {
			name = "";
			int i = 0;
			for(String t : arg) {
				if(i == 0) name = t.replaceAll("&", "§");
				else name = name + " " + t.replaceAll("&", "§");
				i++;
			}
		}
		pnj.setCustomName(name);
		Uconfig.setConfig("location.pnj.jobs." + n + ".name", name);
		pnj.setCustomNameVisible(true);
		pnj.setInvulnerable(true);
		return true;
	}
	
	
	
	//tab complete
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // Complétion pour la première argument (sous-commande principale)
            completions.add("set");
            completions.add("xp");
            completions.add("get");
            completions.add("rl");
            completions.add("save");
            completions.add("reset");
            completions.add("statue");
            completions.add("sete");
            completions.add("setd");

            // Ajoutez ici d'autres complétions pour les commandes PNJ si nécessaire
        } else if(args.length == 2) {
	        if (args[0].equalsIgnoreCase("xp")) {
	            // Complétion pour la deuxième argument lorsque la sous-commande est "xp"
		        completions.add("set");
		        completions.add("get");
		        completions.add("info");
	        } else if(args[0].equalsIgnoreCase("get")) {
	        	for(OfflinePlayer p : Bukkit.getOfflinePlayers()) {
	        		completions.add(p.getName());
	        		completions.add(p.getUniqueId().toString());
	        	}
	        } else if(args[0].equalsIgnoreCase("set")) for(Jobs p : Jobs.values()) completions.add(p.toString());
        
        } else if(args[0].equalsIgnoreCase("xp") && args[1].equalsIgnoreCase("set") && args.length == 3) for(int i = 0; i != 10000; i++) completions.add("" + i);

        return completions;
    }
}
