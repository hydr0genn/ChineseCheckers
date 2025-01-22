package sem3tp.Server;

import sem3tp.Board.Board;
import sem3tp.Creator.Creator;
import sem3tp.GUI.FXPole;
import sem3tp.Game;
import sem3tp.GameState;
import sem3tp.Mover.Mover;
import sem3tp.Mover.Variants;
import sem3tp.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class GameServer {
    private static final List<ObjectOutputStream> writers = new ArrayList<>();
    private static Map<Integer, Game> gamesOn = new HashMap<>();
    private static int next_id = 1;
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


    public GameServer(int maxGamesSimultaneously){
        gamePool=Executors.newFixedThreadPool(maxGamesSimultaneously);
    }


    public static void main(String[] args) throws Exception {

        System.out.println("Server is working\n");
        var pool = Executors.newFixedThreadPool(500);

        try (var listener = new ServerSocket(1989)) {
            while (true) {
                    pool.execute(new Handler(listener.accept(),next_id));
                }
            }
        }

    private static class Handler implements Runnable {
        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;
        private Player player;
        private Game currentGamePlayed;

        public Handler(Socket socket, int id) {
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
                    if(receivedMessage.equals("CSNDMOVE")) {
                        System.out.println("Move on the server");
                        FXPole source = (FXPole) in.readObject();
                        FXPole destination = (FXPole) in.readObject();
                        broadcastMoveToGame(currentGamePlayed, source, destination);
                    }
                    if (receivedMessage.equals("CHNGTURN")){
                        broadcastMessageToGame(currentGamePlayed, "CHNGTURN");
                        currentGamePlayed.nextTurn();
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

        private static synchronized Game createNewGame(Game currentGame,int playNumber, ObjectOutputStream out, Variants variant) throws IOException {
                Creator creator = Creator.getInstance();
                Board board = creator.createBoardBuilder(5, playNumber).build();
                Game game = creator.createGame(board, next_id, playNumber);
                game.setVariant(variant);
                gamesOn.put(next_id, game);
                next_id++;
                currentGame=game;
                out.writeObject("CRTDGAME");
                out.writeObject(game);
                return game;
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
