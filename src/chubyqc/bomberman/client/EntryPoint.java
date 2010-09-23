package chubyqc.bomberman.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;
import com.sunshineapps.gwt.websockets.client.WebSocketCallback;
import com.sunshineapps.gwt.websockets.client.WebSocketClient;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EntryPoint implements com.google.gwt.core.client.EntryPoint {
    
    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        GWTCanvas canvas = new GWTCanvas();
        RootPanel.get().add(canvas);
        new Game(RootPanel.get(), canvas);
        
        final Button sendButton = new Button("Send");
        final TextBox nameField = new TextBox();
        nameField.setText("GWT User");
        final Label errorLabel = new Label();
        
        // We can add style names to widgets
        sendButton.addStyleName("sendButton");
        
        // Add the nameField and sendButton to the RootPanel
        // Use RootPanel.get() to get the entire body element
        RootPanel.get("nameFieldContainer").add(nameField);
        RootPanel.get("sendButtonContainer").add(sendButton);
        RootPanel.get("errorLabelContainer").add(errorLabel);
        
        // Focus the cursor on the name field when the app loads
        nameField.setFocus(true);
        nameField.selectAll();
        
        // Create the popup dialog box
        final DialogBox dialogBox = new DialogBox();
        dialogBox.setText("Remote Procedure Call");
        dialogBox.setAnimationEnabled(true);
        final Button closeButton = new Button("Close");
        // We can set the id of a widget by accessing its Element
        closeButton.getElement().setId("closeButton");
        final Label textToServerLabel = new Label();
        final HTML serverResponseLabel = new HTML();
        VerticalPanel dialogVPanel = new VerticalPanel();
        dialogVPanel.addStyleName("dialogVPanel");
        dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
        dialogVPanel.add(textToServerLabel);
        dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
        dialogVPanel.add(serverResponseLabel);
        dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
        dialogVPanel.add(closeButton);
        dialogBox.setWidget(dialogVPanel);
        
        // Add a handler to close the DialogBox
        closeButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                dialogBox.hide();
                sendButton.setEnabled(true);
                sendButton.setFocus(true);
            }
        });
        
        try {
            final WebSocketClient client = new WebSocketClient(new WebSocketCallback() {
                
                @Override
                public void message(String message) {
                }
                
                @Override
                public void disconnected() {
                }
                
                @Override
                public void connected() {
                }
            });
            String url = Document.get().getURL();
            url = "ws" + url.substring(4, url.lastIndexOf("/")) + "/multiplayer_html5_bomberman/chat";
            client.connect(url);
            
            // Create a handler for the sendButton and nameField
            class MyHandler implements ClickHandler, KeyUpHandler {
                /**
                 * Fired when the user clicks on the sendButton.
                 */
                public void onClick(ClickEvent event) {
                    sendNameToServer();
                }
                
                /**
                 * Fired when the user types in the nameField.
                 */
                public void onKeyUp(KeyUpEvent event) {
                    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                        sendNameToServer();
                    }
                }
                
                /**
                 * Send the name from the nameField to the server and wait for a
                 * response.
                 */
                private void sendNameToServer() {
                    try {
                        client.send("test value");
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            
            // Add a handler to send the name to the server
            MyHandler handler = new MyHandler();
            sendButton.addClickHandler(handler);
            nameField.addKeyUpHandler(handler);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
