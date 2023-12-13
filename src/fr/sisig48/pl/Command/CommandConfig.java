package fr.sisig48.pl.Command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import fr.sisig48.pl.logs;
import fr.sisig48.pl.Utils.Uconfig;

public class CommandConfig implements CommandExecutor, TabCompleter {
	
	//@CommandAlias("hiddencommand")
	//@CommandPermission("myplugin.admin")
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg) {
		if(!sender.isOp()) {
			sender.sendMessage("§4Unknown command");
			return true;
		}
		if(arg.length < 1) return false;
		logs.add("Command exucted by : " + sender.getName() + " Command : /config");
			switch(arg[0]) {
				case "save":
					Uconfig.saveConfig();
					sender.sendMessage("§aSave Complete");
					break;
				case "rl":
					Uconfig.reloadConfig();
					sender.sendMessage("§aReload Complete");
					break;
				case "reload":
					Uconfig.reloadConfig();
					sender.sendMessage("§aReload Complete");
					break;
				case "get":
					if(arg.length < 2) return false;
					sender.sendMessage(Uconfig.getConfig(arg[1]));
					break;
				case "set":
					
					if(arg.length < 3) return false;
					
					String msg = "";
					int i = 2;
					while(0 != (arg.length - i)) {
						if(1 != (arg.length - i)) msg = msg + arg[i] + " ";
						if(1 == (arg.length - i)) msg = msg + arg[i];
						i++;
					}
					Uconfig.setConfig(arg[1], msg);
					sender.sendMessage("§aConfig \"" + arg[1] + "\" has set to \"" + msg + "\"");
					break;
			}
			return true;
	}
	
	//tab complete
		public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
	        List<String> completions = new ArrayList<>();
	        if (args.length == 1) {
	        	completions.add("set");
	        	completions.add("get");
	        	completions.add("rl");
	        	completions.add("save");
	        	completions.add("reload");
	        } 
	        for(String e : completions.toArray(new String[0])) if(args.length > 0 && !e.startsWith(args[args.length-1])) completions.remove(e);
	        return completions;
	    }
}
