package TicTacToe.Game;

import java.io.*;
import java.net.Socket;

public class Notify implements Runnable {
    Socket socket;
    Message message;
    GameData gameData;
    Game game;
    ObjectOutputStream objectOutputStream;
    ObjectOutputStream objectOutputStream2;

    Notify(Message message, GameData gameData) {
        this.gameData = gameData;
        this.message = message;
        OutputStream outputStream = null;
        this.objectOutputStream = gameData.objectOutputStream;
        this.objectOutputStream2 = gameData.objectOutputStream2;

    }

    Notify(Socket socket, Message message, Game game, ObjectOutputStream objectOutputStream) {
        this.socket = socket;
        this.message = message;
        this.game = game;
        this.objectOutputStream = objectOutputStream;
    }
    @Override
    public void run() {

        System.out.println("start of notify");

        try {

            if (message.message.equals("/invalid"))
            {
                System.out.println("Invalid Message");
                objectOutputStream.writeObject(message);

                objectOutputStream.writeObject(game);

                objectOutputStream.writeObject(new Message("/yourTurn"));

            }
            else if (message.message.equals("/valid"))
            {
                System.out.println("Valid Message1");
                boolean gameOver = gameData.game.isGameOver();
                {

                    objectOutputStream.writeObject(message);


                    System.out.println("buttons -- " + gameData.game.buttons());

                    System.out.println(gameData.game.toString());

                    objectOutputStream.writeObject((gameData.game));
                    System.out.println("Valid Message over2");


                    if (gameOver)
                    {
                        System.out.println("game over Message1");

                        objectOutputStream.writeObject(new Message("/gameOver"));
                    }
                }
                {
                    if (gameData.mode == 2)
                    {
                        System.out.println("Valid Message2");


                        objectOutputStream2.writeObject(message);

                        System.out.println("buttons -- " + gameData.game.buttons());
                        System.out.println(gameData.game.toString());
                        objectOutputStream2.writeObject((gameData.game));
                        System.out.println("Valid Message over2");


                        if (gameOver)
                        {
                            System.out.println("game over Message2");
                            objectOutputStream2.writeObject(new Message("/gameOver"));
                        }
                    }

                }

                swapTurns(gameOver);
            }
            else if(message.message.equals("newGame"))
            {
                System.out.println("New TicTacToe.Game Started");
                {
                    objectOutputStream.writeObject(new Message("/1"));

                    objectOutputStream.writeObject(gameData.game);
                    System.out.println("New TicTacToe.Game Started1");
                }
                if (gameData.mode == 2)
                {

                    objectOutputStream2.writeObject(new Message("/2"));

                    objectOutputStream2.writeObject(gameData.game);


                    System.out.println("New TicTacToe.Game Started2");
                }
                swapTurns(false);
            }
            else if(message.message.contains("/shutdown"))
            {
                System.out.println("Shutting down");
                System.out.println("shutdown");
                objectOutputStream.writeObject(message);
                System.out.println("shutdown signal sent");
            }
            else if (message.message.equals("/serverShutdown"))
            {
                System.out.println("Shutting down server");
                objectOutputStream.writeObject(message);
                if (gameData.mode == 2)
                {
                    objectOutputStream2.writeObject(message);
                }
            }
            else if (message.message.contains("/message"))
            {
                if (gameData.mode == 2)
                {
                    objectOutputStream.writeObject(message);
                    objectOutputStream2.writeObject(message);
                    System.out.println("message sent");
                }
            }
            else
            {}

            try {
                if (objectOutputStream != null)
                {
                    objectOutputStream.reset();

                }
                if (objectOutputStream2 != null && gameData.mode == 2)
                {
                    objectOutputStream2.reset();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void swapTurns(boolean gameover) throws IOException {
        System.out.println("swapTurns");
        System.out.println(gameData.game.whoGetsNextMove() + " who gets next move");
        if (gameover)
        {}
        else if (gameData.game.whoGetsNextMove() == 1)
        {

            System.out.println("swapTurns1");

            objectOutputStream.writeObject(new Message("/yourTurn"));

            if (gameData.mode == 2)
            {
                objectOutputStream2.writeObject(new Message("/notYourTurn"));
            }
        }
        else
        {

            System.out.println("swapTurns2");

            objectOutputStream.writeObject(new Message("/notYourTurn"));

            if (gameData.mode == 2) {
                objectOutputStream2.writeObject(new Message("/yourTurn"));
            }
        }
        System.out.println("swap turn end");
    }
}
