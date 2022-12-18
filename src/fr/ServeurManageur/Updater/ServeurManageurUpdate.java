package fr.ServeurManageur.Updater;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ServeurManageurUpdate {

    public static Boolean DoUpdate() {

        try {
        	URL url = new URL("https://github.com/SISIG48/ServeurManageur/archive/refs/heads/main.zip");
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("plugins\\ServeurManageur.jar");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            File file = new File("plugins\\ServeurManageur.jar");
            FileInputStream fis = new FileInputStream(file);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
               if (zipEntry.getName().matches("ServeurManageur-main.*")) {
                  File destFile = new File("plugins\\ServeurManageur.jar");
                  fos = new FileOutputStream(destFile);
                  byte[] bytes = new byte[1024];
                  int length;

                  while ((length = zis.read(bytes)) >= 0) {
                     fos.write(bytes, 0, length);
                  }

                  fos.close();
               }
            }

            zis.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static Boolean CheckUpdate() {
    	try {
        	URL url = new URL("https://github.com/SISIG48/ServeurManageur/archive/refs/heads/main.zip");
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("ServeurManageur.jar"); 
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    	File delta = new File("ServeurManageur.jar");
    	if (!(Paths.get("plugins\\ServeurManageur.jar").toAbsolutePath().equals(Paths.get("ServeurManageur.jar").toAbsolutePath()))) {
    		delta.delete();
	    	return true;
	    } else {
	    	delta.delete();
	    	return false;
	    }
    }

}