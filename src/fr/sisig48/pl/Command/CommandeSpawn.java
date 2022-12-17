package fr.sisig48.pl.Command;

import java.io.IOException;

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
