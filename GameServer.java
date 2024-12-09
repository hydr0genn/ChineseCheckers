import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class GameServer {
    private static final List<PrintWriter> writers = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        Scanner userInput = new Scanner(System.in);

        System.out.println("Wprowadz liczbe graczy [2, 3, 4, 6]: ");
        int maxClients;
        while (true) {
            try {
                maxClients = Integer.parseInt(userInput.nextLine());
                if (GameState.getInstance().setMaxClients(maxClients)) {
                    break;
                } else {
                    System.out.println("Podaj liczbe ze zbioru: [2, 3, 4, 6]: ");
                }
            } catch (NumberFormatException e) {
                System.out.println("źle");
            }
        }

        System.out.println("Serwer gry działa, liczba graczy: " + maxClients);
        var pool = Executors.newFixedThreadPool(500);

        try (var listener = new ServerSocket(1989)) {
            while (true) {
                if (GameState.getInstance().getClientCount() < maxClients) {
                    pool.execute(new Handler(listener.accept()));
                } else {
                    System.out.println("Ni ma miejsca");
                }
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

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);

                synchronized (writers) {
                    writers.add(out);
                }

                while (true) {
                    out.println("SUBMITNAME");
                    username = in.nextLine();
                    if (username == null) {
                        return;
                    }
                    synchronized (GameState.getInstance()) {
                        if (!username.isBlank() && GameState.getInstance().addClient(username)) {
                            break;
                        } else {
                            out.println("NAMEINUSE");
                        }
                    }
                }

                out.println("NAMEACCEPTED " + username);
                broadcast("MESSAGE " + username + " dołączył");

                while (true) {
                    if (GameState.getInstance().isGameStarted()) {
                        out.println("MENU 1. ruch pionkiem 2. wyjście");
                    } else {
                        out.println("MENU 1. ready 2. nieready 3. wyjście");
                    }

                    String input = in.nextLine();
                    if (GameState.getInstance().isGameStarted()) {
                        if (input.equals("1")) {
                            out.println("MESSAGE IMPLEMENTACJA LOGIKI RUCHU W DRODZE");
                        } else if (input.equals("2")) {
                            quit();
                            break;
                        }
                    } else {
                        if (input.equals("1")) {
                            GameState.getInstance().setClientReady(username, true);
                            out.println("MESSAGE Jesteś ready.");
                        } else if (input.equals("2")) {
                            GameState.getInstance().setClientReady(username, false);
                            out.println("MESSAGE Nie jesteś ready.");
                        } else if (input.equals("3")) {
                            quit();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                quit();
            }
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
