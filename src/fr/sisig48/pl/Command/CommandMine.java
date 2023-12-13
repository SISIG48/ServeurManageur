package fr.sisig48.pl.Command;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.sisig48.pl.Automating.Mine;
import fr.sisig48.pl.State.Spawn;

public class CommandMine implements CommandExecutor, TabCompleter {

	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] arg) {
		
		if(sender instanceof Player) {
			Location destination;
			Player player = (Player)sender;
			if (player.isOp()) {
				for (String e : arg) {
					if (e.equals("set")) {
						for (String i : arg) {
							String x = String.valueOf(player.getLocation().getX());
							String y = String.valueOf(player.getLocation().getY());
							String z = String.valueOf(player.getLocation().getZ());
							String ya = String.valueOf(player.getLocation().getYaw());
							String pi = String.valueOf(player.getLocation().getPitch());
							String w = player.getLocation().getWorld().getName().toString();
							String[] loc = {x, y, z, w, ya, pi};
							if (i.equals("in")) {
								Spawn.SetMineInSpawnLocation(loc);
								player.sendMessage("§ein mine set at : §a" + x + " " + y + " " + z + " " + ya + "," + pi + " - in :" + w);
								return true;
							} else if (i.equals("out")) {
								Spawn.SetMineOutSpawnLocation(loc);
								player.sendMessage("§eout mine set at : §a" + x + " " + y + " " + z + " " + ya + "," + pi + " - in :" + w);
								return true;
							} else if (i.equals("zone1")) {
								Mine.SetFillMineAt(player);
								return true;
							} else if (i.equals("zone2")) {
								Mine.SetFillMineTo(player);
								return true;
							}
							
					}
					player.sendMessage("§4You have missed send the type of set mine (§ein or out§4) or (§ezone1 or zone2§4)");
					return false;
					} else if(e.equals("reload") || e.equals("rl")) {
						Mine.FillMine();
						return true;
					}
					
					if ((Bukkit.getPlayer(e) != null) && Bukkit.getPlayer(e).isOnline()) {
						try {
							destination = Spawn.GetMineInSpawnLocation();
							Player p = Bukkit.getPlayer(e);
							p.teleport(destination);
							p.sendMessage("§aVous avez été tp a la §4mine");
							player.sendMessage("§aVous avez tp : §4" + e);
							return true;
						} catch (IOException i) {i.printStackTrace();}
					 }
				player.sendMessage("§4§l" + e + " §4etait inatendu");
				}
			}
			//Location destination = new Location(Bukkit.getWorld("IntheMine"), 0, 0, 0);

			try {
				destination = Spawn.GetMineInSpawnLocation();
				player.teleport(destination);
				player.sendMessage("§aVous avez été tp a la §4mine");
				return true;
			} catch (IOException e) {e.printStackTrace();}
			
			
		} else {
			System.err.println("Action impossible depuis la console");
		}
		
		return false;
	}
	
	
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if(!sender.isOp()) return completions;
        if(args.length == 1) {
        	completions.add("set");
        	completions.add("rl");
        	completions.add("reload");
        	for(Player p : Bukkit.getOnlinePlayers()) {
        		completions.add(p.getName());
        		completions.add(p.getUniqueId().toString());
        	}
        } else if(args.length == 2 && args[0].equalsIgnoreCase("set")) {
        	completions.add("in");
        	completions.add("out");
        	completions.add("zone1");
        	completions.add("zone2");
        }
        for(String e : completions.toArray(new String[0])) if(args.length > 0 && !e.startsWith(args[args.length-1])) completions.remove(e);
        return completions;
    }
}
