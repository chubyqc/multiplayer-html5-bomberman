package chubyqc.bomberman.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

public class BombermanServlet extends WebSocketServlet {
    
    private static final long serialVersionUID = 1L;
    
    class BombermanWebSocket implements WebSocket {
        
        private Outbound _outbound;
        private Game _game;
        
        public BombermanWebSocket(Game game) {
            _game = game;
        }

        public void onConnect(Outbound outbound) {
            _game.addClient(_outbound = outbound);
        }
        
        public void onMessage(byte frame, String data) {
            _game.sendMessage(_outbound, data);
        }
        
        public void onDisconnect() {
        }
        
        public void onMessage(byte frame, byte[] data, int offset, int length) {}
    }
    
    protected WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
        return new BombermanWebSocket((Game)request.getSession().getAttribute("game"));
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getServletContext().getNamedDispatcher("default").forward(request, response);
    }
}
