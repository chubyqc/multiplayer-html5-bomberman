package chubyqc.bomberman.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

public class ChatServlet extends WebSocketServlet {
    
    private static final long serialVersionUID = 1L;
    
    class ChatWebSocket implements WebSocket {
        
        public void onConnect(Outbound outbound) {
            System.err.println("onConnect2");
        }
        
        public void onMessage(byte frame, String data) {
            System.err.println(data);
        }
        
        public void onDisconnect() {
        }
        
        public void onMessage(byte frame, byte[] data, int offset, int length) {
        }
    }
    
    protected WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {
        return new ChatWebSocket();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        getServletContext().getNamedDispatcher("default").forward(request, response);
    }
}
