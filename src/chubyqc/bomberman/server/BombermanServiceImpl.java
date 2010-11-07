package chubyqc.bomberman.server;

import java.util.Collection;

import chubyqc.bomberman.client.BombermanService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class BombermanServiceImpl extends RemoteServiceServlet implements BombermanService {

    private static final long serialVersionUID = 1L;

    @Override
    public void createGame(String name, String password, String username) throws Exception {
        Manager.get().createGame(name, password, username,
            getThreadLocalRequest().getSession());
    }

    @Override
    public Collection<String> getGames() {
        return Manager.get().getGames();
    }

    @Override
    public void joinGame(String gameName, String gamePassword, String username) throws Exception {
        Manager.get().joinGame(gameName, gamePassword, username,
            getThreadLocalRequest().getSession());
    }
}
