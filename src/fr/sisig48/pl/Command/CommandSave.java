package fr.sisig48.pl.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.sisig48.pl.Automating.AutoSave;

public class CommandSave implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if(sender.isOp()) AutoSave.save();
		return true;
	}

}
