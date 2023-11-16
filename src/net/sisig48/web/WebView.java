package net.sisig48.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class WebView {
	public static void PageNotFound(OutputStream out) {
        try {
        	out.write("HTTP/1.1 400 Not Found\r\n".getBytes());
			out.write("Content-Type: text/plain\r\n".getBytes());
			out.write("\r\n".getBytes());
			out.write("Erreur 404".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void returnData(File file, OutputStream out, String type) {
		try {
			byte[] buffer = new byte[1024];
			int bytesRead;
			InputStream fileInputStream = new FileInputStream(file);
			
			out.write("HTTP/1.1 200 OK\r\n".getBytes());
			out.write(("Content-Type: " + type + "\r\n\"").getBytes());
			out.write("Content-Length: ".getBytes());
			out.write(String.valueOf(file.length()).getBytes());
			out.write("\r\n".getBytes());
			out.write("\r\n".getBytes());
			
			while ((bytesRead = fileInputStream.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
			
			fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
