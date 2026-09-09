import java.util.ArrayList;
import java.util.List;

import cs251.project2.GomokuInterface.Square;
import cs251.project2.GomokuInterface.TurnResult;

public class GameBoard {

    private int maxColumns;
    private int maxRows;
    private int numberToWin;
    private TurnResult winner;
    private Square[][] board;

    public GameBoard() {
        throw new UnsupportedOperationException("Must call GameBoard(cols, rows, numToWin) instead");
    }
    
    public GameBoard(int columns, int rows, int numToWin){
        maxColumns = columns;
        maxRows = rows;
        numberToWin = numToWin;
        winner = TurnResult.GAME_NOT_OVER;
        board = new Square[maxColumns][maxRows];
        resetBoard();
    }

    private void resetBoard(){
        for (int row = 0; row < maxRows; ++row){
            for (int col = 0; col < maxColumns; ++col){
                board[col][row] = Square.EMPTY;
            }
        }
    }

    public Square getSquare(int col, int row){
        return board[col][row];
    }

    public Boolean attemptPlayAtSpot(int col, int row, Square player){
        if (isSpotOnBoard(col, row)) { //I don't like short-ciruit tests, but that could work here.
            if (isSpotFree(col, row)) {  //Play is valid
                board[col][row] = player;
                return true; 
            }
        }
        return false; //Play is not valid
    }

    private Boolean isSpotOnBoard(int col, int row){
        if ((col >= 0 && col < maxColumns) && (row >= 0 && row < maxRows)) { return true; }
        return false;
    }

    private Boolean isSpotFree(int col, int row){
        if (board[col][row] == Square.EMPTY) { return true; }
        return false;
    }

    public TurnResult setTurnResult(){
        int[] winnerCount = {0, 0}; //crossCount, ringCount
        var allDirectionArrays = grabAllPossibleDirections(); //Create a list of all possible win directions

        for(Square[] item: allDirectionArrays){ 
            winnerCount = new int[] {0, 0}; //reset the count for each new possible-win array.
            for (int index = 0; index < item.length; ++index){
                updateWinnerCount(winnerCount, item[index]);
                checkForWin(winnerCount);
            }
        }
        return winner;
    }

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

    private Square[] extractArray(int columnStart, int rowStart, int[] direction){
        List<Square> values = new ArrayList<>();
        int rowIndex = rowStart, columnIndex = columnStart;
        //Boolean rowInRange = false;
        //Boolean columnInRange = false;
        int maxSteps = Math.max(numberToWin, numberToWin);
        for(int step = 0; step < maxSteps; ++step){
            //rowInRange = false;
            //columnInRange = false;
            //if (rowIndex >= 0 && rowIndex < maxRows) { rowInRange = true; }
            //if (columnIndex >= 0 && columnIndex < maxColumns) { columnInRange = true; }
            //if (rowInRange && columnInRange ){ values.add(board[columnIndex][rowIndex]); }
            if ( isSpotOnBoard(columnIndex, rowIndex)) { values.add(board[columnIndex][rowIndex]); };
            columnIndex += direction[0];
            rowIndex += direction[1];
        }
        return values.toArray(new Square[0]);
    }

    private void updateWinnerCount(int[] winnerCount, Square square){
         switch (square) {
            case CROSS:
                ++winnerCount[0];
                winnerCount[1] = 0;
                break;
            case RING:
                ++winnerCount[1];
                winnerCount[0] = 0;
                break;
            case EMPTY:
                winnerCount[0] = 0;
                winnerCount[1]= 0;
        }
    }

    private void checkForWin(int[] winnerCount){
        //Due to how the game plays, we won't ever have both of these true at the same time.
        //So we don't need to make sure the other is not true.
        if (winnerCount[0] == numberToWin) {
            winner = TurnResult.CROSS_WINS;
        }
        if (winnerCount[1] == numberToWin) {
            winner = TurnResult.RING_WINS;
        }
    }

}
/*
    private Square[][] gameBoard; 



   
    
   
    
// Needed for my computer player math.
    private List<int[]> listEmptySpots(){
        List<int[]> emptySpots = new ArrayList<>();
        for(int row = 0; row < userSelectedNumRows; ++row){
            for (int col = 0; col < userSelectedNumCols; ++col){
                if (isSpotFree(col,row)) { emptySpots.add(new int[] {col, row}); } 
            }
        }
        return emptySpots;
    }
*/