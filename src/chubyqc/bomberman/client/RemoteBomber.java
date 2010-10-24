package chubyqc.bomberman.client;

public class RemoteBomber extends Bomber {

    RemoteBomber(Level level, int x, int y) {
        super(level, x, y);
    }

    void setPosition(State state, int x, int y) {
        simpleReset(state);
        _x = x;
        _y = y;
    }
}
