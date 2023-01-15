package fr.sisig48.pl.Command;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.sisig48.pl.State.Spawn;
import fr.sisig48.pl.Utils.Uconfig;

public class CommandeSpawn implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] arg) {
		
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if (player.isOp()) {
				for (String e : arg) {
					if (e.equals("set")) {
						if (!player.hasPermission("DEFAULT_PERMISSION"));
						String x = String.valueOf(player.getLocation().getX());
						String y = String.valueOf(player.getLocation().getY());
						String z = String.valueOf(player.getLocation().getZ());
						String ya = String.valueOf(player.getLocation().getYaw());
						String pi = String.valueOf(player.getLocation().getPitch());
						String w = player.getLocation().getWorld().getName().toString();
						String[] loc = {x, y, z, w, ya, pi};
						Spawn.SetSpawnLocation(loc);
						return true;
					}
					
					if (Bukkit.getPlayer(e) != null && Bukkit.getPlayer(e).isOnline()) {
						if (!player.hasPermission("DEFAULT_PERMISSION")) {player.sendMessage("§4You can't do this command"); return false;}
						try {
							Player p = Bukkit.getPlayer(e);
							p.teleport(Spawn.GetSpawnLocation());
							p.sendMessage("§aVous avez été tp au §4spawn");
							player.sendMessage("§aVous avez tp : §4" + e);
							return true;
						} catch (IOException i) {i.printStackTrace();}
					 }
					
				player.sendMessage("§4§l" + e + " §4etait inatendu");
				}
			}
			try {

				if(player.getLocation().getWorld().getName().equals(Uconfig.getConfig("location.mine.in.w")) ) {
					player.teleport(Spawn.GetMineOutSpawnLocation());
					player.sendMessage("§aVous avez été tp au §4spawn");
				} else {
					player.teleport(Spawn.GetSpawnLocation());
					player.sendMessage("§aVous avez été tp au §4spawn");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

}
