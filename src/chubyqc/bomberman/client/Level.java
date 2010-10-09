package chubyqc.bomberman.client;

import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

public class Level extends AbstractDrawable {
    
    private int _size;
    private int _blockSize;
    private int _pathWidth;
    private boolean _needRedraw;
    
    Level(int size, int blockSize, int pathWidth) {
        _size = size;
        _blockSize = blockSize;
        _pathWidth = pathWidth;
        _needRedraw = true;
    }
    
    public void doDraw(GWTCanvas canvas) {
        canvas.strokeRect(1, 1, _size, _size);
        for (int i = _pathWidth; i < _size - _pathWidth; i += _blockSize + _pathWidth) {
            for (int j = _pathWidth; j < _size - _pathWidth; j += _blockSize + _pathWidth) {
                new Block(i, j, _blockSize).draw(canvas);
            }
        }
        _needRedraw = false;
    }
    
    @Override
    public boolean needRedraw() {
        return _needRedraw;
    }
}
