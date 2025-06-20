public class start {
    public static logic gameLogic;
    public static screen gameScreen;
    private static int launchDifficulty;

    public static void main(String[] args){
        launchDifficulty = 2;

        gameLogic = new logic(launchDifficulty);
        gameScreen = new screen(launchDifficulty, gameLogic);

        gameScreen.createMenuObjects();

        gameLogic.generateMines();
        gameLogic.displayGameConfiguration();

        gameScreen.createTileObjects(true);
    }
}
