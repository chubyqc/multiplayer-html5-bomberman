package chubyqc.bomberman.client.game;

import java.util.ArrayList;
import java.util.List;

public class Level extends AbstractDrawable {
    
    private int _blockSize;
    private int _pathWidth;
    private boolean _needRedraw;
    private List<Block> _blocks;
    
    Level(int size, int blockSize, int pathWidth) {
        super(0, 0, size);
        _blockSize = blockSize;
        _pathWidth = pathWidth;
        _needRedraw = true;
        _blocks = new ArrayList<Block>();
    }
    
    public void doDraw(State state) {
        state.getCanvas().strokeRect(0, 0, _size, _size);
        for (int i = _pathWidth; i < _size - _pathWidth; i += _blockSize + _pathWidth) {
            for (int j = _pathWidth; j < _size - _pathWidth; j += _blockSize + _pathWidth) {
                Block block = new Block(i, j, _blockSize);
                _blocks.add(block);
                block.draw(state);
            }
        }
        _needRedraw = false;
    }
    
    @Override
    public boolean needRedraw() {
        return _needRedraw;
    }
    
    protected boolean overlap(int x, int y, int size) {
        if (x <= 0 || y <= 0 || x + size >= _size || y + size >= _size) {
            return true;
        }
        for (Block block : _blocks) {
            if (block.overlap(x, y, size)) {
                return true;
            }
        }
        return false;
    }

    void restrict(int[] xYWidthHeight, int xCenter, int yCenter) {
        for (Block block : _blocks) {
            block.restrict(xYWidthHeight, xCenter, yCenter);
        }
    }
}
