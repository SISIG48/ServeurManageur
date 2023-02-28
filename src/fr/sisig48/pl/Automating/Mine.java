package fr.sisig48.pl.Automating;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import fr.sisig48.pl.Main;
import fr.sisig48.pl.Utils.Uconfig;

public class Mine extends Uconfig {
	public Mine(Main main) {
		super(main);
	}
	public static Thread AutoFill = new Thread(new Runnable() {
		
		
		@Override
		public void run() {
			

			while(true) {
				Bukkit.getConsoleSender().sendMessage("§aLa mine est prête");
				FillMine();
				try {
					Thread.sleep(1800000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	});
	static Main Plug = null;

	public static void SetFillMineAt(Player player) {
		Uconfig.setConfig("location.mine.zone.w", String.valueOf(player.getLocation().getWorld().getName()));
		Uconfig.setConfig("location.mine.zone.at.x", String.valueOf((int) player.getLocation().getX()));
		Uconfig.setConfig("location.mine.zone.at.y", String.valueOf((int) player.getLocation().getY()));
		Uconfig.setConfig("location.mine.zone.at.z", String.valueOf((int) player.getLocation().getZ()));
		
	}
	public static void SetFillMineTo(Player player) {
		Uconfig.setConfig("location.mine.zone.to.x", String.valueOf((int) player.getLocation().getX()));
		Uconfig.setConfig("location.mine.zone.to.y", String.valueOf((int) player.getLocation().getY()));
		Uconfig.setConfig("location.mine.zone.to.z", String.valueOf((int) player.getLocation().getZ()));

	}
	@SuppressWarnings("static-access")
	public static void FillMine() {
		Plug = main.getPlugin(main.getClass());
		String x = Uconfig.getConfig("location.mine.zone.at.x");
		String y = Uconfig.getConfig("location.mine.zone.at.y");
		String z = Uconfig.getConfig("location.mine.zone.at.z");
		World world = Bukkit.getWorld(Uconfig.getConfig("location.mine.zone.w"));
		String x2 = Uconfig.getConfig("location.mine.zone.to.x");
		String y2 = Uconfig.getConfig("location.mine.zone.to.y");
		String z2 = Uconfig.getConfig("location.mine.zone.to.z");
		Bukkit.getScheduler().runTask(Plug, () -> {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/world " + world.getName());
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos1 " + x + "," + y +"," + z);
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos2 " + x2 + "," + y2 +"," + z2);
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/set 29.5%stone,20%iron_ore,10%gold_ore,20%coal_ore,10%redstone_ore,10%lapis_ore,0.5%diamond_ore");
		});
	}
}	
