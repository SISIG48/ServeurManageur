package fr.sisig48.pl.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.ServeurManageur.Updater.ServeurManageurUpdate;

public class CommandeRe implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] arg) {
		if(arg[0].equalsIgnoreCase("get")) {
			sender.sendMessage("§aDetecteur auto de maj : §e" + String.valueOf(ServeurManageurUpdate.NeedUpdate));
			return true;
		}
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission("DEFAULT_PERMISSION")) {player.sendMessage("§4You need are OP to execute this command"); return false;}
			if(ServeurManageurUpdate.DoUpdate(sender)) {player.sendMessage("Maj effectuer"); return true;}
			player.sendMessage("Maj non effectuer"); 
			return false;
		} else {
			if(ServeurManageurUpdate.DoUpdate(sender)) {System.out.println("Maj effectuer"); return true;}
			System.err.println("Maj non effectuer");
			return false;
		}
	}
}
