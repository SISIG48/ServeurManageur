package fr.sisig48.pl;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
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
import net.ess3.api.MaxMoneyException;



public class Listner implements Listener {
	
	Main main;
	public Listner(Main main) {
		this.main = main;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		ServeurManageurUpdate.SendMaj();
		NetherStarMenu.GiveMenu(player);
	}

	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		try {
			Economy.divide(player.getName(), 2);
			player.sendMessage("§4Vous Â§te mort et §4§lavez perdu §2" + Economy.getMoney(player.getName()));
		} catch (NoLoanPermittedException e) {
			e.printStackTrace();
		} catch (UserDoesNotExistException e) {
			e.printStackTrace();
		} catch (MaxMoneyException e) {
			e.printStackTrace();
		}
		NetherStarMenu.GiveMenu(player);
	}
	
	@EventHandler
	public void OnIteract(PlayerInteractEvent event) {
		
		if(event.getPlayer() == null) return;
		if(event.getItem() == null) return;

		try {
			if(event.getItem().getItemMeta().getCustomModelData() < 0 ) return;
		} catch (Exception e) {return;}
		Player player = event.getPlayer();
		int it = event.getItem().getItemMeta().getCustomModelData();
		event.setCancelled(NetherStarMenu.HasMenu(player, it, event.getItem()));
	
	}
		
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		
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
		if(current == null) return;
		try {
			if(Interface.GetActonIfInMainMenu(player, current, inv)) {
				event.setCancelled(true);
			} else {
				event.setCancelled(NetherStarMenu.HasMenu(player, current.getItemMeta().getCustomModelData(), current));
				if(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
					player.sendMessage("§4§l/!\\ Vous Â§tes en créatif, §4merci de ne pas dupliqué votre §cétoile");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
	}
	
	
}