package fr.sisig48.pl.Sociale;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.bukkit.OfflinePlayer;

import fr.sisig48.pl.logs;

public class PlayerJobs {
	private int nl;
	private OfflinePlayer player;
	private Jobs jobs;
	private Jobs Tjobs;
	private int xp;
	private int Txp;
	private BigDecimal PayMent;
	public PlayerJobs(OfflinePlayer player) {
		this.player = player;
		loadP();
	}

	public void add(Jobs jobs) {
		remove();
		this.jobs = jobs;
		if(this.jobs != this.Tjobs) this.Tjobs = jobs;
		if(jobs.equals(Jobs.NOT)) xp = 1000;
		else xp = 0;
	}
	
	public void remove() {
		if(this.jobs != null) this.jobs = Jobs.NOT;
	}
	
	public Jobs get() {
		return jobs;
	}
	
	public int getXp() {
		return xp;
	}
	public void setXp(int xp){
		this.xp = xp;
	}
	public void addXp(int xp) {
		this.xp = this.xp + xp;
	}
	public void subXp(int xp) {
		this.xp = this.xp - xp;
	}
	public BigDecimal getPay() {
		return PayMent;
	}
	
	public void save() {
		close();
	}
	public boolean canChangeFor(Jobs jobsCible) {
		return jobs.isInRules(jobsCible);
	}
	
	public void close() {
		String jobs = "/" + String.valueOf(this.jobs.getJobs()) + "/" + String.valueOf(xp);
		String Tjobs = "/" + String.valueOf(this.Tjobs.getJobs()) + "/" + String.valueOf(Txp);
		if(this.jobs != this.Tjobs) dataJobs.line.add(nl, String.valueOf(player.getUniqueId()) + ":" + Tjobs);
		else dataJobs.line.add(nl, String.valueOf(player.getUniqueId()) + ":" + jobs);
		dataJobs.line.remove(nl + 1);
		logs.add("Jobs : save for " + player.getName());
		
	}
	
	public static void saveAll() {
		try {
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	private void loadP() {
		xp = 0;
		Txp = 0;
		if(dataJobs.line == null) reload();
		logs.add("Jobs : load profile " + player.getName());
		int con = 0;
		int i = 0;
		int re = 1;
		ArrayList<String> line = dataJobs.line;
		for(String e : line) {
			i++;
			String[] l = e.split("\\s*:\\/?\\s*");
			String[] f = null;
			
			if((l.length > 1 && !l[0].equalsIgnoreCase("?Jobs") && l[0].equals(String.valueOf(player.getUniqueId()))) && (f = l[1].split("\\s*\\/\\s*")).length >= 2) {
					jobs = Jobs.valueOf(f[0]);
					Tjobs = jobs;
					Txp = Integer.valueOf(f[1]);
					xp = Txp;
					PayMent = BigDecimal.valueOf(jobs.getPay());
					if(!jobs.isEnable()) {
						jobs = Jobs.NOT;
						xp = 0;
						PayMent = BigDecimal.valueOf(0);
					}
					con++;
					
				}
				
			if (l[0].equals(String.valueOf(player.getUniqueId()))) {
				if(l.length == 1) {
					Tjobs = Jobs.NOT;
					jobs = Jobs.NOT;
					xp = 0;
					Txp = 0;
					save();
				} else if (l[1].split("\\s*\\/\\s*").length == 1) {
					xp = 0;
					Txp = 0;
				}
				con++;
				re = i;
			}
}
		if(con == 0) {
			logs.add("Initialisation d'un compte \"Jobs\" pour \"UUID :" + player.getUniqueId() + " Name : " + player.getName() + "\"");
			dataJobs.line.add(String.valueOf(player.getUniqueId()) + ":");
			re = dataJobs.line.indexOf(String.valueOf(player.getUniqueId()) + ":");
			jobs = Jobs.NOT;
			Tjobs = Jobs.NOT;
		}
		this.nl = re - 1;
		save();
		return;
	}
	
	public static void load() {
		reload();
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
