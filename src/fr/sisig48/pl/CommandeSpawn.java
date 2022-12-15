package fr.sisig48.pl;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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
			try {
				player.teleport(Spawn.GetSpawnLocation());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			player = Bukkit.getPlayer(arg[1]);
			Location destination = new Location(Bukkit.getWorld("IntheMine"), 0, 0, 0);
			player.teleport(destination);
		}
		return false;
	}

}
