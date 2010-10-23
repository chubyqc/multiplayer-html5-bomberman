package chubyqc.bomberman.client;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;

public class LocalBomber extends Bomber {

    private static final int KEY_DOWN = 40;
    private static final int KEY_LEFT = 37;
    private static final int KEY_UP = 38;
    private static final int KEY_RIGHT = 39;
    private static final int KEY_SPACE = 32;
    
    private HandlerRegistration _listener;
    private IBomber _network;
    
    public LocalBomber(Level level, IBomber network) {
        super(level);
        _network = network;
        _listener = Event.addNativePreviewHandler(new Event.NativePreviewHandler() {
            
            @Override
            public void onPreviewNativeEvent(Event.NativePreviewEvent event) {
                
                if ((event.getTypeInt() & Event.KEYEVENTS) != 0) {
                    if (event.getTypeInt() == Event.ONKEYDOWN) {
                        handleKeyDown(event.getNativeEvent().getKeyCode());
                    } else if (event.getTypeInt() == Event.ONKEYUP) {
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
}
