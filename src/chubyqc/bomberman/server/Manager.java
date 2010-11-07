package chubyqc.bomberman.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

public class Manager {
    
    private Map<String, Game> _games;
    private Collection<String> _gameNames;
    
    private static Manager _instance = new Manager();
    static Manager get() {
        return _instance;
    }
    
    private Manager() {
        _games = new HashMap<String, Game>();
        _gameNames = new ArrayList<String>();
    }
    
    private boolean isEmpty(String field) {
        return field == null || field.trim().length() == 0;
    }

    public void createGame(String name, String password, String username, HttpSession session) throws Exception {
        if (isEmpty(name) || isEmpty(password)) {
            throw new Exception("All fields are required");
        }
        if (_games.containsKey(name)) {
            throw new Exception("Game already exists");
        }
        Game game = new Game(password, username);
        _games.put(name, game);
        _gameNames.add(name);
        setSessionGame(session, game);
    }

    public Collection<String> getGames() {
        return _gameNames;
    }

    public void joinGame(String gameName, String gamePassword, String username, HttpSession session) throws Exception {
        Game game = _games.get(gameName);
        if (game == null) {
            throw new Exception("Game doesn't exist");
        }
        if (!game.join(username, gamePassword)) {
            throw new Exception("Join attempt failed");
        }
        setSessionGame(session, game);
    }
    
    private void setSessionGame(HttpSession session, Game game) {
        session.setAttribute("game", game);
    }
}
