package chubyqc.bomberman.client;

import com.google.gwt.widgetideas.graphics.client.Color;

public class Block extends AbstractDrawable {
    
    Block(int x, int y, int size) {
        _x = x;
        _y = y;
        _size = size;
    }
    
    public void doDraw(State state) {
        state.getCanvas().setFillStyle(Color.BLACK);
        state.getCanvas().fillRect(_x, _y, _size, _size);
    }
    
    @Override
    public boolean needRedraw() {
        return false;
    }
}
