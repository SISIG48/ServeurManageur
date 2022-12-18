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
import java.util.zip.ZipOutputStream;


public class ServeurManageurUpdate {

    public static Boolean DoUpdate() {

        try {
        	URL url = new URL("https://github.com/SISIG48/ServeurManageur/archive/refs/heads/main.zip");
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("plugins\\ServeurManageur.zip");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            FileInputStream fin = new FileInputStream("plugins\\ServeurManageur.zip");
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;

            while ((ze = zin.getNextEntry()) != null) {
                if(ze.isDirectory()) {
                    File f = new File(ze.getName());
                    f.mkdirs();
                } else {
                    FileOutputStream fout = new FileOutputStream(ze.getName());
                    for (int c = zin.read(); c != -1; c = zin.read()) {
                        fout.write(c);
                    }
                    zin.closeEntry();
                    fout.close();
                }
            }
            zin.close();

            // Compression

            String source = "plugins\\ServeurManageur\\ServeurManageur-main";
            String destination = "ServeurManageur.jar";

            FileOutputStream fout = new FileOutputStream(destination);
            ZipOutputStream zout = new ZipOutputStream(fout);

            File f = new File(source);
            File[] files = f.listFiles();

            for(int i=0; i < files.length; i++) {
                FileInputStream fin2 = new FileInputStream(files[i]);
                zout.putNextEntry(new ZipEntry(files[i].getName()));
                int length;
                while((length = fin2.read()) > 0) {
                    zout.write(length);
                }
                zout.closeEntry();
                fin2.close();
            }
            zout.close();
            File delta = new File("ServeurManageur-main");
            delta.delete();
            delta = new File("plugins\\ServeurManageur.zip");
            delta.delete();
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

