package net.sisig48.web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;

import fr.sisig48.pl.logs;
import fr.sisig48.pl.Utils.Uconfig;

public class WebServer {
	private static String path = "/web";
	private static List<String> access;
	private static List<WebServer> serverList = new ArrayList<WebServer>(); 
	private ServerSocket serverSocket = null;
	private int port;
	public static void setPath(String path) {
		WebServer.path = path;
	}
	
	public WebServer(int port) {
		this.port = port;
		serverList.add(this);
	}
	
	private static List<String> getFiles() {
		List<String> l = new ArrayList<String>();
		File file = new File(path + "/access.txt");
		l.add("!/access.txt");
		l.add("!/keystore.jks");
		
		try {
			if(!file.exists()) {
				file.getParentFile().mkdir();
				file.createNewFile();
				FileWriter MyFileW = new FileWriter(file);
			    BufferedWriter bufWriter = new BufferedWriter(MyFileW);
			    
			    List<String> line = new ArrayList<String>();
			    line.add("//Denied file access, write \"!/path/file.ext\"");
			    line.add("//To create a file shortcut, write \"/file/shortcut to /path/file.ext\" ; add \"$\" before file path for set absolute path of the file");
			    line.add("//Use \"//\" to comment out this file");
			    
			    for(String s : line) {
			    	bufWriter.write(s);
			    	bufWriter.newLine();
			    }
			    
		        bufWriter.close();
		        MyFileW.close();
			}
			
			FileReader MyFileR;
			MyFileR = new FileReader(file);
			BufferedReader br = new BufferedReader(MyFileR);
			String r;
			@SuppressWarnings("unused")
			ArrayList<String> line = new ArrayList<String>();
			while((r = br.readLine()) != null) if(r.startsWith("//")) continue; else {
				l.add(r);
				if(!r.startsWith("!") && r.contains(" to ")) {
					l.add("!!" + r.split("\\s*to\\s*")[0]);
					l.add("!" + r.split("\\s*to\\s*")[1]);
				}
			}
			MyFileR.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return l;
	}

	public void stop() {
		try {
			if(serverSocket != null) serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				start1();
			}
		}, "Thread web :" + port).start();
	}
	
	public void start0() {
		start1();
	}
	
	public void start1() {
		access = getFiles();
		try {
			File key = new File(path + "/keystore.jks");
			
			if(!key.exists() || Uconfig.getConfig("web.password") == null || Uconfig.getConfig("web.password").equals("none")) {
				Uconfig.setConfig("web.password", "none");
				return;
			}
			
			
			SSLContext sslContext = SSLContext.getInstance("TLS");
            char[] password = Uconfig.getConfig("web.password").toCharArray();
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(path + "/keystore.jks"), password);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, password);

            sslContext.init(kmf.getKeyManagers(), null, null);
            SSLServerSocketFactory ssf = sslContext.getServerSocketFactory();
            
			try{
            	if(serverSocket == null || !serverSocket.isBound()) serverSocket = ssf.createServerSocket(port);
            } catch (BindException e) {
            	System.out.println("start failed on :" + port);
            	return;
            }
            
            System.out.println("start server on :" + port);
            while (true) {
                try {
                	Socket clientSocket = serverSocket.accept();
                	
                	Thread thread = new Thread(() -> {
                		try (
                				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                				OutputStream out = clientSocket.getOutputStream()
                				) {
                			String request = in.readLine();
                			@SuppressWarnings("unused")
                			File file;
                			if (request != null) {
                				if(request.contains("?")) request = (String) request.subSequence(0, request.lastIndexOf("?")) ;
                				if(request.startsWith("GET /")) {
                					request = request.split(" ")[1];
                					File Outfile = new File(path + request);
                					if(access.contains("!" + request) || !Outfile.getPath().replace("\\", "/").startsWith(path)) {
                						WebView.PageNotFound(out);
                						return;
                					}
                					
                					if(access.contains("!!" + request)) {
                						int index = access.indexOf("!!" + request);
                						if(access.get(index-1).split("\\s*to\\s*")[1].startsWith("$")) Outfile = new File(access.get(index-1).split(" to ")[1].replaceAll("\\$\\/?", ""));
                						else Outfile = new File(path + access.get(index-1).split("\\s*to\\s*")[1]);
                					}
                					
                					if(Outfile.exists() && Outfile.isFile()) {
                						for(WebType wt : WebType.values()) if(Outfile.getPath().endsWith(wt.getExt())) { 
                							WebView.returnData(Outfile, out, wt.getInfo());
                							return;
                						}
                						WebView.returnData(Outfile, out, WebType.text.getInfo());
                						return;
                					}
                					
                					
                					WebView.PageNotFound(out);
                					return;
                				}
                			} else {
                				out.write("HTTP/1.1 400 Bad Request\r\n".getBytes());
                				out.write("Content-Type: text/plain\r\n".getBytes());
                				out.write("\r\n".getBytes());
                				out.write("Requête invalide".getBytes());
                			}
                		} catch (IOException e) {
                		}
                		
                		try {
                			clientSocket.close();
                		} catch (IOException e) {
                			e.printStackTrace();
                		}
                	});
                	
                	thread.start();
                } catch (SocketException e) {return;}
            }
        } catch (IOException | NoSuchAlgorithmException | CertificateException | KeyManagementException | KeyStoreException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
	}
	
	public static void stopAll() {
		for(WebServer w : serverList) if(w != null) w.stop();
	}
	
}
