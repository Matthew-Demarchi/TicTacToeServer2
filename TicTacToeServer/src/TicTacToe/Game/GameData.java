package TicTacToe.Game;

import TicTacToe.Game.AI.TicTacToeAI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import TicTacToe.Game.AI.TicTacToeAI;


public class GameData implements Runnable { // rename to gameManager

    public Game game;
    public Socket socket1;
    public Socket socket2;
    public Listening listening1;
    public Listening listening2;
    public boolean shutdown = false;
    public Object sync = new Object();
    String message;
    int turn = 1;
    int mode = 0;
    boolean easy = false; // for AI
    boolean switchSides = true; // for AI
    ObjectOutputStream objectOutputStream;
    ObjectOutputStream objectOutputStream2;


    public GameData(Socket socket1, Socket socket2, int mode, long ID)
    {
        this.socket1 = socket1;
        this.socket2 = socket2;
        game = new Game(ID);
        this.mode = mode;
        try {
            this.objectOutputStream = new ObjectOutputStream(socket1.getOutputStream());
            this.objectOutputStream2 = new ObjectOutputStream(socket2.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public GameData(Socket socket1, int mode, long ID)
    {
        this.socket1 = socket1;
        this.mode = mode;
        game = new Game(ID);
        try {
            this.objectOutputStream = new ObjectOutputStream(socket1.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setListening1(Listening listening1) {
        this.listening1 = listening1;
    }
    public void setListening2(Listening listening2) {
        this.listening2 = listening2;
    }
    public void run()
    {
        //System.out.println("gamedata started");
        if (mode == 2)
        {
            //System.out.println("mode 2 started");
            listening1.setSocket(socket1, 1);
            listening2.setSocket(socket2, 2);
            listening1.setGameData(this);
            listening2.setGameData(this);

            new Thread(new Notify(new Message("newGame"), this)).start();

            while (!shutdown) {
                synchronized (sync) {
                    try {
                        //System.out.println("Waiting for sockets...");
                        sync.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //System.out.println("not waiting for sockets...");
                if (message.contains("/move")) {
                    //System.out.println("calling Notify for valid move");
                        new Thread(new Notify(new Message("/valid"), this)).start();

                    try {
                        gameOver();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (message.contains("/quit"))
                {
                    //System.out.println("quit");
                    playerShutdown(String.valueOf(message.charAt(message.length()-1)));
                }
                else if (message.contains("/message"))
                {
                    //System.out.println("sending message to notify");
                    new Thread(new Notify(new Message("/message", message.substring(8)), this)).start();
                }
                else if (message.contains("/serverShutdown"))
                {
                    System.out.println("serverShutdown");
                }
                else
                {
                    System.out.println("error, non valid message received -- " + message);
                }
            }
        }
        else if (mode == 1) {
            //System.out.println("mode 1 started");
            listening1.setSocket(socket1, 1);
            listening1.setGameData(this);


            new Thread(new Notify(new Message("newGame"), this)).start();

            while (!shutdown) {
                synchronized (sync) {
                    try {
                        //System.out.println("Waiting for socket... -- AI Game");
                        sync.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //System.out.println("not waiting for socket...");
                if (message.contains("/move")) {
                    //System.out.println("calling Notify for valid moveAI");

                    new Thread(new Notify(new Message("/valid"), this)).start();

                    if (!game.isGameOver())
                    {
                        aiMakeMove();
                    }

                    try {
                        gameOver();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else if (message.contains("/quit")) {
                    //System.out.println("quit");
                    playerShutdown(String.valueOf(message.charAt(message.length() - 1)));
                } else if (message.contains("/difficulty")) {
                    setEasy(String.valueOf(message.charAt(message.length() - 1)));
                }
                else if (message.contains("/clearBoard"))
                {
                    clearBoard();
                }
                else if (message.contains("/switchSides"))
                {
                    setSwapTurns(String.valueOf(message.charAt(message.length() - 1)));
                }
                else if (message.contains("/serverShutdown"))
                {
                    System.out.println("serverShutdown");
                }
                else
                {
                    System.out.println("error, non valid message received -- " + message);
                }
            }
        }
        else {
            //System.out.println("ERROR INVALID MODE");
            shutdown();
        }
        //System.out.println("gamedata stopped");
    }
    private void clearBoard() {
        game.resetboard(false);

        swapTurn();

        game.currentPlayer = game.getxGoesTo();
        //System.out.println("turn is equal to " + turn);

        if (game.getxGoesTo() == 2)
        {
            aiMakeMove();
        }
        else
        {
            new Thread(new Notify(new Message("/valid"), this)).start();
        }

    }
    public void gameOver() throws InterruptedException {
        if (game.isGameOver())
        {
            //System.out.println("resetting board");
            swapTurn();
            Thread.sleep(3000);
            if (mode == 2)
            {
                game.resetboard(true);
                new Thread(new Notify(new Message("/valid"), this)).start();
            }
            else
            {
                game.resetboard(switchSides);
                if (!switchSides)
                {
                    game.currentPlayer = game.getxGoesTo();
                    //System.out.println("turn is equal to " + turn);
                }
                new Thread(new Notify(new Message("/valid"), this)).start();
                if (game.getxGoesTo() == 2)
                {
                    aiMakeMove();
                }

            }
        }
        else
        {

        }
    }

    private void setEasy(String a)
    {
        if (a.equalsIgnoreCase("E"))
        {
            easy = true;
        }
        else
        {
            easy = false;
        }
    }
    private void setSwapTurns(String a)
    {
        if (a.equalsIgnoreCase("Y"))
        {
            switchSides = true;
        }
        else
        {
            switchSides = false;
        }
    }

    private void swapTurn()
    {
        if (mode == 2 || switchSides)
        {
            if (game.isGameOver())
            {
                if (turn == 1)
                {
                    turn = 2;
                    //System.out.println("swapped to turn 2");
                }
                else
                {
                    turn = 1;
                    //System.out.println("swapped to turn 1");
                }
            }
            //System.out.println("swapped to switch turn");
        }

    }
    private void playerShutdown(String playerThatQuit)
    {
        shutdown();
        if (mode == 2)
        {
            if (playerThatQuit.equals("1"))
            {
                new Thread(new Notify(socket2, new Message("/shutdown"), game, objectOutputStream2)).start();
            }
            else
            {
                new Thread(new Notify(socket1, new Message("/shutdown"), game, objectOutputStream)).start();
            }
        }
    }
    private void shutdown()
    {
        shutdown = true;
        GamesList.endGame(game.gameID);
    }
    public void serverShutdown()
    {
        shutdown = true;
        GamesList.endGame(game.gameID);
        new Thread(new Notify(new Message("/serverShutdown"), this)).start();
        alert ("/serverShutdown");
    }

    public void alert(String message)
    {
        synchronized (sync) {
            this.message = message;
            sync.notify();
        }
    }
    public boolean isShutdown()
    {
        return shutdown;
    }
    private void aiMakeMove()
    {
        if (!game.isGameOver())
        {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            int aiMove = TicTacToeAI.TicTacToeAI(easy, game.buttons, 2, 1);
            boolean valid = game.buttonPressed(aiMove, 2);
            new Thread (new Notify(new Message("/valid"), this)).start();
        }

    }
}
