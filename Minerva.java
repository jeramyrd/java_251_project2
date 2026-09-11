import java.util.List;
import cs251.project2.GomokuInterface.Square;
import cs251.project2.GomokuInterface.TurnResult;

public class Minerva {
    
    private GameBoard gameBoard;
    List<int[]> emptySpotsList;

    Minerva(GameBoard board){
        gameBoard = board;
    }
     
    // Remember the computer is always the CROSS
    public int[] bestMove(){
        var testList = gameBoard.listEmptySpots();
        double ringHighScore = 0;
        double crossHighScore = 0;
        int[] bestRingSpot = {-1, -1};
        int[] bestCrossSpot = {-1, -1};
        double scale = 1.0 / (4.0 * gameBoard.getWinCount());
        //System.out.println("Scale is: " + scale);
        int[] directionColumn = {1, 0, 1, -1};
        int[] directionRow = {0, 1, 1, 1};
        int crossCount = 0, ringCount = 0;
        double ringLocalScore = 0;
        double crossLocalScore = 0;
        int tempCol = 0;
        int tempRow = 0;
        int offGridScaler = 1;

        // loop through all empty spots
        //System.out.println("STARTING THE SCORING SPREE!!!!!");
        for (int[] spot: testList){            
            //System.out.println("Empty Spot (" + spot[0] + "," + spot[1] +") ");
            // Need to calculate 4 different directions
            ringLocalScore = 0;
            crossLocalScore = 0;

            gameBoard.attemptPlayAtSpot(spot[0],spot[1], Square.CROSS);
            if (gameBoard.setTurnResult() == TurnResult.CROSS_WINS) {
                gameBoard.resetSpot(spot[0], spot[1]);
                gameBoard.setTurnResult();
                return spot;
            }
            gameBoard.resetSpot(spot[0], spot[1]);

            for (int directionIndex = 0; directionIndex < 4; ++directionIndex){
                //System.out.println("Direction: " + directionIndex + " ---> 0 right 1 down 2 Dup 3 Ddown");
                // Now we have userSelectedNumInLineForWin sets to calculate....
                for (int setShift = gameBoard.getWinCount() - 1; setShift >= 0; --setShift ){
                    //System.out.println("Setshift is " + setShift);
                    tempCol = spot[0]-directionColumn[directionIndex]*setShift;
                    tempRow = spot[1]-directionRow[directionIndex]*setShift;
                    //System.out.println("SetShifted Start spot is (" + tempCol + "," + tempRow +") ");
                    ringCount = 0;
                    crossCount = 0;
                    offGridScaler = 1;
                    //Now finally, calculate the block. 
                    for (int i = 0; i < gameBoard.getWinCount(); ++i){
                        //System.out.println("Array Spot is currently (" + tempCol + "," + tempRow +") ");
                        if (gameBoard.isSpotOnBoard(tempCol, tempRow)){
                            switch (gameBoard.getSpot(tempCol, tempRow)) {
                                case CROSS:
                                    ++crossCount;
                                    if (crossCount == gameBoard.getWinCount()) {
                                        return spot;
                                    }
                                    break;
                                case RING:
                                    ++ringCount;
                                    break;
                                case EMPTY:
                                    break;
                            }
                        }
                        else { 
                            offGridScaler = 0;
                        }
                        tempCol += directionColumn[directionIndex];
                        tempRow += directionRow[directionIndex];
                    }
                    //System.out.println("Offgrid (zero is yes): " + offGridScaler);
                    //System.out.println("crossCount = " + crossCount + " So math is:" + Math.pow(10, crossCount));
                    //System.out.println("ringCount = " + ringCount + " So math is:" + Math.pow(10, ringCount));
                    //finished the set - add to total
                    //System.out.println("Cross score add would be: " + offGridScaler*scale*Math.pow(10, crossCount));
                    //System.out.println("Ring score add would be: " + offGridScaler*scale*Math.pow(10, ringCount));
                    if (ringCount == 0) { 
                        crossLocalScore += offGridScaler*scale*Math.pow(10, crossCount);
                    }
                    if (crossCount == 0) { 
                        ringLocalScore += offGridScaler*scale*Math.pow(10, ringCount);
                    }
                    //System.out.println("Ring Local score: " + ringLocalScore + " Cross Local Score: " + crossLocalScore);
                }
            }
            if (ringLocalScore > ringHighScore) {
                ringHighScore = ringLocalScore;
                bestRingSpot = spot;
            }
            if (crossLocalScore > crossHighScore) {
                crossHighScore = crossLocalScore;
                bestCrossSpot = spot;
            }
            System.out.println("Ring High score: " + ringHighScore + " Ring best spot (" + bestRingSpot[0] + "," + bestRingSpot[1] + ")");
            System.out.println("Cross High score: " + crossHighScore + " Cross best spot (" + bestCrossSpot[0] + "," + bestCrossSpot[1] + ")");
        }
        if (ringHighScore > crossHighScore + 0.0005) {
            return bestRingSpot;
        } //Had some precision errors ruining the math.
        else { 
            return bestCrossSpot;
        }
    }
}
