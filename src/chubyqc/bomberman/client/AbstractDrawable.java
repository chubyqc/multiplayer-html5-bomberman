package chubyqc.bomberman.client;

import com.google.gwt.widgetideas.graphics.client.Color;

abstract class AbstractDrawable implements IDrawable {

    protected int _x;
    protected int _y;
    protected int _size;
    
    protected AbstractDrawable(int x, int y, int size) {
        _x = x;
        _y = y;
        _size = size;
    }
    
    protected void reset(State state) {
        state.getCanvas().setFillStyle(Color.WHITE);
        state.getCanvas().fillRect(_x, _y, _size, _size);
    }
    
    protected boolean overlap(int x, int y, int size) {
        return x + size >= _x && x <= _x + _size &&
            y + size >= _y && y <= _y + _size;
    }
    
    public void draw(State state) {
        state.getCanvas().saveContext();
        reset(state);
        doDraw(state);
        state.getCanvas().restoreContext();
    }
    
    abstract void doDraw(State state);
}
