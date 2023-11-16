package fr.sisig48.pl.Automating;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import fr.sisig48.pl.Main;
import fr.sisig48.pl.Economie.EconomieData;
import fr.sisig48.pl.Economie.EconomieInfo;

public class WebAuto {
	private static File in = new File(Main.Plug.getDataFolder() + "/index.html");
	private static File out = new File(Main.Plug.getDataFolder() + "/web/index.html");
	
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
