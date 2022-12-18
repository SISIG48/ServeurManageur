package fr.ServeurManageur.Updater;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;

public class ServeurManageurUpdate {

    public static Boolean DoUpdate() {

        try {
        	URL url = new URL("https://github.com/SISIG48/ServeurManageur/archive/refs/heads/main.zip");
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("plugins\\ServeurManageur.jar");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static Boolean CheckUpdate() {
    
	    if (!(Paths.get("plugins\\ServeurManageur.jar").toAbsolutePath().equals(Paths.get("https://github.com/SISIG48/ServeurManageur/archive/refs/heads/main.zip").toAbsolutePath()))) {
	       return true;
	    } else {
	        return false;
	    }
    }

}