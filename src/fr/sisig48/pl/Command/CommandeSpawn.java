package fr.sisig48.pl.Command;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.sisig48.pl.State.Spawn;

public class CommandeSpawn implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] arg) {
		Player player = (Player) sender;
		if(sender instanceof Player) {
			for (String e : arg) {
				if (e.equals("set")) {
					if (!player.hasPermission("DEFAULT_PERMISSION"));
					String x = String.valueOf(player.getLocation().getX());
					String y = String.valueOf(player.getLocation().getY());
					String z = String.valueOf(player.getLocation().getZ());
					String w = player.getLocation().getWorld().getName().toString();
					String[] loc = {x, y, z, w};
					Spawn.SetSpawnLocation(loc);
					return true;
				}
				
				if (Bukkit.getPlayer(e) != null && Bukkit.getPlayer(e).isOnline()) {
					if (!player.hasPermission("DEFAULT_PERMISSION")) {player.sendMessage("§4You need are OP to execute this command"); return false;}
					try {
						Player p = Bukkit.getPlayer(e);
						p.teleport(Spawn.GetSpawnLocation());
						p.sendMessage("§aVous avez été tp au §4spawn");
						player.sendMessage("§aVous avez tp : §4" + e);
						return true;
					} catch (IOException i) {i.printStackTrace();}
				 }
				
			player.sendMessage("§4§l" + e + " §4etait inatendu");
			}
			try {
				player.teleport(Spawn.GetSpawnLocation());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

}
