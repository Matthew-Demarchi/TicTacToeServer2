package TicTacToe.Game;

import java.io.*;
import java.net.Socket;

public class NotifyAI implements Runnable {
    Socket socket;
    Message message;
    Game game;
    OutputStream outputStream;
    ObjectOutputStream objectOutputStream;



    NotifyAI(Socket socket, Message message, Game game, ObjectOutputStream objectOutputStream)
    {
        this.socket = socket;
        this.message = message;
        this.game = game;
        this.objectOutputStream = objectOutputStream;
    }
    @Override
    public void run() {
        synchronized (this)
        {
            try {

                if (message.message.equals("/invalid"))
                {
//                System.out.println("Invalid Message");
//                out = new PrintWriter(socket.getOutputStream(), true);
//                outputStream = socket.getOutputStream();
//                objectOutputStream = new ObjectOutputStream(outputStream);
//                out.println(message);
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                objectOutputStream.writeObject(game);
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                out.println("yourTurn");
                    // THIS FUNCTION IS NOT SPECIFIC TO AI SO IT IS DONE IN THE STANDARD NOTIFIER
                }
                else if (message.message.equals("/valid"))
                {
                    System.out.println("Valid Message1");
                    boolean gameOver = game.isGameOver();
                    {

                        objectOutputStream.writeObject(message);

                        objectOutputStream.writeObject(game);
                        System.out.println("Valid Message over2");


                        if (gameOver)
                        {
                            System.out.println("game over Message1");
                            objectOutputStream.writeObject(new Message("/gameOver"));

                        }
                    }
                    swapTurns(gameOver);
                }
                else if(message.message.equals("newGame"))
                {
                    System.out.println("New TicTacToe.Game Started");
                    {
                        objectOutputStream.writeObject(new Message("/1"));

                        objectOutputStream.writeObject(game);
                        System.out.println("New TicTacToe.Game Started1");
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
                else if (message.message.contains("/message"))
                {
                    objectOutputStream.writeObject(message);
                    System.out.println("message sent in notify.AI PROBLEM THIS SHOULD NEVER ACTUALLY BE ABLE TO HAPPEN");
                }
                else
                {}

                if (objectOutputStream != null)
                {
                    objectOutputStream.reset();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }
    private void swapTurns(boolean gameover) throws IOException {
        System.out.println("swapTurns");

        System.out.println(game.whoGetsNextMove() + " who gets next move");
        if (gameover)
        {}
        else if (game.whoGetsNextMove() == 1)
        {
            System.out.println("swapTurns1");

            objectOutputStream.writeObject(new Message("/yourTurn"));


        }
        else
        {
            System.out.println("swapTurns2");

            objectOutputStream.writeObject(new Message("/notYourTurn"));



        }
        System.out.println("swap turn end");

    }

}
