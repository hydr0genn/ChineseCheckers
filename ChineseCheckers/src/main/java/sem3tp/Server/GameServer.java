package sem3tp.Server;

import sem3tp.Board.Board;
import sem3tp.Creator.Creator;
import sem3tp.GUI.FXPole;
import sem3tp.Game;
import sem3tp.GameState;
import sem3tp.Mover.Directions;
import sem3tp.Mover.Mover;
import sem3tp.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
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
        Scanner userInput = new Scanner(System.in);

        System.out.println("Server is working\n");
        var pool = Executors.newFixedThreadPool(500);

        try (var listener = new ServerSocket(1989)) {
            while (true) {
                    pool.execute(new Handler(listener.accept(),next_id));
                }
            }
        }

//    public static synchronized void broadcast(String message) {
//        for (ObjectOutputStream writer : writers) {
//            writer.println(message);
//            writer.flush();
//        }
//    }

    private static class Handler implements Runnable {
        private String username;
        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;
        private Player player;
        private Game currentGamePlayed;
//        private int next_id;
        Creator creator = Creator.getInstance();

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
//TODO add movement logic + sending currentplayer to client
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
                        this.currentGamePlayed=createNewGame(currentGamePlayed, numberOfPlayers,player, out);
                    }
                    if(receivedMessage.startsWith("CRTGAMV2")) {
                        String number = receivedMessage.substring(8);
                        int numberOfPlayers = Integer.parseInt(number);
                        this.currentGamePlayed=createNewGame(currentGamePlayed, numberOfPlayers,player, out);
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


//        public void run() {
//            try {
//                in = new ObjectInputStream(socket.getInputStream());
//                out = new ObjectOutputStream(socket.getOutputStream());
//
//                synchronized (writers) {
//                    writers.add(out);
//                }
//                loginAsUser();
//
//                registerPlayerWriter(player, out);
//
//                out.println("Nazwa zaakceptowana " + username);
//                broadcast(username + " dołączył");
//
//                currentGamePlayed=gameCreateOptions();
//                waitForGameToStart();
//                gameLogic();
//
//            } catch (Exception e) {
//                System.out.println(e);
//            } finally {
//                unregisterPlayerWriter(player, out);
//                quit();
//            }
//        }

//        private void gameLogic(){
//            while(!currentGamePlayed.hasEnded()){
//                if(player!=currentGamePlayed.getCurrentPlayer()){
//                    out.println("To nie jest twoja tura");
//                    try{
//                        Thread.sleep(1000);
//                    } catch (Exception e) {
//                        Thread.currentThread().interrupt();
//                    }
//                    continue;
//                }
//
//                out.println("Twoja tura");
//                out.println("INPUT Mozesz ruszyc sie w nastepujacych kierunkach: 1. E 2. W 3. NE 4. NW 5. SE 6. SW");
//                String input = in.nextLine();
//                Mover mover = Creator.getInstance().createMover();
//                Directions direction;
//                try {
//                    direction = mover.setDirection(input);
//                    currentGamePlayed.processMoves(direction);
//                    broadcastToGame(currentGamePlayed,"Gracz o kolorze x wykonal ruch:" +input);//tutaj bedzie tez kolor
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//        private void waitForGameToStart() throws InterruptedException {
//            while (!currentGamePlayed.isOn()){
//                out.println("Twoj status to: "+player.getReady());
//                out.println("INPUT Czy jestes gotowy? odpowiedz: Tak lub Nie");
//                String input = in.nextLine();
//                if(input.equals("Tak")){
//                    this.player.setReady(true);
//                    currentGamePlayed.checkReadiness();
//                    while(!currentGamePlayed.isOn()){
//                        Thread.sleep(10);
//                    }
//                } else if (input.equals("Nie")) {
//                    this.player.setReady(false);
//                }
//            }
//            out.println("Gra sie zaczyna");
//        }

        private void loginAsUser(String username) throws IOException {
                synchronized (GameState.getInstance()) {
                    if (!username.isBlank()) { //nie moze byc takie samo
                        player = new Player(username);
                        out.writeObject("SNDPLAYR");
                        out.writeObject(player);
                    } else {
                        out.writeObject("ERROR USERNAME");
                    }
                }
            }
        }


//        private synchronized Game gameCreateOptions(){
//            while(true){
//                out.println("INPUT Wybierz co chcesz zrobic: 1. Utworz nowa gre 2. Wczytaj gre 3. Dolacz do gry");
//                String input = in.nextLine();
//                if(input.equals("1")){
//                    return createNewGame();
//                } else if (input.equals("2")) {
//                    return loadGame();
//                } else if (input.equals("3")) {
//                    return joinGame();
//                }
//                out.println("Podaj liczbe w zakresie 1-3");//albo throw excpetion
//            }
//        }

        private static synchronized Game createNewGame(Game currentGame,int playNumber, Player player, ObjectOutputStream out) throws IOException {
                Creator creator = Creator.getInstance();
                Board board = creator.createBoardBuilder(5, playNumber).build();
                Game game = creator.createGame(board, next_id, playNumber);
                gamesOn.put(next_id, game);
                next_id++;
                currentGame=game;
                out.writeObject("CRTDGAME");
                out.writeObject(game);
                return game;
        }

//        private synchronized void setNumberOfPlayers(Game game){
//            while(game.getPlayersNumber() < 2){
//                out.println("INPUT Podaj liczbe graczy w grze - 2, 3, 4 , 6");
//                String input = in.nextLine();
//                int number = Integer.parseInt(input);
//                if(Arrays.asList(2,3,4,6).contains(number)) {
//                    game.setPlayers_num(number);
//                }else {
//                    out.println("Liczba ma byc w zakresie 2, 3, 4, 6");//albo throw
//                }
//            }
//        }
//
//        private void printAvailableGames(){
//            out.println("Oto lista dostepnych rozgrywek");
//            gamesOn.forEach((key,value)->{
//                out.println("Gra o id: "+key);
//            });
//        }

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

        private synchronized Game loadGame(){
//pobieramy boarda z bazy danych i tworzymy z nim nowa gre
            return new Game(next_id);
        }

        private void sendReadyRequest(){//logika wysylania ready

        }

        private static void quit(String username, ObjectOutputStream out, Socket socket) {
            if (username != null) {
                System.out.println(username + " NAURAAAA");
                GameState.getInstance().removeClient(username);
                synchronized (writers) {
                    writers.remove(out);
                }
//                broadcast(username + " wyszedł");
            }
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
