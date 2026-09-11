
import java.util.Random;
import cs251.project2.*;

/**
 * @author  Jeramy Dickerson
 * CS 251 with Professor Brooke Chenoweth - Fall 2026
 * Project 2 - Gomoku Game
 * 
 * Main lives here.
 * Use GomokuInterface to allow GUI to run. This runs the game flow.
 * Computer logic exists in Computer.java and Minerva.java.
 * Input checks live in ArgCheck.java (allows you to customize game on command line)
 * GameBoard holds game mechanics logic
 */
public class Gomoku implements GomokuInterface{
    
    private GameBoard gameBoard;
    private int computerSelection = 0;
    private Square computerTurn = Square.CROSS; //If a computer is used, it plays the CROSS.
    private Square currentTurn = Square.EMPTY;
    private Minerva minervaPlayer;
    private Computer computerPlayer;

    /**
     * @param args command line arguments you can use to customize game and computer you play
     * Examples:
     * java -cp project2.jar:. Gomoku Minerva 3 3 3 -> to always draw Tic-Tac-Toe"
     * java -cp project2.jar:. Gomoku Computer 10 10 5 -> easy mode
     * java -cp project2.jar:. Gomoku Minerva -> hard mode, default
     * java -cp project2.jar:. Gomoku 80 40 7 -> fun(?) custom game for two players that will never end.
     * Sets up board and selects a random player to start.
     */
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

    /**** Interface methods *****/

    /**
     * @see cs251.project2.GomokuInterface#getNumRows()
     * @return Max number of rows
     */
    public int getNumRows() {
        return gameBoard.getMaxRows();
    }
    
    /**   
     * 
     * @see cs251.project2.GomokuInterface#getNumCols()
     * @return Max number of rows
     */
    public int getNumCols() { 
        return gameBoard.getMaxColumns();
    }

    /**   
     * 
     * @see cs251.project2.GomokuInterface#getNumInLineForWin()
     * @return Max of sequential marks needed to win.
     */
    public int getNumInLineForWin() {
        return gameBoard.getWinCount();
    }
    
    /**
     * 
     * @see cs251.project2.GomokuInterface#handleClickAt(int, int)
     * @param row -> from user clicked row, attempt a move.
     * @param col -> from user clicked col, attempt a move.
     * @return TurnResult -> If the move worked, update TurnResult (win, lose, draw, or keep going)
     */
    public TurnResult handleClickAt(int row, int col){
        if (isGameADraw()){
            return TurnResult.DRAW;
        }
        TurnResult winCondition;

        if (computerSelection == 0) { // Two players, no checks needed, just process input
            if ( gameBoard.attemptPlayAtSpot(col, row, currentTurn)) {
                changePlayer(); 
            }
        }
        else { //If player starts, do both, if computer starts, skip first 'handleClickAt' is program driven, skip player.
            if (currentTurn != computerTurn){
                if ( gameBoard.attemptPlayAtSpot(col, row, currentTurn)) { 
                    changePlayer(); 
                }
                winCondition = gameBoard.setTurnResult();
                if (winCondition == TurnResult.RING_WINS) { 
                    return winCondition; 
                }
            }
            var move = runComputerTurn();
            System.out.println("Just recieved the computer move");
            if (move[0] == -1) { //No moves can win
                return TurnResult.DRAW; 
            } 
            if ( gameBoard.attemptPlayAtSpot(move[0], move[1], computerTurn) ) { 
                changePlayer(); 
                System.out.println("Just played from the handler the computer move and changed players.");
            }

        }
        return gameBoard.setTurnResult();
    }

    /**
     * @return true if there are no more empty spots
     */
    private boolean isGameADraw(){
        var checkForDraw = gameBoard.listEmptySpots();
        if (checkForDraw.isEmpty()){
            return true;
        }
        return false;
    }

    /**    
     * start the game. Let's the winning player move first next game. Harsh. 
     * @see cs251.project2.GomokuInterface#initGame()
     */
    public void initGame() {
        gameBoard.resetBoard();
        changePlayer(); // We always toggle to the other player after a move. Game requirement is that the winner starts, so revert change.
        if (currentTurn == computerTurn && computerSelection > 0) {
            handleClickAt(0, 0); //Trigger computer move, it will recalculate it there.
        }
    }

    /**
     * Run the computer move be letting the computer logic select its best move.
     * Right now I have to instantiate every computer model, even though I know there is only one.
     * How do I fix that. 
     */
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
                throw new UnsupportedOperationException("Somehow you think you are running a computer, but I don't know which one.");
        }
        return move;
    }

    /**
     * 
     * @see cs251.project2.GomokuInterface#getBoardString()
     * @return a string formated array the GUI internally translates to make the board.
     */
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

    /**   
     * @see cs251.project2.GomokuInterface#getCurrentPlayer()
     * @return the current player, which we keep as a field.
     */
    public Square getCurrentPlayer(){
        return currentTurn;
    }
    /**
     * 
     * @see cs251.project2.GomokuInterface#initComputerPlayer(java.lang.String)
     * As there is not a dialog box to select the computer difficulty, we handle this logic 
     * elsewhere. This is just an empty class to fullfil the requirement from GomokuInterface.
     * @param difficultyLevel -> A string we ignore.
     */
    public void initComputerPlayer(String difficultyLevel){
    }

    /**
     * Grab that info and start the game!
     * @param args see constructor Gomoku
     */
    public static void main ( String [] args ) {
        Gomoku game = new Gomoku(args);
        GomokuGUI.showGUI(game);
    }
}