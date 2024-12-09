import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameState {
    private static final GameState INSTANCE = new GameState();
    private final Map<String, ClientInfo> clients = new ConcurrentHashMap<>();
    private boolean gameStarted;
    private int maxClients;

    private GameState() {
        gameStarted = false;
    }

    public static GameState getInstance() {
        return INSTANCE;
    }

    public synchronized boolean setMaxClients(int max) {
        if (Arrays.asList(2, 3, 4, 6).contains(max)) {
            this.maxClients = max;
            return true;
        }
        return false;
    }

    public synchronized int getMaxClients() {
        return maxClients;
    }

    public synchronized int getClientCount() {
        return clients.size();
    }

    public synchronized boolean addClient(String username) {
        if (!clients.containsKey(username) && clients.size() < maxClients) {
            clients.put(username, new ClientInfo(username));
            return true;
        }
        return false;
    }

    public synchronized void removeClient(String username) {
        clients.remove(username);
    }

    public synchronized void setClientReady(String username, boolean ready) {
        if (clients.containsKey(username)) {
            clients.get(username).setReady(ready);
            checkGameStart();
        }
    }

    private synchronized void checkGameStart() {
        if (clients.size() == maxClients &&
            clients.values().stream().allMatch(ClientInfo::isReady)) {
            startGame();
        }
    }

    private synchronized void startGame() {
        if (!gameStarted) {
            gameStarted = true;
            System.out.println("Gra wystartowała!");
            GameServer.broadcast("GAMESTART");
        }
    }

    public synchronized boolean isGameStarted() {
        return gameStarted;
    }

    public static class ClientInfo {
        private final String username;
        private boolean ready;

        public ClientInfo(String username) {
            this.username = username;
            this.ready = false;
        }

        public String getUsername() {
            return username;
        }

        public boolean isReady() {
            return ready;
        }

        public void setReady(boolean ready) {
            this.ready = ready;
        }
    }
}
