package TicTacToe.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Main {
    public static boolean shutdown = false;
    public static ArrayList<Socket> sockets = new ArrayList();
    private static ServerSocket serverSocket;
    public static void main(String[] args) {

        try {
            serverSocket = new ServerSocket(80);
            Socket clientSocket;
            new Thread(new ServerCommands()).start();

            while (!shutdown)
            {
                sockets.add(serverSocket.accept());
                System.out.println("accepted");
                EstablishGame.addSocket(sockets.get(sockets.size() - 1));
            }



        } catch (IOException e) {
            if (e instanceof SocketException && e.getMessage().equals("Socket closed")) {
                System.out.println("Server socket was shut down.");
                // Handle the shutdown gracefully
            } else {
                System.out.println("Error while waiting for connection:");
                e.printStackTrace();
            }
        }
    }
    public static void removeSocket(Socket socket)
    {
        sockets.remove(socket);
    }
    public static void shutdown(String notice) {
        if (notice.equalsIgnoreCase("stop"))
        {
            System.out.println("Stop has been triggered. The server will now stop taking new requests");
            if (!serverSocket.isClosed())
            {
                shutdown = true;
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        else
        {
            System.out.println("Shutdown has been triggered. The server will now shutdown");

            try {
                // Close the server socket to stop accepting new connections
                if (!serverSocket.isClosed()) {
                    shutdown = true;
                    serverSocket.close();
                }
                int numberOfGamesToShutdown = GamesList.getNumberOfGames();
                for (int i = 0; i < numberOfGamesToShutdown; i++)
                {

                    GamesList.games.get(numberOfGamesToShutdown - 1 - i).serverShutdown();
                }
            } catch (IOException e) {
                // Handle the exception appropriately
                e.printStackTrace();
            }
        }


    }
}