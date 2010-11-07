package chubyqc.bomberman.client;

import java.util.Collection;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("bombermanService")
public interface BombermanService extends RemoteService {
    
    void createGame(String name, String password, String username) throws Exception;
    
    Collection<String> getGames();
    
    void joinGame(String gameName, String gamePassword, String username) throws Exception;
}
