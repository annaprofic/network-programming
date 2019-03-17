import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 2019;
    private static void runServer() {
        System.out.println("Connecting...");
        try {
            ServerSocket socket = new ServerSocket(PORT);

            System.out.println("Server is up.");
            Socket connection;

            while (true) {
                connection = socket.accept();

                Threads serverThread = new Threads(connection);

                Thread thread = new Thread(serverThread);

                thread.start();
            }
        } catch (IOException ignored) {
        }
    }

    public static void main(String[] args) {
        runServer();
    }
}
