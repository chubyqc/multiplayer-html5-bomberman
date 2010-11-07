package chubyqc.bomberman.client.game;

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
    private Map<String, RemoteBomber> _bombers;
    private INetworkListener _listener;
    private Level _level;
    private LocalBomber _bomber;
    private State _state;
    
    public Network(String id, INetworkListener listener, Level level, State state) {
        _id = id;
        _bombers = new HashMap<String, RemoteBomber>();
        _listener = listener;
        _level = level;
        _state = state;
        initExecutors();
        initSockets();
    }
    
    private void send(String action, Object... params) {
        String msg = System.currentTimeMillis() + "," + M_START + action + "," + M_BOMBER + _id;
        for (Object param : params) {
            msg += "," + param.toString();
        }
        _client.send(msg);
    }
    
    private void join() {
        send(A_JOIN);
    }
    
    @Override
    public void notMoving() {
        sendWithPosition(A_NOTMOVING);
    }
    
    @Override
    public void moveUp() {
        sendWithPosition(A_MOVEUP);
    }
    
    @Override
    public void moveLeft() {
        sendWithPosition(A_MOVELEFT);
    }
    
    @Override
    public void moveRight() {
        sendWithPosition(A_MOVERIGHT);
    }
    
    @Override
    public void moveDown() {
        sendWithPosition(A_MOVEDOWN);
    }
    
    @Override
    public void dropBomb() {
        sendWithPosition(A_DROPBOMB);
    }
    
    private void sendWithPosition(String action) {
        send(action, _bomber.getX(), _bomber.getY());
    }
    
    private void newBomber(String id, int x, int y) {
        RemoteBomber newBomber = new RemoteBomber(_level, x, y);
        _bombers.put(id, newBomber);
        _listener.bomberJoined(newBomber);
    }
    
    private void initExecutors() {
        _executors = new HashMap<String, IExecutor>();
        _executors.put(A_DROPBOMB, new ExecutorWithPosition() {
            @Override
            public void execute(String id, RemoteBomber bomber) {
                bomber.dropBomb();
            }
        });
        _executors.put(A_JOIN, new IExecutor() {
            @Override
            public void execute(Network network, String id, RemoteBomber bomber, String[] params) {
                newBomber(id, 0, 0);
                send(A_ALREADY_THERE, _bomber.getX(), _bomber.getY());
            }
        });
        _executors.put(A_ALREADY_THERE, new IExecutor() {
            @Override
            public void execute(Network network, String id, RemoteBomber bomber, String[] params) {
                newBomber(id, Integer.parseInt(params[0]), Integer.parseInt(params[1]));
            }
        });
        _executors.put(A_MOVEDOWN, new ExecutorWithPosition() {
            @Override
            public void execute(String id, RemoteBomber bomber) {
                bomber.moveDown();
            }
        });
        _executors.put(A_MOVELEFT, new ExecutorWithPosition() {
            @Override
            public void execute(String id, RemoteBomber bomber) {
                bomber.moveLeft();
            }
        });
        _executors.put(A_MOVERIGHT, new ExecutorWithPosition() {
            @Override
            public void execute(String id, RemoteBomber bomber) {
                bomber.moveRight();
            }
        });
        _executors.put(A_MOVEUP, new ExecutorWithPosition() {
            @Override
            public void execute(String id, RemoteBomber bomber) {
                bomber.moveUp();
            }
        });
        _executors.put(A_NOTMOVING, new ExecutorWithPosition() {
            @Override
            public void execute(String id, RemoteBomber bomber) {
                bomber.notMoving();
            }
        });
    }
    
    private void initSockets() {
        _client = new WebSocketClient(new WebSocketCallback() {
            
            @Override
            public void message(String message) {
                String[] parts = message.split(",", 4);
                String[] actionParts = parts[1].split("=");
                String[] bomberParts = parts[2].split("=");
                
                RemoteBomber bomber = _bombers.get(bomberParts[1]);
                
                _executors.get(actionParts[1]).execute(Network.this, bomberParts[1], bomber, (parts.length == 4) ? parts[parts.length - 1].split(",") : null);
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
        void execute(Network network, String id, RemoteBomber bomber, String[] params);
    }
    
    private static abstract class ExecutorWithPosition implements IExecutor {
        @Override
        public void execute(Network network, String id, RemoteBomber bomber, String[] params) {
            bomber.setPosition(network._state, Integer.parseInt(params[0]), Integer.parseInt(params[1]));
            execute(id, bomber);
        }
        
        protected abstract void execute(String id, RemoteBomber bomber);
    }
    
    void setBomber(LocalBomber bomber) {
        _bomber = bomber;
    }
}
