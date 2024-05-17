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

        try {

            if (message.message.equals("/invalid"))
            {
                //send the updated board and have them remake their turn
                objectOutputStream.writeObject(message);
                objectOutputStream.writeObject(game);
                objectOutputStream.writeObject(new Message("/yourTurn"));
            }
            else if (message.message.equals("/valid"))
            {
                //update the board and check if game is over
                boolean gameOver = gameData.game.isGameOver();
                {
                    objectOutputStream.writeObject(message);
                    objectOutputStream.writeObject((gameData.game));

                    if (gameOver)
                    {
                        objectOutputStream.writeObject(new Message("/gameOver"));
                    }
                }
                {
                    if (gameData.mode == 2)
                    {
                        objectOutputStream2.writeObject(message);
                        objectOutputStream2.writeObject((gameData.game));

                        if (gameOver)
                        {
                            objectOutputStream2.writeObject(new Message("/gameOver"));
                        }
                    }

                }
                swapTurns(gameOver);
            }
            else if(message.message.equals("newGame"))
            {
                //give the players their IDs and set who goes first
                {
                    objectOutputStream.writeObject(new Message("/1"));

                    objectOutputStream.writeObject(gameData.game);
                }
                if (gameData.mode == 2)
                {

                    objectOutputStream2.writeObject(new Message("/2"));

                    objectOutputStream2.writeObject(gameData.game);
                }
                swapTurns(false);
            }
            else if(message.message.contains("/shutdown"))
            {
                objectOutputStream.writeObject(message);
            }
            else if (message.message.equals("/serverShutdown"))
            {
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
                }
            }
            else
            {
                System.out.println("Invalid Message -- " + message.message + " from game: " + gameData.game.gameID);
            }

            try { // reset the output streams to avoid errors
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

    private void swapTurns(boolean gameOver) throws IOException {
        //swap the turns.... literally just as the title implies
        if (gameOver)
        {}
        else if (gameData.game.whoGetsNextMove() == 1)
        {
            objectOutputStream.writeObject(new Message("/yourTurn"));

            if (gameData.mode == 2)
            {
                objectOutputStream2.writeObject(new Message("/notYourTurn"));
            }
        }
        else
        {
            objectOutputStream.writeObject(new Message("/notYourTurn"));

            if (gameData.mode == 2) {
                objectOutputStream2.writeObject(new Message("/yourTurn"));
            }
        }
    }
}
