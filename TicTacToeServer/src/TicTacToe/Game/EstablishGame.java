package TicTacToe.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class EstablishGame
{
    public static ArrayList<Socket> sockets = new ArrayList();
    static long gameID = 0;
    static ArrayList<Listening> listenings = new ArrayList();

    static void addSocket(Socket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String i = reader.readLine();
        System.out.println(i + "-- i");
        if (i.contains("1"))
        {
            makeAIGame(socket);
        }
        else {
            sockets.add(socket);
            listenings.add(new Listening(socket));
            new Thread(listenings.get(listenings.size()-1)).start();
            System.out.println("add socket");

            makeGame();
        }
    }

    static void makeGame()
    {
        if (sockets.size() >= 2)
        {
            GameData gameData = new GameData(sockets.get(0), sockets.get(1), 2, gameID++);
            gameData.setListening1(listenings.get(0));
            gameData.setListening2(listenings.get(1));
            Controller.newGame(gameData);
            new Thread(gameData).start();
            gameData = null;
            sockets.clear();
            listenings.clear();
            System.out.println("SocketSize " + sockets.size());
        }
    }
    static void makeAIGame(Socket socket)
    {

            GameData gameData = new GameData(socket, 1, gameID++);
            gameData.setListening1(new Listening(socket));
            Controller.newGame(gameData);
            new Thread(gameData).start();
            gameData = null;
            System.out.println("AI GAME STARTED ");
        }
        static void removeSocket(Socket socket, Listening listening)
        {
            sockets.remove(socket);
            listenings.remove(listening);
        }
    }

