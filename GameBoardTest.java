import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {

    @Test
    void canInstantiateGameBoard() {
        // Exercise the constructor
        GameBoard board = new GameBoard();

        // Verify it actually returned a non-null object
        assertNotNull(board, "GameBoard should be instantiable");
    }
}