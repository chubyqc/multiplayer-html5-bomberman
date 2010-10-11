package chubyqc.bomberman.client;

import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

abstract class AbstractDrawable implements IDrawable {

    protected int _x;
    protected int _y;
    protected int _size;
    
    private void reset(GWTCanvas canvas) {
        canvas.setFillStyle(Color.WHITE);
        canvas.fillRect(_x, _y, _size, _size);
    }
    
    protected boolean overlap(int x, int y, int size) {
        return x + size >= _x && x <= _x + _size &&
            y + size >= _y && y <= _y + _size;
    }
    
    public void draw(State state) {
        state.getCanvas().saveContext();
        reset(state.getCanvas());
        doDraw(state);
        state.getCanvas().restoreContext();
    }
    
    abstract void doDraw(State state);
}
