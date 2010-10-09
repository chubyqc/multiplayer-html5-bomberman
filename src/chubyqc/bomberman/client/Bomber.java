package chubyqc.bomberman.client;

import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

public class Bomber extends AbstractDrawable {
    
    private static final int SIZE = 10;

    private static final int DIRECTION_UP = 0;
    private static final int DIRECTION_LEFT = 1;
    private static final int DIRECTION_RIGHT = 2;
    private static final int DIRECTION_DOWN = 3;
    
    private static final int NOT_MOVING = -1;
    
    private int _movingDirection;
    private int _x;
    private int _y;
    
    Bomber() {
        _movingDirection = -1;
        _x = 0;
        _y = 0;
    }

    @Override
    public void doDraw(GWTCanvas canvas) {
        canvas.setFillStyle(Color.WHITE);
        canvas.fillRect(_x, _y, SIZE, SIZE);
        canvas.setFillStyle(Color.GREEN);
        canvas.fillRect(getX(), getY(), SIZE, SIZE);
    }
    
    @Override
    public boolean needRedraw() {
        return true;
    }
    
    private int getX() {
        if (isMovingLeft()) {
            --_x;
        } else if (isMovingRight()) {
            ++_x;
        }
        return _x;
    }
    
    private int getY() {
        if (isMovingUp()) {
            --_y;
        } else if (isMovingDown()) {
            ++_y;
        }
        return _y;
    }
    
    protected void notMoving() {
        _movingDirection = NOT_MOVING;
    }
    
    void moveUp() {
        _movingDirection = DIRECTION_UP; 
    }
    
    void moveLeft() {
        _movingDirection = DIRECTION_LEFT;
    }
    
    void moveRight() {
        _movingDirection = DIRECTION_RIGHT;
    }
    
    void moveDown() {
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
}
