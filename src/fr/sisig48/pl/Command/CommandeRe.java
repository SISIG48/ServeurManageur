package fr.sisig48.pl.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.ServeurManageur.Updater.ServeurManageurUpdate;

public class CommandeRe implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] arg) {
		for (String e : arg) {
			if(e.equalsIgnoreCase("get")) {
				sender.sendMessage("§aDetecteur auto de maj : §e" + String.valueOf(ServeurManageurUpdate.NeedUpdate));
				return true;
			}
			if(e.equalsIgnoreCase("reload")) {
				sender.sendMessage("§aDetecteur auto de maj : §eSTART");
				if(ServeurManageurUpdate.CheckUpdate()) {
					sender.sendMessage("§eYou need do §4UPDATE");
					ServeurManageurUpdate.NeedUpdate = 1;
				} else {
					sender.sendMessage("§eYou dont need §aUPDATE");
					ServeurManageurUpdate.NeedUpdate = 0;
				}
				return true;
			}
			if(e.equalsIgnoreCase("force")) {
				sender.sendMessage("§eVerrUpdate Syetem say : §4You dont need §aUPDATE");
				sender.sendMessage("§eVerrUpdate Syetem say : §4Your §aUPDATE §4starting...");
				if (!sender.isOp()) {sender.sendMessage("§4You need are OP to execute this command"); return false;}
				if(ServeurManageurUpdate.DoUpdate(sender)) {sender.sendMessage("§eVerrUpdate Syetem say : §4Your §aUPDATE §4ending | §aSUCCESS"); return true;}
				sender.sendMessage("§eVerrUpdate Syetem say : §4Your §aUPDATE §4ending | §4FAILED"); 
				return false;
			}
		}
		
		if(ServeurManageurUpdate.NeedUpdate == 1) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if (!player.isOp()) {player.sendMessage("§4You need are OP to execute this command"); return false;}
				if(ServeurManageurUpdate.DoUpdate(sender)) {player.sendMessage("Maj effectuer"); return true;}
				player.sendMessage("Maj non effectuer"); 
				return false;
			} else {
				if(ServeurManageurUpdate.DoUpdate(sender)) {System.out.println("Maj effectuer"); return true;}
				System.err.println("Maj non effectuer");
				return false;
			}
		}
		sender.sendMessage("§eVerrUpdate Syetem say : §4You dont need §aUPDATE");
		return true;
	}
}
