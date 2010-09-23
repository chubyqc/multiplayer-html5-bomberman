package chubyqc.bomberman.client;

import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

public class Level implements IDrawable {
    
    private int _size;
    private int _blockSize;
    private int _pathWidth;
    
    Level(int size, int blockSize, int pathWidth) {
        _size = size;
        _blockSize = blockSize;
        _pathWidth = pathWidth;
    }
    
    public void draw(GWTCanvas canvas) {
        canvas.strokeRect(1, 1, _size, _size);
        for (int i = _pathWidth; i < _size - _pathWidth; i += _blockSize + _pathWidth) {
            for (int j = _pathWidth; j < _size - _pathWidth; j += _blockSize + _pathWidth) {
                new Block(i, j, _blockSize).draw(canvas);
            }
        }
    }
}
