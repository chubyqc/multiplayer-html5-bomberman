package chubyqc.bomberman.client;

import com.google.gwt.widgetideas.graphics.client.Color;

public class Bomb extends AbstractDrawable {
    
    private static final int SIZE = 10;
    private static final int BOMB_TIME = 3000;
    private static final int EXPLOSION_TIME = 1000;
    private static final int EXPLOSION_LENGTH = 200;
    
    private long _dropTime;
    private long _now;
    private boolean _exploded;
    private Level _level;
    private int[] xYWidthHeight;
    
    public Bomb(int x, int y, Level level) {
        super(x - SIZE / 2, y - SIZE / 2, SIZE);
        _level = level;
        _dropTime = System.currentTimeMillis();
        xYWidthHeight = new int[4];
    }

    @Override
    public boolean needRedraw() {
        return true;
    }

    @Override
    void doDraw(State state) {
        _now = state.now();
        state.getCanvas().setFillStyle(Color.RED);
        if (!explode(state)) {
            state.getCanvas().fillRect(_x, _y, _size, _size);
        }
    }
    
    private boolean explode(State state) {
        if (!exploded() && _now - _dropTime >= BOMB_TIME - EXPLOSION_TIME) {
            drawExplosion(state);
            return true;
        }
        return false;
    }
    
    private void drawExplosion(State state) {
        drawExplosionPart(state, _x + _size / 2 - EXPLOSION_LENGTH / 2, _y, EXPLOSION_LENGTH, _size);
        drawExplosionPart(state, _x, _y + _size / 2 - EXPLOSION_LENGTH / 2, _size, EXPLOSION_LENGTH);
    }

    private void drawExplosionPart(State state, int x, int y, int width, int height) {
        xYWidthHeight[0] = x;
        xYWidthHeight[1] = y;
        xYWidthHeight[2] = width;
        xYWidthHeight[3] = height;
        _level.restrict(xYWidthHeight, x + width / 2, y + height / 2);
        state.getCanvas().fillRect(xYWidthHeight[0], xYWidthHeight[1], 
            xYWidthHeight[2], xYWidthHeight[3]);
    }

    boolean exploded() {
        return _exploded = (_now - _dropTime >= BOMB_TIME);
    }
    
    @Override
    protected void reset(State state) {
        super.reset(state);
        if (_exploded) {
            drawExplosion(state);
        }
    }
}
