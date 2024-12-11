package sem3tp.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {

    private static final String SERVER_ADDRESS = "localhost"; 
    private static final int SERVER_PORT = 1989; 
    private Scanner in;
    private PrintWriter out;
    private Scanner consoleInput;

    public void run() throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            consoleInput = new Scanner(System.in);

            System.out.println("Połączono się z serwerem");

            while (in.hasNextLine()) {
                String serverMessage = in.nextLine();

                if (serverMessage.startsWith("INPUT")) {
                    System.out.println(serverMessage.substring(6));
                    String input = consoleInput.nextLine();
                    out.println(input);
                }
                else {
                    System.out.println(serverMessage);
                }
            }
        } finally {
            System.out.println("NAURAAA");
        }
    }

    public static void main(String[] args) {
        try {
            GameClient client = new GameClient();
            client.run();
        } catch (IOException e) {
            System.err.println("Nie udało się połączyć z serwerem, upewnij się, ze działa yś");
            e.printStackTrace();
        }
    }
}