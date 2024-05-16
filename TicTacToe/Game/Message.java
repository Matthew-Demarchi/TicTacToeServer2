package TicTacToe.Game;

import java.io.Serializable;

public class Message implements Serializable
{
    public String message;
    public int move;
    public String chatMessage;
    public Game game;


    public Message(String message)
    {
        this.message = message;
    }
    public Message (String message, String chatMessage)
    {
        this.message = message;
        this.chatMessage = chatMessage;
    }
    public Message(String message, Game game)
    {
        this.message = message;
        this.game = game;
    }

}
