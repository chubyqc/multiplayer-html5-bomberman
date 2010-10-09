package chubyqc.bomberman.client;

import com.google.gwt.user.client.Event;

public class LocalBomber extends Bomber {

    private static final int KEY_DOWN = 40;
    private static final int KEY_LEFT = 37;
    private static final int KEY_UP = 38;
    private static final int KEY_RIGHT = 39;
    
    public LocalBomber() {
        Event.addNativePreviewHandler(new Event.NativePreviewHandler() {
            
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
            moveDown();
        } else if (keyCode == KEY_LEFT) {
            moveLeft();
        } else if (keyCode == KEY_UP) {
            moveUp();
        } else if (keyCode == KEY_RIGHT) {
            moveRight();
        }
    }
    
    private void handleKeyUp(int keyCode) {
        if (keyCode == KEY_DOWN || keyCode == KEY_RIGHT || keyCode == KEY_LEFT || keyCode == KEY_UP) {
            notMoving();
        }
    }
}
