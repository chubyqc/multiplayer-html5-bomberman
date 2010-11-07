package chubyqc.bomberman.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocket.Outbound;

class Game {
    
    private String _password;
    private Set<String> _players;
    private Collection<Outbound> _clients;

    Game(String password, String username) {
        _password = password;
        _players = new HashSet<String>();
        _players.add(username);
        _clients = Collections.synchronizedCollection(new ArrayList<WebSocket.Outbound>());
    }
    
    boolean join(String username, String gamePassword) {
        if (_password.equals(gamePassword) && !_players.contains(username)) {
            _players.add(username);
            return true;
        }
        return false;
    }
    
    void addClient(Outbound client) {
        _clients.add(client);
    }
    
    void sendMessage(Outbound from, String data) {
        synchronized (_clients) {
            for (Outbound client : _clients) {
                if (client != from && client.isOpen()) {
                    try {
                        client.sendMessage(data);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
