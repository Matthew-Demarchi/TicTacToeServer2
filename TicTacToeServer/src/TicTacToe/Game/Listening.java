package TicTacToe.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Listening implements Runnable {

    GameData gameData = null;
    boolean shutdown = false;
    Socket socket = null;
    int player;
    String message;
//    public Listening(GameData gameData)
//    {
//        this.gameData = gameData;
//    }

public Listening(Socket socket)
    {
        this.socket = socket;
    }

    public void setSocket(Socket socket, int player) {
        this.socket = socket;
        this.player = player;
        System.out.println("Listening on Player" + player);
    }
    public void setGameData(GameData gameData)
    {
        this.gameData = gameData;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        while (!shutdown) // make sockets trade off by move
        {
            System.out.println("the socket is closed -- " + socket.isClosed());
            System.out.println("reading in data for Player " + player);
            message = in.readLine();
            System.out.println("read in data for Player " + player);

            if (gameData != null)
            {
                if (message == null) {
//                System.out.println("no message");
                    if (socket.isClosed()) {
                        shutdown();
                    }
                    continue;
                } else if (message.contains("/move")) {
                    turn(String.valueOf(message.charAt(message.length() - 1)));
                } else if (message.contains("/quit")) {
                    if (!gameData.isShutdown()) {
                        gameData.alert("/quit" + player);
                    }
                    shutdown = true;
                    Main.removeSocket(socket);
                } else if (message.contains("/difficulty")) {
                    gameData.alert(message);
                }
                else if (message.contains("/clearBoard"))
                {
                    System.out.println("clear board message received");
                    gameData.alert(message);
                }
                else if (message.contains("/switchSides"))
                {
                    System.out.println("switch sides message received");
                    gameData.alert(message);
                }
                else if (message.contains("/message"))
                {
                    System.out.println("chat message received");
                    gameData.alert(message);
                }
                else
                {continue;}
            }
            else
            {
                if (message.contains("/quit"))
                {
                    EstablishGame.removeSocket(socket, this);
                    Main.removeSocket(socket);
                    shutdown = true;
                }
            }


        }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally
        {
            System.out.println("Shutting down listener for player" + player);
        }
    }
    private void turn(String move) throws IOException
    {
        boolean valid = true;

            System.out.println("reading in for turn " + player);
            int buttonPressed = Integer.parseInt(move);
            System.out.println(buttonPressed + " -- buttonpressed");
            valid = gameData.game.buttonPressed(buttonPressed, player);
            System.out.println(valid);
            if (!valid)
            {
                new Thread(new Notify(socket, new Message("/invalid"), gameData.game, gameData.objectOutputStream)).start(); // FIX THIS---------------------------------------------------------------
            }
            else
            {
                System.out.println("alert for move");
                gameData.alert("/move");
            }
    }

    private void shutdown()
    {
        shutdown = true;
    }


}
