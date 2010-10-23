package chubyqc.bomberman.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocket.Outbound;
import org.eclipse.jetty.websocket.WebSocketServlet;

public class BombermanServlet extends WebSocketServlet {
    
    private static final long serialVersionUID = 1L;
    
    private Collection<Outbound> _clients;
    
    public BombermanServlet() {
        _clients = Collections.synchronizedCollection(new ArrayList<WebSocket.Outbound>());
    }
    
    class BombermanWebSocket implements WebSocket {
        
        private Outbound _outbound;
        
        public void onConnect(Outbound outbound) {
            _clients.add(_outbound = outbound);
        }
        
        public void onMessage(byte frame, String data) {
            synchronized (_clients) {
                for (Outbound client : _clients) {
                    if (client != _outbound && client.isOpen()) {
                        try {
                            client.sendMessage(data);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            try {
                _outbound.sendMessage("");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        public void onDisconnect() {
        }
        
        public void onMessage(byte frame, byte[] data, int offset, int length) {}
    }
    
    protected WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
        return new BombermanWebSocket();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getServletContext().getNamedDispatcher("default").forward(request, response);
    }
}
