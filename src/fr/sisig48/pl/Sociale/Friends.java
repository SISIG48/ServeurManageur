package fr.sisig48.pl.Sociale;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.sisig48.pl.logs;

public class Friends {
	public static ArrayList<String> FriendsResquet = new ArrayList<String>(); 
	private int nl;
	private OfflinePlayer player;
	private ArrayList<OfflinePlayer> listFriends = new ArrayList<OfflinePlayer>();
	public Friends(OfflinePlayer player) {
		this.player = player;
		loadP();
	}

	public void add(Player friends) {
		if(!listFriends.contains(friends)) {
			listFriends.add(friends);
			((CommandSender) player).sendMessage("§aVous avez ajouter §4" + friends.getName() + "§a dans vos amis");
		}
		logs.add("Friends : " + friends.getName() + " Added for " + player.getName());
	}
	
	public void remove(OfflinePlayer friends) {
		 if(listFriends.contains(friends)) listFriends.remove(friends);

		 if(friends.isOnline()) ((CommandSender) friends).sendMessage("§e" + player.getName() + " §4vous a suprimé de ces amis");
		 
		 logs.add("Friends : " + friends.getName() + " Removed for " + player.getName());
		 return;
	}
	
	public ArrayList<OfflinePlayer> get() {
		return listFriends;
	}
	
	
	public void save() {
		close();
	}
	
	public void close() {
		String friend = "";
		for (OfflinePlayer u : listFriends) {
			friend = friend + "/" + String.valueOf(u.getUniqueId());
			
		}
		Friendsdata.line.add(nl, String.valueOf(player.getUniqueId()) + ":" + friend);
		Friendsdata.line.remove(nl + 1);
		logs.add("Friends : save for " + player.getName());
		
	}
	
	public static void saveAll() {
		try {
			Friendsdata.save();
			logs.add("Friends : Saving all player ");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void reload() {
			try {
				Friendsdata.reload();
				logs.add("Friends : Reloading");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	private void loadP() {
		if(Friendsdata.line == null) reload();
		logs.add("Friends : load profile " + player.getName());
		int con = 0;
		int i = 0;
		int re = 1;
		for(String e : Friendsdata.line) {
			i++;
			String[] l = e.split("\\s*:\\/?\\s*");
				if(l.length > 1 & !l[0].equalsIgnoreCase("?Friends") & l[0].equals(String.valueOf(player.getUniqueId()))) {
				
					for(String f : l[1].split("\\s*\\/\\s*")) {
					listFriends.add(Bukkit.getOfflinePlayer(UUID.fromString(f)));
						
					}
				
				
				con++;
			}
			if (l[0].equals(String.valueOf(player.getUniqueId()))) {
				con++;
				re = i;
			}
		}
		if(con == 0) {
			logs.add("Initialisation d'un compte \"friend\" pour \"UUID :" + player.getUniqueId() + " Name : " + player.getName() + "\"");
			Friendsdata.line.add(String.valueOf(player.getUniqueId()) + ":");
			re = Friendsdata.line.indexOf(String.valueOf(player.getUniqueId()) + ":");
		}
		this.nl = re - 1;
		return;
	}
	
	public static void load() {
		reload();
	}
}

class Friendsdata {
	
	static ArrayList<String> line = new ArrayList<String>();

	static void reload() throws IOException {
		File file = new File("plugins/ServeurManageur/data/friends.txt");
		if(!file.exists()) file.createNewFile();
		
	    FileReader MyFileR = new FileReader("plugins/ServeurManageur/data/friends.txt");
	    BufferedReader br = new BufferedReader(MyFileR);
	    StringBuffer sb = new StringBuffer();    
	    String r;
	    line = new ArrayList<String>();
	    while((r = br.readLine()) != null) {
	        // ajoute la ligne au buffer
	        sb.append(r);      
	        sb.append("\\;\\");     
	      }
	    MyFileR.close();
	    for(String li : sb.toString().split("\\\\;\\\\")) line.add(li);
	    int i = 0;
	    int rs = line.size() -1;
	    while(rs != 0 & i < rs & !line.contains("?Friends")) {
	    	rs = line.size() -1;
	    	if(line.get(i).contains("?Friends")) {
	    		line.remove(i);
	    	} else {
	    		i++;
	    	}

	    }
	    if(!line.get(0).equalsIgnoreCase("?Friends")) line.add(0, "?Friends");
	    save();
	}
	
	
	
	//test GITHUB
	static void save() throws IOException {
		File file = new File("plugins/ServeurManageur/data/friends.txt");
		if(!file.exists()) file.createNewFile();
		FileWriter MyFileW = new FileWriter("plugins/ServeurManageur/data/friends.txt");
	    BufferedWriter bufWriter = new BufferedWriter(MyFileW);
	    for(String e : line) {    
	    	bufWriter.write(e);
	    	bufWriter.newLine();
	    }
        bufWriter.close();
        MyFileW.close();
	}
}
