package fr.sisig48.pl.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.sisig48.pl.JobsHouse.HouseData;
import fr.sisig48.pl.JobsHouse.HouseList;

public class CommandHouse implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] arg) {
		Player p = (Player) sender;
		try {
			if(arg.length >= 1) p.teleport(HouseList.getHouseBySlot(Integer.parseInt(arg[0])).getLocation());
	 	} catch (NumberFormatException nada) {
	 		if(p.isOp()) {
				switch (arg[0]) {
				case "add":
					HouseData.addLoc(p.getLocation());
					p.sendMessage("Shop ajouter");
					return true;

				}
			}
	 	}
		
		p.sendMessage("§4Command invalide");
		return true;
	}
}
