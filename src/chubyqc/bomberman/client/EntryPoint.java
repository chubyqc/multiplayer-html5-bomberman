package chubyqc.bomberman.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EntryPoint implements com.google.gwt.core.client.EntryPoint {
    
    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        final GWTCanvas canvas = new GWTCanvas();
        RootPanel.get().add(canvas);

        final TextBox nameField = new TextBox();
        RootPanel.get().add(nameField);
        RootPanel.get().add(new Button("Start", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (nameField.getText().length() == 0) {
                    Window.alert("Enter a unique name.");
                } else {
                    ((Button)event.getSource()).setVisible(false);
                    new Game(nameField.getText(), RootPanel.get(), canvas);
                }
            }
        }));
    }
}
