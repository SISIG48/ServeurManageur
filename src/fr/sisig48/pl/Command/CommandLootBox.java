package fr.sisig48.pl.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.sisig48.pl.Economie.LootBox;


public class CommandLootBox implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String m, String[] arg) {
		if(sender.isOp()) LootBox.resetBox();
		return true;
	}

}
