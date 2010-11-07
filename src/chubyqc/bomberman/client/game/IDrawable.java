package chubyqc.bomberman.client.game;


interface IDrawable {
    void draw(State state);
    boolean needRedraw();
    boolean shouldRemove();
}
