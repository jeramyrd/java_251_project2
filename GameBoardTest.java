import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

import cs251.project2.GomokuInterface.Square;
import cs251.project2.GomokuInterface.TurnResult;

class GameBoardTest {

    GameBoard board;
    @BeforeEach
    void createInstance() {
        board = new GameBoard(3, 3, 3);
    }

    @Test
    void defaultConstructorFails() {
        assertThrows(UnsupportedOperationException.class,
            () -> new GameBoard(),
            "No-arg GameBoard() should throw");
    }

    @Test
    void newBoardIsFullOfEmptySquares() {
        for(int row = 0; row < 3; ++row){
            for(int col = 0; col < 3; ++col){
                assertEquals(Square.EMPTY,  board.getSquare(col, row));
            }
        }
    }

    @Test
    void boardIsMadeOfSquareEnums() {
        assertInstanceOf(Square.class,  board.getSquare(1,1));
    }

    @Test
    void throwsOutOfIndexError() {
        assertThrows(IndexOutOfBoundsException.class,
            () -> board.getSquare(4,4),
            "index out of bounds should throw");
    }

    @Test
    void returnFalseIfSpotIsFull() {
        board.attemptPlayAtSpot(1, 1, Square.CROSS);
        assertEquals(false, board.attemptPlayAtSpot(1, 1, Square.CROSS));
    }

    @Test
    void returnFalseForOutOfBoundsPlay() {
        assertEquals(false, board.attemptPlayAtSpot(10, 10, Square.CROSS));
    }

    @Test
    void returnTrueForEmptyValidSpot() {
        assertEquals(true, board.attemptPlayAtSpot(1, 1, Square.CROSS));
    }

    @Test
    void checkForCrossWinOne() {
        board.attemptPlayAtSpot(0, 0, Square.CROSS);
        board.attemptPlayAtSpot(1, 0, Square.CROSS);
        board.attemptPlayAtSpot(2, 0, Square.CROSS);
        assertEquals(TurnResult.CROSS_WINS, board.setTurnResult());
    }

    @Test
    void checkForCrossWinTwo() {
        board.attemptPlayAtSpot(0, 0, Square.CROSS);
        board.attemptPlayAtSpot(1, 1, Square.CROSS);
        board.attemptPlayAtSpot(2, 2, Square.CROSS);
        assertEquals(TurnResult.CROSS_WINS, board.setTurnResult());
    }

    @Test
    void checkForRingWinOne() {
        board.attemptPlayAtSpot(0, 0, Square.RING);
        board.attemptPlayAtSpot(0, 1, Square.RING);
        board.attemptPlayAtSpot(0, 2, Square.RING);
        assertEquals(TurnResult.RING_WINS, board.setTurnResult());
    }

    @Test
    void checkForGameNotOver() {
        board.attemptPlayAtSpot(0, 0, Square.RING);
        board.attemptPlayAtSpot(0, 1, Square.CROSS);
        board.attemptPlayAtSpot(0, 2, Square.RING);
        assertEquals(TurnResult.GAME_NOT_OVER, board.setTurnResult());
    }
}