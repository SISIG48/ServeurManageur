package fr.sisig48.pl.Command;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.sisig48.web.WebAccount;

public class CommandAccount implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(args.length > 0 && sender.isOp()) {
			if(args[0].equals("setPerm")) {
				if(args.length >= 3) {
					try {
						WebAccount account = WebAccount.getUserByTag("UUID", UUID.fromString(args[1]));
						if(account != null) {
							List<String> perms = new ArrayList<String>();
							for(int i = 2; i < args.length; i++) perms.add(args[i]); 
							account.setPerm(perms);
							sender.sendMessage("§aJoueur §e" + account.getTag() + " §8(TAG), §aID §e" + account.getId() + " §aest maintenant §4"  + args[2]);
						} else sender.sendMessage("§4Compte joueur invalide");
					} catch (IllegalArgumentException e) {
						WebAccount account = WebAccount.getUserByTag("name", args[1]);
						if(account != null) {
							List<String> perms = new ArrayList<String>();
							for(int i = 2; i < args.length; i++) perms.add(args[i]); 
							account.setPerm(perms);
							sender.sendMessage("§aJoueur §e" + args[1] + " §8(TAG), §aID §e" + account.getId() + " §aest maintenant §4"  + args[2]);
						}
						else sender.sendMessage("§4Compte joueur invalide");
					}
				} else sender.sendMessage("§4COMMANDE INVALIDE : §8/account setPerm <UUID> <perm1> <perm2> <...>");
				return true;
			} else if(args[0].equals("create")) {
				if(args.length == 4) {
					if(args[1].length() < 8 || WebAccount.AlreadySet(args[1]) || args[2].length() < 16 || WebAccount.getUserByTag("name", args[3]) != null) sender.sendMessage("§4INVALIDE §8(ID [min. 8 Caractère] ou MDP [min. 16 Caractère])");
					else {
						HashMap<Object, Object> tags = new HashMap<Object, Object>();
						tags.put("name", args[3]);
						WebAccount account = WebAccount.createAccount(args[1], args[2], tags);
						
						sender.sendMessage("§9Nom d'utilisateur : " + account.getId());
						sender.sendMessage("§9Mot de passe : " + account.getPassword());
						try {
							TextComponent msgl = new TextComponent("§e[§e§lLOGIN§e] §8(https://" + InetAddress.getLocalHost().getHostAddress() + "/login.html?id=" + account.getId() + "&pass=" + account.getPassword() + ")");
							msgl.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://" + InetAddress.getLocalHost().getHostAddress() + "/login.html?id=" + account.getId() + "&pass=" + account.getPassword()));
							sender.spigot().sendMessage(msgl);
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
					}
					return true;
				}
				sender.sendMessage("§4COMMANDE INVALIDE : §8/account create <ID> <PASSWORD> <USERNAME>");
				return true;
			} else if(args[0].equals("delete") && args.length == 2) {
				WebAccount account = null;
				try {
					account = WebAccount.getUserByTag("UUID", UUID.fromString(args[1]));
				} catch (IllegalArgumentException e) {
					account = WebAccount.getUserByTag("name", args[1]);
				}
				if(account != null) {
					WebAccount.delAccount(account);
					sender.sendMessage("§aCompte trouvé et suprimé : " + account.getId());
				}
				else sender.sendMessage("§4Compte introuvable");
				return true;
			}
		}
		
		if(!(sender instanceof Player)) {
			WebAccount account = WebAccount.getUserByTag("name", "00-CONSOLE");
			
			if(args.length == 1) {
				if(args[0].equals("delete")) {
					WebAccount.delAccount(account);
					sender.sendMessage("§4Votre compte est suprimé");
					return true;
				}
				if(args[0].equals("reset")) {
					WebAccount.delAccount(account);
					account = null;
				}
			}
			
			if(account == null) {
				Map<Object, Object> tags = new HashMap<Object, Object>();
				tags.put("name", "00-CONSOLE");
				account = WebAccount.createAccount(WebAccount.createID(), WebAccount.createPassword(), tags);
			}
			
			sender.sendMessage("§9Nom d'utilisateur : " + account.getId());
			sender.sendMessage("§9Mot de passe : " + account.getPassword());
			try {
				TextComponent msgl = new TextComponent("§e[§e§lLOGIN§e] §8(https://" + InetAddress.getLocalHost().getHostAddress() + "/login.html?id=" + account.getId() + "&pass=" + account.getPassword() + ")");
				msgl.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://" + InetAddress.getLocalHost().getHostAddress() + "/login.html?id=" + account.getId() + "&pass=" + account.getPassword()));
				sender.spigot().sendMessage(msgl);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			return true;
		}
		Player p = (Player) sender;
		WebAccount account = WebAccount.getUserByTag("UUID", p.getUniqueId());
		
		if(args.length == 1 && account != null) {
			if(args[0].equals("delete")) {
				WebAccount.delAccount(account);
				p.sendMessage("§4Votre compte est suprimé");
				return true;
			}
			if(args[0].equals("reset")) {
				WebAccount.delAccount(account);
				account = null;
			}
		}
		
		if(account == null) {
			Map<Object, Object> tags = new HashMap<Object, Object>();
			tags.put("UUID", p.getUniqueId());
			tags.put("name", p.getName());
			account = WebAccount.createAccount(WebAccount.createID(), WebAccount.createPassword(), tags);
		}
		
		account.setIp(p.getAddress().getAddress());
		p.sendMessage("§9Nom d'utilisateur : " + account.getId());
		p.sendMessage("§9Mot de passe : " + account.getPassword());
		
		try {
			TextComponent msgl = new TextComponent("§e[§e§lLOGIN§e]");
			msgl.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://" + InetAddress.getLocalHost().getHostAddress() + "/login.html?id=" + account.getId() + "&pass=" + account.getPassword()));
			p.spigot().sendMessage(msgl);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if(args.length == 1) {
        	completions.addAll(Arrays.asList("reset", "delete"));
        	if(sender.isOp()) {
        		completions.add("setPerm");
        		completions.add("create");
        	}
        }
        if(args.length == 2 && sender.isOp()) {
        	if(args[0].equals("setPerm")) for(OfflinePlayer p : Bukkit.getOfflinePlayers()) completions.add(p.getUniqueId().toString());
        	if(args[0].equals("create") || args[0].equals("delete")) completions.add("" + WebAccount.createID());
        }
        for(String e : completions.toArray(new String[0])) if(args.length > 0 && !e.startsWith(args[args.length-1])) completions.remove(e);
        return completions;
    }
}
