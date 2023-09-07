package fr.sisig48.pl.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

import fr.sisig48.pl.State.ShopPNJ;
import fr.sisig48.pl.Utils.Uconfig;

public class CommandShop extends ShopPNJ implements CommandExecutor, TabCompleter {
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg) {
		if(sender.isOp()) {
			String path = "location.pnj.shop.";
			int n = -1;
			if(arg.length == 1 && arg[0].length() == 36) {
				try {
					
					if(ShopPNJ.getUUIDS().contains(UUID.fromString(arg[0]))) {
						//UUID Entrer
						UUID uuid = UUID.fromString(arg[0]);
						
						@SuppressWarnings("unused")
						//Detection emplacement de save
						String temp = "";
						for(int i = 0; (temp = Uconfig.getConfig(path + i)) != null ; i++) try{if(Uconfig.getConfig(path + i + ".uuid").equals(uuid.toString())) n = i;} catch (NullPointerException e) {};
						if(n == -1) sender.sendMessage("§4Erreur interne");
						
						//Recupération du nom sauvegarder
						String name = Uconfig.getConfig(path + n + ".name");
						
						//Destruction de la save
						Uconfig.setConfig(path + n, "void");
						ShopPNJ.dellUUID(uuid);
						
						//Supressions du PNJ
						Bukkit.getEntity(uuid).remove();
						
						//Message de supression
						sender.sendMessage("§a§l" + arg[0] + " §esuprimé §8§l(§d" + name + "§8§l)");
						return true;
					} else {
						sender.sendMessage("§4§lUUID non trouvé");
						return true;
					}
				} catch (IllegalArgumentException e) {
					sender.sendMessage("§4§lUUID invalide");
					return true;
				}
			}
			
			//Detection emplacement de save
			String r = "-";
			for(int i = 0; r != null && !r.equals("void") ; n = i++) r = Uconfig.getConfig(path + i);
			
			
			//Récupération du nom PNJ
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
			
			//Création PNJ
			Location loc = Bukkit.getPlayer(sender.getName()).getLocation();
			Villager pnj = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
			pnj.setAI(false);
			pnj.setCanPickupItems(false);
			pnj.setCollidable(false);
			pnj.setCustomName(name);
			ShopPNJ.SetType(pnj);
			pnj.setCustomNameVisible(true);
			pnj.setInvulnerable(true);
			
			
			//Créeation de la sauvegarde (location) PNJ
			ShopPNJ.setLoc(loc, n);
			Uconfig.setConfig(path + n + ".name", name);
			//Sauvegarde UUID
			Uconfig.setConfig(path + n + ".uuid", String.valueOf(pnj.getUniqueId()));
			ShopPNJ.addUUID(pnj.getUniqueId());
			return true;
		}
		sender.sendMessage("Unknown command. Type \"/help\" for help.");
		return true;
	}
	
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) for(UUID uuid : ShopPNJ.getUUIDS()) completions.add(uuid.toString());
        return completions;
    }
}
