package chubyqc.bomberman.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Window;
import com.sunshineapps.gwt.websockets.client.WebSocketCallback;
import com.sunshineapps.gwt.websockets.client.WebSocketClient;

public class Network implements IBomber {

    private static final String K_ACTION = "a";
    private static final String K_BOMBER = "b";

    private static final String A_NOTMOVING = "0";
    private static final String A_MOVEUP = "1";
    private static final String A_MOVERIGHT = "2";
    private static final String A_MOVELEFT = "3";
    private static final String A_MOVEDOWN = "4";
    private static final String A_DROPBOMB = "5";
    private static final String A_JOIN = "6";
    private static final String A_ALREADY_THERE = "7";
    
    private static final String M_START = K_ACTION + "=";
    private static final String M_BOMBER = K_BOMBER + "=";
    
    private WebSocketClient _client;
    private String _id;
    private Map<String, IExecutor> _executors;
    private Map<String, IBomber> _bombers;
    private INetworkListener _listener;
    private Level _level;
    
    public Network(String id, INetworkListener listener, Level level) {
        _id = id;
        _bombers = new HashMap<String, IBomber>();
        _listener = listener;
        _level = level;
        initExecutors();
        initSockets();
    }

    private void send(String action) {
        _client.send(M_START + action + "," + M_BOMBER + _id);
    }

    private void join() {
        send(A_JOIN);
    }

    @Override
    public void notMoving() {
        send(A_NOTMOVING);
    }

    @Override
    public void moveUp() {
        send(A_MOVEUP);
    }

    @Override
    public void moveLeft() {
        send(A_MOVELEFT);
    }

    @Override
    public void moveRight() {
        send(A_MOVERIGHT);
    }

    @Override
    public void moveDown() {
        send(A_MOVEDOWN);
    }

    @Override
    public void dropBomb() {
        send(A_DROPBOMB);
    }
    
    private void newBomber(String id) {
        Bomber newBomber = new Bomber(_level);
        _bombers.put(id, newBomber);
        _listener.bomberJoined(newBomber);
    }
    
    private void initExecutors() {
        _executors = new HashMap<String, IExecutor>();
        _executors.put(A_DROPBOMB, new IExecutor() { 
            @Override
            public void execute(String id, IBomber bomber) {
                bomber.dropBomb();
            }
        });
        _executors.put(A_JOIN, new IExecutor() { 
            @Override
            public void execute(String id, IBomber bomber) {
                newBomber(id);
                send(A_ALREADY_THERE);
            }
        });
        _executors.put(A_ALREADY_THERE, new IExecutor() { 
            @Override
            public void execute(String id, IBomber bomber) {
                newBomber(id);
            }
        });
        _executors.put(A_MOVEDOWN, new IExecutor() { 
            @Override
            public void execute(String id, IBomber bomber) {
                bomber.moveDown();
            }
        });
        _executors.put(A_MOVELEFT, new IExecutor() { 
            @Override
            public void execute(String id, IBomber bomber) {
                bomber.moveLeft();
            }
        });
        _executors.put(A_MOVERIGHT, new IExecutor() { 
            @Override
            public void execute(String id, IBomber bomber) {
                bomber.moveRight();
            }
        });
        _executors.put(A_MOVEUP, new IExecutor() { 
            @Override
            public void execute(String id, IBomber bomber) {
                bomber.moveUp();
            }
        });
        _executors.put(A_NOTMOVING, new IExecutor() { 
            @Override
            public void execute(String id, IBomber bomber) {
                bomber.notMoving();
            }
        });
    }
    
    private void initSockets() {
        _client = new WebSocketClient(new WebSocketCallback() {
            
            @Override
            public void message(String message) {
                String[] parts = message.split(",");
                String[] actionParts = parts[0].split("=");
                String[] bomberParts = parts[1].split("=");
                
                _executors.get(actionParts[1]).execute(bomberParts[1], _bombers.get(bomberParts[1]));
            }
            
            @Override
            public void disconnected() {
                Window.alert("Disconnected");
            }
            
            @Override
            public void connected() {
                join();
            }
        });
        String url = Document.get().getURL();
        url = "ws" + url.substring(4, url.lastIndexOf("/")) + "/multiplayer_html5_bomberman/bomberman";
        _client.connect(url);
    }

    private static interface IExecutor {
        void execute(String id, IBomber bomber);
    }
}
