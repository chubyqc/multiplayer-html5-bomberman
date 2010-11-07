package chubyqc.bomberman.client;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EntryPoint implements com.google.gwt.core.client.EntryPoint {
    
    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        new GameCreator();
    }
}
