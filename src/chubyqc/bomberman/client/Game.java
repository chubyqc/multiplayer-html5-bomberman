package chubyqc.bomberman.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

public class Game implements INetworkListener {
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
    private Set<AbstractDrawable> _elementsToDraw;
    private Set<Bomber> _bombers;
    private Level _level;
    
    private long _previousFramerateShow;
    
    Game(String id, Panel container, GWTCanvas canvas) {
        _container = container;
        _canvas = canvas;
        _elementsToDraw = new HashSet<AbstractDrawable>();
        _bombers = new HashSet<Bomber>();
        initUI();
        initCanvas();
        createLevel();
        
        Network network = new Network(id, this, _level);
        bomberJoined(new LocalBomber(_level, network));
        
        startDrawing();
    }

    @Override
    public void bomberJoined(Bomber bomber) {
        _elementsToDraw.add(bomber);
        _bombers.add(bomber);
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
        _elementsToDraw.add(_level = new Level(size, 
            Integer.parseInt(_blockSizeTextBox.getText()),
            Integer.parseInt(_pathWidthTextBox.getText())));
    }
    
    private void showFrameRate(State state) {
        if (System.currentTimeMillis() - _previousFramerateShow > 1000) {
            _frameRateLabel.setText(String.valueOf(state.getFrameRate()));
            _previousFramerateShow = System.currentTimeMillis();
        }
    }
    
    private void scheduleNextFrame(State state) {
        state.scheduleNextFrame();
        _drawer.schedule(state.getWaitTime());
    }
    
    private void startDrawing() {
        final State state = new State(_canvas, this);
        final List<AbstractDrawable> toRemove = new ArrayList<AbstractDrawable>();
        _drawer = new Timer() {
            
            @Override
            public void run() {
                for (AbstractDrawable element : _elementsToDraw) {
                    if (element.shouldRemove()) {
                        element.reset(state);
                        toRemove.add(element);
                    } else if (element.needRedraw()) {
                        element.draw(state);
                    }
                }
                for (AbstractDrawable element : toRemove) {
                    _elementsToDraw.remove(element);
                }
                scheduleNextFrame(state);
                _drawer.schedule(state.getWaitTime());
                showFrameRate(state);
            }
        };
        scheduleNextFrame(state);
    }
    
    void bombExploded(State state, Bomb bomb) {
        for (Bomber bomber : _bombers) {
            if (bomb.overlap(bomber)) {
                bomber.died(bomb);
            }
        }
    }
}
