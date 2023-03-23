package fr.sisig48.pl.Command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import fr.sisig48.pl.Sociale.Jobs;
import fr.sisig48.pl.Sociale.PlayerJobs;
import fr.sisig48.pl.State.JobsPNJ;
import fr.sisig48.pl.Utils.Uconfig;

public class CommandJobs implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg) {
		/*PlayerJobs.reload();
		PlayerJobs.load();
		PlayerJobs playerJobs = new PlayerJobs((OfflinePlayer) sender);
		playerJobs.add(Jobs.HUNTER);
		if(arg.length == 1) playerJobs.add(Jobs.valueOf(arg[0]));
		playerJobs.save();
		playerJobs.close();
		playerJobs.saveAll();
		sender.sendMessage(playerJobs.get().getName());*/
		if(!(sender instanceof Player)) {
			if(arg.length < 1) return false;
			switch(arg[0]) {
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
		if(arg.length >= 1) {
			PlayerJobs p = new PlayerJobs((OfflinePlayer) sender);
			switch(arg[0]) {
			case "set" :
				p.add(Jobs.valueOf(arg[1]));
				sender.sendMessage("§aYou have set : §4" + p.get().getName());
				p.save();
				return true;
			case "setXp" :
				p.setXp(Integer.valueOf(arg[1]));
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
		
		
		String StringUUID = Uconfig.getConfig("location.pnj.jobs.uuid");
		if(StringUUID != null) Bukkit.getEntity(UUID.fromString(StringUUID)).remove();
		Location loc = Bukkit.getPlayer(sender.getName()).getLocation();
		JobsPNJ.setLoc(loc);
		Villager pnj = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
		Uconfig.setConfig("location.pnj.jobs.uuid", String.valueOf(pnj.getUniqueId()));
		pnj.setAI(false);
		pnj.setCanPickupItems(false);
		pnj.setCollidable(false);
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
		Uconfig.setConfig("location.pnj.jobs.name", name);
		pnj.setCustomNameVisible(true);
		pnj.setInvulnerable(true);
		return true;
	}

}
