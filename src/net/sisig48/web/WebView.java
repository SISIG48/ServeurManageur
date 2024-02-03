package net.sisig48.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class WebView {
	public static void PageNotFound(OutputStream out)  {
        try {
        	out.write("HTTP/1.1 404 Not Found\r\n".getBytes());
			out.write("Content-Type: text/plain\r\n".getBytes());
			out.write("\r\n".getBytes());
			out.write("Error 404 : Page not found".getBytes());
		} catch (Exception e) {
			 
		}
	}
	
	public static void PageBadResquest(OutputStream out)  {
		try {
			out.write("HTTP/1.1 400 Bad Request\r\n".getBytes());
			out.write("Content-Type: text/plain\r\n".getBytes());
			out.write("\r\n".getBytes());
			out.write("Error 400 : Bad Request".getBytes());
		} catch (Exception e) {
			 
		}
	}
	
	public static void PageForbidden(OutputStream out)  {
		try {
			out.write("HTTP/1.1 403 Forbidden\r\n".getBytes());
			out.write("Content-Type: text/plain\r\n".getBytes());
			out.write("\r\n".getBytes());
			out.write("Error 403 : Forbidden".getBytes());
		} catch (Exception e) {
			 
		}
	}
	
	public static void PageUnauthorized(OutputStream out)  {
		try {
			out.write("HTTP/1.1 401 Unauthorized\r\n".getBytes());
			out.write("Content-Type: text/plain\r\n".getBytes());
			out.write("\r\n".getBytes());
			out.write("Error 401 : Unauthorized\r\n".getBytes());
		} catch (Exception e) {
			 
		}
	}
	
	public static void returnData(File file, OutputStream out, String type)  {
		try {
			byte[] buffer = new byte[1024];
			int bytesRead;
			InputStream fileInputStream = new FileInputStream(file);
			
			out.write("HTTP/1.1 200 OK\r\n".getBytes());
			out.write(("Content-Type: " + type + "\r\n").getBytes());
			out.write("Content-Length: ".getBytes());
			out.write(String.valueOf(file.length()).getBytes());
			out.write("\r\n".getBytes());
			out.write("\r\n".getBytes());
			
			while ((bytesRead = fileInputStream.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
			
			fileInputStream.close();
		} catch (Exception e) {
			 
		}
		
	}
	
	public static void returnText(String data, OutputStream out)  {
		try {
			out.write("HTTP/1.1 200 OK\r\n".getBytes());
			out.write(("Content-Type: " + WebType.text.getInfo() + "\r\n").getBytes());
			out.write("\r\n".getBytes());
			out.write("\r\n".getBytes());
			out.write(data.getBytes());
		} catch (Exception e) {
			 
		}
		
	}
	public static void returnText(List<String> data, OutputStream out, String cookie)  {
		try {
			out.write("HTTP/1.1 200 OK\r\n".getBytes());
			out.write(("Content-Type: " + WebType.text.getInfo() + "\r\n").getBytes());
			for(String s : cookie.split("; ")) out.write(("Set-Cookie: " + s + "\r\n").getBytes());
			out.write("\r\n".getBytes());
			for(String s : data) out.write((s).getBytes());
		} catch (Exception e) {
			 
		}
		
	}
	
	public static void sendData(OutputStream out, List<String> data)  {
		try {
			for(String s : data) out.write(s.getBytes());
		} catch (Exception e) {
			 
		}
	}
	
	
	
}
