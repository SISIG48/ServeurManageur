package fr.sisig48.pl;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
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
import fr.sisig48.pl.Automating.PayPal;
import fr.sisig48.pl.Menu.Interface;
import fr.sisig48.pl.Menu.JobsMenu;
import fr.sisig48.pl.Menu.ShopMenu;
import fr.sisig48.pl.NetherStar.NetherStarMenu;
import fr.sisig48.pl.Sociale.Friends;
import fr.sisig48.pl.Sociale.PlayerJobs;
import fr.sisig48.pl.State.JobsPNJ;
import fr.sisig48.pl.State.ShopPNJ;
import fr.sisig48.pl.State.Spawn;
import net.ess3.api.MaxMoneyException;

@SuppressWarnings("deprecation")
public class Listner implements Listener {
	
	Main main;
	public Listner(Main main) {
		this.main = main;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		fr.sisig48.pl.users.Interface.launch();
		Player player = event.getPlayer();
		player.sendMessage("§aBienvenue sur le serveur");
		player.sendMessage("§eSi tu as un problème, contacte le staff sur le §4discord §eou");
		player.sendMessage("§eexécute la commande §4/bug [arg...]");
		if (player.isOp()) {
		    player.sendMessage("");
		    player.sendMessage("§4Vous êtes administrateur sur ce serveur :");
		    player.sendMessage("  §e- /help ServeurManager pour obtenir la liste des commandes disponibles");
		}
		
		try {
			logs.PlayerConect(player);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ServeurManageurUpdate.SendMaj();
		NetherStarMenu.GiveMenu(player);
		Friends f = new Friends(player);
		int i = 0;
		
		for(OfflinePlayer p : f.get()) {
			if(p.isOnline()) {
				i++;
				p.getPlayer().sendMessage("§e"+ player.getName() + "§a s'est connecter");
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
			player.sendMessage("§4Vous êtes mort et §4§lavez perdu §2" + Economy.getMoney(player.getName()));
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
		if(event.getPlayer() == null || event.getItem() == null || event.getItem().getItemMeta() == null) return;
		
		ItemStack is = event.getItem();
		Player player = event.getPlayer();
		Boolean isVania = !event.getItem().getItemMeta().hasCustomModelData();

		if(!isVania) event.setCancelled(NetherStarMenu.HasMenu(player, is.getItemMeta().getCustomModelData(), is));
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
		//Not null
		if(event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) return;
		
		//Init variable
		Inventory inv = event.getInventory();
		Player player = (Player) event.getWhoClicked();
		ItemStack current = event.getCurrentItem();
		Boolean isVania = !current.getItemMeta().hasCustomModelData();
		
		if(!isVania) {
			int mod = current.getItemMeta().getCustomModelData();
			try {
				if(Interface.GetActonIfInMainMenu(player, current, inv)) event.setCancelled(true);
				else event.setCancelled(NetherStarMenu.HasMenu(player, mod, current));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(!event.isCancelled()) event.setCancelled(ShopMenu.TcheckShopMenuAction(player, event.getCurrentItem()));
	
	}
	
	/*@EventHandler
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
	}*/
	
	
}