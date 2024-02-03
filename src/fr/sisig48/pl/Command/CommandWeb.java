package fr.sisig48.pl.Command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.sisig48.web.WebResponses;
import net.sisig48.web.WebServer;

public class CommandWeb implements CommandExecutor, TabCompleter{
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!sender.isOp() || args.length < 1) return true;
		if(args[0].equals("start")) new WebServer(443).startHTTPS();
		if(args[0].equals("stop")) WebServer.stopAll();
		if(args[0].equals("rl")) {
			WebServer.stopAll();
			WebResponses.load();
			new WebServer(443).startHTTPS();
		}
		if(args[0].equals("http")) new WebServer(80).startHTTP();
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command arg1, String alias, String[] args) {
		List<String> completions = new ArrayList<>();
        if(args.length == 1 && sender instanceof Player && sender.isOp()) {
        	completions.add("start");
        	completions.add("stop");
        	completions.add("rl");
        	completions.add("http");
        } 
        for(String e : completions.toArray(new String[0])) if(args.length > 0 && !e.startsWith(args[args.length-1])) completions.remove(e);
        return completions;
	}

}
