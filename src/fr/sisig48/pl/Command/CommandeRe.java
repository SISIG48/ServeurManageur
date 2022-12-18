package fr.sisig48.pl.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.ServeurManageur.Updater.ServeurManageurUpdate;

public class CommandeRe implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] arg) {
		Player player = (Player) sender;
		if(sender instanceof Player) {
			if(ServeurManageurUpdate.DoUpdate()) {player.sendMessage("Maj effectuer"); return true;}
			player.sendMessage("Maj non effectuer"); 
			return false;
		} else {
			if(ServeurManageurUpdate.DoUpdate()) {System.out.println("Maj effectuer"); return true;}
			System.err.println("Maj non effectuer");
			return false;
		}
	}
}
