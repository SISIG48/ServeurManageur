package net.sisig48.web;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebResponsesData {
	private Map<String, String> cookies;
	private List<String> requestList;
	private BufferedReader in;
	private OutputStream out;
	private Socket clientSocket;
	private List<String> request = new ArrayList<String>();
	private WebAccount account = null;
	WebResponsesData(Socket clientSocket, List<String> requestList, BufferedReader in, OutputStream out) {
		this.clientSocket = clientSocket;
		this.requestList = requestList;
		cookies = parseCookies(requestList);
		if(requestList.size() > 0)
		try {request.addAll(Arrays.asList(URLDecoder.decode(requestList.get(0), StandardCharsets.UTF_8.toString()).split(" ")));} catch (UnsupportedEncodingException e) {}
		if(getCokkies().get("account") != null && getCokkies().get("pass") != null && getCokkies().get("token") != null) {
			account = WebAccount.getUser(getCokkies().get("account"), getCokkies().get("pass"), getCokkies().get("token"), clientSocket.getInetAddress());
		}
		this.in = in;
		this.out = out;
	}
	
	public BufferedReader getBufferedReader() {
		return in;
	}
	
	public OutputStream getOutputStream() {
		return out;
	}
	
	public Socket getSocket() {
		return clientSocket;
	}
	
	public String[] get() {
		return requestList.toArray(new String[0]);
	}
	
	public Map<String, String> getCokkies() {
		return cookies;
	}
	
	public String getType() {
		return request.get(0);
	}
	
	public String getRequest() {
		return request.get(1);
	}
	
	public WebAccount getAccount() {
		return account;
	}
	
	private static Map<String, String> parseCookies(List<String> request) {
        Map<String, String> cookies = new HashMap<>();
        String cookieHeader = extractHeaderValue(request, "Cookie");
        if (cookieHeader != null) {
            String[] cookiePairs = cookieHeader.split("; ");
            for (String cookiePair : cookiePairs) {
                String[] parts = cookiePair.split("=");
                if (parts.length == 2) {
                    cookies.put(parts[0], parts[1]);
                }
            }
        }
        return cookies;
    }

    private static String extractHeaderValue(List<String> request, String headerName) {
    	if(request != null) for(String line : request) {
            if (line.startsWith(headerName + ":")) {
                return line.substring(headerName.length() + 2).trim();
            }
        }
        return null;
    }
}
