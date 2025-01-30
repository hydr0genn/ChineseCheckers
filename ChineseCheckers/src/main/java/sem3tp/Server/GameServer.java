package sem3tp.Server;

import org.springframework.beans.factory.annotation.Autowired;
import sem3tp.Board.Board;
import sem3tp.Creator.Creator;
import sem3tp.Database.GameDB;
import sem3tp.Database.JsonUtils;
import sem3tp.GUI.FXPole;
import sem3tp.Game;
import sem3tp.GameState;
import sem3tp.Mover.Mover;
import sem3tp.Mover.Variants;
import sem3tp.Player;
import sem3tp.Poles.Pole;

import javax.validation.constraints.Null;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class GameServer {
    private static final List<ObjectOutputStream> writers = new ArrayList<>();
    private static Map<Integer, Game> gamesOn = new HashMap<>();
    private static int next_id = getMaxGameID() + 1;
    private static ExecutorService gamePool;

    public static final Map<Player, ObjectOutputStream> playerWriterMap = new ConcurrentHashMap<>();
    public static synchronized void registerPlayerWriter(Player player, ObjectOutputStream writer) {
        playerWriterMap.put(player, writer);
    }
    public static synchronized void unregisterPlayerWriter(Player player, ObjectOutputStream writer) {
        playerWriterMap.remove(player, writer);
    }

    private static void move(FXPole source, FXPole destination, Game game){
        Mover mover = Mover.getInstance();
        mover.move(source, destination, game);
    }

    private static void sendMove(String prefix,Object source, Object destination, Game game, ObjectOutputStream out) throws IOException {
        out.writeObject(prefix);
        out.writeObject(source);
        out.writeObject(destination);
        out.flush();
        move((FXPole) source,(FXPole) destination, game);
    }

    public static synchronized void broadcastMoveToGame(Game game, Object source, Object destination) throws IOException {

        for (Player player : game.getPlayersList().getAll()) {
            ObjectOutputStream playerWriter = playerWriterMap.get(player);
            if (playerWriter != null) {
                sendMove("SSNDMOVE", source, destination, game, playerWriter);
            }
        }
    }

    public static synchronized void broadcastMessageToGame(Game game, String message) throws IOException {
        for (Player player : game.getPlayersList().getAll()) {
            ObjectOutputStream playerWriter = playerWriterMap.get(player);
            if (playerWriter != null) {
                playerWriter.writeObject(message);
            }
        }
    }

    public static synchronized void broadcastObjecteToGame(Game game, String message, Object object) throws IOException {
        for (Player player : game.getPlayersList().getAll()) {
            ObjectOutputStream playerWriter = playerWriterMap.get(player);
            if (playerWriter != null) {
                playerWriter.writeObject(message);
                playerWriter.writeObject(object);
            }
        }
    }



    private static final String URL = "jdbc:mysql://localhost:3306/ChineseCheckers";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void saveMove(int gameID, String actionType, int initialX, int initialY, int initialZ, int finalX, int finalY, int finalZ) {
        String sql = "INSERT INTO Actions (GameID, ActionType, InitialX, InitialY, InitialZ, FinalX, FinalY, FinalZ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, gameID);
            pstmt.setString(2, actionType);
            pstmt.setInt(3, initialX);
            pstmt.setInt(4, initialY);
            pstmt.setInt(5, initialZ);
            pstmt.setInt(6, finalX);
            pstmt.setInt(7, finalY);
            pstmt.setInt(8, finalZ);

            pstmt.executeUpdate();
            System.out.println("Udało się zapisać");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveGame(int numberOfPlayers, int gameVariant, int numberOfBots) {
        String sql = "INSERT INTO Games (NumberOfPlayers, GameVariant, NumberOfBots) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, numberOfPlayers);
            pstmt.setInt(2, gameVariant);
            pstmt.setInt(3, numberOfBots);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int gameID = generatedKeys.getInt(1);
                        System.out.println("Udało się zapisać gre o id: " + gameID);
                    } else {
                        System.out.println("ni ma id");
                    }
                }
            } else {
                System.out.println("nie zapisano");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int NumberOfPlayers(int gameID) {
        String sql = "SELECT NumberOfPlayers FROM Games WHERE GameID = ?";
        int numberOfPlayers = 0;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, gameID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    numberOfPlayers = rs.getInt("NumberOfPlayers");
                } else {
                    System.out.println("nie ma gry o id " + gameID);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return numberOfPlayers;
    }

    public static boolean doesGameExist(int gameID) {
        String sql = "SELECT 1 FROM Games WHERE GameID = ?";
        boolean exists = false;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, gameID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    exists = true;
                } else {
                    System.out.println("nie ma gry o id " + gameID);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exists;
    }

    public static int getMaxGameID() {
        String sql = "SELECT MAX(GameID) AS MaxGameID FROM Games";
        int maxGameID = -1;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                maxGameID = rs.getInt("MaxGameID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return maxGameID;
    }





    public GameServer(int maxGamesSimultaneously){
        gamePool=Executors.newFixedThreadPool(maxGamesSimultaneously);
    }


    public static void main(String[] args) throws Exception {

        System.out.println("Server is working\n");
        var pool = Executors.newFixedThreadPool(500);

        try (var listener = new ServerSocket(1989)) {
            while (true) {
                pool.execute(new Handler(listener.accept()));
            }
        }
    }

    private static class Handler implements Runnable {
        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;
        private Player player;
        private Game currentGamePlayed;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run(){
            try {
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());

                synchronized (writers) {
                    writers.add(out);
                }
                while(true){
                    String receivedMessage = (String)in.readObject();
                    if(receivedMessage.startsWith("LOGINXXX")){
                        String username = receivedMessage.substring(8);
                        loginAsUser(username);
                        System.out.println("witamy" + receivedMessage.substring(8));
                    }
                    if (receivedMessage.startsWith("JOINGAME")) {
                        String number = receivedMessage.substring(8);
                        int numberOfGame = Integer.parseInt(number);
                        this.currentGamePlayed=joinGame(numberOfGame, out, player);
                    }
                    if(receivedMessage.startsWith("CRTGAMV1")) {
                        String number = receivedMessage.substring(8);
                        int numberOfPlayers = Integer.parseInt(number);
                        this.currentGamePlayed=createNewGame(currentGamePlayed, numberOfPlayers, out, Variants.OneJump);
                    }
                    if(receivedMessage.startsWith("CRTGAMV2")) {
                        String number = receivedMessage.substring(8);
                        int numberOfPlayers = Integer.parseInt(number);
                        this.currentGamePlayed=createNewGame(currentGamePlayed, numberOfPlayers, out, Variants.TwoJumps);
                    }
                    if(receivedMessage.startsWith("LOADGAME")) {
                        String index = receivedMessage.substring(8);
                        int gameID = Integer.parseInt(index);
                        this.currentGamePlayed = loadGame(gameID);
                    }
                    if(receivedMessage.equals("CSNDMOVE")) {
                        System.out.println("Move on the server");
                        FXPole source = (FXPole) in.readObject();
                        FXPole destination = (FXPole) in.readObject();
                        broadcastMoveToGame(currentGamePlayed, source, destination);
                        saveMove(currentGamePlayed.id, "move", source.getPole().getxCord(), source.getPole().getyCord(), source.getPole().getzCord(), destination.getPole().getxCord(), destination.getPole().getyCord(), destination.getPole().getzCord());
                    }
                    if (receivedMessage.equals("CHNGTURN")){
                        broadcastMessageToGame(currentGamePlayed, "CHNGTURN");
                        currentGamePlayed.nextTurn();
                        saveMove(currentGamePlayed.id, "next_turn", 0,0,0,0,0,0);
                    }
                    if(receivedMessage.equals("GETGAMES")){
                        out.writeObject("SNDGAMES");
                        HashSet<Integer> keySet = new HashSet<>(gamesOn.keySet());
                        out.writeObject(keySet);
                    }
                    if (receivedMessage.equals("NOTREADY")){
                        Player changedPlayer = (Player) in.readObject();
                        changedPlayer = currentGamePlayed.getPlayersList().get(changedPlayer);
                        changedPlayer.setReady(false);
                    }
                    if(receivedMessage.equals("READYXXX")){
                        Player changedPlayer = (Player) in.readObject();
                        changedPlayer = currentGamePlayed.getPlayersList().get(changedPlayer);
                        changedPlayer.setReady(true);
                        if(currentGamePlayed.checkReadiness()){
                            currentGamePlayed.turnOn();
                            currentGamePlayed.setFirstPlayer();
                            broadcastMessageToGame(currentGamePlayed,"TURNONXX");
                            if(doesGameExist(currentGamePlayed.id)){
                                loadBoard(currentGamePlayed.id, currentGamePlayed);
                            }
                        }
                    }
                }

            } catch (Exception e) {
                System.out.println(e);
            } finally {
                unregisterPlayerWriter(player, out);
                quit(this.player.getUsername(), out, socket);
            }
        }

        private void loginAsUser(String username) throws IOException {
            synchronized (GameState.getInstance()) {
                if (!username.isBlank()) {
                    player = new Player(username);
                    out.writeObject("SNDPLAYR");
                    out.writeObject(player);
                } else {
                    out.writeObject("ERROR USERNAME");
                }
            }
        }
    }

    private static synchronized Game createNewGame(Game currentGame,int playNumber, ObjectOutputStream out, Variants variant) throws Exception {
        Creator creator = Creator.getInstance();
        Board board = creator.createBoardBuilder(5, playNumber).build();
        Game game = creator.createGame(board, next_id, playNumber);
        game.setVariant(variant);
        gamesOn.put(next_id, game);
        next_id++;
        currentGame=game;
        out.writeObject("CRTDGAME");
        out.writeObject(game);

        saveGame(playNumber, 1, 0);
        return game;
    }



    private static synchronized Game loadGame(int index) throws Exception {
        Creator creator = Creator.getInstance();
        Board board = creator.createBoardBuilder(5, NumberOfPlayers(index)).build();
        Game game = creator.createGame(board, index, NumberOfPlayers(index));
        game.setVariant(Variants.OneJump);
        gamesOn.put(index, game);
        return game;
    }

    private static synchronized void loadBoard(int index, Game currentGame) throws Exception {
        String sql = "SELECT MoveID, ActionType, InitialX, InitialY, InitialZ, FinalX, FinalY, FinalZ " +
                "FROM Actions WHERE GameID = ? ORDER BY MoveID ASC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, index);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String actionType = rs.getString("ActionType");

                    if ("move".equals(actionType)) {
                        int initialX = rs.getInt("InitialX");
                        int initialY = rs.getInt("InitialY");
                        int initialZ = rs.getInt("InitialZ");
                        int finalX = rs.getInt("FinalX");
                        int finalY = rs.getInt("FinalY");
                        int finalZ = rs.getInt("FinalZ");

                        FXPole init = null;
                        FXPole destination = null;

                        for (FXPole fxPole : currentGame.getBoard().getAllFXPoles().getAll()) {
                            if (fxPole.getPole().getxCord() == initialX &&
                                    fxPole.getPole().getyCord() == initialY &&
                                    fxPole.getPole().getzCord() == initialZ) {
                                init = fxPole;
                            }
                            if (fxPole.getPole().getxCord() == finalX &&
                                    fxPole.getPole().getyCord() == finalY &&
                                    fxPole.getPole().getzCord() == finalZ) {
                                destination = fxPole;
                            }
                        }

                        broadcastMoveToGame(currentGame, init, destination);
                    }

                    if ("next_turn".equals(actionType)) {
                        broadcastMessageToGame(currentGame, "CHNGTURN");
                        currentGame.nextTurn();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Nie udało się wczytać gry o id: " + index, e);
        }
    }


    private static synchronized Game joinGame(int id, ObjectOutputStream out, Player player) throws IOException {//wypisac wszystkie ktore sa w hashmapie
        Game game = gamesOn.get(id);
        out.writeObject("oJOINGME");
        out.writeObject(game);
        game.addNewPlayer(player);
        registerPlayerWriter(player, out);
        System.out.println(game.getPlayersList().getByIndex(game.getPlayersList().getSize()-1));
        broadcastObjecteToGame(game, "PLYRJIND", player);
        return game;
    }

    private static void quit(String username, ObjectOutputStream out, Socket socket) {
        if (username != null) {
            System.out.println(username + " NAURAAAA");
            GameState.getInstance().removeClient(username);
            synchronized (writers) {
                writers.remove(out);
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
