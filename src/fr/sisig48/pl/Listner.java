package fr.sisig48.pl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

import fr.ServeurManageur.Updater.ServeurManageurUpdate;
import fr.sisig48.pl.Automating.PayPal;
import fr.sisig48.pl.Menu.Interface;
import fr.sisig48.pl.Menu.JobsMenu;
import fr.sisig48.pl.Menu.MenuPP;
import fr.sisig48.pl.Menu.ShopMenu;
import fr.sisig48.pl.NetherStar.NetherStarMenu;
import fr.sisig48.pl.Sociale.Friends;
import fr.sisig48.pl.Sociale.PlayerJobs;
import fr.sisig48.pl.State.JobsPNJ;
import fr.sisig48.pl.State.ShopPNJ;
import fr.sisig48.pl.State.Spawn;
import fr.sisig48.pl.Utils.Uconfig;
import net.ess3.api.MaxMoneyException;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;


@SuppressWarnings("deprecation")
public class Listner implements Listener {
	
	Main main;
	public Listner(Main main) {
		this.main = main;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		player.sendMessage("§aBienvenu sur le serveur");
		player.sendMessage("§eSi tu a un problème contact le staff sur le Â§4discord Â§eou");
		player.sendMessage("§eexécute la command §4/bug [arg...]");
		if(player.isOp()) {
			player.sendMessage("");
			player.sendMessage("§4Vous ête administrateur sur ce serveur :");
			player.sendMessage("  §e- /help ServeurManageur pour obtenir la list des command disponible");
		}
		
		try {
			logs.PlayerConect(player);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ServeurManageurUpdate.SendMaj();
		NetherStarMenu.GiveMenu(player);
		Friends f = new Friends(player);
		int i = 0;
		
		for(OfflinePlayer p : f.get()) {
			if(p.isOnline()) {
				i++;
				p.getPlayer().sendMessage("§e"+ player.getName() + "§a c'est connecter");
			}
			
		}
		if(i != 0) player.sendMessage("§aVous avez §e" + i + " §aamis en ligne");
		new PayPal(player);	
		
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer(); 
		new PlayerJobs(player).close();
		PayPal.clear(player);
		try {
			logs.PlayerLeave(player);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		logs.add("Player : UUID : " + player.getUniqueId() + " | Name :" + player.getName() + " Respawn");
		try {
			player.teleport(Spawn.GetSpawnLocation());
			Economy.divide(player.getName(), 2);
			player.sendMessage("§4Vous ête mort et §4§lavez perdu §2" + Economy.getMoney(player.getName()));
		} catch (NoLoanPermittedException | UserDoesNotExistException | MaxMoneyException | IOException e) {
			e.printStackTrace();
		}
		NetherStarMenu.GiveMenu(player);
	}
	
	@EventHandler
	public void OnEntityInteract(PlayerInteractEntityEvent event) {
		event.setCancelled(true);
		UUID uuid = event.getRightClicked().getUniqueId();
		if(JobsPNJ.getUUIDS().contains(uuid)) JobsMenu.OpenJobsMenu(event.getPlayer());
		else if(ShopPNJ.getUUIDS().contains(uuid)) ShopMenu.OpenShop(event.getPlayer());
		else event.setCancelled(false);
		
	}
	
	@EventHandler
	public void OnIteract(PlayerInteractEvent event) {
		
		if(event.getPlayer() == null) return;
		
		Player player = event.getPlayer();
		if(event.getItem() == null) return;
		event.setCancelled(ShopMenu.TcheckShopMenuAction(player, event.getItem()));
		if(!event.getItem().getItemMeta().hasCustomModelData()) return;
		try {if(event.getItem().getItemMeta().getCustomModelData() < 0 ) return;
		} catch (Exception e) {return;}
		int it = event.getItem().getItemMeta().getCustomModelData();
		event.setCancelled(NetherStarMenu.HasMenu(player, it, event.getItem()));
	}
		
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if(!event.getItemDrop().getItemStack().getItemMeta().hasCustomModelData()) return;
		Player player = event.getPlayer();
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
		event.setCancelled(ShopMenu.TcheckShopMenuAction(player, event.getCurrentItem()));
		if(!current.getItemMeta().hasCustomModelData() || current.getItemMeta().getCustomModelData() == 122 || event.isCancelled()) return;
		
		logs.add("Player : UUID : " + player.getUniqueId() + " | Name :" + player.getName() + " Click whith : " + String.valueOf(current.getType()));
		try {
			if(Interface.GetActonIfInMainMenu(player, current, inv)) {
				event.setCancelled(true);
			} else {
				event.setCancelled(NetherStarMenu.HasMenu(player, current.getItemMeta().getCustomModelData(), current));
				if(player.getGameMode() == GameMode.CREATIVE) {
					player.kickPlayer(("§4§l/!\\ Nous avons detecter une duplication"));
					logs.add("Player : UUID : " + player.getUniqueId() + " | Name :" + player.getName() + " Risk of duplication (CHEAT ERROR) Target : Nether Start Menu");
				}

			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	@EventHandler
	public void OnPlayerPickupItemEvent(PlayerPickupItemEvent e) {
		Player player = e.getPlayer();
		Item block = e.getItem();
		if((player == null) || e.isCancelled() || (block.getItemStack().getItemMeta().hasCustomModelData() && block.getItemStack().getItemMeta().getCustomModelData() == 122)) return;
		ItemMeta m = block.getItemStack().getItemMeta();
		int i = 0;
		if(!player.isSneaking()) {
			PlayerJobs Pjobs = new PlayerJobs(player);
			ArrayList<String> lore = new ArrayList<String>();
			if(m.getLore() != null) lore.addAll(m.getLore());
			lore.remove("§dXp encore dans l'objet");
			lore.remove("§dreprenez l'objet pour récupérer l'XP.");
			m.setLore(lore);
			m.setCustomModelData(122);
			block.getItemStack().setItemMeta(m);
			ItemStack IS;
			while((i++)<36) if((IS = player.getInventory().getItem(i)) == block.getItemStack()) IS.setItemMeta(m);
			Pjobs.MaterialAddXp(block.getItemStack().getType(), block.getItemStack().getAmount());
		} else {
			ArrayList<String> lore = new ArrayList<String>();
			lore.remove("§dXp encore dans l'objet");
			lore.remove("§dreprenez l'objet pour récupérer l'XP.");
			
			lore.add("§dXp encore dans l'objet");
			lore.add("§dreprenez l'objet pour récupérer l'XP.");
			
			m.setLore(lore);
			block.getItemStack().setItemMeta(m);
			ItemStack IS;
			while((i++)<36) if((IS = player.getInventory().getItem(i)) == block.getItemStack()) IS.setItemMeta(m);
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§dL'expérience est restée dans l'objet §8(sneaking)"));

		}
		player.updateInventory();
	}
	
	
}