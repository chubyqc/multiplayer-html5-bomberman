package chubyqc.bomberman.client;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BombermanServiceAsync {
    
    void createGame(String name, String password, String username, AsyncCallback<Void> callback);
    
    void getGames(AsyncCallback<Collection<String>> callback);
    
    void joinGame(String gameName, String gamePassword, String username, AsyncCallback<Void> callback);
    
}
