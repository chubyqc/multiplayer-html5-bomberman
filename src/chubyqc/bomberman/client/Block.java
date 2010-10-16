package chubyqc.bomberman.client;

import com.google.gwt.widgetideas.graphics.client.Color;

public class Block extends AbstractDrawable {
    
    public Block(int x, int y, int size) {
        super(x, y, size);
    }
    
    public void doDraw(State state) {
        state.getCanvas().setFillStyle(Color.BLACK);
        state.getCanvas().fillRect(_x, _y, _size, _size);
    }
    
    @Override
    public boolean needRedraw() {
        return false;
    }

    void restrict(int[] xYWidthHeight, int xCenter, int yCenter) {
        int x = xYWidthHeight[0], y = xYWidthHeight[1], width = xYWidthHeight[2],
            height = xYWidthHeight[3];
        
        if (width > height && y + height >= _y && 
            y <= _y + _size) {
            if (xCenter <= _x) {
                if (x + width >= _x) {
                    width = _x - x - 1;
                }
            } else if (xCenter >= _x + _size) {
                if (x <= _x + _size) {
                    width -= (_x + _size + 1) - x;
                    x = _x + _size + 1;
                }
            }
        } else if (x + width >= _x && 
            x <= _x + _size) {
            if (yCenter <= _y) {
                if (y + height >= _y) {
                    height = _y - y - 1;
                }
            } else if (yCenter >= _y + _size) {
                if (y <= _y + _size) {
                    height -= (_y + _size + 1) - y;
                    y = _y + _size + 1;
                }
            }
        }
        xYWidthHeight[0] = x;
        xYWidthHeight[1] = y;
        xYWidthHeight[2] = width;
        xYWidthHeight[3] = height;
        
//        x + size >= _x && x <= _x + _size &&
//        y + size >= _y && y <= _y + _size
    }
}
