package TicTacToe.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class EstablishGame
{
    public static ArrayList<Socket> sockets = new ArrayList();
    long gameID = 0;

    void addSocket(Socket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String i = reader.readLine();
        System.out.println(i + "-- i");
        if (i.contains("1"))
        {
            makeAIGame(socket);
        }
        else {
            sockets.add(socket);
            System.out.println("add socket");

            makeGame();
        }
    }

    void makeGame()
    {
        if (sockets.size() >= 2)
        {
            GameData gameData = new GameData(sockets.get(0), sockets.get(1), 2, gameID++);
            gameData.setListening1(new Listening(gameData));
            gameData.setListening2(new Listening(gameData));
            Controller.newGame(gameData);
            new Thread(gameData).start();
            gameData = null;
            sockets.clear();
            System.out.println("SocketSize " + sockets.size());
        }
    }
    void makeAIGame(Socket socket)
    {

            GameData gameData = new GameData(socket, 1, gameID++);
            gameData.setListening1(new Listening(gameData));
            Controller.newGame(gameData);
            new Thread(gameData).start();
            gameData = null;
            System.out.println("AI GAME STARTED ");
        }
    }

