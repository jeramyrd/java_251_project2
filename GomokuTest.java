
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import cs251.project2.*;

//import org.junit.jupiter.api.BeforeEach;

class GomokuTest{

    @Test
    void testBadArgsInitializationGivesDefault(){
        Gomoku game = new Gomoku(new String[] {"one", "two", "three"});
        assertEquals(GomokuInterface.DEFAULT_NUM_COLS, game.getNumCols(), "Should return the default value for number of rows");
        assertEquals(GomokuInterface.DEFAULT_NUM_ROWS, game.getNumRows(), "Should return the default value for number of columns");
        assertEquals(GomokuInterface.SQUARES_IN_LINE_FOR_WIN, game.getNumInLineForWin(), "Should return the default value for number of NumInLineForWin");
    }

    @Test
    void testZeroArgsInitializationGivesDefault(){
        Gomoku game = new Gomoku(new String[] {"0", "0", "0"});
        assertEquals(1, game.getNumCols(), "Should return the default value for number of rows");
        assertEquals(1, game.getNumRows(), "Should return the default value for number of columns");
        assertEquals(1, game.getNumInLineForWin(), "Should return the default value for number of NumInLineForWin");
    }

    @Test
    void testGoodArgsInitializationGivesUnique(){     
        Gomoku game = new Gomoku(new String[] {"20", "25", "7"});
        assertEquals(20, game.getNumRows(), "Should return the default value for number of rows");
        assertEquals(25, game.getNumCols(), "Should return the default value for number of columns");
        assertEquals(7, game.getNumInLineForWin(), "Should return the given set for size of NumInLineForWin.");
    }

    @Test
    void testTooBig(){     
        Gomoku game = new Gomoku(new String[] {"135", "145", "117"});
        assertEquals(ArgCheck.MAXROWS, game.getNumRows(), "Should return the MAXSIZE value for number of rows");
        assertEquals(ArgCheck.MAXCOLS, game.getNumCols(), "Should return the MAXSIZE value for number of columns");
        assertEquals(ArgCheck.MAXNUMTOWIN, game.getNumInLineForWin(), "Should return the MAXSIZE for the NumInLineForWin");
    }

    @Test 
    void resetBoardSmall(){
        Gomoku game = new Gomoku(new String[] {"3", "3", "3"});
        String smallBoardZeros = "---\n---\n---\n";
        game.initGame();
        assertEquals(smallBoardZeros, game.getBoardString(), "Small board should match");
    }

    @Test 
    void resetBoardSmallRec(){
        Gomoku game = new Gomoku(new String[] {"3", "10", "3"});
        String smallBoardZeros = "----------\n----------\n----------\n";
        game.initGame();
        assertEquals(smallBoardZeros, game.getBoardString(), "Small board should match");
    }


}

