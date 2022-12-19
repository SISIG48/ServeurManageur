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
            
            
            File zipfile = new File("plugins\\ServeurManageur.zip");
            File folder = new File("plugins\\");
            ZipInputStream zis = new ZipInputStream(
                    new BufferedInputStream(
                            new FileInputStream(zipfile.getCanonicalFile())));

            // extractions des entr�es du fichiers zip (i.e. le contenu du zip)
            ZipEntry ze = null;
            try {
                while((ze = zis.getNextEntry()) != null){

                    // Pour chaque entr�e, on cr�e un fichier
                    // dans le r�pertoire de sortie "folder"
                    File f = new File(folder.getCanonicalPath(), ze.getName());
               
                    // Si l'entr�e est un r�pertoire,
                    // on le cr�e dans le r�pertoire de sortie
                    // et on passe � l'entr�e suivante (continue)
                    if (ze.isDirectory()) {
                        f.mkdirs();
                        continue;
                    }
                   
                    // L'entr�e est un fichier, on cr�e une OutputStream
                    // pour �crire le contenu du nouveau fichier
                    f.getParentFile().mkdirs();
                    OutputStream fot = new BufferedOutputStream(
                            new FileOutputStream(f));
               
                    // On �crit le contenu du nouveau fichier
                    // qu'on lit � partir de la ZipInputStream
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

            String sourceDir = "plugins\\ServeurManageur-main\\";
            String zipFile = "plugins\\ServeurManageur-main\\ServeurManageur-main.zip";
            fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            addDirToZipArchive(zos, sourceDir, null);
            zos.close();
            fos.close();
            File delta = new File("plugins\\ServeurManageur-main");
            delta.delete();
            
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
            byte[] remoteFileBytes = Files.readAllBytes(Paths.get("plugins\\ServeurManageur.zip"));
            byte[] localFileBytes = url.openStream().readAllBytes();
            if (Arrays.equals(remoteFileBytes, localFileBytes)) {
            	return false;
            }
    	} catch (Exception e) {
    	    e.printStackTrace();
    	}
	    return true;
    }

}

