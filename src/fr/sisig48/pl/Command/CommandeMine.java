package fr.sisig48.pl.Command;


import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.sisig48.pl.State.Spawn;

public class CommandeMine implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] arg) {
		
		if(sender instanceof Player) {
			Location destination;
			Player player = (Player)sender;
			for (String e : arg) {
				if (e.equals("set")) {
					if (!player.hasPermission("DEFAULT_PERMISSION")) {player.sendMessage("�4You need are OP to execute this command"); return false;}
					for (String i : arg) {
						String x = String.valueOf(player.getLocation().getX());
						String y = String.valueOf(player.getLocation().getY());
						String z = String.valueOf(player.getLocation().getZ());
						String w = player.getLocation().getWorld().getName().toString();
						String[] loc = {x, y, z, w};
						if (i.equals("in")) {
							Spawn.SetMineInSpawnLocation(loc);
							return true;
						} else if (i.equals("out")) {
							Spawn.SetMineOutSpawnLocation(loc);
							return true;
						}
						
					}
					player.sendMessage("�4You have missed send the type of set mine (�ein or out�4)");
					return false;
				}
				
				if (Bukkit.getPlayer(e) != null && Bukkit.getPlayer(e).isOnline()) {
					if (!player.hasPermission("DEFAULT_PERMISSION")) {player.sendMessage("�4You need are OP to execute this command"); return false;}
					try {
						destination = Spawn.GetMineInSpawnLocation();
						Player p = Bukkit.getPlayer(e);
						p.teleport(destination);
						p.sendMessage("�aVous avez �t� tp a la �4mine");
						player.sendMessage("�aVous avez tp : �4" + e);
						return true;
					} catch (IOException i) {i.printStackTrace();}
				 }
			player.sendMessage("�4�l" + e + " �4etait inatendu");
			}
			
			//Location destination = new Location(Bukkit.getWorld("IntheMine"), 0, 0, 0);

			try {
				destination = Spawn.GetMineInSpawnLocation();
				player.teleport(destination);
				player.sendMessage("�aVous avez �t� tp a la �4mine");
				return true;
			} catch (IOException e) {e.printStackTrace();}
			
			
		} else {
			System.err.println("Action impossible depuis la console");
		}
		
		return false;
	}

}
