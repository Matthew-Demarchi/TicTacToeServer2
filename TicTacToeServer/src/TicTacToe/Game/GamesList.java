package TicTacToe.Game;

import java.util.ArrayList;


public class GamesList
{
    static ArrayList<GameData> games = new ArrayList<>();

    static public int getNumberOfGames()
    {
        return games.size();
    }

    static public void newGame(GameData game)
    {
        games.add(game);
    }
    static public void endGame(long gameID)
    {
        for (int i = 0; i < games.size(); i++)
        {
            if (gameID == games.get(i).game.getGameID())
            {
                Main.removeSocket(games.get(i).socket1);
                if (games.get(i).mode == 2)
                {
                    Main.removeSocket(games.get(i).socket2);
                }
                games.remove(i);
                break;
            }
        }
    }
    static public void outputGames()
    {
        for (int i = 0; i < games.size(); i++)
        {
            if (games.get(i).mode == 2)
            {
                System.out.println("2 Player Game -- GameID " + games.get(i).game.getGameID());
            }
            else
            {
                System.out.println("AI Game -- GameID " + games.get(i).game.getGameID());
            }
        }
    }
}
