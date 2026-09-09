import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import cs251.project2.Interface.Square;

class GameBoardTest {

    @Test
    void defaultConstructorFails() {
        assertThrows(UnsupportedOperationException.class,
            () -> new GameBoard(),
            "No-arg GameBoard() should throw");
    }

    @Test
    void boardIsMadeOfSquareEnums() {
       // GameBoard board = new GameBoard(3, 3);
      //  assertInstanceOf(Square, board[1][1]);
    }




}