package chubyqc.bomberman.client;


interface IDrawable {
    void draw(State state);
    boolean needRedraw();
    boolean shouldRemove();
}
