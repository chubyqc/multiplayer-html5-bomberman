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
        simpleReset(state);
    }
    
    protected void simpleReset(State state) {
        state.getCanvas().setFillStyle(Color.WHITE);
        state.getCanvas().fillRect(_x, _y, _size, _size);
    }
    
    protected boolean overlap(AbstractDrawable drawable) {
        return overlap(drawable._x, drawable._y, drawable._size);
    }
    
    protected boolean overlap(int x, int y, int size) {
        return overlap(_x, _y, _size, _size, x, y, size, size);
    }
    
    static boolean overlap(int x0, int y0, int width0, int height0, 
        int x1, int y1, int width1, int height1) {
        return x1 + width1 >= x0 && x1 <= x0 + width0 &&
            y1 + height1 >= y0 && y1 <= y0 + height0;
    }
    
    public void draw(State state) {
        state.getCanvas().saveContext();
        reset(state);
        doDraw(state);
        state.getCanvas().restoreContext();
    }
    
    @Override
    public boolean shouldRemove() {
        return false;
    }
    
    abstract void doDraw(State state);
}
