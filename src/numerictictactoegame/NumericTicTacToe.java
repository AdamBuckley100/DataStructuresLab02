package numerictictactoegame;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import numerictictactoegame.StdDraw;

/**
 * Module: Data Structures.
 * 
 * This class (with the help of the StdDraw class,) allows a user to play a game
 * of Numeric Tic-Tac-Toe using Swing GUI. (exclusively a player vs. computer game).
 * 
 * The real-life player can only input the even numbers between 1-9 on their turn
 * (there is checks to make fully sure of this) and the computer can only place the odd
 * numbers on the board between 1-9 (inclusive).
 * 
 * The first move is an odd number being placed somewhere randomly on the board
 * (i.e. computer's turn, computer goes first).
 * 
 * In order to win the game, either the computer or the real-life player must
 * be the last user to place a number into a square where that number being
 * added to the two other numbers in a straight line of that chosen square add up
 * to 15. (this can be a diagonal straight line as well).
 * 
 * References: Slides from Data Structures module on Moodle & Classes.
 * 
 * @author Adam Buckley (Student I.D: 20062910 at Waterford Institute of Technology).
 * @version 1.
 * @date First coded: 25/01/2016.
 * @date Last modified: 01/02/2016.
 */

public class NumericTicTacToe{

	final static int EMPTY = 0;
	final static Random random = new Random();

	/**
	 * The main method: this method is executed on game start-up and executes the two 
	 * methods within the main method itself.
	 * 
	 * @param args - String[] args
	 */
	public static void main(String[] args) 
	{
		StartGameOrNotPopUp();
		overallGameActions();
	}

	/**
	 * This contains the execution of the actual game itself.
	 * This method is always called in the main method and if
	 * a user decides to play again then this method is called again
	 * via the gameEndOptions() method).
	 */
	public static void overallGameActions()
	{
		// Allocate identifiers to represent game state
		// Using an array of int so that summing along a row, a column or a
		// diagonal is a easy test for a win
		//Frame frame = new JFrame("Game Of Tic-Tac-Toe");
		int[][] board = new int[3][3];

		// array of booleans, the 5 usable numbers for the computer. The default value for a boolean (primitive) is always false.
		// Note: false = unused. true = already used on board.
		boolean[] computerBooleanArray = new boolean[5];

		// The default value for a boolean (primitive) always is false. false = unused. true = already used on board.
		boolean[] humanBooleanArray = new boolean[4];

		// Initializing local variables inside this Game method.
		int row = 0;
		int col = 0;
		int move = 0;
		boolean boardFull;
		boolean playerWon;
		int whichPlayerWon = 0;

		// Setup graphics and draw empty board
		StdDraw.setPenRadius(0.04); // draw thicker lines
		StdDraw.line(0, 0.33, 1, 0.33);
		StdDraw.line(0, 0.66, 1, 0.66);
		StdDraw.line(0.33, 0, 0.33, 1.0);
		StdDraw.line(0.66, 0, 0.66, 1.0);

		StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 64)); // Font size

		do {
			if(move % 2 == 1)
			{
				System.out.println("\tHuman move ...");

				// The displayUserMovePrompt simple is the method that allows on the bottom of the game
				// for there to be writing saying that is it the real life user's turn when it is
				// the real-life user's turn.
				displayUserMovePrompt();

				int theNumber = choicePopUp(board);

				// use mouse position to get slot
				boolean mousePressed = false;
				do {
					if (StdDraw.mousePressed()) {

						col = (int) (StdDraw.mouseX() * 3);	

						row = (int) (StdDraw.mouseY() * 3);

						mousePressed = true;
					}
				}while(!mousePressed || board[row][col] != EMPTY);

				board[row][col] = theNumber; // valid move (empty slot)
				humanBooleanArray[(theNumber/2)-1] = true;
			}
			else
			{
				System.out.println("\tComputer move ...");

				// The displayCompMovePrompt simple is the method that allows on the bottom of the game
				// for there to be writing saying that is it the computer's turn when it is
				// the computer's turn.
				displayCompMovePrompt();

				// Initializing two variables of type int that will in certain cases with assigned during the game.
				int numberForComputerToWin;
				int numberComputerBlocksWith;

				int[] ComputerToWinPositions = computerToWin(computerBooleanArray, board);

				if (ComputerToWinPositions != null)
				{
					row = ComputerToWinPositions[0];
					col = ComputerToWinPositions[1];
					numberForComputerToWin = ComputerToWinPositions[2];
					board[row][col] = numberForComputerToWin;

					// assign the number that the computer uses to win with true boolean.
					// (true = the number has been used).
					computerBooleanArray[(numberForComputerToWin-1)/2] = true;
				}
				else if (ComputerToWinPositions == null)
				{
					int[] ComputerToBlockPositions = computerToBlock(humanBooleanArray, board);
					if (ComputerToBlockPositions != null)
					{
						row = ComputerToBlockPositions[0];
						col = ComputerToBlockPositions[1];
						// The number used to block the real-life user's win doesn't matter, thus
						// the random selection below.
						numberComputerBlocksWith = pickingComputerRandomNumber(computerBooleanArray, board);
						board[row][col] = numberComputerBlocksWith;
						computerBooleanArray[(numberComputerBlocksWith-1)/2] = true;
					}
					else
					{
						int[] computerRandomPositions = getEmptySquare(board);

						row = computerRandomPositions[0];
						col = computerRandomPositions[1];

						int randomNumberComputer;
						randomNumberComputer = pickingComputerRandomNumber(computerBooleanArray, board);
						board[row][col] = randomNumberComputer;   // valid move (empty slot)

						// assign the number that the computer uses to win with true boolean.
						// (true = the number has been used).
						computerBooleanArray[(randomNumberComputer-1)/2] = true;
					}
				}
				//keep a time of 650ms for the computer to make a move
				//(helps the player experience feel more authentic).
				try {
					Thread.sleep(650);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// Update screen to reflect board change
			// move is the move number, col is the current move's column, row 
			//is the current move's row
			double x = col * .33 + 0.15;
			double y = row * .33 + 0.15;

			//Directly below: board reflecting the change: if the move int value is an even
			//number then the user's X value position choice is reflected but it move is an
			//odd number the computer's (random) position choice is reflected on this update.
			StdDraw.text(x, y, String.valueOf(board[row][col]));
			move++;
			boardFull = isBoardFull(board);
			playerWon = hasSomebodyWon(board);

			if (move % 2 == 0)
			{
				// Real-life player won
				whichPlayerWon = 1;
			}
			else if (move % 2 == 1)
			{
				// computer won
				whichPlayerWon = -1;
			}
		}while (!boardFull && !playerWon);

		if (playerWon == true)
		{
			if (whichPlayerWon == -1)
			{
				JOptionPane.showMessageDialog(null, "Game Over: You Lost");
				gameEndOptions(whichPlayerWon, board);
			}
			else if (whichPlayerWon == 1)
			{
				JOptionPane.showMessageDialog(null, "Game Over: You Won");
				gameEndOptions(whichPlayerWon, board);
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "It's a Draw");
			gameEndOptions(whichPlayerWon, board);
		}
	}

	/**
	 * This method allows a pop up to appear right before the very first game
	 * is about to be played. This pop-up gives a user an option to play the game
	 * (yes button being pressed) or not play the game (no or cancel buttons being pressed).
	 */
	public static void StartGameOrNotPopUp()
	{
		final JFrame frame = new JFrame("JOptionPane Demo");
		int i = JOptionPane.showOptionDialog(frame,
				"Would You Like The Tic-Tac-Toe Game To begin?",
				"Tic-Tac-Toe: Video Game",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE, // icon
				null,
				null,
				null);

		// Code directly below: if my i int is equals to 0 (user click user action) then the game 
		// does not terminate. However if i is not equals to 0 then the game does terminate.
		if (i != 0)
		{
			System.exit(0);
		}
	}

	/**
	 * This method clears all the board positions for the 
	 * start of a brand new game when a game was played 
	 * immediately before the brand new game.
	 * 
	 * @param int[][] - A two dimensional array called board is 
	 * passed into this method.  (this is board itself as it is currently).
	 */
	private static void clear(int[][] board)
	{
		for (int col = 0; col < 3; col++)
		{
			for (int row = 0; row < 3; row++)
			{
				double x = col * .33 + 0.15;
				double y = row * .33 + 0.15;

				if (board[row][col] != EMPTY || board[row][col] != EMPTY)
				{
					// Below: the clear each cell, pen colour is changed to white, a
					// filled white square is placed over the previous cells' areas.
					StdDraw.setPenColor(Color.WHITE);
					StdDraw.filledSquare(x, y, 0.08);
				}
				// Below: sets pen colour back to black.
				StdDraw.setPenColor();
			}
		}		
	}

	/**
	 * This method allows a pop up to appear the end of each Tic-Tac-Toe game.
	 * The user is given an option to play again (yes button clicked) or not play again
	 * (either no or cancel buttons being clicked).
	 * 
	 * Note: A different pop-up window pops up based on whether the player won
	 * the game, lost the game or the game being a draw
	 * 
	 * @param int - An int value that showcases if the user won the game (1),
	 * lost the game (-1) or the game was a draw (0), is passed into the method.
	 * 
	 * @param int[][] - A two dimensional int array called
	 * board  is passed into this method as a parameter.
	 */
	public static void gameEndOptions(int whichPlayerWon, int[][] board)
	{
		// This JFrame prompts a user on end of a game is they want to player again or not.
		final JFrame frame = new JFrame("JOptionPane Demo");
		if (whichPlayerWon == 0)
		{
			int i = JOptionPane.showOptionDialog(frame,
					"It's a Draw!.. Do You Want To Play The Game Again?",
					"Tic-Tac-Toe: Video Game",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE, // icon
					null,
					null,
					null);

			// Code directly below: if my i int is equals to 0 (user click user action) then the game 
			// does not terminate. However if i is not equals to 0 then the game does terminate.
			if (i == 0)
			{
				clear(board);
				overallGameActions();
			}
			else
			{
				System.exit(0);
			}
		}
		else if (whichPlayerWon == -1)
		{
			int i = JOptionPane.showOptionDialog(frame,
					"You Lost.. Do You Want To Try Again?",
					"Tic-Tac-Toe: Video Game",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE, // icon
					null,
					null,
					null);

			// Code directly below: if my i int is equals to 0 (user click user action) then the game 
			// does not terminate. However if i is not equals to 0 then the game does terminate.
			if (i == 0)
			{
				clear(board);
				overallGameActions();
			}
			else
			{
				System.exit(0);
			}
		}
		else if (whichPlayerWon == 1)
		{
			int i = JOptionPane.showOptionDialog(frame,
					"You Win! Do You Want To Play The Game Again?",
					"Tic-Tac-Toe: Video Game",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE, // icon
					null,
					null,
					null);

			// Code directly below: if my i int is equals to 0 (user click user action) then the game 
			// does not terminate. However if i is not equals to 0 then the game does terminate.
			if (i == 0)
			{
				clear(board);
				overallGameActions();
			}
			else
			{
				System.exit(0);
			}
		}
	}

	/**
	 * This method checks to see if the board is currently full.
	 * 
	 * @param board - a two dimensional array called board is
	 * passed into this method. (this is board itself as it is currently).
	 * 
	 * @return boolean - a boolean value: true or false is returned:
	 * true if the board is full and false is board has an empty cell.
	 */
	public static boolean isBoardFull(int[][] board)
	{

		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				if(board[row][col] == EMPTY)
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * This method returns a boolean based on if the game
	 * was won or not won.
	 * 
	 * @param int[][] - A two dimensional int array called
	 * board  is passed into this method as a parameter.
	 * (this is board itself as it is currently).
	 * 
	 * @return boolean - Returns an true if either player has won the game.
	 */
	public static boolean hasSomebodyWon(int[][] board)
	{
		for (int row = 0; row < 3; row++)
		{
			int sum = board[row][0] + board[row][1] + board[row][2];
			if ((sum == 15) && (board[row][0] != EMPTY && board[row][1] != EMPTY && board[row][2] != EMPTY))
			{
				return true;
			}
		}

		for (int col = 0; col < 3; col++)
		{
			int sum = board[0][col] + board[1][col] + board[2][col];
			if ((sum == 15) && (board[0][col] != EMPTY && board[1][col] != EMPTY && board[2][col] != EMPTY))
			{
				return true;
			}
		}

		int sum = board[0][0] + board[1][1] + board[2][2];
		int sum2 = board[0][2] + board[1][1] + board[2][0];

		if ((sum == 15 || sum2 == 15) && (board[0][0] != EMPTY && board[1][1] != EMPTY && board[2][2] != EMPTY))
		{
			return true;
		}

		if ((sum == 15 || sum2 == 15) && (board[0][2] != EMPTY && board[1][1] != EMPTY && board[2][0] != EMPTY))
		{
			return true;
		}
		return false;
	}

	/**
	 * This method simply returns a random empty position on
	 * the board.
	 * 
	 * @param int[][] - A two dimensional int array called
	 * board  is passed into this method as a parameter.
	 * (this is board itself as it is currently).
	 * 
	 * @return int[] - Returns an int array, position
	 * 0 gets the row value and position 1 gets the column value.
	 */
	public static int[] getEmptySquare(int[][] board)
	{
		int row = 0;
		int col = 0;

		do {
			//below: random which is an object of type Random will generate a random
			//number between 0 and 2 (completely inclusive) for the eventually chosen
			//row (horizontal) and column (vertical).
			row = random.nextInt(3);
			col = random.nextInt(3);
			//below: while that specific square is empty, an int array is returned (result)
			//that contains the row in question (position 0 in int array) and the column 
			//(position 1 in int array).
		}while (board[row][col] != EMPTY);
		int[] chosenEmptySquare = {row, col};
		return chosenEmptySquare;
	}

	/**
	 * This method places the text on the bottom of the board saying that 
	 * it is the user's turn when it is the user's turn.
	 */
	public static void displayUserMovePrompt()
	{
		StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 20)); // Font SIZE!
		StdDraw.setPenColor(Color.WHITE);
		StdDraw.text(0.5, 0.03, "Comp Move");
		StdDraw.setPenColor();
		StdDraw.text(0.5, 0.03, "User Move");
		StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 64)); // Font SIZE!
	}

	/**
	 * This method places the text on the bottom of the board saying that 
	 * it is the Computer's turn when it is the Computer's turn.
	 */
	public static void displayCompMovePrompt()
	{
		StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 20)); // Font SIZE!
		StdDraw.setPenColor(Color.WHITE);
		StdDraw.text(0.5, 0.03, "User Move");
		StdDraw.setPenColor();
		StdDraw.text(0.5, 0.03, "Comp Move");
		StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 64)); // Font SIZE!
	}

	/**
	 * This method is the method that is executed when it is the real-life user's turn,
	 * this method allows a JOption pop-up to show up for the user to input the number
	 * they would like placed on the board. (A check is in place to only allow a user to
	 * choose a number that hasn't been chosen before and is an even number between 2 and 8
	 * (inclusive). 
	 * 
	 * @param int[][] - A two dimensional array called board is 
	 * passed into this method.  (this is board itself as it is currently).
	 * 
	 * @return int - An int that is the number the user actually chose.
	 */
	public static int choicePopUp(int[][] board)
	{
		int number;
		do 
		{
			// Below is the JOption prompt that allows a user to enter a number (with strict limitations).
			String s = JOptionPane.showInputDialog(null, "Enter numbers 2,4,6 or 8.\nYou can use each number once.");
			// Number is parsed in below.
			number = Integer.parseInt(s);
		}
		//This whole do while loop situation effectively prevents any number other than 2,4,6 or 8
		//from being inputted successfully, provided they have not been previously used.
		while (number % 2 == 1 || (hasANumberBeenUsed(board, number)) || (number >= 10) || (number==0));

		return number;
	}

	/**
	 * This method simply checks if a number has been used,
	 * 
	 * @param int[][] - A two dimensional array called board is 
	 * passed into this method.  (this is board itself as it is currently).
	 * 
	 * @param chosenNumber - The number that is being tested to see if it has been used.
	 * 
	 * @return boolean - if the number has not been used, a false boolean is returned and true
	 * is return if it the number has been used.
	 */
	public static boolean hasANumberBeenUsed(int[][] board, int chosenNumber)
	{
		for (int row = 0; row < 3; row++)
		{
			for (int col = 0; col < 3; col++)
			{
				if (board[row][col] == chosenNumber)
				{
					return true; // true = it has been used.
				}
			}
		}
		return false; // false = it has not been used.
	}

	/**
	 * This method is the intelligent way the computer automatically
	 * grabs a win when a potential win is in place. 
	 * (board is checked after every round in the main method).
	 * Note: all scerarios are checked: rows, columns and both diagonals
	 * (play game to see).
	 * 
	 * @param booleanArray
	 * 
	 * @param int[][] - A two dimensional array called board is 
	 * passed into this method.  (this is board itself as it is currently).
	 * 
	 * @return int[] - An int array is returned with the row,column and the number the
	 * computer will have to put down in order to win. The row is position 0 in the int
	 * array, the column is position 1 and the number itself is in position 2.
	 */
	public static int[] computerToWin(boolean computerBooleanArray[], int board[][])
	{
		// Below: because there is 5 options for the computer.
		// (obviously one value in this array will already be true after the first number
		// is placed on the board on the initial turn.
		for (int i = 0; i < 5; i++)
		{
			// Below: check: if the position in the array (from 0 to 4). If the particular value in
			// the boolean array is false (unused) then continue within:
			if (!computerBooleanArray[i])
			{
				// Get the actual number itself;
				int number = (i*2) + 1;

				for (int row = 0; row < 3; row++)
				{
					// Below: check to see if exactly 1 of the squares is empty and two are occupied
					if ((board[row][0] == 0 && board[row][1] != 0 && board[row][2] !=0)
							|| (board[row][0] != 0 && board[row][1] == 0 && board[row][2] !=0)
							|| (board[row][0] != 0 && board[row][1] != 0 && board[row][2] ==0))
					{
						int sum = board[row][0] + board[row][1] + board[row][2];

						// Below: if the two filled cells in question and the candidate (candidate = number 
						// to potentially maybe be put in) equal to 15 then go within:
						if (sum + number == 15)
						{
							int col = 0;
							if (board[row][0] == 0)
							{
								col = 0;
							}
							else if (board[row][1] == 0)
							{
								col = 1;
							}
							else if (board[row][2] == 0)
							{
								col = 2;
							}
							// Return that specific row,col combination cause this will let the computer win and the
							// number which the computer need to win with.
							int[] itelligentChosenEmptySquare = {row, col, number};
							return itelligentChosenEmptySquare;
						}
					}
				}
			}
		}

		for (int j = 0; j < 5; j++)
		{
			// Below: check: if the position in the array (from 0 to 4), if the value in the boolean array
			// is true then continue within:
			if (!computerBooleanArray[j])
			{
				// Get the actual number
				int number = (j*2) + 1;

				for (int col = 0; col < 3; col++)
				{
					// Below: check to see if exactly 1 of the squares is empty and two are occupied
					if ((board[0][col] == 0 && board[1][col] != 0 && board[2][col] !=0)
							|| (board[0][col] != 0 && board[1][col] == 0 && board[2][col] !=0)
							|| (board[0][col] != 0 && board[1][col] != 0 && board[2][col] ==0))
					{
						int sum = board[0][col] + board[1][col] + board[2][col];
						{
							// Below: if the two cells in question and the candidate (candidate = number 
							// to potentially maybe be put in) equal to 15 then go within:
							if (sum + number == 15)
							{
								int row = 0;
								if (board[0][col] == 0)
								{
									row = 0;
								}
								else if (board[1][col] == 0)
								{
									row = 1;
								}
								else if (board[2][col] == 0)
								{
									row = 2;
								}
								// Return that specific row,col combination cause this will let the computer win and the
								// number which the computer need to win with.
								int[] itelligentChosenEmptySquare = {row, col, number};
								return itelligentChosenEmptySquare;
							}
						}
					}
				}
			}
		}

		// Below: because there is 5 options for the computer.
		// (obviously one value in this array will already be true after the first number
		// is placed on the board on the initial turn.
		for (int i = 0; i < 5; i++)
		{
			// Below: check: if the position in the array (from 0 to 4). If the particular value in
			// the boolean array is false (it is unused) then continue within:
			if (!computerBooleanArray[i])
			{
				// Get the actual number itself;
				int number = (i*2) + 1;

				int diagonalSum = board[0][0] + board[1][1] + board[2][2];
				int diagonalSum2 = board[0][2] + board[1][1] + board[2][0];

				if (diagonalSum + number == 15)
				{
					if ((board[0][0] == 0 && board[1][1] != 0 && board[2][2] != 0)
							|| (board[0][0] != 0 && board[1][1] == 0 && board[2][2] != 0)
							|| (board[0][0] != 0 && board[1][1] != 0 && board[2][2] == 0))
					{
						for (int k = 0; k < 3; k++)
							if (board[k][k] == 0)
							{
								// the board position is returned along with the number
								// to win the game with (below).
								int[] itelligentChosenEmptySquare = {k, k, number};
								return itelligentChosenEmptySquare;
							}
					}

					if (diagonalSum2 + number == 15)
					{
						if ((board[0][2] == 0 && board[1][1] != 0 && board[2][0] != 0)
								|| (board[0][2] != 0 && board[1][1] == 0 && board[2][0] != 0)
								|| (board[0][2] != 0 && board[1][1] != 0 && board[2][0] == 0))
						{
							for (int row = 0; row < 3; row++)
								for (int col = 0; col < 3; col++)
								{
									// below while: only board positions [0][2], [1][1] and [2][0] will
									// enter below's while loop:
									if (row+col == 2)
									{
										// One of them has to be empty (see above closest above if statement).
										if (board[row][col] == 0)
										{
											// Return that specific row,col combination cause this will let the computer win and the
											// number which the computer need to win with.
											int[] itelligentChosenEmptySquare = {row, col, number};
											return itelligentChosenEmptySquare;
										}
									}
								}
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * This method simply returns a number that is randomly selected by the computer
	 * (a check is in place to make sure the randomly generated number is appropriate.
	 * 
	 * @return int - returns the actual randomly selected number to be placed on the number.
	 * 
	 * @parameter boolean[] - A boolean array for the computer.
	 * 
	 * @param int[][] - A two dimensional array called board is 
	 * passed into this method.  (this is board itself as it is currently).
	 */
	public static int pickingComputerRandomNumber(boolean[] computerBooleanArray, int[][] board)
	{
		int number;

		do 
		{
			// Below: get a random number between 1 and 9 (inclusive), checked later.
			number = random.nextInt(9) + 1;
		}
		// This whole do while loop prevents a number that has already been used by the computer
		// or a number that is not odd from being used by the computer.
		while (number % 2 == 0 || hasANumberBeenUsed(board, number));

		return number;
	}

	/**
	 * This method returns a position for a square
	 * for the computer to block a real-life user win.
	 * this method only executes if a potential win does not
	 * exist for computer.
	 * This method only executes if real-life user has a potential win in place.
	 * The number chosen by the computer does not matter (it can be random).
	 * 
	 * Note: all scerarios are checked: rows, columns and both diagonals
	 * (play game to see).
	 * 
	 * @param int[][] - A two dimensional int array called
	 * board  is passed into this method as a parameter.
	 * (this is board itself as it is currently).
	 * 
	 * @return int[] int array - Returns an int array, position
	 * 0 gets the row value and position 1 gets the column value.
	 */
	public static int[] computerToBlock(boolean humanBooleanArray[], int board[][])
	{
		// Below: because there is 5 options for the computer.
		// (obviously one value in this array will already be true after the first number
		// is placed on the board on the initial turn.
		for (int i = 0; i < 4; i++)
		{
			// Below: check: if the position in the array (from 0 to 4). If the particular value in
			// the boolean array is false (unused) then continue within:
			if (!humanBooleanArray[i])
			{
				// Get the actual number itself;
				int number = ((i+1)*2);

				for (int row = 0; row < 3; row++)
				{
					// Below: check to see if exactly 1 of the squares is empty and two are occupied
					if ((board[row][0] == 0 && board[row][1] != 0 && board[row][2] !=0)
							|| (board[row][0] != 0 && board[row][1] == 0 && board[row][2] !=0)
							|| (board[row][0] != 0 && board[row][1] != 0 && board[row][2] ==0))
					{
						int sum = board[row][0] + board[row][1] + board[row][2];

						// Below: if the two filled cells in question and the candidate (candidate = number 
						// to potentially maybe be put in) equal to 15 then go within:
						if (sum + number == 15)
						{
							int col = 0;
							if (board[row][0] == 0)
							{
								col = 0;
							}
							else if (board[row][1] == 0)
							{
								col = 1;
							}
							else if (board[row][2] == 0)
							{
								col = 2;
							}
							// Return that specific row,col combination cause this will let the computer win
							int[] itelligentChosenEmptySquare = {row, col};
							return itelligentChosenEmptySquare;
						}
					}
				}

			}
		}

		for (int j = 0; j < 4; j++)
		{
			// Below: check: if the position in the array (from 0 to 4), if the value in the boolean array
			// is true then continue within:
			if (!humanBooleanArray[j])
			{
				// Below: get the actual number
				int number = ((j+1)*2);

				for (int col = 0; col < 3; col++)
				{
					// Below: check to see if exactly 1 of the squares is empty and two are occupied
					if ((board[0][col] == 0 && board[1][col] != 0 && board[2][col] !=0)
							|| (board[0][col] != 0 && board[1][col] == 0 && board[2][col] !=0)
							|| (board[0][col] != 0 && board[1][col] != 0 && board[2][col] ==0))
					{
						int sum = board[0][col] + board[1][col] + board[2][col];
						{
							// Below: if the two cells in question and the candidate (candidate = number 
							// to potentially maybe be put in) equal to 15 then go within:
							if (sum + number == 15)
							{
								int row = 0;
								if (board[0][col] == 0)
								{
									row = 0;
								}
								else if (board[1][col] == 0)
								{
									row = 1;
								}
								else if (board[2][col] == 0)
								{
									row = 2;
								}
								// Return that specific row,col combination cause this will let the computer win
								int[] itelligentChosenEmptySquare = {row, col};
								return itelligentChosenEmptySquare;
							}
						}
					}
				}
			}
		}

		// Below: because there is 5 options for he computer.
		// (obviously one value in this array will already be true after the first number
		// is placed on the board on the initial turn.
		for (int i = 0; i < 4; i++)
		{
			// Below: check: if the position in the array (from 0 to 4). If the particular value in
			// the boolean array is false (it is unused) then continue within:
			if (!humanBooleanArray[i])
			{
				// Get the actual number itself;
				int number = ((i+1)*2);

				int diagonalSum = board[0][0] + board[1][1] + board[2][2];
				int diagonalSum2 = board[0][2] + board[1][1] + board[2][0];

				if (diagonalSum + number == 15)
				{
					if ((board[0][0] == 0 && board[1][1] != 0 && board[2][2] != 0)
							|| (board[0][0] != 0 && board[1][1] == 0 && board[2][2] != 0)
							|| (board[0][0] != 0 && board[1][1] != 0 && board[2][2] == 0))
					{
						for (int k = 0; k < 3; k++)
							if (board[k][k] == 0)
							{
								int[] itelligentChosenEmptySquare = {k, k};
								return itelligentChosenEmptySquare;
							}
					}
				}

				if (diagonalSum2 + number == 15)
				{
					if ((board[0][2] == 0 && board[1][1] != 0 && board[2][0] != 0)
							|| (board[0][2] != 0 && board[1][1] == 0 && board[2][0] != 0)
							|| (board[0][2] != 0 && board[1][1] != 0 && board[2][0] == 0))
					{
						for (int row = 0; row < 3; row++)
							for (int col = 0; col < 3; col++)
							{
								// below while: only board positions [0][2], [1][1] and [2][0] will
								// enter below's while loop:
								if (row+col == 2)
								{
									// One of them has to be empty (see above closest above if statement).
									if (board[row][col] == 0)
									{
										// now return that empty col position
										int[] itelligentChosenEmptySquare = {row, col};
										return itelligentChosenEmptySquare;
									}
								}
							}
					}
				}
			}
		}
		return null;
	}
}