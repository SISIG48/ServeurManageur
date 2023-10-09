package fr.sisig48.pl.Command;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.sisig48.pl.logs;

public class CommandBug implements CommandExecutor, TabCompleter {
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String voi, String[] arg) {
		if(arg.length > 0 && sender.isOp()) {
			if(arg[0].equalsIgnoreCase("get")) {
				
				try {
					for(String e : logs.ReadFile("bug.txt")) sender.sendMessage("§4" + e);
				} catch (IOException e1) {

					e1.printStackTrace();
				}
				
				return true;
			}
		}
		if(arg.length < 1) {
			sender.sendMessage("§6Pour sigaler un bug : §4/bug §e<message>");
			if(sender.isOp()) sender.sendMessage("§6Pour voir les bugs sigaler : §4/bug §eget");
			return true;
		}
		logs.add("Command exucted by : " + sender.getName() + " Command : /bug");
		String msg = "";
		for(String e : arg) {
			msg = msg + " " + e; 
		}
		sender.sendMessage("§eVous avez signaler :§4" + msg + " §ecomme bug,");
		sender.sendMessage("§enos équipe de dévlopement traiterons votre demmande");
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(!p.isOp()) break;
			p.sendMessage("§e§lBugs §8- §6Report by : §a§l" + sender.getName() + " §8- §6Reported bug : §4" + msg);
		}
		try {
			logs.reportBug(msg, sender.getName());
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e1) { 
			e1.printStackTrace();
		}
		return true;
	}
	
	//tab complete
		public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
	        List<String> completions = new ArrayList<>();
	        if (args.length == 1 && sender.isOp()) completions.add("get");
	        return completions;
	    }
}
