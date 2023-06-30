package fr.sisig48.pl.Command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

import fr.sisig48.pl.State.ShopPNJ;
import fr.sisig48.pl.Utils.Uconfig;

public class CommandShop extends ShopPNJ implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg) {
		if(sender.isOp()) {
			String r = "-";
			int n = 0;
			if(arg.length == 1) {
				try {
					String uuids;
					for(int i = 0; (r = Uconfig.getConfig("location.pnj.shop."+ i)) != null && (uuids = Uconfig.getConfig("location.pnj.shop."+ r +".uuid")) != null && !uuids.equals(arg[0]); n = i++);
					r = Uconfig.getConfig("location.pnj.shop."+ n +".uuid");
					if(r != null && r.equals(arg[0])) {
						UUID uuid = UUID.fromString(arg[0]);
						Bukkit.getEntity(uuid).remove();
						sender.sendMessage("§a§l" + arg[0] + " §esuprimé §8§l(§d" + Uconfig.getConfig("location.pnj.shop."+ n + ".name") + "§8§l)");
						if(n != -1) Uconfig.setConfig("location.pnj.shop."+ n, "void");
						return true;
					} else {
						sender.sendMessage("§4§lUUID non trouvé");
						return true;
					}
				} catch (IllegalArgumentException e) {}
			}
					
			for(int i = 0; r != null && !r.equals("void") ; n = i++) r = Uconfig.getConfig("location.pnj.shop."+ i);
			Location loc = Bukkit.getPlayer(sender.getName()).getLocation();
			ShopPNJ.setLoc(loc, n);
			Villager pnj = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
			Uconfig.setConfig("location.pnj.shop." + n + ".uuid", String.valueOf(pnj.getUniqueId()));
			ShopPNJ.addUUID(pnj.getUniqueId());
			pnj.setAI(false);
			pnj.setCanPickupItems(false);
			pnj.setCollidable(false);
			ShopPNJ.SetType(pnj);
			String name = "§aShop";
			if(arg.length >= 1) {
				name = "";
				int i = 0;
				for(String t : arg) {
					if(i == 0) name = t.replaceAll("&", "§");
					else name = name + " " + t.replaceAll("&", "§");
					i++;
				}
			}
			pnj.setCustomName(name);
			Uconfig.setConfig("location.pnj.shop." + n + ".name", name);
			pnj.setCustomNameVisible(true);
			pnj.setInvulnerable(true);
			return true;
		}
		sender.sendMessage("Unknown command. Type \"/help\" for help.");
		return true;
	}
}
