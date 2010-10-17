package chubyqc.bomberman.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.widgetideas.graphics.client.Color;

public class Bomber extends AbstractDrawable {
    
    private static final int SIZE = 35;
    private static final float SPEED = .1f; // Pixel per millisecond

    private static final int DIRECTION_UP = 0;
    private static final int DIRECTION_LEFT = 1;
    private static final int DIRECTION_RIGHT = 2;
    private static final int DIRECTION_DOWN = 3;
    
    private static final int NOT_MOVING = -1;
    
    private int _movingDirection;
    private Level _level;
    private float _unusedStep;
    private Set<Bomb> _bombs;
    private int _halfSize;
    private Bomb _killedBy;
    
    Bomber(Level level) {
        super(0, 0, SIZE);
        _halfSize = SIZE / 2;
        _level = level;
        _movingDirection = -1;
        _unusedStep = 0f;
        _bombs = new HashSet<Bomb>();
    }

    @Override
    public void doDraw(State state) {
        computeNewPosition(state);
        state.getCanvas().setFillStyle(Color.GREEN);
        state.getCanvas().fillRect(_x, _y, _size, _size);
        drawBombs(state);
    }
    
    private void drawBombs(State state) {
        List<Bomb> toRemove = new ArrayList<Bomb>();
        for (Bomb bomb : _bombs) {
            if (bomb.exploded()) {
                toRemove.add(bomb);
                bomb.reset(state);
            } else {
                bomb.draw(state);
            }
        }
        for (Bomb bomb : toRemove) {
            _bombs.remove(bomb);
        }
    }
    
    private void computeNewPosition(State state) {
        int step = getStep(state.getFrameTime());
        int x = getNewX(step), y = getNewY(step);
        if (!_level.overlap(x, y, _size)) {
            _x = x;
            _y = y;
        }
    }
    
    private int getStep(long elapsedTime) {
        float step = elapsedTime * SPEED + _unusedStep;
        int trueStep = (int)step;
        _unusedStep = step - trueStep;
        return trueStep;
    }

    @Override
    public boolean needRedraw() {
        return true;
    }
    
    private int getNewX(int step) {
        if (isMovingLeft()) {
            return _x - step;
        } else if (isMovingRight()) {
            return _x + step;
        }
        return _x;
    }
    
    private int getNewY(int step) {
        if (isMovingUp()) {
            return _y - step;
        } else if (isMovingDown()) {
            return _y + step;
        }
        return _y;
    }
    
    protected void dropBomb() {
        if (_bombs.size() < 2) {
            _bombs.add(new Bomb(_x + _halfSize, _y + _halfSize, _level));
        }
    }
    
    protected void notMoving() {
        _movingDirection = NOT_MOVING;
    }
    
    protected void moveUp() {
        _movingDirection = DIRECTION_UP; 
    }
    
    protected void moveLeft() {
        _movingDirection = DIRECTION_LEFT;
    }
    
    protected void moveRight() {
        _movingDirection = DIRECTION_RIGHT;
    }
    
    protected void moveDown() {
        _movingDirection = DIRECTION_DOWN;
    }
    
    boolean isMovingUp() {
        return _movingDirection == DIRECTION_UP; 
    }
    
    boolean isMovingLeft() {
        return _movingDirection == DIRECTION_LEFT; 
    }
    
    boolean isMovingRight() {
        return _movingDirection == DIRECTION_RIGHT; 
    }
    
    boolean isMovingDown() {
        return _movingDirection == DIRECTION_DOWN; 
    }

    void died(Bomb killedBy) {
        _killedBy = killedBy;
    }
    
    @Override
    public boolean shouldRemove() {
        return _killedBy != null && _killedBy.exploded();
    }
    
    @Override
    protected void reset(State state) {
        super.reset(state);
        for (Bomb bomb : _bombs) {
            bomb.reset(state);
        }
    }
}
