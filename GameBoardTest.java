import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import cs251.project2.GomokuInterface.Square;

class GameBoardTest {

    @Test
    void defaultConstructorFails() {
        assertThrows(UnsupportedOperationException.class,
            () -> new GameBoard(),
            "No-arg GameBoard() should throw");
    }

    @Test
    void newBoardIsFullOfEmptySquares() {
        GameBoard board = new GameBoard(3, 3);
        for(int row = 0; row < 3; ++row){
            for(int col = 0; col < 3; ++col){
                assertEquals(Square.EMPTY,  board.getSquare(col, row));
            }
        }
    }

    @Test
    void boardIsMadeOfSquareEnums() {
        GameBoard board = new GameBoard(3, 3);
        assertInstanceOf(Square.class,  board.getSquare(1,1));
    }
}