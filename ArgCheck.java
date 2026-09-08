/**
 * @author  Jeramy Dickerson
 * CS 251 with Professor Brooke Chenoweth - Fall 2026
 * 
 * ArgCheck is designed to handle main args to make it suitable for the program. 
 * Cases we are OK with:
 * No args up to less than 3 -> An empty list is sent back
 * 3 or more int values, -> first 3 sanitized to be in 1 to 100 range. 
 */
public final class ArgCheck {

    // Prevent instantiation
    private ArgCheck() {}
    public final static int MAXCOLS = 80;
    public final static int MAXROWS = 40;
    public final static int MAXNUMTOWIN = Math.min(MAXCOLS, MAXROWS);
    
    /**
     * @param args can contain whatever you want. 
     * Returns either an empty Integer array or an array of 3 validated integers. 
     */
    public static int[] validateStartStrings(String[] args){
        int[] validArray = new int[0];
        if (args == null){ return validArray;}

        
        int validIntegerToFind = 0; 
        int rows = 0;
        int columns = 0;
        int toWinLength = 0;
        
        //Loop through all arguments provided, searching for and storing the first 3 integers. Overkill for the assignment...
        for (String arg : args) {
            if (isInteger(arg)) {
                switch (validIntegerToFind) {
                    case 0:
                        rows = Math.min(MAXROWS, correctUserInput(Integer.parseInt(arg)));
                        validIntegerToFind++;
                        break;
                    case 1:
                        columns = Math.min(MAXCOLS, correctUserInput(Integer.parseInt(arg)));
                        validIntegerToFind++;
                        break;
                    case 2:
                        toWinLength = Math.min(MAXNUMTOWIN, correctUserInput(Integer.parseInt(arg)));
                        validIntegerToFind++;
                        break;
                    default:
                        //System.out.println("Thats more integers than I need. Let me place that carefully here... *places in the garbage bin*");
                        break;
                }
            }
        }
        if (validIntegerToFind > 2){
            validArray = new int[] {rows, columns, toWinLength};
            System.out.println(validArray);
            return validArray;
        }
        else{
            return validArray;
        }
    }

    /**
     * Returns true if the given string can be parsed as an integer.
     *
     * @param s the string to test (may be null or empty)
     * @return true if s is a valid base-10 integer, false otherwise
     */
    private static boolean isInteger(String s){ 
        if (s == null || s.isEmpty()) {
            //System.out.println("Does not compute...");
            return false;
        }
        try {
            Integer.parseInt(s);
            return true; //It can be done!
        }
        catch (NumberFormatException e){
            //System.out.println("Does not compute...");
            return false; //It cannot be done!
        }
    }

    /**
     * Sanitizes a user-supplied integer:  
     *  – maps 0 → 1 with a warning,  
     *  – maps negative values → their absolute value,  
     *  – Finally if x > 100, reduce to 100 max. 
     *
     * @param x the raw integer to correct
     * @return a positive non-zero integer
     */
    private static int correctUserInput(int x){
        if (x == 0) {
            //System.out.println("I can't do zeros.. sorry. You meant 1 right? Using 1 instead....");
            x = 1;
        }
        if (x < 0) {
            //System.out.println("I can't do negative integers.. sorry. Let me fix that for you...");
            x = Math.abs(x);
        }
        return x; // All is well!
    }
}

