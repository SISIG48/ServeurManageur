package fr.sisig48.pl.Command;


import java.io.IOException;

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
			Player player = (Player)sender;
			player.sendMessage("You have use : /mine");
			//Location destination = new Location(Bukkit.getWorld("IntheMine"), 0, 0, 0);
			Location destination;
			try {
				destination = Spawn.GetMineInSpawnLocation();
				player.teleport(destination);
			} catch (IOException e) {e.printStackTrace();}
			
			return true;
		} else {
		System.err.println("Action impossible depuis la console");
		}
		
		return false;
	}

}
