package sem3tp.Server;

import sem3tp.Creator.Creator;
import sem3tp.Game;
import sem3tp.GameState;
import sem3tp.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer {
    private static final List<PrintWriter> writers = new ArrayList<>();
    private static Map<Integer, Game> gamesOn = new HashMap<>();
    private static int next_id = 1;
    private static ExecutorService gamePool;

    public GameServer(int maxGamesSimultaneously){
        gamePool=Executors.newFixedThreadPool(maxGamesSimultaneously);
    }


    public static void main(String[] args) throws Exception {
        Scanner userInput = new Scanner(System.in);

//        System.out.println("Wprowadz liczbe graczy [2, 3, 4, 6]: ");
//        int maxClients;
//        while (true) {
//            try {
//                maxClients = Integer.parseInt(userInput.nextLine());
//                if (GameState.getInstance().setMaxClients(maxClients)) {
//                    break;
//                } else {
//                    System.out.println("Podaj liczbe ze zbioru: [2, 3, 4, 6]: ");
//                }
//            } catch (NumberFormatException e) {
//                System.out.println("źle");
//            }
//        }

        System.out.println("Server is working\n");
        var pool = Executors.newFixedThreadPool(500);

        try (var listener = new ServerSocket(1989)) {
            while (true) {
                    pool.execute(new Handler(listener.accept(),next_id));
                }
            }
        }


    public static synchronized void broadcast(String message) {
        for (PrintWriter writer : writers) {
            writer.println(message);
            writer.flush();
        }
    }

    private static class Handler implements Runnable {
        private String username;
        private Socket socket;
        private Scanner in;
        private PrintWriter out;
        private Player player;
//        private int next_id;
        Creator creator = Creator.getInstance();

        public Handler(Socket socket, int id) {
            this.socket = socket;
        }


        public void run() {
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);

                synchronized (writers) {
                    writers.add(out);
                }
                loginAsUser();

                out.println("NAMEACCEPTED " + username);
                broadcast("MESSAGE " + username + " dołączył");

                gameCreateOptions();


//                while (true) {
//                    if (GameState.getInstance().isGameStarted()) {
//                        out.println("MENU 1. ruch pionkiem 2. wyjście");
//                    } else {
//                        out.println("MENU 1. ready 2. nieready 3. wyjście");
//                    }
//
//                    String input = in.nextLine();
//                    if (GameState.getInstance().isGameStarted()) {
//                        if (input.equals("1")) {
//                            out.println("MESSAGE IMPLEMENTACJA LOGIKI RUCHU W DRODZE");
//                        } else if (input.equals("2")) {
//                            quit();
//                            break;
//                        }
//                    } else {
//                        if (input.equals("1")) {
//                            GameState.getInstance().setClientReady(username, true);
//                            out.println("MESSAGE Jesteś ready.");
//                        } else if (input.equals("2")) {
//                            GameState.getInstance().setClientReady(username, false);
//                            out.println("MESSAGE Nie jesteś ready.");
//                        } else if (input.equals("3")) {
//                            quit();
//                            break;
//                        }
//                    }
//                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                quit();
            }
        }

        private void loginAsUser(){
            while (true) {
                out.println("SUBMITNAME");
                username = in.nextLine();
                if (username == null) {
                    return;
                }
                synchronized (GameState.getInstance()) {
                    if (!username.isBlank()) {
                        player = new Player(username);
                        break;
                    } else {
                        out.println("NAMEINUSE");
                    }
                }
            }
        }


        private void gameCreateOptions(){
            while(true){
                out.println("Wybierz co chcesz zrobic: 1. Utworz nowa gre 2. Wczytaj gre 3. Dolacz do gry");
                String input = in.nextLine();
                if(input.equals("1")){
                    createNewGame();
                    break;
                } else if (input.equals("2")) {
                    loadGame();
                    break;
                } else if (input.equals("3")) {
                    joinGame();
                    break;
                }
                out.println("Podaj liczbe w zakresie 1-3");//albo throw excpetion
            }
        }

        private synchronized void createNewGame(){
                Game game = creator.createGame(next_id);
                gamesOn.put(next_id, game);
                next_id++;
                setNumberOfPlayers(game);
                game.turnOn();
                gamePool.execute(game);
        }

        private synchronized void setNumberOfPlayers(Game game){
            while(true){
                out.println("Podaj liczbe graczy w grze - 2, 3, 4 , 6");
                String input = in.nextLine();
                int number = Integer.parseInt(input);
                if(Arrays.asList(2,3,4,6).contains(number)) {
                    game.setPlayers_num(number);
                }else {
                    out.println("Liczba ma byc w zakresie 2, 3, 4, 6");//albo throw
                }
            }
        }

        private synchronized void printAvailableGames(){
            out.print("Oto lista dostepnych rozgrywek");
            gamesOn.forEach((key,value)->{
                out.print("Gra o id: "+key);
            });
        }

        private void joinGame(){//wypisac wszystkie ktore sa w hashmapie
            printAvailableGames();
            String input = in.nextLine();
            int selectedId = Integer.parseInt(input);
            Game game = gamesOn.get(selectedId);
            game.addNewPlayer(player);
            //polaczyc tak
        }

        private void loadGame(){
//pobieramy boarda z bazy danych i tworzymy z nim nowa gre
        }

        private void sendReadyRequest(){//logika wysylania ready

        }

        private void quit() {
            if (username != null) {
                System.out.println(username + " NAURAAAA");
                GameState.getInstance().removeClient(username);
                synchronized (writers) {
                    writers.remove(out);
                }
                broadcast("MESSAGE " + username + " wyszedł");
            }
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
