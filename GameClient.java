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

                if (serverMessage.startsWith("SUBMITNAME")) {
                    System.out.print("Podaj nazwe: ");
                    String username = consoleInput.nextLine();
                    out.println(username);
                } else if (serverMessage.startsWith("NAMEINUSE")) {
                    System.out.println("Nazwa w urzyciu");
                } else if (serverMessage.startsWith("NAMEACCEPTED")) {
                    System.out.println("Nazwa zaakceptowana");
                    System.out.println("Wyczekiwanie na rozpoczęcie rozgrywki");
                } else if (serverMessage.startsWith("MENU")) {
                    System.out.println(serverMessage.substring(5));
                    String choice = consoleInput.nextLine();
                    out.println(choice);
                } else if (serverMessage.startsWith("MESSAGE")) {
                    System.out.println(serverMessage.substring(8));
                } else if (serverMessage.startsWith("GAMESTART")) {
                    System.out.println("GRA SIĘ ZACZĘŁA");
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