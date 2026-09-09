
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

import cs251.project2.*;

public class Gomoku implements GomokuInterface{

    //Interface methods
    public int getNumRows() {return userSelectedNumRows;}
    public int getNumCols(){ return userSelectedNumCols;}
    public int getNumInLineForWin() {return userSelectedNumInLineForWin;}
    public TurnResult handleClickAt(int row, int col){
        // Prevent out-of-bound events.
        if (row >= userSelectedNumRows || col >= userSelectedNumCols) { return TurnResult.GAME_NOT_OVER;}
        if (isSpotFree(col, row)){ 
            gameBoard[col][row] = currentTurn;
            //changePlayer();
            
        // while(victoryIsBelongTo == TurnResult.GAME_NOT_OVER){
            var computerMove = bestMove();
            if (indexOnBoard(computerMove[0], computerMove[1])) {
                gameBoard[computerMove[0]][computerMove[1]] = Square.CROSS;
            }
            else { return TurnResult.DRAW; }
            //boardFullCheck(); //Draw if board is full (brute force check, not a smart check)
            //changePlayer(); //game not over and change player.
            isGameOver();
        
        //  computerMove = bestMove();
        //  if (indexOnBoard(computerMove[0], computerMove[1])) {
        //      gameBoard[computerMove[0]][computerMove[1]] = Square.RING;
        //  }
        // else { return TurnResult.DRAW; }
            //boardFullCheck(); //Draw if board is full (brute force check, not a smart check)
            //changePlayer(); //game not over and change player.
        // isGameOver();
        // }
        }
        return victoryIsBelongTo;
    }
    public void initGame() {
        resetBoard();
        victoryIsBelongTo = TurnResult.GAME_NOT_OVER;
    }
    public String getBoardString(){
        StringBuilder boardSB = new StringBuilder();
        for (int row = 0; row < userSelectedNumRows; ++row){
            for (int col = 0; col < userSelectedNumCols; ++col){
                boardSB.append(gameBoard[col][row].toChar());
            }
            boardSB.append('\n');
        }
        return boardSB.toString();
    }
    public Square getCurrentPlayer(){
        return currentTurn;
    }
    public void initComputerPlayer(String difficultyLevel){
    }


    private int userSelectedNumCols = 0;
    private int userSelectedNumRows = 0;
    private int userSelectedNumInLineForWin = 0;
    private Square currentTurn = Square.EMPTY;
    private TurnResult victoryIsBelongTo = TurnResult.GAME_NOT_OVER;

    public Gomoku(String[] args){
        int[] validatedStartParameters = ArgCheck.validateStartStrings(args);
        if (validatedStartParameters.length > 0){
            userSelectedNumRows = validatedStartParameters[0];
            userSelectedNumCols = validatedStartParameters[1];
            userSelectedNumInLineForWin = validatedStartParameters[2];
        }
        else{
            userSelectedNumRows = DEFAULT_NUM_ROWS;
            userSelectedNumCols = DEFAULT_NUM_COLS;
            userSelectedNumInLineForWin = SQUARES_IN_LINE_FOR_WIN;            
        }
        randomFirstPlayer();
    }

    private Square[][] gameBoard; 
    private void resetBoard(){
        gameBoard = new Square[userSelectedNumCols][userSelectedNumRows];
        for (int row = 0; row < userSelectedNumRows; ++row){
            for (int col = 0; col < userSelectedNumCols; ++col){
                gameBoard[col][row] = Square.EMPTY;
            }
        }
    }
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





    private void randomFirstPlayer(){
        Random coin = new Random();
        if(coin.nextBoolean()){
            currentTurn = Square.RING;
        }
        else{
            currentTurn = Square.RING;
        }
    }
    private void changePlayer(){
        if (currentTurn == Square.CROSS){
            currentTurn = Square.RING;
        }
        else {
            currentTurn = Square.CROSS;
        }
    }

    private int[] bestMove(){
        var testList = listEmptySpots();
        double ringHighScore = 0;
        double crossHighScore = 0;
        int[] bestRingSpot = {-1, -1};
        int[] bestCrossSpot = {-1, -1};
        double scale = 1.0 / (4.0 * userSelectedNumInLineForWin);
        //System.out.println("Scale is: " + scale);
        int[] directionColumn = {1, 0, 1, -1};
        int[] directionRow = {0, 1, 1, 1};
        int crossCount = 0, ringCount = 0;
        double ringLocalScore = 0;
        double crossLocalScore = 0;
        int tempCol = 0;
        int tempRow = 0;
        int offGridScaler = 1;

        //loop through all empty spots
       // System.out.println("STARTING THE SCORING SPREE!!!!!");
        for (int[] spot: testList){            
        //    System.out.println("Empty Spot (" + spot[0] + "," + spot[1] +") ");
            //Need to calculate 4 different directions
            ringLocalScore = 0;
            crossLocalScore = 0;
            for (int directionIndex = 0; directionIndex < 4; ++directionIndex){
             //   System.out.println("Direction: " + directionIndex + " ---> 0 right 1 down 2 Dup 3 Ddown");

                //Now we have userSelectedNumInLineForWin sets to calculate....
                for (int setShift = userSelectedNumInLineForWin - 1; setShift >= 0; --setShift ){
              //      System.out.println("Setshift is " + setShift);
                    tempCol = spot[0]-directionColumn[directionIndex]*setShift;
                    tempRow = spot[1]-directionRow[directionIndex]*setShift;
              //      System.out.println("SetShifted Start spot is (" + tempCol + "," + tempRow +") ");
                    ringCount = 0;
                    crossCount = 0;
                    offGridScaler = 1;

                    //Now finally, calculate the block. 
                    for (int i = 0; i < userSelectedNumInLineForWin; ++i){
                   //     System.out.println("Array Spot is currently (" + tempCol + "," + tempRow +") ");
                        if (indexOnBoard(tempCol, tempRow)){
                            switch (gameBoard[tempCol][tempRow]) {
                                case CROSS:
                                    ++crossCount;
                                    break;
                                case RING:
                                    ++ringCount;
                                    break;
                                case EMPTY:
                                    break;
                            }
                        }
                        else { offGridScaler = 0; }
                        tempCol += directionColumn[directionIndex];
                        tempRow += directionRow[directionIndex];
                    }
                //    System.out.println("Offgrid (zero is yes): " + offGridScaler);
                //    System.out.println("crossCount = " + crossCount + " So math is:" + Math.pow(10, crossCount));
                //    System.out.println("ringCount = " + ringCount + " So math is:" + Math.pow(10, ringCount));
                    //finished the set - add to total
                //    System.out.println("Cross score add would be: " + offGridScaler*scale*Math.pow(10, crossCount));
                //    System.out.println("Ring score add would be: " + offGridScaler*scale*Math.pow(10, ringCount));
                    if (ringCount == 0) { crossLocalScore += offGridScaler*scale*Math.pow(10, crossCount);}
                    if (crossCount == 0) { ringLocalScore += offGridScaler*scale*Math.pow(10, ringCount);}
                //    System.out.println("Ring Local score: " + ringLocalScore + " Cross Local Score: " + crossLocalScore);
                }
            }
            if (ringLocalScore > ringHighScore) {
                ringHighScore = ringLocalScore;
                bestRingSpot = spot;
            }
            if (crossLocalScore > crossHighScore) {
                crossHighScore = crossLocalScore;
                bestCrossSpot = spot;
            }
            System.out.println("Ring High score: " + ringHighScore + " Ring best spot (" + bestRingSpot[0] + "," + bestRingSpot[1] + ")");
            System.out.println("Cross High score: " + crossHighScore + " Cross best spot (" + bestCrossSpot[0] + "," + bestCrossSpot[1] + ")");
        }
        if (ringHighScore > crossHighScore) {return bestRingSpot;}
        else { return bestCrossSpot; }
    }



    public static void main ( String [] args ) {
        Gomoku game = new Gomoku(args);
        game.initComputerPlayer("NONE");
        //if ( args.length > 0) {
        //    game.initComputerPlayer(args[0]);
        // }
        GomokuGUI.showGUI(game);
    }



}

/*
\
3) Computer player. Basic bruh - handleClickAt needs to follow up with a computer move. Moves are legal, but can be stupid- but no random or just picking the first square available. It should be able to win if the human is not paying attention (OR ALWAYS MAHAHAHAHAHAH)
Computer is only triggered by the initComputerPlayer method. 
NONE = no computer.
COMPUTER = computer. 
GODMODE = you are not going to win :)

class GomokuGUI 
static void showGUI(GomokuInterface model)

GomokuInterface
static enum GomokuInterface.Square -> represents the status of each square on the board. 
static enum GomokuInterface.TurnResult -> Type to represent correct outcome status of the game. 

Fields:
static final int DEFAULT_NUM_COLS
static final int DEFAULT_NUM_ROWS
static final int SQUARES_IN_LINE_FOR_WIN

String getBoardString() 
GomokuInterface.Square getCurrentPlayer()
int getNumCols()
int getNumInLineForWin()
int getNumRows()
GomokuInterface.TurnResult handleClickAt(int row, int col)
void initComputerPlayer(String opponent)
void initGame()
Square.toChar method 
-, O, X and newline

GomokuInterface.Square
CROSS, EMPTY, RING
char toChar()
static GomokuInterface.Square valueOf(String name)
Returns the enum constant of this class with the specified name.

static GomokuInterface.Square values()
Returns an array containing the constants of this enum class, in the order they are declared.

public static enum GomokuInterface.TurnResult
CROSS_WINS
DRAW
GAME_NOT_OVER
RING_WINS


I can make new pieces appear on the board in response to mouse clicks
I correctly detect legal moves
I alternate whose turn it is

I correctly check for 5 in a row in all directions (horizontal, vertical, both diagonals)
I can detect the end of the game
I can determine who has won the game or if it is a tie
I have a computer player, but it doesn't make legal moves

I have a computer player that makes legal moves

I have a computer player that makes legal moves that are better than just random or first available spot

I'm done and have submitted my assignment


*/