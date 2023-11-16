package fr.sisig48.pl.Command;



import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.sisig48.pl.Sociale.Friends;

public class CommandFriends implements CommandExecutor, TabCompleter {

	@SuppressWarnings({ "deprecation"})
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] arg) {
		if(!(sender instanceof Player)) sender.sendMessage("Imposible depuis une console");
		Player player = (Player) sender;
		
		if(arg.length >= 1) {
			if(arg.length >= 2) {
				switch(arg[0]) {
					case "add" :
						OfflinePlayer af = Bukkit.getOfflinePlayer(arg[1]);
						if(af == player) {
							player.sendMessage("§4Erreur: vous ne pouvez pas vous envoyer de demmand");
							return true;
						}
						if(!af.isOnline() || af == null) {
							player.sendMessage("§4Erreur: le joueur n'est pas connecter ou n'existe pas");
							return true;
						}
						
						Friends paf = new Friends(player);
						
						if(paf.get().contains(af)) {
								player.sendMessage("§4Erreur: le joueur est déja votre amis");
								return true;
							
						}
						paf.addRequest(af.getPlayer());
						player.sendMessage("§aVous avez envoyer une demmand d'ami a §4" + af.getName());
						af.getPlayer().sendMessage("§aVous avez reçus une demmand d'ami de §4" + player.getName());
						
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
						if(!Aaf.isOnline()) {
							player.sendMessage("§4Erreur: le joueur n'est pas conecter");
							return true;
						}
						Friends Afpl = new Friends(player);
						Friends Afaf = new Friends(Aaf);
						
						if(!Afaf.hasRequest(player)) {
							player.sendMessage("§4Erreur: le joueur ne vous a pas fait de demmande");
							return true;
						}
						
						
						if(Afaf.get().contains(player)) {
								player.sendMessage("§4Erreur: le joueur est déja votre amis");
								return true;
							
						}
							
						Afpl.add(Aaf);	
						Afpl.close();
						Afaf.add(player);
						Afaf.close();
						return true;

					
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
	
	
	//tab complete
		public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
	        List<String> completions = new ArrayList<>();
	        if(!(sender instanceof Player)) return completions;
	        if(args.length == 1) {
	        	completions.add("add");
	        	completions.add("remove");
	        	completions.add("accept");
	        } else if(args.length == 2) {
	        	if(args[0].equalsIgnoreCase("add")) for(Player p : Bukkit.getOnlinePlayers()) if(p != (Player) sender) completions.add(p.getName());
	        	if(args[0].equalsIgnoreCase("remove")) for(OfflinePlayer p : new Friends((OfflinePlayer) sender).get()) completions.add(p.getName());
	        	if(args[0].equalsIgnoreCase("accept")) for(Player p : Bukkit.getOnlinePlayers()) if(new Friends(p).hasRequest((Player) sender)) completions.add(p.getName());
	        }
	        for(String e : completions.toArray(new String[0])) if(args.length > 0 && !e.startsWith(args[args.length-1])) completions.remove(e);
	        return completions;
	    }
}
