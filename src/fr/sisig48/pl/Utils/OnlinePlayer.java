package fr.sisig48.pl.Utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnlinePlayer {
	public static ArrayList <String>OnlinePlayer = new ArrayList<String>();
	public static void ReadUsercache() throws IOException {
		try {
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader("ops.json"));
			String strCurrentLine;
			while ((strCurrentLine = reader.readLine()) != null) {
				Pattern p = Pattern.compile("\\w{8}-(\\w{4}-){3}\\w{12}");
				Matcher m = p.matcher(strCurrentLine);
				while (m.find()) {
					OnlinePlayer.add(m.group());
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
