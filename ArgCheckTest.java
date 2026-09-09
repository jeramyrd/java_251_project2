
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ArgCheckTest{
    @Test
    void testNullArgs(){
        String[] nullString = null;
        assertArrayEquals(new int[0], ArgCheck.validateStartStrings(nullString), "null string should return empty array.");
    }

    @Test
    void testEmptyStringArgs(){
        String[] emptyString = { "" };
        assertArrayEquals(new int[0], ArgCheck.validateStartStrings(emptyString), "Empty string should return empty array");
    }

    @Test
    void test1Args(){
        String[] testString = { "30" };
        assertArrayEquals(new int[0], ArgCheck.validateStartStrings(testString), "One valid string should return empty array");
    }

    @Test
    void test1badArgs(){
        String[] testString = { "one" };
        assertArrayEquals(new int[0], ArgCheck.validateStartStrings(testString), "One invalid string should return empty array");
    }

    @Test
    void test2Args(){
        String[] testString = { "30", "30" };
        assertArrayEquals(new int[0], ArgCheck.validateStartStrings(testString), "Two valid strings should return empty array");
    }

    @Test
    void test2badArgs(){
        String[] testString = { "gee", "whiz" };
        assertArrayEquals(new int[0], ArgCheck.validateStartStrings(testString), "Two invalid string should return empty array");
    }

    @Test
    void test3Args(){
        String[] testString = { "30", "30", "5" };
        int[] expected = {30,30,5};
        assertArrayEquals(expected, ArgCheck.validateStartStrings(testString), "Valid 3 should return valid three");
    }

    @Test
    void test3ArgsPlusGarbage(){
        String[] testString = { "gee", "30", "whiz", "sam", "30", "is", "a", "5", "lot", "45" };
        int[] expected = {30,30,5};
        assertArrayEquals(expected, ArgCheck.validateStartStrings(testString), "Lots should return valid first 3");
    }

    @Test
    void testNegativeSet(){
        String[] testString = { "-30", "-30", "-4"};
        int[] expected = {30,30,4};
        assertArrayEquals(expected, ArgCheck.validateStartStrings(testString), "Lots should return valid first 3");
    }

    @Test
    void testZeroSet(){
        String[] testString = { "0", "0", "0"};
        int[] expected = {1,1,1};
        assertArrayEquals(expected, ArgCheck.validateStartStrings(testString), "Lots should return valid first 3");
    }

    @Test
    void testRailedSet(){
        String[] testString = { "1000", "-1000", "1000"};
        int[] expected = {ArgCheck.MAXROWS,ArgCheck.MAXCOLS,ArgCheck.MAXNUMTOWIN};
        assertArrayEquals(expected, ArgCheck.validateStartStrings(testString), "Lots should return valid first 3");
    }
}

