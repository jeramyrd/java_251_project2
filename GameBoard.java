import java.util.ArrayList;
import java.util.List;

import cs251.project2.GomokuInterface.Square;
import cs251.project2.GomokuInterface.TurnResult;

/**
 * @author  Jeramy Dickerson
 * CS 251 with Professor Brooke Chenoweth - Fall 2026
 * Project 2 - Gomoku Game
 * 
 * GameBoard is the core of the game engine. It will handle how the game works.
 * The Computer and Minerva class will handle how the computer thinks.
 * Some of the original GomokuInterface methods are calls to this class's methods.
 */
public class GameBoard {

    private int maxColumns;
    private int maxRows;
    private int numberToWin;
    private TurnResult winner;
    private Square[][] board;



    /**
     * Reject the default consructor. You have to give it parameters.
     * @throws UnsupportedOperationException indicating you can't do this.
     */
    public GameBoard() {
        throw new UnsupportedOperationException("Must call GameBoard(cols, rows, numToWin) instead");
    }

    
    /**
     * @param columns = number of columns in the game board.
     * @param rows = number of rows in the game board.
     * @param numToWin = number of marks in a row for any direction to win.
     * Sets the private class fields and sets all board spots to EMPTY through a resetBoard call.
     */
    public GameBoard(int columns, int rows, int numToWin){
        maxColumns = columns;
        maxRows = rows;
        numberToWin = numToWin;
        winner = TurnResult.GAME_NOT_OVER;
        board = new Square[maxColumns][maxRows];
        resetBoard();
    }

    public void resetBoard(){
        for (int row = 0; row < maxRows; ++row){
            for (int col = 0; col < maxColumns; ++col){
                board[col][row] = Square.EMPTY;
            }
        }
    }

    /**
     * Getter
     * @return maximum Columns for the board
     */
    public int getMaxColumns(){
        return maxColumns;
    }

    /**
     * Getter
     * @return maximum Rows for the board
     */
    public int getMaxRows(){
        return maxRows;
    }

    /**
     * Getter
     * @return win condition legth
     */
    public int getWinCount(){
        return numberToWin;
    }

    /**
     * Make sure the spot is on the board and EMPTY before you can mark it.
     * @param col -> spot column you want to play at
     * @param row -> spot row you want to play at
     * @param player -> CROSS or RING if successful.
     * @return true if the spot is playable (and plays it) false if not (and does not play it).
     */
    public Boolean attemptPlayAtSpot(int col, int row, Square player){
        if (isSpotOnBoard(col, row)) { //I don't like short-ciruit tests, but that could work here.
            if (isSpotFree(col, row)) {  
                //Play is now valid
                //System.out.println("Attempting to play at {" + col + "," + row + ") for player" + player);
                board[col][row] = player;
                return true; 
            }
        }
        return false; //Play is not valid
    }

    /**
     * This is mostly logic for the computer to 'test' as spot -> then reset the board before
     * an actual move is made.
     * @param col -> spot column you want to reset
     * @param row -> spot row you want to reset
     */
    public void resetSpot(int col, int row){
        if (isSpotOnBoard(col, row)) { 
            board[col][row] = Square.EMPTY;
        }
    }

    /**
     * 
     * @param col -> spot column you want to test
     * @param row -> spot row you want to test
     * @return true if the position is on the board (prevent out of range index errors)
     */
    public Boolean isSpotOnBoard(int col, int row){
        if ((col >= 0 && col < maxColumns) && (row >= 0 && row < maxRows)) { 
            return true;
        }
        return false;
    }

    /**
     * 
     * @param col -> spot column you want to test
     * @param row -> spot row you want to test
     * @return true if the position is EMPTY.
     */
    public Boolean isSpotFree(int col, int row){
        if (board[col][row] == Square.EMPTY) {
            return true; 
        }
        return false;
    }

    /**
     * 
     * @param col -> spot column you want to query.
     * @param row -> spot row you want to query.
     * @return EMPTY, CROSS, or RING status of that position
     * @throws IndexOutOfBoundsException if you mess up and don't test the spot first.
     */
    public Square getSpot(int col, int row){
        if (isSpotOnBoard(col, row)) {
            return board[col][row];
        }
        else {
            throw new IndexOutOfBoundsException("Access violation.");
        }
    }

    /**
     * After each move we test for a win condition or a draw. If neither, then
     * game continues.
     * @return TurnResult of a winner, a draw, or game still in play.
     */
    public TurnResult setTurnResult(){
        //System.out.println("\n\n\nEntering the check for turn result");
        int[] winnerCount = {0, 0}; //crossCount, ringCount
        var allDirectionArrays = grabAllPossibleDirections(); //Create a list of all possible win directions

        for(Square[] item: allDirectionArrays){ 
            winnerCount = new int[] {0, 0}; //reset the count for each new possible-win array.
            //System.out.println("\nEntering the check for this array direction.");
            for (int index = 0; index < item.length; ++index){
                //System.out.println("item value: " + item[index]);
                switch (item[index]) {
                    case CROSS: //Add to the cross count streak and reset the ring streak to 0.
                        ++winnerCount[0];
                        winnerCount[1] = 0;
                        break;
                    case RING:
                        ++winnerCount[1]; //reset the cross count streak and add the ring streak
                        winnerCount[0] = 0;
                        break;
                    case EMPTY:  //reset both streaks if EMPTY spot exists - can't win if not strictly in a row.
                        winnerCount[0] = 0;
                        winnerCount[1] = 0;
                }
                //System.out.println("winner 0: " + winnerCount[0] + " winner 1: "+ winnerCount[1]);
                checkForWin(winnerCount);
                //System.out.println("Turn result: " + winner);
                if (winner == TurnResult.CROSS_WINS || winner == TurnResult.RING_WINS) {
                    return winner;
                }
            }
        }
        return winner;
    }

    /**
     * Convert all directions into an array of rows to test in setTurnResult.
     * @return List of 'rows' to test.
     */
    private List<Square[]> grabAllPossibleDirections(){
        List<Square[]> possibleWinArray = new ArrayList<>();
        int[] diagonalUp = {-1, 1}; //column delta, row delta
        int[] accross = {1, 0};
        int[] diagonalDown = {1, 1};
        int[] down = {0, 1};
        
        for (int columnSpotStart = 0, rowSpotStart = 0; columnSpotStart < 2*maxColumns - 1; ++columnSpotStart){
            possibleWinArray.add(extractArray(columnSpotStart, rowSpotStart, diagonalUp));
            possibleWinArray.add(extractArray(columnSpotStart, rowSpotStart, down));
        }
        for (int columnSpotStart = 0, rowSpotStart = -maxRows + 1; rowSpotStart < maxRows; ++rowSpotStart){
            possibleWinArray.add(extractArray(columnSpotStart, rowSpotStart, diagonalDown));
            possibleWinArray.add(extractArray(columnSpotStart, rowSpotStart, accross));
        }
        return possibleWinArray;
    }

    /**
     * Start somewhere (even offboard) and add spots if they exist in the directin you are traveling.
     * @param columnStart - a column spot (not necessary on the board) to start an array from.
     * @param rowStart - a column spot (not necessary on the board) to start an array from.
     * @param direction - Determines if we steping through a row, a column, or a diaganol direction.
     * @return
     */
    private Square[] extractArray(int columnStart, int rowStart, int[] direction){
        List<Square> values = new ArrayList<>(); //Need a list so we can add dynamically.
        int rowIndex = rowStart, columnIndex = columnStart;
        int maxSteps = Math.max(maxColumns, maxRows);
        for(int step = 0; step < maxSteps; ++step){
            if ( isSpotOnBoard(columnIndex, rowIndex)) {
                values.add(board[columnIndex][rowIndex]);
            }
            columnIndex += direction[0];
            rowIndex += direction[1];
        }
        return values.toArray(new Square[0]); //Converts the list to an array of type Square. Had to look that one up :)
    }

    /**
     * Move the logic to here to clear up code.
     * @param winnerCount holds streak for CROSS or RING
     */
    private void checkForWin(int[] winnerCount){
        winner = TurnResult.GAME_NOT_OVER;  //Need this for game reset.
        //Due to how the game plays, we won't ever have both of these true at the same time.
        //So we don't need to make sure the other is not true.
        if (winnerCount[0] == numberToWin) {
            winner = TurnResult.CROSS_WINS;
        }
        if (winnerCount[1] == numberToWin) {
            winner = TurnResult.RING_WINS;
        }
    }

    /**
     * @return Returns a list of all EMPTY spots for Computer move logic.
     */
    public List<int[]> listEmptySpots(){
        List<int[]> emptySpots = new ArrayList<>();
        for(int row = 0; row < maxRows; ++row){
            for (int col = 0; col < maxColumns; ++col){
                if (isSpotFree(col,row)) { 
                    emptySpots.add(new int[] {col, row});
                } 
            }
        }
        return emptySpots;
    }
}