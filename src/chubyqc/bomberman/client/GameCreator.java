package chubyqc.bomberman.client;

import java.util.Collection;

import chubyqc.bomberman.client.game.Game;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

public class GameCreator {
    
    private BombermanServiceAsync _service;
    private VerticalPanel _panel;
    private GWTCanvas _canvas;
    private ListBox _existingGames;
    
    public GameCreator() {
        _service = GWT.create(BombermanService.class);
        RootPanel.get().add(_panel = new VerticalPanel());
        _canvas = new GWTCanvas();
        RootPanel.get().add(_canvas);
        createForm();
        createList();
    }
    
    private void createList() {
        _existingGames.addItem("none");
        _service.getGames(new AsyncCallback<Collection<String>>() {
            
            @Override
            public void onSuccess(Collection<String> result) {
                for (String name : result) {
                    _existingGames.addItem(name);
                }
            }
            
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }
        });
    }
    
    private void createGame(String gameName) {
        _panel.removeFromParent();
        new Game(gameName, RootPanel.get(), _canvas);
    }
    
    private AsyncCallback<Void> createStartCallback(final TextBox nameField) {
        return new AsyncCallback<Void>() {
            
            @Override
            public void onSuccess(Void result) {
                createGame(nameField.getText());
            }
            
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }
        };
    }
    
    private void createForm() {
        final TextBox nameField = new TextBox();
        final PasswordTextBox passwordField = new PasswordTextBox();
        final TextBox usernameField = new TextBox();
        _existingGames = new ListBox(false);
        
        _panel.add(new Label("New game name:"));
        _panel.add(nameField);
        _panel.add(new Label("Existing game name:"));
        _panel.add(_existingGames);
        _panel.add(new Label("Game password:"));
        _panel.add(passwordField);
        _panel.add(new Label("Username:"));
        _panel.add(usernameField);
        
        final AsyncCallback<Void> callback = createStartCallback(nameField);
        _panel.add(new Button("Create", new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                _service.createGame(nameField.getText(), passwordField.getText(), 
                    usernameField.getText(), callback);
            }
        }));
        _panel.add(new Button("Join", new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                _service.joinGame(_existingGames.getItemText(_existingGames.getSelectedIndex()),
                    passwordField.getText(), usernameField.getText(), callback);
            }
        }));
    }
}
