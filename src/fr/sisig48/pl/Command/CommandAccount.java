package fr.sisig48.pl.Command;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		if(!(sender instanceof Player)) return true;
		Player p = (Player) sender;
		WebAccount account = WebAccount.getUser(p.getUniqueId().toString());
		
		if(args.length == 1) {
			if(args[0].equals("delete")) {
				WebAccount.delAccount(account);
				p.sendMessage("§4Votre compte est suprimé");
				return true;
			}
			if(args[0].equals("reset")) {
				WebAccount.delAccount(account);
				account = new WebAccount(p.getUniqueId().toString());
			}
		}
		
		if(account == null) account = new WebAccount(p.getUniqueId().toString());
		
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
        if(args.length == 1) completions.addAll(Arrays.asList("reset", "delete"));
        for(String e : completions.toArray(new String[0])) if(args.length > 0 && !e.startsWith(args[args.length-1])) completions.remove(e);
        return completions;
    }
}
