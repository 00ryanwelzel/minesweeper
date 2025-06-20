import java.lang.Math;

public class logic {
    //Easy: 10% mines 10x10
    //Medium: 20% mines 14x14
    //Hard: 30% mines 18x18
    //Brutal: 40% mines 22x22
    private double minePercentage;
    private int dimensions;
    private int mineCount;
    private int[][] gameConfig;

    public logic(int difficulty){
        switch (difficulty){
            case 1:
                minePercentage = 0.1;
                dimensions = 10;
                break;

            case 2:
                minePercentage = 0.2;
                dimensions = 14;
                break;

            case 3:
                minePercentage = 0.3;
                dimensions = 18;
                break;

            case 4:
                minePercentage = 0.4;
                dimensions = 22;
                break;
        }
    }

    public void generateMines(){
        int[][] outMap = new int[dimensions][dimensions];
        int remainingMines = (int)(minePercentage * dimensions * dimensions);
        double thisMinePercentage = 1 - minePercentage;

        mineCount = remainingMines;

        for(int i = 0; i < dimensions; i++){
            for(int j = 0; j < dimensions; j++){
                double mineRandom = Math.random();

                if(mineRandom >= thisMinePercentage){
                    outMap[i][j] = -1;
                    remainingMines -= 1;
                } else {
                    outMap[i][j] = 0;
                }

                //If there are equivalent empty squares left as there are mines left, forces all squares to be mines
                if((dimensions * i) + j + 1 + remainingMines >= dimensions * dimensions){
                    System.out.println("This was called");
                    thisMinePercentage = -1;
                }

                if(remainingMines <= 0){
                    break;
                }
            }
            if(remainingMines <= 0){
                break;
            }
        }

        gameConfig = outMap;
    }

    public void generateNumbers() throws IndexOutOfBoundsException{
        int[][] outMap = new int[dimensions][dimensions];

        for(int i = 0; i < dimensions; i++){
            for(int j = 0; j < dimensions; j++){
                int tileValue = 0;

                //Checks all squares adjacent to a given tile for if they are mines
                if(gameConfig[i][j] != -1){
                    for(int k = i - 1; k < i + 2; k++){
                        for(int l = j - 1; l < j + 2; l++){
                            try {
                                if(gameConfig[k][l] == -1){
                                    tileValue++;
                                }
                            }
                            catch (Exception e){}
                        }
                    }
                    outMap[i][j] = tileValue;
                }
                //If square is already a mine
                else {
                    outMap[i][j] = -1;
                }
            }
        }
        gameConfig = outMap;

    }

    //Gives players some room to work with on the first click
    public void manageFirstClick(int inRow, int inCol) throws IndexOutOfBoundsException{
        int minesToMove = 0;

        //Determines the number of mines in the space adjacent to the players first click
        for(int i = inRow - 2; i < inRow + 3; i++){
            for(int j = inCol - 2; j < inCol + 3; j++){
                try{
                    if(gameConfig[i][j] == -1){
                        gameConfig[i][j] = 0;
                        minesToMove += 1;
                    }
                }
                catch (Exception e){}
            }
        }

        //Wherever there is room in the board, places the mines back
        if(inRow >= Math.floor(dimensions / 2)){
            for(int i = 0; i < dimensions; i++){
                for(int j = 0; j < dimensions; j++){
                    if(gameConfig[i][j] == 0){
                        gameConfig[i][j] = -1;
                        minesToMove -= 1;
                    }
                    if(minesToMove <= 0){
                        break;
                    }
                }
                if(minesToMove <= 0){
                    break;
                }
            }
        }

        //If the first click was on the top half of the board, places the mines towards the end
        if(inRow < Math.floor(dimensions / 2)){
            for(int i = dimensions - 1; i >= 0; i--){
                for(int j = dimensions - 1; j >= 0; j--){
                    if(gameConfig[i][j] == 0){
                        gameConfig[i][j] = -1;
                        minesToMove -= 1;
                    }
                    if(minesToMove <= 0){
                        break;
                    }
                }
                if(minesToMove <= 0){
                    break;
                }
            }
        }
    }

    public void displayGameConfiguration(){
        int totalMines = 0;
        for(int i = 0; i < dimensions; i++){
            for (int j = 0; j < dimensions; j++){
                if(gameConfig[i][j] == -1){
                    System.out.print("| X |");
                    totalMines++;
                }
                else {
                    System.out.print("| " +  (gameConfig[i][j]) + " |");
                }
            }
            System.out.println();

        }
        System.out.println(totalMines);

    }

    public int getMineCount(){ return mineCount; }

    public int[][] getCurrentConfiguration(){
        return gameConfig;
    }
}
