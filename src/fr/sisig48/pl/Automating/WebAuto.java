package fr.sisig48.pl.Automating;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.UserDoesNotExistException;

import fr.sisig48.pl.Main;
import fr.sisig48.pl.Economie.EconomieData;
import fr.sisig48.pl.Economie.EconomieInfo;
import fr.sisig48.pl.Sociale.PlayerJobs;
import fr.sisig48.pl.Utils.Utils;
import net.sisig48.web.WebResponses;
import net.sisig48.web.WebResponsesData;
import net.sisig48.web.WebResponsesListener;
import net.sisig48.web.WebView;
import nl.svenar.lib.json.simple.JSONObject;

public class WebAuto {
	private static File in = new File(Main.Plug.getDataFolder() + "/index.html");
	private static File out = new File(Main.Plug.getDataFolder() + "/web/index.html");
	
	static {
		WebResponses.AddWebResponsesListener(new WebResponsesListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public boolean onWebResponse(WebResponsesData data) {
				if(data.get().length <= 0) return false;
				
				if(data.getType().equals("GET")) {
					String request = data.getRequest();
					if(request.equals("/account.data")) {
						if(data.getAccount() == null) WebView.PageUnauthorized(data.getOutputStream()); 
						else {
							OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(data.getAccount().getTag()));
							JSONObject json = new JSONObject();
							
							JSONObject minecraft = new JSONObject();
							minecraft.put("name", player.getName()); 	
							minecraft.put("uuid", player.getUniqueId().toString());
							minecraft.put("icon", Utils.getBase64Profile(player.getUniqueId()));
							minecraft.put("first_player", player.getFirstPlayed());
							
							// Information plugin
							JSONObject info = new JSONObject();
							try {info.put("money", Economy.getMoneyExact(player.getUniqueId()));} catch (UserDoesNotExistException e) {}
							info.put("jobs", new PlayerJobs(player).get().getName());
							info.put("xp", new PlayerJobs(player).getXp());
							
							// Information globale du joueur
							JSONObject playerINFO = new JSONObject();
							playerINFO.put("minecraft", minecraft);
							playerINFO.put("external", info);
							
							// Information sur le compte
							JSONObject accountINFO = new JSONObject();
							accountINFO.put("id", data.getAccount().getId());
							accountINFO.put("tag", data.getAccount().getTag());
							accountINFO.put("last-ip", data.getAccount().getIp().getHostAddress());
							
							json.put("account", accountINFO);
							json.put("player", playerINFO);
							
							WebView.returnText(json.toJSONString(), data.getOutputStream());
						}
						return true;
					}
				}
				return false;
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	public static void SaveChange() {
		if(!in.exists()) {
			in.mkdir();
			return;
		}
		
	    try {
	    	FileReader MyFileR = new FileReader(in);
	    	BufferedReader br = new BufferedReader(MyFileR);
	    	String r;
	    	ArrayList<String> line = new ArrayList<String>();
			while((r = br.readLine()) != null) {
				if(r.contains("!DATA HTML BUTTON")) {
					for(EconomieData ed : EconomieData.getSaves()) line.add("<button id=\""+ ed.getType().name().toLowerCase() +"-button\">" + ed.getType().name().toLowerCase().replace("_"," ") + "</button>");
				} else if(r.contains("!DATA BASE")) {
			    	for(EconomieData ed : EconomieData.getSaves()) {
			    		line.add("        " + ed.getType().name().toLowerCase() + ": [");
			    		for(EconomieInfo ei : ed.getInfo()) {
			    			Date date = ei.getFrom(); 
			    			line.add("          { price: " + ei.getPrice() + ", date: '" + date.getHours() + ":" + date.getMinutes() + " " + date.getDate() + "/" + (date.getMonth()+1) + "/" + (date.getYear() + 1900) + "' },");
			    		}
			    		line.add("        ],");
			    	}
			    	
			    } else if(r.contains("!DATA BUTTON")) {
			    	for(EconomieData ed : EconomieData.getSaves()) {
			    		line.add("      document.getElementById('" + ed.getType().name().toLowerCase() + "-button').addEventListener('click', function() {");
			    		line.add("        searchInput.value = '" + ed.getType().name().toLowerCase().replace("_"," ") + "';");
			    		line.add("        search();");
			    		line.add("      });");
			    	}
			    } else line.add(r);
			    
			  }
			MyFileR.close();
			
			if(!out.exists()) out.createNewFile();
			FileWriter MyFileW = new FileWriter(out);
		    BufferedWriter bufWriter = new BufferedWriter(MyFileW);
		    for(String e : line) {    
		    	bufWriter.write(e);
		    	bufWriter.newLine();
		    }
	        bufWriter.close();
	        MyFileW.close();
		
	    } catch (IOException e) {
			e.printStackTrace();
		}
	    
	}
}
