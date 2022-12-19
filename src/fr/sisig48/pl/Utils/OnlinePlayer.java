package fr.sisig48.pl.Utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class OnlinePlayer {
	public static ArrayList <String>OnlinePlayer = new ArrayList<String>();
	public static void ReadUsercache() throws IOException {
		try {
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader("usercache.json"));
			String strCurrentLine;
			int po;
			int ps;
			int pe;
			while ((strCurrentLine = reader.readLine()) != null) {
				po = strCurrentLine.indexOf("uuid");
				
				ps = po + 7;
				pe = po + 43;
				if(strCurrentLine.substring(ps, pe) != null) OnlinePlayer.add(strCurrentLine.substring(ps, pe));

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
