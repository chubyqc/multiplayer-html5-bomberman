package chubyqc.bomberman.client;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

public class Game {
    private static final int WAIT_TIME_MAX = 1000 / 20;
    private static final int SIZE_DEFAULT = 400;
    private static final int BLOCK_SIZE_DEFAULT = 50;
    private static final int PATH_WIDTH = 50;
    private static final int MARGINS = 1;
    private Panel _container;
    private GWTCanvas _canvas;

    private TextBox _sizeTextBox;
    private TextBox _blockSizeTextBox;
    private TextBox _pathWidthTextBox;
    private Label _frameRateLabel;
    
    private Timer _drawer;
    private List<AbstractDrawable> _elementsToDraw;
    private long _previousTime;
    private long _now;
    private int _waitTime;
    private int _frameTime;
    
    private long _previousFramerateShow;
    
    Game(Panel container, GWTCanvas canvas) {
        _container = container;
        _canvas = canvas;
        _elementsToDraw = new LinkedList<AbstractDrawable>();
        _previousTime = 0;
        initUI();
        initCanvas();
        createLevel();
        addBomber(new LocalBomber());
        startDrawing();
    }
    
    private void addBomber(Bomber bomber) {
        _elementsToDraw.add(bomber);
    }

    private void initUI() {
        _sizeTextBox = new TextBox();
        _sizeTextBox.setText(String.valueOf(SIZE_DEFAULT));
        _container.add(_sizeTextBox);
        _blockSizeTextBox = new TextBox();
        _blockSizeTextBox.setText(String.valueOf(BLOCK_SIZE_DEFAULT));
        _container.add(_blockSizeTextBox);
        _pathWidthTextBox = new TextBox();
        _pathWidthTextBox.setText(String.valueOf(PATH_WIDTH));
        _container.add(_pathWidthTextBox);
        _container.add(_frameRateLabel = new Label());
    }
    
    private void initCanvas() {
        _canvas.setLineWidth(1);
        _canvas.setStrokeStyle(Color.BLACK);
    }
    
    private void createLevel() {
        int size = Integer.parseInt(_sizeTextBox.getText());
        int margins = MARGINS * 2;
        _canvas.resize(size + margins, size + margins);
        _elementsToDraw.add(new Level(size, 
            Integer.parseInt(_blockSizeTextBox.getText()),
            Integer.parseInt(_pathWidthTextBox.getText())));
    }
    
    private void scheduleNextFrame() {
        _now = System.currentTimeMillis();
        _frameTime = (int)(_now - _previousTime);
        _waitTime = Math.max(1, WAIT_TIME_MAX - _frameTime);
        _previousTime = _now;
        _drawer.schedule(_waitTime);
    }
    
    private void showFrameRate() {
        if (System.currentTimeMillis() - _previousFramerateShow > 1000) {
            _frameRateLabel.setText(String.valueOf(1000 / _frameTime));
            _previousFramerateShow = System.currentTimeMillis();
        }
    }
    
    private void startDrawing() {
        _drawer = new Timer() {
            
            @Override
            public void run() {
                for (AbstractDrawable element : _elementsToDraw) {
                    if (element.needRedraw()) {
                        element.draw(_canvas);
                        scheduleNextFrame();
                        showFrameRate();
                    }
                }
            }
        };
        _previousTime = System.currentTimeMillis();
        scheduleNextFrame();
    }
}
