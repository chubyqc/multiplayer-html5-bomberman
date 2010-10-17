package chubyqc.bomberman.client;

import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

public class State {

    private static final int WAIT_TIME_MAX = 1000 / 20;
    
    private GWTCanvas _canvas;
    private Game _game;
    private long _previousTime;
    private int _waitTime;
    private int _frameTime;

    State(GWTCanvas canvas, Game game) {
        _canvas = canvas;
        _game = game;
        _frameTime = 0;
        _previousTime = System.currentTimeMillis();
    }
    
    Game getGame() {
        return _game;
    }
    
    GWTCanvas getCanvas() {
        return _canvas;
    }
    
    long getFrameTime() {
        return _frameTime;
    }
    
    int getFrameRate() {
        return 1000 / _frameTime;
    }
    
    int getWaitTime() {
        return _waitTime;
    }
    
    void scheduleNextFrame() {
        long now = System.currentTimeMillis();
        _frameTime = (int)(now - _previousTime);
        _waitTime = Math.max(1, WAIT_TIME_MAX - _frameTime);
        _previousTime = now;
    }
    
    long now() {
        return _previousTime;
    }
}
