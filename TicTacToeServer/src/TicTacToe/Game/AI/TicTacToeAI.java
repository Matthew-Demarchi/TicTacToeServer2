package TicTacToe.Game.AI;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class TicTacToeAI {

    public static int TicTacToeAI(boolean easy, int[] buttons, int ai, int human) {
        //getting the number of empty cells
        int numOfEmptyCells = 0;
        for (int button : buttons) {
            if (button == 0) {
                numOfEmptyCells++;
            }
        }

        //calling a smart or dumb ai
        if (!easy) {
            return smartAI(buttons, ai, human, numOfEmptyCells);
        } else {
            return dumbAI(buttons, numOfEmptyCells);
        }
    }


    private static int smartAI(int[] buttons, int ai, int human, int numOfEmptyCells) {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = 0;
            for (int i = 0; i < buttons.length; i++) {
                if (buttons[i] == 0) {
                    buttons[i] = ai;
                    int score = TicTacToeAI.minimax(buttons, numOfEmptyCells, false, ai, human, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    buttons[i] = 0;
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = i;
                    }
                }
            }

        return bestMove;
    }

    private static int dumbAI(int[] buttons, int numOfEmptyCells)
    {
        int[] moves = new int[numOfEmptyCells];
        int j = 0;
        for (int i = 0; i < buttons.length; i++)
        {
            if (buttons[i] == 0)
            {
                moves[j] = i;
                j++;
            }
        }
        Random random = new Random();
        int randomIndex = random.nextInt(numOfEmptyCells);
        return moves[randomIndex];
    }

    private static int minimax(int[] buttons, int depth, boolean maximizingPlayer, int ai, int human, int alpha, int beta) {
        int result = checkWinner(buttons, ai, human);
        if (depth == 0 || result != -2)
        {
            return result;
        }
        if (maximizingPlayer)
        {
            int bestEvaluation = Integer.MIN_VALUE;
            for (int i = 0; i < buttons.length; i++) {
                if (buttons[i] == 0)
                {
                    buttons[i] = ai;
                    int evaluation = minimax(buttons, depth-1, false, ai, human, alpha, beta);
                    buttons[i] = 0;

                    bestEvaluation = Math.max(evaluation, bestEvaluation);
                    alpha = Math.max(alpha, evaluation);
                    if (beta <= alpha)
                    {
                        break;
                    }
                }
            }
            return bestEvaluation;
        }
        else
        {
            int bestEvaluation = Integer.MAX_VALUE;
            for (int i = 0; i < buttons.length; i++) {
                if (buttons[i] == 0)
                {
                    buttons[i] = human;
                    int evaluation = minimax(buttons, depth-1, true, ai, human, alpha, beta);
                    buttons[i] = 0;
                    bestEvaluation = Math.min(evaluation, bestEvaluation);
                    beta = Math.min(beta, evaluation);
                    if (beta <= alpha)
                    {
                        break;
                    }
                }
            }
            return bestEvaluation;
        }

    }


    private static int checkWinner(int[] buttons, int ai, int human) {
        int result = -1;
        int symbol = human;

        for (int k = 0; k < 2; k++)
        {
            for (int i = 0; i < 3; i++) {
                if (buttons[i * 3] == (symbol) &&
                        buttons[i * 3 + 1] == (symbol) &&
                        buttons[i * 3 + 2] == (symbol)) {
                    return result; // Winning row
                }
            }

            // Check columns
            for (int i = 0; i < 3; i++) {
                if (buttons[i] == (symbol) &&
                        buttons[i + 3] == (symbol) &&
                        buttons[i + 6] == (symbol)) {
                    return result; // Winning column
                }
            }

            // Check diagonals
            if (buttons[0] == (symbol) &&
                    buttons[4] == (symbol) &&
                    buttons[8] == (symbol)) {
                return result; // Winning diagonal
            }
            if (buttons[2] == (symbol) &&
                    buttons[4] == (symbol) &&
                    buttons[6] == (symbol)) {
                return result; // Winning diagonal
            }
            result = 1;
            symbol = ai;
        }
        // Check rows

        int buttonNumber = 0;
        for (int i = 0; i < 9; i++)
        {
            if (buttons[i] != 0)
            {
                buttonNumber++;
            }
        }
        if (buttonNumber == 9)
        {
            return 0;
        }

        return -2; // No winning combination found and not a tie
    }

}
