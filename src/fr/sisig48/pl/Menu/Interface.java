package fr.sisig48.pl.Menu;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.sisig48.pl.logs;
import fr.sisig48.pl.State.Spawn;
import fr.sisig48.pl.Utils.Item;
import fr.sisig48.pl.Utils.Uconfig;

public class Interface {

	public static ArrayList<Inventory> inventory = new ArrayList<Inventory>();
	private static Player player;
	
	
	public static Boolean GetActonIfInMenuEco(ItemStack it, Player player) {
		if(it == null) {return false;}
		ItemStack i = Item.GiveOwnsPlayerHead( 1, "Personelle", "null", player.getName(), 124);
		if(it.equals(i)) {
			try {
				player.closeInventory();
				EconomieMenu.OpenMenuEcoPerso(player);
				
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;	
	}
	
	public static Boolean GetActonIfInMainMenu(Player players, ItemStack current, Inventory inv) {
		if(players == null) {return false;}
		player = players;
		if(current == null) {return false;}
		try {
			if(current.getItemMeta().getCustomModelData() == 125) return true;
		} catch (Exception e) {}
		if(inventory == null) {
			player.closeInventory();
			return false;  
		}
		if(JobsMenu.TcheckJobsMenuAction(players, current)) return true;
		if(!inventory.contains(inv)) return GetActonIfInMenuEco(current, player);
		switch(current.getType()) {
			
			case PAPER :
				
				try {
					CloseMenu();
					EconomieMenu.OpenMenuEco(player);
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				break ;
				
				
			case PLAYER_HEAD :
				switch(current.getItemMeta().getCustomModelData()) {
					case 124:
						try {
							EconomieMenu.OpenMenuEcoPerso(players);
						} catch (Exception e) {
							e.printStackTrace();
						}	
						break;
					case 125:
						break;
					case 128 : try {
						EconomieMenu.OpenMenuEcoFriends(players);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					default:
						break;
				}
				break;
			case GOLDEN_APPLE :
			try {
				EconomieMenu.OpenMenuEcoPublic(players);
			} catch (Exception e) {
				e.printStackTrace();
			}
				break;
			
			
			case BARRIER :
				try {
					EconomieMenu.OpenMenuEcoFriends(players);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				break;
			case ENDER_CHEST :
			
			try {
				switch(current.getItemMeta().getCustomModelData()) {
					case 123 : EconomieMenu.OpenMenuEcoPublicMoney(players);
					break;
					case 128 : EconomieMenu.OpenMenuEcoFriendsMoney(players);
					break;
						
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				player.sendMessage("§4Error, contact the §adev-staff");
				e.printStackTrace();
			}
				break;
			
			case ITEM_FRAME:
				if(current.getItemMeta().getCustomModelData() != 127) break;
				MenuTP.OpenMenuTP(players);
				logs.add("Player : UUID : " + player.getUniqueId() + " | Name :" + player.getName() + " Enter to MenuTP");
				break;
			
				
			case ENDER_PEARL:
				if(current.getItemMeta().getCustomModelData() != 127) break;
				try {
					logs.add("Player : UUID : " + player.getUniqueId() + " | Name :" + player.getName() + " Teleported to spawn");
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
				break;
				
			
			case DEEPSLATE_GOLD_ORE:
				if(current.getItemMeta().getCustomModelData() != 127) break;
			try {
				player.teleport(Spawn.GetMineInSpawnLocation());
				player.sendMessage("§aVous avez été tp a la §4mine");
				logs.add("Player : UUID : " + player.getUniqueId() + " | Name :" + player.getName() + " Teleported to mine");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
				break;
				
			default:
				JobsMenu.TcheckJobsMenuAction(players, current);
				return true;
				
		}
		JobsMenu.TcheckJobsMenuAction(players, current);
		return true;
		
		
		
	}
	
	public static void CloseMenu() {
		player.closeInventory();
		
	}
}