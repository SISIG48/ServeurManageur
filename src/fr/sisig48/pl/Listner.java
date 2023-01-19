package fr.sisig48.pl;

import java.io.IOException;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

import fr.ServeurManageur.Updater.ServeurManageurUpdate;
import fr.sisig48.pl.Menu.Interface;
import fr.sisig48.pl.Menu.MenuPP;
import fr.sisig48.pl.NetherStar.NetherStarMenu;
import fr.sisig48.pl.State.Spawn;
import net.ess3.api.MaxMoneyException;



public class Listner implements Listener {
	
	Main main;
	public Listner(Main main) {
		this.main = main;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		try {
			logs.PlayerConect(player);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ServeurManageurUpdate.SendMaj();
		NetherStarMenu.GiveMenu(player);
		
		
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		try {
			logs.PlayerLeave(player);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		logs.add("Player : UUID : " + player.getUniqueId() + " | Name :" + player.getName() + " Respawn");
		try {
			player.teleport(Spawn.GetSpawnLocation());
			Economy.divide(player.getName(), 2);
			player.sendMessage("§4Vous ête mort et §4§lavez perdu §2" + Economy.getMoney(player.getName()));
		} catch (NoLoanPermittedException e) {
			e.printStackTrace();
		} catch (UserDoesNotExistException e) {
			e.printStackTrace();
		} catch (MaxMoneyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NetherStarMenu.GiveMenu(player);
	}
	
	@EventHandler
	public void OnIteract(PlayerInteractEvent event) {
		if(event.getItem() == null) return;
		if(!event.getItem().getItemMeta().hasCustomModelData()) return;
		if(event.getPlayer() == null) return;
		if(event.getItem() == null) return;
		Player player = event.getPlayer();
		logs.add("Player : UUID : " + player.getUniqueId() + " | Name :" + player.getName() + " Enter to MenuEcoPublic");
		try {
			if(event.getItem().getItemMeta().getCustomModelData() < 0 ) return;
		} catch (Exception e) {return;}
		int it = event.getItem().getItemMeta().getCustomModelData();
		event.setCancelled(NetherStarMenu.HasMenu(player, it, event.getItem()));
	
	}
		
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if(!event.getItemDrop().getItemStack().getItemMeta().hasCustomModelData()) return;
		Player player = event.getPlayer();
		
		//Verif du Menu
		int it = event.getItemDrop().getItemStack().getItemMeta().getCustomModelData();
		event.setCancelled(NetherStarMenu.HasMenu(player, it));
		
		
	}


	@EventHandler
	public void OnClick(InventoryClickEvent event) {
		
		Inventory inv = event.getInventory();
		
		Player player = (Player) event.getWhoClicked();
		
		MenuPP.current = event.getCurrentItem();
		ItemStack current = event.getCurrentItem();
		if(current == null || current.getItemMeta() == null) return;
		if(!current.getItemMeta().hasCustomModelData()) return;
		logs.add("Player : UUID : " + player.getUniqueId() + " | Name :" + player.getName() + " Click whith : " + String.valueOf(current.getType()));
		try {
			if(Interface.GetActonIfInMainMenu(player, current, inv)) {
				event.setCancelled(true);
			} else {
				event.setCancelled(NetherStarMenu.HasMenu(player, current.getItemMeta().getCustomModelData(), current));
				if(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
					player.sendMessage("§4§l/!\\ Vous Â§tes en créatif, §4merci de ne pas dupliqué votre §cétoile");
					logs.add("Player : UUID : " + player.getUniqueId() + " | Name :" + player.getName() + " Risk of duplication (GAMEMODE 1 ERROR) Target : Nether Start Menu");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
	}
	
	
}