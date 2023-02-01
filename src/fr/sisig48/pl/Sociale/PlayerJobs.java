package fr.sisig48.pl.Sociale;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.bukkit.OfflinePlayer;

import fr.sisig48.pl.logs;

public class PlayerJobs {
	private int nl;
	private OfflinePlayer player;
	private Jobs jobs;
	private Jobs Tjobs;
	public PlayerJobs(OfflinePlayer player) {
		this.player = player;
		loadP();
	}

	public void add(Jobs jobs) {
		remove();
		this.jobs = jobs;
		if(this.jobs != this.Tjobs) this.Tjobs = jobs;
		
	}
	
	public void remove() {
		if(this.jobs != null) this.jobs = Jobs.NOT;
	}
	
	public Jobs get() {
		return jobs;
	}
	
	
	public void save() {
		close();
	}
	
	public void close() {
		String jobs = "/" + String.valueOf(this.jobs.getJobs());
		String Tjobs = "/" + String.valueOf(this.Tjobs.getJobs());
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
		if(dataJobs.line == null) reload();
		logs.add("Jobs : load profile " + player.getName());
		int con = 0;
		int i = 0;
		int re = 1;
		for(String e : dataJobs.line) {
			i++;
			
			String[] l = e.split("\\s*:\\/?\\s*");
				if(l.length > 1 & !l[0].equalsIgnoreCase("?Jobs") & l[0].equals(String.valueOf(player.getUniqueId()))) {
					for(String f : l[1].split("\\s*\\/\\s*")) {
						jobs = Jobs.valueOf(f);
						Tjobs = jobs;
						if(!jobs.isEnable()) {
							jobs = Jobs.NOT;
							player.getPlayer().sendMessage("§4Attention votre métier est vérouillé : §6Aucune action n'est possible pour votre jobs : " + Tjobs.getName());
						}
					}
					con++;
			}
			if (l[0].equals(String.valueOf(player.getUniqueId()))) {
				con++;
				re = i;
			}
		}
		if(con == 0) {
			logs.add("Initialisation d'un compte \"Jobs\" pour \"UUID :" + player.getUniqueId() + " Name : " + player.getName() + "\"");
			dataJobs.line.add(String.valueOf(player.getUniqueId()) + ":");
			re = dataJobs.line.indexOf(String.valueOf(player.getUniqueId()) + ":");
		}
		this.nl = re - 1;
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
	        // ajoute la ligne au buffer
	        sb.append(r);      
	        sb.append("\\;\\");     
	      }
	    MyFileR.close();
	    for(String li : sb.toString().split("\\\\;\\\\")) line.add(li);
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
