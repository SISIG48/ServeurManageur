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
		String path = "location.pnj.jobs.";
		int n = -1;
		if(arg.length == 1 && arg[0].length() == 36) {
			try {
				
				if(JobsPNJ.getUUIDS().contains(UUID.fromString(arg[0]))) {
					//UUID Entrer
					UUID uuid = UUID.fromString(arg[0]);
					
					@SuppressWarnings("unused")
					//Detection emplacement de save
					String temp = "";
					for(int i = 0; (temp = Uconfig.getConfig(path + i)) != null ; i++) try{if(Uconfig.getConfig(path + i + ".uuid").equals(uuid.toString())) n = i;} catch (NullPointerException e) {};
					if(n == -1) sender.sendMessage("§4Erreur interne");
					
					//Recupération du nom sauvegarder
					String name = Uconfig.getConfig(path + n + ".name");
					
					//Destruction de la save
					Uconfig.setConfig(path + n, "void");
					JobsPNJ.dellUUID(uuid);
					
					//Supressions du PNJ
					Bukkit.getEntity(uuid).remove();
					
					//Message de supression
					sender.sendMessage("§a§l" + arg[0] + " §esuprimé §8§l(§d" + name + "§8§l)");
					return true;
				} else {
					sender.sendMessage("§4§lUUID non trouvé");
					return true;
				}
			} catch (IllegalArgumentException e) {
				sender.sendMessage("§4§lUUID invalide");
				return true;
			}
		}
				
		//Detection emplacement de save
		String r = "-";
		for(int i = 0; r != null && !r.equals("void") ; n = i++) r = Uconfig.getConfig(path + i);
		
		
		//Récupération du nom PNJ
		String name = "§aJobs §4§lOffice";
		if(arg.length >= 1) {
			name = "";
			int i = 0;
			for(String t : arg) {
				if(i == 0) name = t.replaceAll("&", "§");
				else name = name + " " + t.replaceAll("&", "§");
				i++;
			}
		}
		
		//Création PNJ
		Location loc = Bukkit.getPlayer(sender.getName()).getLocation();
		Villager pnj = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
		pnj.setAI(false);
		pnj.setCanPickupItems(false);
		pnj.setCollidable(false);
		pnj.setCustomName(name);
		pnj.setCustomNameVisible(true);
		pnj.setInvulnerable(true);
		JobsPNJ.SetType(pnj);
		
		
		//Créeation de la sauvegarde (location) PNJ
		JobsPNJ.setLoc(loc, n);
		Uconfig.setConfig(path + n + ".name", name);
		//Sauvegarde UUID
		Uconfig.setConfig(path + n + ".uuid", String.valueOf(pnj.getUniqueId()));
		JobsPNJ.addUUID(pnj.getUniqueId());
		return true;
	}
	
	
	
	//tab complete
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (!(sender instanceof Player) && args.length == 1) {
        	completions.add("rl");
        	completions.add("save");
			completions.add("xp");
			completions.add("reset");
			completions.add("statue");
			completions.add("setE");
			completions.add("setD");
			return completions;
        }
			
        if(args.length == 1) {
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
            for(UUID uuid : JobsPNJ.getUUIDS()) completions.add(uuid.toString());
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
