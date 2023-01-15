package fr.ServeurManageur.Updater;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import fr.sisig48.pl.Utils.OnlinePlayer;


public class ServeurManageurUpdate {
	public static int NeedUpdate;
    public static Boolean DoUpdate(CommandSender sender) {

        try {
        	URL url = new URL("https://github.com/SISIG48/ServeurManageur/archive/refs/heads/main.zip");
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("plugins/ServeurManageur.zip");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            
            
            File zipfile = new File("plugins/ServeurManageur.zip");
            File folder = new File("plugins/");
            ZipInputStream zis = new ZipInputStream(
                    new BufferedInputStream(
                            new FileInputStream(zipfile.getCanonicalFile())));

            // extractions des entrées du fichiers zip (i.e. le contenu du zip)
            ZipEntry ze = null;
            try {
                while((ze = zis.getNextEntry()) != null){

                    // Pour chaque entrée, on crée un fichier
                    // dans le répertoire de sortie "folder"
                    File f = new File(folder.getCanonicalPath(), ze.getName());
               
                    // Si l'entrée est un répertoire,
                    // on le crée dans le répertoire de sortie
                    // et on passe à l'entrée suivante (continue)
                    if (ze.isDirectory()) {
                        f.mkdirs();
                        continue;
                    }
                   
                    // L'entrée est un fichier, on crée une OutputStream
                    // pour écrire le contenu du nouveau fichier
                    f.getParentFile().mkdirs();
                    OutputStream fot = new BufferedOutputStream(
                            new FileOutputStream(f));
               
                    // On écrit le contenu du nouveau fichier
                    // qu'on lit à partir de la ZipInputStream
                    // au moyen d'un buffer (byte[])
                    try {
                        try {
                            final byte[] buf = new byte[8192];
                            int bytesRead;
                            while (-1 != (bytesRead = zis.read(buf)))
                                fot.write(buf, 0, bytesRead);
                        }
                        finally {
                            fot.close();
                        }
                    }
                    catch (final IOException ioe) {
                        // en cas d'erreur on efface le fichier
                        f.delete();
                        throw ioe;
                    }
                }
            }
            finally {
                // fermeture de la ZipInputStream
                zis.close();
            }

            String sourceDir = "plugins/ServeurManageur-main/";
            String zipFile = "plugins/ServeurManageur-main/ServeurManageur-main.zip";
            fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            addDirToZipArchive(zos, sourceDir, null);
            zos.close();
            fos.close();
            File delta = new File("plugins/ServeurManageur-main");
            delta.delete();
            Bukkit.dispatchCommand(sender, "rl");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static void addDirToZipArchive(ZipOutputStream zos, String sourceDir, String baseDir) throws IOException {
        File srcFile = new File(sourceDir);
        String[] fileNames = srcFile.list();
        if (fileNames != null && fileNames.length > 0) {
            for (String fileName : fileNames) {
                File file = new File(sourceDir + File.separator + fileName);
                if (baseDir == null) {
                    baseDir = srcFile.getName();
                }
                if (file.isDirectory()) {
                    String dir = baseDir + File.separator + file.getName();
                    zos.putNextEntry(new ZipEntry(dir + File.separator));
                    addDirToZipArchive(zos, file.getAbsolutePath(), dir);
                } else {
                    zos.putNextEntry(new ZipEntry(baseDir + File.separator + file.getName()));
                    byte[] bytes = Files.readAllBytes(file.toPath());
                    zos.write(bytes, 0, bytes.length);
                    zos.closeEntry();
                }
            }
        }
    }
    public static Boolean CheckUpdate() {
    	try {
    		URL url = new URL("https://github.com/SISIG48/ServeurManageur/archive/refs/heads/main.zip");
            byte[] remoteFileBytes = Files.readAllBytes(Paths.get("plugins/ServeurManageur.zip"));
            byte[] localFileBytes = url.openStream().readAllBytes();
            if (Arrays.equals(remoteFileBytes, localFileBytes)) {
            	NeedUpdate = 0;
            	return false;
            }
    	} catch (Exception e) {
    	    e.printStackTrace();
    	}
	    NeedUpdate = 1;
    	return true;
    }
    
    public static void SendMaj() {
    	for (String e : OnlinePlayer.OnlinePlayer) {
    		if(NeedUpdate == 0 || e == null) return;
    		System.out.println(UUID.fromString(e));
    		if(Bukkit.getPlayer(UUID.fromString(e)) == null) break;
    		if(Bukkit.getPlayer(UUID.fromString(e)).isOnline() && Bukkit.getPlayer(UUID.fromString(e)).isOp()) {
    			Bukkit.getPlayer(UUID.fromString(e)).sendMessage("§4You need update plugin </re>");
    		}
    	}
    }

}

