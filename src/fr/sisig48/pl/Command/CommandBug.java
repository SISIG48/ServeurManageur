package fr.sisig48.pl.Command;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.sisig48.pl.logs;

public class CommandBug implements CommandExecutor {
	
	//@CommandAlias("hiddencommand")
	//@CommandPermission("myplugin.admin")
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String voi, String[] arg) {
		if(arg.length < 1) return false;
		logs.add("Command exucted by : " + sender.getName() + " Command : /bug");
		String msg = "";
		for(String e : arg) {
			msg = msg + " " + e; 
		}
		sender.sendMessage("§eVous avez signaler :§4" + msg + " §ecomme bug,");
		sender.sendMessage("§enos équipe de dévlopement traiterons votre demmande");
		try {
			logs.reportBug(msg, sender.getName());
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return true;
	}

}
