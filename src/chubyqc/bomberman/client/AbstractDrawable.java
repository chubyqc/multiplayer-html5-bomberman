package chubyqc.bomberman.client;

import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

abstract class AbstractDrawable implements IDrawable {
    public void draw(GWTCanvas canvas) {
        canvas.saveContext();
        doDraw(canvas);
        canvas.restoreContext();
    }
    
    abstract void doDraw(GWTCanvas canvas);
}
