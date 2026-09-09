import cs251.project2.GomokuInterface.Square;

public class GameBoard {

    private int maxColumns;
    private int maxRows;
    private Square[][] board;

    public GameBoard() {
        throw new UnsupportedOperationException("Must call GameBoard(cols, rows) instead");
    }
    
    public GameBoard(int columns, int rows){
        maxColumns = columns;
        maxRows = rows;
        board = new Square[maxColumns][maxRows];
        resetBoard();
    }

    private void resetBoard(){
        for (int row = 0; row < maxRows; ++row){
            for (int col = 0; col < maxColumns; ++col){
                board[col][row] = Square.CROSS;
            }
        }
    }

    public Square getSquare(int col, int row){
        return board[col][row];
    }

}


/*
    private Square[][] gameBoard; 

    private Boolean indexOnBoard(int col, int row){
        if ((col >= 0 && col < userSelectedNumCols) && (row >= 0 && row < userSelectedNumRows)) { return true; }
        return false;
    }

    private Boolean isSpotFree(int col, int row){
        if (gameBoard[col][row] == Square.EMPTY) { return true; }
        return false;
    }
    private Boolean isGameOver(){
        int[] winnerCount = {0, 0}; //crossCount, ringCount
        var allDirectionArrays = grabAllPossibleDirections();

        for(Square[] item: allDirectionArrays){
            updateWinnerCount(winnerCount,Square.EMPTY);
            for (int index = 0; index < item.length; ++index){
                updateWinnerCount(winnerCount, item[index]);
                if (isThereAWinner(winnerCount)) {return true;}
            }
        }
        System.out.println();
        return false; 
    }
    private List<Square[]> grabAllPossibleDirections(){
        List<Square[]> possibleWinArray = new ArrayList<>();
        int[] diagonalUp = {-1, 1}; //column delta, row delta
        int[] accross = {1, 0};
        int[] diagonalDown = {1, 1};
        int[] down = {0, 1};
        
        for (int columnSpotStart = 0, rowSpotStart = 0; columnSpotStart < 2*userSelectedNumCols - 1; ++columnSpotStart){
            possibleWinArray.add(extractArray(columnSpotStart, rowSpotStart, diagonalUp));
            possibleWinArray.add(extractArray(columnSpotStart, rowSpotStart, down));
        }
        for (int columnSpotStart = 0, rowSpotStart = -userSelectedNumRows + 1; rowSpotStart < userSelectedNumRows; ++rowSpotStart){
            possibleWinArray.add(extractArray(columnSpotStart, rowSpotStart, diagonalDown));
            possibleWinArray.add(extractArray(columnSpotStart, rowSpotStart, accross));
        }
        return possibleWinArray;
    }
    private Square[] extractArray(int columnStart, int rowStart, int[] direction){
        List<Square> values = new ArrayList<>();
        int rowIndex = rowStart, columnIndex = columnStart;
        Boolean rowInRange = false;
        Boolean columnInRange = false;
        int maxSteps = Math.max(userSelectedNumCols, userSelectedNumRows);
        for(int step = 0; step < maxSteps; ++step){
            rowInRange = false;
            columnInRange = false;
            if (rowIndex >= 0 && rowIndex < userSelectedNumRows) { rowInRange = true; }
            if (columnIndex >= 0 && columnIndex < userSelectedNumCols) { columnInRange = true; }
            if (rowInRange && columnInRange ){ values.add(gameBoard[columnIndex][rowIndex]); }
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
    private Boolean isThereAWinner(int[] winnerCount){
        if (winnerCount[0] == userSelectedNumInLineForWin) {
            victoryIsBelongTo = TurnResult.CROSS_WINS;
            return true;
        }
        if (winnerCount[1] == userSelectedNumInLineForWin) {
            victoryIsBelongTo = TurnResult.RING_WINS;
            return true;
        }
        return false; //No winner :(
    }
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