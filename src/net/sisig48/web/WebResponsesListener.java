package net.sisig48.web;

import java.net.SocketException;

public abstract class WebResponsesListener {
    public abstract boolean onWebResponse(WebResponsesData data) throws SocketException;
}
