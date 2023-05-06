package fr.sisig48.pl.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.sisig48.pl.JobsHouse.HouseData;

public class CommandHouse implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] arg) {
		Player p = (Player) sender;
		if (arg[0].equals("1")) HouseData.delAllHouse(p);
		else HouseData.addHouse(p, p.getLocation());
		return true;
	}
}
