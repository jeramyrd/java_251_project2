
import java.util.Random;
import cs251.project2.*;

public class Gomoku implements GomokuInterface{
    
    private GameBoard gameBoard;
    private int computerSelection = 0;
    private Square computerTurn = Square.CROSS; //If a computer is used, it plays the CROSS.
    private Square currentTurn = Square.EMPTY;
    private Minerva minervaPlayer;
    private Computer computerPlayer;


    public Gomoku(String[] args){
        int[] validatedStartParameters = ArgCheck.validateStartStrings(args);
        for(int arg: validatedStartParameters){
            System.out.print("Arg: " + arg);
        }
        computerSelection = validatedStartParameters[0];
        if (validatedStartParameters.length > 1){ //only filled if valid.
            gameBoard = new GameBoard(validatedStartParameters[1], validatedStartParameters[2], validatedStartParameters[3]);
        }
        else{

            gameBoard = new GameBoard(DEFAULT_NUM_COLS, DEFAULT_NUM_ROWS, SQUARES_IN_LINE_FOR_WIN);            
        }
        minervaPlayer = new Minerva(gameBoard);
        computerPlayer = new Computer(gameBoard);
        selectFirstPlayer();
    }

    private void selectFirstPlayer(){
        //Not fair that the human always starts, so we randomly pick.
        Random coin = new Random();
        if(coin.nextBoolean()){
            currentTurn = Square.RING; //Human, if used, goes first. ;)
        }
        else{
            currentTurn = Square.CROSS; //computer, if used goes first.
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

    //Interface methods
    public int getNumRows() {return gameBoard.getMaxRows();}
    public int getNumCols(){ return gameBoard.getMaxColumns();}
    public int getNumInLineForWin() {return gameBoard.getWinCount();}
    
    public TurnResult handleClickAt(int row, int col){
        var checkForDraw = gameBoard.listEmptySpots();
        if (checkForDraw.isEmpty()){
            return TurnResult.DRAW;
        }
        if (currentTurn == computerTurn && computerSelection > 0){ //ensure a computer is playing and it is its turn.
            var move = runComputerTurn();
            if (move[0] == -1) { return TurnResult.DRAW; }
            if ( gameBoard.attemptPlayAtSpot(move[0], move[1], computerTurn) ) { changePlayer(); }
        }
        else {
            if ( gameBoard.attemptPlayAtSpot(col, row, currentTurn)) { changePlayer(); }
        }
        return gameBoard.setTurnResult();
    }

    public void initGame() {
        gameBoard.resetBoard();
        if (currentTurn == computerTurn && computerSelection > 0) {
            runComputerTurn(); //We don't care about the return result, its the first move.
        }
    }

    private int[] runComputerTurn(){
        int[] move = {0,0};
        switch (computerSelection){
                case 1:
                    move = computerPlayer.bestMove();
                    break;
                case 2:
                    move = minervaPlayer.bestMove();
                    break;
                default:
                    //Something is broken, revert to a human player.
                    throw new UnsupportedOperationException("Somehow you think you are running a computer, but I don't know which one.");
        }
        return move;
    }

    public String getBoardString(){
        StringBuilder boardSB = new StringBuilder();
        for (int row = 0; row < gameBoard.getMaxRows(); ++row){
            for (int col = 0; col < gameBoard.getMaxColumns(); ++col){
                boardSB.append(gameBoard.getSpot(col, row).toChar());
            }
            boardSB.append('\n');
        }
        return boardSB.toString();
    }
    public Square getCurrentPlayer(){
        return currentTurn;
    }
    public void initComputerPlayer(String difficultyLevel){
        //We are not using this method to create the Computer player
        //I wanted a way to pick everything from command line arguments.
    }

    public static void main ( String [] args ) {
        Gomoku game = new Gomoku(args);
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