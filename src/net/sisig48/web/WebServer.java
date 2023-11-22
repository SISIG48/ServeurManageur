package net.sisig48.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
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

public class WebServer {
	private static String path = "/web";
	private static List<WebServer> serverList = new ArrayList<WebServer>();
	private static char[] password;
	private ServerSocket serverSocket = null;
	private int port;
	public static void setPath(String path) {
		WebServer.path = path;
	}
	public static String getPath() {
		return path;
	}
	
	public WebServer(int port) {
		this.port = port;
		serverList.add(this);
	}
	
	public static void setPassword(char[] password) {
		WebServer.password = password;
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
		WebResponses.load();
		try {
			File key = new File(path + "/keystore.jks");
			
			SSLContext sslContext = SSLContext.getInstance("TLS");
            
			KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(key), password);

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
                	new WebResponses(serverSocket.accept());
                } catch (SocketException e) {
                	try {
                		Thread.sleep(1000);
                	} catch (InterruptedException e1) {
                		return;
                	}
                }
            }
        } catch (IOException | NoSuchAlgorithmException | CertificateException | KeyManagementException | KeyStoreException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
	}
	

	
	public static void stopAll() {
		for(WebServer w : serverList) if(w != null) w.stop();
	}
	
}
