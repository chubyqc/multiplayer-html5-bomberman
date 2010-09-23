package chubyqc.bomberman.client;

import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

public class Block implements IDrawable {
    private int _x;
    private int _y;
    private int _size;
    
    Block(int x, int y, int size) {
        _x = x;
        _y = y;
        _size = size;
    }
    
    public void draw(GWTCanvas canvas) {
        canvas.fillRect(_x, _y, _size, _size);
    }
}
