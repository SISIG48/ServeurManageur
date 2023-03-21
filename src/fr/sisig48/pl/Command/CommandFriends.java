package fr.sisig48.pl.Command;



import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.sisig48.pl.Sociale.Friends;

public class CommandFriends implements CommandExecutor {

	@SuppressWarnings({ "deprecation"})
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] arg) {
		if(!(sender instanceof Player)) sender.sendMessage("Imposible depuis une console");
		Player player = (Player) sender;
		
		if(arg.length >= 1) {
			if(arg.length >= 2) {
				switch(arg[0]) {
					case "add" :
						Player af = Bukkit.getPlayer(arg[1]);
						if(af == player) {
							player.sendMessage("§4Erreur: vous ne pouvez pas vous envoyer de demmand");
							return true;
						}
						if(!af.isOnline()) {
							player.sendMessage("§4Erreur: le joueur n'est pas connecter ou n'existe pas");
							return true;
						}
						
						Friends paf = new Friends(player);
						
						if(paf.get().contains(af)) {
								player.sendMessage("§4Erreur: le joueur est déja votre amis");
								return true;
							
						}
						Friends.FriendsResquet.add(String.valueOf(player.getUniqueId()) + "/" + String.valueOf(Bukkit.getPlayer(arg[1]).getUniqueId()));
						player.sendMessage("§aVous avez envoyer une demmand d'ami a §4" + af.getName());
						af.sendMessage("§aVous avez reçus une demmand d'ami de §4" + player.getName());
						return true;
					
					case "remove" :
						OfflinePlayer ofp = Bukkit.getOfflinePlayer(arg[1]);
						if(ofp.getName() == null) {
							player.sendMessage("§4Erreur: le joueur n'existe pas");
							return true;
						}
						Friends sf = new Friends(player);
						Friends cf = new Friends(ofp);
						if(!sf.get().contains(ofp)) {
							player.sendMessage("§4Erreur: ce joueur n'est pas votre amis");
							return true;
						}
						sf.remove(ofp);
						sf.close();
						cf.remove(player);
						cf.close();
						player.sendMessage("§aLe joueur §4" + ofp.getName() + "§a n'est plus votre amis");
						if(ofp.isOnline()) ((CommandSender) ofp).sendMessage("§a" + player.getName() + "§4 vous avez suprimé de ces amis");
						
						return true;
						
					case "accept" :
						
						Player Aaf = Bukkit.getPlayer(arg[1]);
						String cont = String.valueOf(Bukkit.getPlayer(arg[1]).getUniqueId()) + "/" + String.valueOf(player.getUniqueId());
						if(!Aaf.isOnline()) {
							player.sendMessage("§4Erreur: le joueur n'est pas conecter");
							return true;
						}
						Friends Afpl = new Friends(Bukkit.getOfflinePlayer(player.getUniqueId()));
						Friends Afaf = new Friends(Aaf);
						
						if(!Friends.FriendsResquet.contains(cont)) {
							player.sendMessage("§4Erreur: le joueur ne vous a pas fait de demmande");
							return true;
						}
						
						
						if(Afaf.get().contains(player)) {
								player.sendMessage("§4Erreur: le joueur est déja votre amis");
								return true;
							
						}
						for (String r : Friends.FriendsResquet) {
							String[] j = r.split("/");
							if(j.length < 2) break;
							if(!j[1].equals(String.valueOf(player.getUniqueId()))) break;
							if(!j[0].equals(String.valueOf(Aaf.getUniqueId()))) break;
							Afpl.add(Aaf);	
							Afpl.close();
							Afaf.add(player);
							Afaf.close();
							Friends.FriendsResquet.remove(cont);
							return true;
						}
						

					
				}
				
			}
			if(player.isOp()) {
				switch(arg[0]) {
					case "rl" :
						sender.sendMessage("§eStarting reload");
						Friends.reload();
						sender.sendMessage("§aReload complete");
						return true;
					
					case "save" :
						sender.sendMessage("§eStarting save");
						Friends.saveAll();
						sender.sendMessage("§aSave complete");
						return true;
				}

			}
			player.sendMessage("§4Formulation incorect");
			return false;
		}
		Friends f = new Friends(player);
		if(f.get().size() >= 1) {	
			player.sendMessage("§4List amis : ");
			for(OfflinePlayer e : f.get()) {
				player.sendMessage("§a  - " + e.getName());
			}
		} else {
			player.sendMessage("§4Vous n'avez pas d'amis");
		}
		return true;
	}

}
