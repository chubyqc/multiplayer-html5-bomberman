package chubyqc.bomberman.client.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
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
    private State _state;
    
    public Game(String id, Panel container, GWTCanvas canvas) {
        _container = container;
        _canvas = canvas;
        _elementsToDraw = new HashSet<AbstractDrawable>();
        _bombers = new HashSet<Bomber>();
        initUI();
        createLevel();

        _state = new State(_canvas, this);
        Network network = new Network(id, this, _level, _state);
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
        _sizeTextBox.setVisible(false);
        _sizeTextBox.setText(String.valueOf(SIZE_DEFAULT));
        _container.add(_sizeTextBox);
        _blockSizeTextBox = new TextBox();
        _blockSizeTextBox.setVisible(false);
        _blockSizeTextBox.setText(String.valueOf(BLOCK_SIZE_DEFAULT));
        _container.add(_blockSizeTextBox);
        _pathWidthTextBox = new TextBox();
        _pathWidthTextBox.setVisible(false);
        _pathWidthTextBox.setText(String.valueOf(PATH_WIDTH));
        _container.add(_pathWidthTextBox);
        _container.add(_frameRateLabel = new Label());
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
        final List<AbstractDrawable> toRemove = new ArrayList<AbstractDrawable>();
        _drawer = new Timer() {
            
            @Override
            public void run() {
                for (AbstractDrawable element : _elementsToDraw) {
                    if (element.shouldRemove()) {
                        element.reset(_state);
                        toRemove.add(element);
                    } else if (element.needRedraw()) {
                        element.draw(_state);
                    }
                }
                for (AbstractDrawable element : toRemove) {
                    _elementsToDraw.remove(element);
                }
                scheduleNextFrame(_state);
                _drawer.schedule(_state.getWaitTime());
                showFrameRate(_state);
            }
        };
        scheduleNextFrame(_state);
    }
    
    void bombExploded(State state, Bomb bomb) {
        for (Bomber bomber : _bombers) {
            if (bomb.overlap(bomber)) {
                bomber.died(bomb);
            }
        }
    }
}
