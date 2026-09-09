import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

import cs251.project2.GomokuInterface.Square;

class GameBoardTest {

    GameBoard board;
    @BeforeEach
    void createInstance() {
        board = new GameBoard(3, 3);
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
}