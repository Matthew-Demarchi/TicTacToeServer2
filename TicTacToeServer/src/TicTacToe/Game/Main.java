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
            EstablishGame establishGame = new EstablishGame();

            while (!shutdown)
            {
                sockets.add(serverSocket.accept());
                System.out.println("accepted");
                establishGame.addSocket(sockets.get(sockets.size() - 1));
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
}