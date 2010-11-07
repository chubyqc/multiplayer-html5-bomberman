package chubyqc.bomberman.client.game;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;

public class LocalBomber extends Bomber {

    private static final int KEY_DOWN = 40;
    private static final int KEY_LEFT = 37;
    private static final int KEY_UP = 38;
    private static final int KEY_RIGHT = 39;
    private static final int KEY_SPACE = 32;
    
    private HandlerRegistration _listener;
    private Network _network;
    private int _currentKeyDown;
    
    public LocalBomber(Level level, Network network) {
        super(level, 0, 0);
        _network = network;
        _network.setBomber(this);
        _currentKeyDown = -1;
        _listener = Event.addNativePreviewHandler(new Event.NativePreviewHandler() {
            
            @Override
            public void onPreviewNativeEvent(Event.NativePreviewEvent event) {
                
                if ((event.getTypeInt() & Event.KEYEVENTS) != 0) {
                    if (event.getTypeInt() == Event.ONKEYDOWN) {
                        int keyCode = event.getNativeEvent().getKeyCode();
                        if (_currentKeyDown != keyCode) {
                            _currentKeyDown = keyCode;
                            handleKeyDown(event.getNativeEvent().getKeyCode());
                        }
                    } else if (event.getTypeInt() == Event.ONKEYUP) {
                        _currentKeyDown = -1;
                        handleKeyUp(event.getNativeEvent().getKeyCode());
                    }
                }
            }
        });
    }
    
    private void handleKeyDown(int keyCode) {
        if (keyCode == KEY_DOWN) {
            _network.moveDown();
            moveDown();
        } else if (keyCode == KEY_LEFT) {
            _network.moveLeft();
            moveLeft();
        } else if (keyCode == KEY_UP) {
            _network.moveUp();
            moveUp();
        } else if (keyCode == KEY_RIGHT) {
            _network.moveRight();
            moveRight();
        } else if (keyCode == KEY_SPACE) {
            _network.dropBomb();
            dropBomb();
        }
    }
    
    private void handleKeyUp(int keyCode) {
        if (keyCode == KEY_DOWN || keyCode == KEY_RIGHT || keyCode == KEY_LEFT || keyCode == KEY_UP) {
            _network.notMoving();
            notMoving();
        }
    }
    
    @Override
    void died(Bomb killedBy) {
        super.died(killedBy);
        _listener.removeHandler();
        notMoving();
    }
    
    int getX() {
        return _x;
    }
    
    int getY() {
        return _y;
    }
}
