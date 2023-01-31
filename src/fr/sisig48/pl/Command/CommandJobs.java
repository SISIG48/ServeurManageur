package fr.sisig48.pl.Command;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.sisig48.pl.Sociale.Jobs;
import fr.sisig48.pl.Sociale.PlayerJobs;

public class CommandJobs implements CommandExecutor {

	@SuppressWarnings("static-access")
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg) {
		PlayerJobs.reload();
		PlayerJobs.load();
		PlayerJobs playerJobs = new PlayerJobs((OfflinePlayer) sender);
		playerJobs.add(Jobs.HUNTER);
		if(arg.length == 1) playerJobs.add(Jobs.valueOf(arg[0]));
		playerJobs.save();
		playerJobs.close();
		playerJobs.saveAll();
		sender.sendMessage(playerJobs.get().getName());
		return true;
	}

}
