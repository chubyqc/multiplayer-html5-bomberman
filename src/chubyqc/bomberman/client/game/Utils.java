package chubyqc.bomberman.client.game;

public class Utils {
    private static Utils _instance = new Utils();
    static Utils get() {
        return _instance;
    }
    
    private Utils() {}
}
