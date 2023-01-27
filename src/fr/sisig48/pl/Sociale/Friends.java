package fr.sisig48.pl.Sociale;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
			((CommandSender) player).sendMessage("�aVous avez ajouter �4" + friends.getName() + "�a dans vos amis");
		}
		logs.add("Friends : " + friends.getName() + " Added for " + player.getName());
	}
	
	public void remove(OfflinePlayer friends) {
		 if(listFriends.contains(friends)) listFriends.remove(friends);

		 if(friends.isOnline()) ((CommandSender) friends).sendMessage("�e" + player.getName() + " �4vous a suprim� de ces amis");
		 
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
		data.line.add(nl, String.valueOf(player.getUniqueId()) + ":" + friend);
		data.line.remove(nl + 1);
		logs.add("Friends : save for " + player.getName());
		
	}
	
	public static void saveAll() {
		try {
			data.save();
			logs.add("Friends : Saving all player ");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void reload() {
			try {
				data.reload();
				logs.add("Friends : Reloading");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	private void loadP() {
		if(data.line == null) reload();
		logs.add("Friends : load profile " + player.getName());
		int con = 0;
		int i = 0;
		int re = 1;
		for(String e : data.line) {
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
			data.line.add(String.valueOf(player.getUniqueId()) + ":");
			re = data.line.indexOf(String.valueOf(player.getUniqueId()) + ":");
		}
		this.nl = re - 1;
		return;
	}
	
	public static void load() {
		reload();
	}
}

class data {
	
	static ArrayList<String> line = new ArrayList<String>();

	static void reload() throws IOException {
		
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
	    while(rs != 0 & i < rs & !line.contains("?friends")) {
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
	
	
	
	
	static void save() throws IOException {
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