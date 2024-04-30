package TicTacToe.Game;

import java.util.ArrayList;


public class Controller
{
    static ArrayList<GameData> games = new ArrayList<>();

    static public void newGame(GameData game)
    {
        games.add(game);
        outputGames();
    }
    static public void endGame(long gameID)
    {
        outputGames();
        for (int i = 0; i < games.size(); i++)
        {
            if (gameID == games.get(i).game.getGameID())
            {
                Main.removeSocket(games.get(i).socket1);
                games.remove(i);
                break;
            }
        }
    }
    static private void outputGames()
    {
        for (int i = 0; i < games.size(); i++)
        {
            System.out.println("GameID " + games.get(i).game.getGameID());
        }
    }
}
