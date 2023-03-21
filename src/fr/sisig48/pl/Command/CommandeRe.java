package fr.sisig48.pl.Command;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.ServeurManageur.Updater.ServeurManageurUpdate;
import fr.sisig48.pl.logs;

public class CommandeRe implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] arg) {
		if (!sender.isOp()) return true;
		if (arg.length > 0) {
			if(arg[0].equalsIgnoreCase("get")) {
				sender.sendMessage("§aDetecteur auto de maj : §e" + String.valueOf(ServeurManageurUpdate.NeedUpdate));
				logs.add("Command exucted by : " + sender.getName() + " Command : /re get");
				return true;
			}
			if(arg[0].equalsIgnoreCase("reload")) {
				sender.sendMessage("§aDetecteur auto de maj : §eSTART");
				if(ServeurManageurUpdate.CheckUpdate()) {
					sender.sendMessage("§eYou need do §4UPDATE");
					logs.add("Command exucted by : " + sender.getName() + " Command : /re reload Exit : No Maj");
					ServeurManageurUpdate.NeedUpdate = 1;
				} else {
					sender.sendMessage("§eYou dont need §aUPDATE");
					logs.add("Command exucted by : " + sender.getName() + " Command : /re reload Exit : Maj detect");
					ServeurManageurUpdate.NeedUpdate = 0;
				}
				return true;
			}
			if(arg[0].equalsIgnoreCase("force")) {
				logs.add("Command exucted by : " + sender.getName() + " Command : /re force");
				sender.sendMessage("§eVerrUpdate Syetem say : §4You dont need §aUPDATE");
				sender.sendMessage("§eVerrUpdate Syetem say : §4Your §aUPDATE §4starting...");
				if (!sender.isOp()) {sender.sendMessage("§4You need are OP to execute this command"); return true;}
				if(ServeurManageurUpdate.DoUpdate(sender)) {sender.sendMessage("§eVerrUpdate Syetem say : §4Your §aUPDATE §4ending | §aSUCCESS"); return true;}
				sender.sendMessage("§eVerrUpdate Syetem say : §4Your §aUPDATE §4ending | §4FAILED"); 
				return true;
			}
			
			if(arg[0].equalsIgnoreCase("v") & arg.length == 2) {
				logs.add("Command exucted by : " + sender.getName() + " Command : /re v " + arg[1]);
				if (!sender.isOp()) {sender.sendMessage("§4You need are OP to execute this command"); return true;}
				if(ServeurManageurUpdate.DoSpecificUpdate(sender, arg[1])) {sender.sendMessage("§eSyetem say : §4Your §aUPDATE §4ending | §aSUCCESS"); return true;}
				sender.sendMessage("§eSyetem say : §4Your §aUPDATE §4ending | §4FAILED"); 
				return true;
			}
			
			if(arg[0].equalsIgnoreCase("news")) {
				try {
					String[] news = logs.ReadFile("note.txt");
					for(String e : news) {
						sender.sendMessage(e);

					}
				} catch (IOException e) {}
				
				
				return true;
			}
			return false;
			
		}
		
		if(ServeurManageurUpdate.NeedUpdate == 1) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if (!player.isOp()) {player.sendMessage("§4You need are OP to execute this command"); return false;}
				if(ServeurManageurUpdate.DoUpdate(sender)) {
					player.sendMessage("Maj effectuer"); 
					logs.add("Command exucted by : " + sender.getName() + " Command : /re Exit : Maj effectued");
					return true;
				}
				player.sendMessage("Maj non effectuer");
				logs.add("Command exucted by : " + sender.getName() + " Command : /re Exit : Err do maj");
				return true;
			} else {
				if(ServeurManageurUpdate.DoUpdate(sender)) {
					System.out.println("Maj effectuer"); 
					logs.add("Command exucted by : " + sender.getName() + " Command : /re Exit : Maj effectued");
					return true;
				}
				System.err.println("Maj non effectuer");
				logs.add("Command exucted by : " + sender.getName() + " Command : /re Exit : Err do maj");
				return true;
			}
		}
		logs.add("Command exucted by : " + sender.getName() + " Command : /re Exit : Don't need update");
		sender.sendMessage("§eVerrUpdate Syetem say : §4You dont need §aUPDATE");
		return true;
	}
}
