package fr.sisig48.pl.Sociale;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import fr.sisig48.pl.logs;
import fr.sisig48.pl.JobsHouse.HouseData;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerJobs {
	private static ArrayList<PlayerJobs> InstanceList = new ArrayList<PlayerJobs>();
	private static ArrayList<OfflinePlayer> PlayerInstanceList = new ArrayList<OfflinePlayer>();
	private int nl;
	private Location locHouse;
	private OfflinePlayer player;
	private Jobs jobs;
	private Jobs Tjobs;
	private float xp;
	private float Txp;
	private BigDecimal PayMent;
	private PlayerJobs pj;
	public PlayerJobs(OfflinePlayer player) {
		if(PlayerInstanceList.contains(player)) {
			pj = InstanceList.get(PlayerInstanceList.indexOf(player));
			if(!player.isOnline()) save();
			return;
		}
		pj=this;
		pj.player = player;
		PlayerInstanceList.add(player);
		InstanceList.add(this);
		loadP();
	}

	public void add(Jobs jobs) {
		remove();
		delHouse();
		pj.jobs = jobs;
		pj.xp = (float) Math.round(pj.xp*1000)/10000;
		if(pj.jobs != pj.Tjobs) pj.Tjobs = jobs;
		if(jobs.equals(Jobs.NOT)) pj.xp = 1000;
		else xp = 0;
	}
	
	public void remove() {
		if(pj.jobs == Jobs.NOT) pj.xp = 0;
		pj.jobs = Jobs.NOT;
	}
	
	public Jobs get() {
		return pj.jobs;
	}
	
	public Float getXp() {
		pj.xp = (float) Math.round(pj.xp*1000)/1000;
		if(pj.xp > 10000) pj.xp = 10000;
		return pj.xp;
	}
	public void setXp(float xp){
		pj.xp = xp;
	}
	public void addXp(float xp) {
		pj.xp = pj.xp + xp;
	}
	public void subXp(float xp) {
		pj.xp = pj.xp - xp;
	}
	public BigDecimal getPay() {
		return pj.PayMent;
	}
	
	public void save() {
		close();
	}
	public boolean canChangeFor(Jobs jobsCible) {
		return pj.jobs.isInRules(jobsCible);
	}
	
	public void close() {
		String jobs = "/" + String.valueOf(pj.jobs.getJobs()) + "/" + String.valueOf(pj.xp);
		String Tjobs = "/" + String.valueOf(pj.Tjobs.getJobs()) + "/" + String.valueOf(pj.Txp);
		if(pj.jobs != pj.Tjobs) dataJobs.line.add(pj.nl, String.valueOf(pj.player.getUniqueId()) + ":" + Tjobs);
		else dataJobs.line.add(pj.nl, String.valueOf(pj.player.getUniqueId()) + ":" + jobs);
		dataJobs.line.remove(pj.nl + 1);
		logs.add("Jobs : save for " + pj.player.getName());
		
	}
	
	public static void saveAll() {
		try {
			for(PlayerJobs t : InstanceList) t.close();
			dataJobs.save();
			logs.add("Jobs : Saving all player ");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void reload() {
			try {
				dataJobs.reload();
				logs.add("Jobs : Reloading");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public void setHouse(Location loc) {
		pj.locHouse = loc;
	}

	public void addHouse(Location loc) {
		pj.locHouse = loc;
		HouseData.addHouse(pj.player.getPlayer(), loc);
	}
	
	public void delHouse() {
		if(pj.locHouse == null) return;
		HouseData.delHouse(pj.locHouse, pj.player.getPlayer());
		pj.locHouse = null;
	}
	
	public Location getHouse() {
		return pj.locHouse;
	}
	
	private void loadP() {
		pj.xp = 0;
		pj.Txp = 0;
		if(dataJobs.line == null) reload();
		logs.add("Jobs : load profile " + pj.player.getName());
		int con = 0;
		int i = 0;
		int re = 1;
		ArrayList<String> line = dataJobs.line;
		for(String e : line) {
			i++;
			String[] l = e.split("\\s*:\\/?\\s*");
			String[] f = null;
			
			if((l.length > 1 && !l[0].equalsIgnoreCase("?Jobs") && l[0].equals(String.valueOf(pj.player.getUniqueId()))) && (f = l[1].split("\\s*\\/\\s*")).length >= 2 && f[0] != null) {
					pj.jobs = Jobs.valueOf(f[0]);
					pj.Tjobs = pj.jobs;
					pj.Txp = Float.valueOf(f[1]);
					pj.xp = pj.Txp;
					pj.PayMent = BigDecimal.valueOf(jobs.getPay());
					if(!pj.jobs.isEnable()) {
						pj.jobs = Jobs.NOT;
						pj.xp = 0;
						pj.PayMent = BigDecimal.valueOf(0);
					}
					con++;
					
				}
				
			if (l[0].equals(String.valueOf(pj.player.getUniqueId()))) {
				if(l.length == 1) {
					pj.Tjobs = Jobs.NOT;
					pj.jobs = Jobs.NOT;
					pj.xp = 0;
					pj.Txp = 0;
					save();
				} else if (l[1].split("\\s*\\/\\s*").length == 1) {
					pj.xp = 0;
					pj.Txp = 0;
				}
				con++;
				re = i;
			}
}
		if(con == 0) {
			logs.add("Initialisation d'un compte \"Jobs\" pour \"UUID :" + pj.player.getUniqueId() + " Name : " + pj.player.getName() + "\"");
			dataJobs.line.add(String.valueOf(pj.player.getUniqueId()) + ":");
			re = dataJobs.line.indexOf(String.valueOf(pj.player.getUniqueId()) + ":");
			pj.jobs = Jobs.NOT;
			pj.Tjobs = Jobs.NOT;
		}
		pj.nl = re - 1;
		if(pj.jobs == Jobs.NOT) pj.xp = 10000;
		save();
		return;
	}
	
	public static void load() {
		reload();
	}
	
	public void MaterialAddXp(Material m, int count) {
		Player p = (Player) pj.player;
		if(pj.xp >= 10000) {pj.xp = 10000;return;}
		if(pj.jobs.getXpGain(m) == 0) return;
		Float Gxp = pj.jobs.getXpGain(m);
		int i = 1;
		
		
		//
		Float initial = pj.xp;
		pj.xp = (float) ((2.5/(0.75 + (pj.xp/1000))) * Gxp) + pj.xp;
		while((i++) != count) pj.xp = (float) ((2.5/(0.75 + (pj.xp/1000))) * Gxp) + pj.xp;
		
		
		
		String round = String.valueOf(Math.round((pj.xp-initial)*1000));
		round = String.valueOf(Float.valueOf(round)/1000);
		if(round.equals("0.0")) return;
		String mss = "§aVous avez gagné §4" + round + "§axp";
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(mss));
	}
	
	public void MaterialSubXp(Material m, int count) {
		Player p = (Player) pj.player;
		if(pj.xp > 0) {pj.xp = 0;return;}
		if(pj.jobs.getXpGain(m) == 0) return;
		Float Gxp = pj.jobs.getXpGain(m);
		int i = 1;
		
		
		//
		Float initial = pj.xp;
		pj.xp = (float) ((2.5/(0.75 + (pj.xp/1000))) * Gxp) - pj.xp;
		while((i++) != count) pj.xp = (float) ((2.5/(0.75 + (pj.xp/1000))) * Gxp) - pj.xp;
		
		//
		String round = String.valueOf(Math.round((initial - pj.xp)*1000));
		round = String.valueOf(Float.valueOf(round)/1000);
		if(round.equals("0.0")) return;
		String mss = "§dVous avez perdu §4" + round + "§axp";
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(mss));
	}
	
	public void delVarHouse() {
		pj.locHouse = null;
	}
}





class dataJobs {
	
	static ArrayList<String> line = new ArrayList<String>();

	static void reload() throws IOException {
		File file = new File("plugins/ServeurManageur/data/jobs.txt");
		if(!file.exists()) file.createNewFile();
		
	    FileReader MyFileR = new FileReader("plugins/ServeurManageur/data/jobs.txt");
	    BufferedReader br = new BufferedReader(MyFileR);
	    StringBuffer sb = new StringBuffer();    
	    String r;
	    line = new ArrayList<String>();
	    while((r = br.readLine()) != null) {
	        sb.append(r);      
	        sb.append("\\;\\");
	    }
	    MyFileR.close();
	    for(String li : sb.toString().split("\\\\;\\\\")) if(!line.contains(li)) line.add(li);
	    
	    int i = 0;
	    int rs = line.size() -1;
	    while(rs != 0 & i < rs & !line.contains("?Jobs")) {
	    	rs = line.size() -1;
	    	if(line.get(i).contains("?Jobs")) {
	    		line.remove(i);
	    	} else {
	    		i++;
	    	}

	    }
	    if(!line.get(0).equalsIgnoreCase("?Jobs")) line.add(0, "?Jobs");
	    save();
	}
	
	
	
	//test GITHUB
	static void save() throws IOException {
		File file = new File("plugins/ServeurManageur/data/jobs.txt");
		if(!file.exists()) file.createNewFile();
		FileWriter MyFileW = new FileWriter("plugins/ServeurManageur/data/jobs.txt");
	    BufferedWriter bufWriter = new BufferedWriter(MyFileW);

	    for(String e : line) {    
	    	bufWriter.write(e);
		    bufWriter.newLine();
	    }
        bufWriter.close();
        MyFileW.close();
	}
}
