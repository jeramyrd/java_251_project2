
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ArgCheckTest{
    @Test
    void testNullArgs(){
        String[] nullString = null;
        int[] vString = ArgCheck.validateStartStrings(nullString);
        assertEquals(1, vString.length, "null string should return empty array.");
    }

    @Test
    void testEmptyStringArgs(){
        String[] emptyString = { "" };
        int[] vString = ArgCheck.validateStartStrings(emptyString);
        assertEquals(1, vString.length, "Empty string should return empty array");
    }

    @Test
    void test1Args(){
        String[] testString = { "30" };
        int[] vString = ArgCheck.validateStartStrings(testString);
        assertEquals(1, vString.length,  "One valid string should return empty array");
    }

    @Test
    void test1badArgs(){
        String[] testString = { "one" };
        int[] vString = ArgCheck.validateStartStrings(testString);
        assertEquals(1, vString.length, "One invalid string should return empty array");
    }

    @Test
    void test2Args(){
        String[] testString = { "30", "30" };
        int[] vString = ArgCheck.validateStartStrings(testString);
        assertEquals(1, vString.length, "Two valid strings should return empty array");
    }

    @Test
    void test2badArgs(){
        String[] testString = { "gee", "whiz" };
        int[] vString = ArgCheck.validateStartStrings(testString);
        assertEquals(1, vString.length, "Two invalid string should return empty array");
    }

    @Test
    void test3Args(){
        String[] testString = { "30", "30", "5" };
        int[] expected = {0, 30,30,5};
        assertArrayEquals(expected, ArgCheck.validateStartStrings(testString), "Valid 3 should return valid three");
    }

    @Test
    void test3ArgsPlusGarbage(){
        String[] testString = { "gee", "30", "whiz", "sam", "30", "is", "a", "5", "lot", "45" };
        int[] expected = {0, 30,30,5};
        assertArrayEquals(expected, ArgCheck.validateStartStrings(testString), "Lots should return valid first 3");
    }

    @Test
    void testNegativeSet(){
        String[] testString = { "-30", "-30", "-4"};
        int[] expected = {0, 30,30,4};
        assertArrayEquals(expected, ArgCheck.validateStartStrings(testString), "Lots should return valid first 3");
    }

    @Test
    void testZeroSet(){
        String[] testString = { "0", "0", "0"};
        int[] expected = {0, 1,1,1};
        assertArrayEquals(expected, ArgCheck.validateStartStrings(testString), "Lots should return valid first 3");
    }

    @Test
    void testRailedSet(){
        String[] testString = { "1000", "-1000", "1000"};
        int[] expected = {0, ArgCheck.MAXCOLS,ArgCheck.MAXROWS,ArgCheck.MAXNUMTOWIN};
        assertArrayEquals(expected, ArgCheck.validateStartStrings(testString), "Lots should return valid first 3");
    }
}

