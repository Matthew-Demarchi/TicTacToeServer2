package TicTacToe.Game;

import java.util.Scanner;

public class ServerCommands implements Runnable
{
    @Override
    public void run() {
        System.out.println("type cuc(current user count), cgc(current game count), gl(game list), stop(stop taking new connections), shutdown(shutdown server even if someone is connected)");
        Scanner scanner = new Scanner(System.in);
        String console = "";
        while (true)
        {
            console = scanner.nextLine();
            if (console.equalsIgnoreCase("cuc"))
            {
                System.out.println("The server is currently managing " + Main.sockets.size() + " users.");
            }
            else if (console.equalsIgnoreCase("cgc"))
            {
                System.out.println("THe server is currently managing " + GamesList.getNumberOfGames() + " games.");
            }
            else if (console.equalsIgnoreCase("gl"))
            {
                GamesList.outputGames();
            }
            else if (console.equalsIgnoreCase("stop"))
            {
                Main.shutdown(console);
            }
            else if (console.equalsIgnoreCase("shutdown"))
            {
                Main.shutdown(console);
                break;
            }
            else
            {
                System.out.println("invalid input\ntype cuc(current user count), cgc(current game count), gl(game list), stop(stop taking new connections), shutdown(shutdown server even if someone is connected)");
            }
        }
    }
}
