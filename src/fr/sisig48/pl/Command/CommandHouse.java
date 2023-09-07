package fr.sisig48.pl.Command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.sisig48.pl.JobsHouse.HouseData;
import fr.sisig48.pl.JobsHouse.HouseList;
import fr.sisig48.pl.JobsHouse.HouseListInfo;

public class CommandHouse implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] arg) {
		Player p = (Player) sender;
		try {
			if(arg.length >= 1) {
				p.teleport(HouseList.getHouseBySlot(Integer.parseInt(arg[0])).getLocation());
				return true;
			}
	 	} catch (NumberFormatException nada) {
	 		if(p.isOp()) {
				switch (arg[0]) {
				case "add":
					HouseData.addLoc(p.getLocation());
					p.sendMessage("§aShop ajouter");
					return true;
				case "delete":
					HouseData.delLoc(p.getLocation());
					p.sendMessage("§4Shop suprimé");
					return true;

				}
			}
	 	}
		
		p.sendMessage("§4Commande invalide");
		return true;
	}
	
	//tab complete
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> completions = new ArrayList<>();
		Player p = (Player) sender;
		if (args.length == 1) {
			if(!HouseData.getLoc().contains(p.getLocation())) completions.add("add");
	    	if(HouseData.getLoc().contains(p.getLocation())) completions.add("delete");
	    	for(int i = 0 ; i != HouseList.getSlots() ; i++) completions.add("" + i);
		}
	         
	    return completions;
	}
}
