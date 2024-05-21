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
//        System.out.println(i + "-- i");
        if (i != null) {
            if (i.contains("1")) {
                makeAIGame(socket);
            } else {
                sockets.add(socket);
                listenings.add(new Listening(socket));
                new Thread(listenings.get(listenings.size() - 1)).start();
//                System.out.println("add socket");

                makeGame();
            }
        }
        else
        {
            //error has occurred,
            if (socket.isConnected())
            {
                socket.close();
            }
            Main.removeSocket(socket);

        }
    }

    static void makeGame()
    {
        if (sockets.size() >= 2)
        {
            GameData gameData = new GameData(sockets.get(0), sockets.get(1), 2, gameID++);
            gameData.setListening1(listenings.get(0));
            gameData.setListening2(listenings.get(1));
            GamesList.newGame(gameData);
            new Thread(gameData).start();
            gameData = null;
            for (int i = 0; i < 2; i++)
            {
                sockets.remove(0);
                listenings.remove(0);
            }
//            System.out.println("SocketSize " + sockets.size());
        }
    }
    static void makeAIGame(Socket socket)
    {
//            System.out.println("makeAIGame");
            GameData gameData = new GameData(socket, 1, gameID++);
            Listening temp = new Listening(socket);
            new Thread(temp).start();
            gameData.setListening1(temp);
            temp = null;
            GamesList.newGame(gameData);
            new Thread(gameData).start();
            gameData = null;
//            System.out.println("AI GAME STARTED ");
        }
        static void removeSocket(Socket socket, Listening listening)
        {
            sockets.remove(socket);
            listenings.remove(listening);
        }
    }

