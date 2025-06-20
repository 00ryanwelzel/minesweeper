import javax.swing.*;
import java.awt.event.*;
import javax.swing.JDialog;
import java.awt.*;
import java.util.ArrayList;

//Easy: 10% mines 10x10
//Medium: 20% mines 14x14
//Hard: 30% mines 18x18
//Brutal: 40% mines 22x22

public class screen {
    public static JButton[][] displayedTileButtons;
    public static ArrayList<JComponent> displayedMenuButtons;
    public static JDialog gameWindow = new JDialog();
    public static JDialog difficultyWindow = new JDialog();
    public Color backgroundColor = new Color(80, 120, 60);
    public Color tileColor = new Color(130, 205, 90);
    public Color lightTileColor = new Color(160, 235, 120);
    public Color lessOffensiveRed = new Color(182, 0, 0);
    public Font smallFont;
    public Font mediumFont;
    public Font largeFont;
    private logic fedLogic;
    private String difficultyDisplay;
    private int tileSize;
    private int dimensions;
    private int baseX;
    private int baseY;
    private int clickRow;
    private int clickCol;

    public JLabel flagInfo;
    public JButton difficultySelectionButton;

    public screen(int difficulty, logic inLogic){
        switch (difficulty){
            case 1:
                difficultyDisplay = "EASY";
                dimensions = 10;
                tileSize = 56;
                break;
            case 2:
                difficultyDisplay = "MEDIUM";
                dimensions = 14;
                tileSize = 52;
                break;
            case 3:
                difficultyDisplay = "HARD";
                dimensions = 18;
                tileSize = 48;
                break;
            case 4:
                difficultyDisplay = "INSANE";
                dimensions = 22;
                tileSize = 44;
                break;
        }

        baseX = tileSize * dimensions;
        baseY = baseX + tileSize * 2;

        smallFont = new Font("SansSerif", Font.BOLD, (int)(tileSize * 0.3));
        mediumFont = new Font("SansSerif", Font.BOLD, (int)(tileSize * 0.5));
        largeFont = new Font("SansSerif", Font.BOLD, (int)(tileSize * 0.8));

        fedLogic = inLogic;
        
        displayedTileButtons = new JButton[dimensions][dimensions];
        displayedMenuButtons = new ArrayList<>();
        
        gameWindow.setSize(baseX, baseY);
        gameWindow.setLocationRelativeTo(null);
        gameWindow.setUndecorated(true);
        gameWindow.setVisible(true);
        gameWindow.getContentPane().setBackground(backgroundColor);
        gameWindow.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

    public void createMenuObjects(){
        //JButton difficultySelectionButton = new JButton();
        JButton closeButton = new JButton();
        //JLa

        flagInfo = new JLabel();
        difficultySelectionButton = new JButton();

        difficultySelectionButton.setBounds(
                (int)(0.25 * tileSize),
                (int)(0.25 * tileSize),
                (int)(3.25 * tileSize),
                (int)(1.5 * tileSize)
        );

        difficultySelectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                difficultyDropdown();
            }
        });

        difficultySelectionButton.setText(difficultyDisplay);
        difficultySelectionButton.setForeground(Color.BLACK);
        difficultySelectionButton.setBackground(Color.WHITE);
        difficultySelectionButton.setFocusPainted(false);
        difficultySelectionButton.setBorderPainted(false);
        difficultySelectionButton.setFont(mediumFont);
        difficultySelectionButton.setVisible(true);

        closeButton.setBounds(
                (int)(baseX - (0.25 * tileSize) - (1.5 * tileSize)),
                (int)(0.25 * tileSize),
                (int)(1.5 * tileSize),
                (int)(1.5 * tileSize)
        );

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameWindow.dispose();
                difficultyWindow.dispose();
            }
        });

        closeButton.setText("X");
        closeButton.setForeground(Color.WHITE);
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setFont(largeFont);
        closeButton.setVisible(true);

        flagInfo.setBounds(
                (int)(0.25 * tileSize + (5 * tileSize)),
                (int)(0.25 * tileSize),
                (int)(3.25 * tileSize),
                (int)(1.5 * tileSize)
        );

        flagInfo.setText(String.valueOf(fedLogic.getMineCount()));
        flagInfo.setForeground(lessOffensiveRed);
        flagInfo.setFont(mediumFont);
        flagInfo.setVisible(true);

        //Deal with all the tracking stuff below

        displayedMenuButtons.add(difficultySelectionButton);
        displayedMenuButtons.add(closeButton);
        displayedMenuButtons.add(flagInfo);

        gameWindow.add(difficultySelectionButton);
        gameWindow.add(closeButton);
        gameWindow.add(flagInfo);

        gameWindow.repaint();
    }

    public void createTileObjects(boolean isFirstClick){
        int colorFlipFlop = 1;
        int[][] board = fedLogic.getCurrentConfiguration();

        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++){
                JButton tempTileButton = new JButton();

                tempTileButton.setBounds(
                        (j * tileSize),
                        (i * tileSize + 2 * tileSize),
                        (tileSize),
                        (tileSize)
                );

                tempTileButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        clickRow = (tempTileButton.getY() / tileSize - 2);
                        clickCol = (tempTileButton.getX() / tileSize );

                        if(isFirstClick){
                            fedLogic.manageFirstClick(clickRow, clickCol);
                            fedLogic.generateNumbers();
                            fedLogic.displayGameConfiguration();

                            clearTileButtons();
                            clearMenuButtons();

                            createTileObjects(false);
                            createMenuObjects();
                        }
                        if(!isFirstClick){
                            if(SwingUtilities.isLeftMouseButton(e) && (int)(tempTileButton.getClientProperty("flag")) != 1){
                                reveal(tempTileButton);

                                if((int)(tempTileButton.getClientProperty("value")) == -1){;
                                    mineHit();
                                }
                                if((int)(tempTileButton.getClientProperty("value")) == 0){
                                    System.out.println("now here");
                                    revealAdjacentZeroes(clickRow, clickCol);
                                }
                            }
                            if(SwingUtilities.isRightMouseButton(e) && (boolean)(tempTileButton.getClientProperty("reveal")) != true){
                                flag(tempTileButton);
                            }

                        }
                    }
                });

                if(colorFlipFlop == 1){
                    tempTileButton.setBackground(tileColor);
                    tempTileButton.putClientProperty("dark", true);
                }
                if(colorFlipFlop == -1){
                    tempTileButton.setBackground(lightTileColor);
                    tempTileButton.putClientProperty("dark", false);
                }

                colorFlipFlop *= -1;

                tempTileButton.putClientProperty("value", board[i][j]);
                tempTileButton.putClientProperty("reveal", false);
                tempTileButton.putClientProperty("flag", -1);

                tempTileButton.setForeground(tempTileButton.getBackground());
                tempTileButton.setText(String.valueOf(board[i][j]));
                tempTileButton.setFocusable(false);
                tempTileButton.setFocusPainted(false);
                tempTileButton.setBorderPainted(false);
                tempTileButton.setFont(mediumFont);
                tempTileButton.setVisible(true);

                displayedTileButtons[i][j] = tempTileButton;

                gameWindow.add(tempTileButton);
                gameWindow.repaint();
            }

            colorFlipFlop *= -1;
        }

        if(!isFirstClick){
            revealAdjacentZeroes(clickRow, clickCol);
        }
    }

    //TODO: FINISH IMPLEMENTING THIS FUNCTION
    public void difficultyDropdown() throws Error {
        JButton easyDifficulty = new JButton();
        JButton mediumDifficulty = new JButton();
        JButton hardDifficulty = new JButton();
        JButton insaneDifficulty = new JButton();
        JButton closeButton = new JButton();

        ArrayList<JButton> difficultyWindowDisplayedButtons = new ArrayList<>();

        difficultyWindow = new JDialog();

        difficultyWindow.setSize(
                (int)(3.75 * tileSize),
                (int)(9.00 * tileSize)
        );

        difficultyWindow.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {}

            @Override
            public void windowLostFocus(WindowEvent e) {
                difficultyWindow.dispose();
            }
        });

        difficultyWindow.setLocationRelativeTo(gameWindow);
        difficultyWindow.setUndecorated(true);
        difficultyWindow.setVisible(true);
        difficultyWindow.getContentPane().setBackground(backgroundColor);
        difficultyWindow.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        closeButton.setBounds(
                (int)(0.25 * tileSize),
                (int)(0.25 * tileSize),
                (int)(3.25 * tileSize),
                (int)(1.50 * tileSize)
        );

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(JButton b : difficultyWindowDisplayedButtons){
                    difficultyWindow.remove(b);
                    difficultyWindow.repaint();
                }

                difficultyWindow.dispose();
            }
        });

        closeButton.setText("CANCEL");
        closeButton.setForeground(Color.BLACK);
        closeButton.setBackground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setFont(mediumFont);
        closeButton.setVisible(true);

        easyDifficulty.setBounds(
                (int)(0.25 * tileSize),
                (int)(2.00 * tileSize),
                (int)(3.25 * tileSize),
                (int)(1.50 * tileSize)
        );

        easyDifficulty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                difficultyWindow.dispose();
                gameWindow.removeAll();
                gameWindow.dispose();

                logic easyGameLogic = new logic(2);
                screen easyGameWindow = new screen(2, easyGameLogic);

                easyGameWindow.createMenuObjects();

                easyGameLogic.generateMines();
                easyGameLogic.displayGameConfiguration();

                easyGameWindow.createTileObjects(true);
            }
        });

        easyDifficulty.setText("EASY");
        easyDifficulty.setForeground(Color.BLACK);
        easyDifficulty.setBackground(Color.WHITE);
        easyDifficulty.setFocusPainted(false);
        easyDifficulty.setBorderPainted(false);
        easyDifficulty.setFont(mediumFont);
        easyDifficulty.setVisible(true);

        mediumDifficulty.setBounds(
                (int)(0.25 * tileSize),
                (int)(3.75 * tileSize),
                (int)(3.25 * tileSize),
                (int)(1.50 * tileSize)
        );

        mediumDifficulty.setText("MEDIUM");
        mediumDifficulty.setForeground(Color.BLACK);
        mediumDifficulty.setBackground(Color.WHITE);
        mediumDifficulty.setFocusPainted(false);
        mediumDifficulty.setBorderPainted(false);
        mediumDifficulty.setFont(mediumFont);
        mediumDifficulty.setVisible(true);

        hardDifficulty.setBounds(
                (int)(0.25 * tileSize),
                (int)(5.50 * tileSize),
                (int)(3.25 * tileSize),
                (int)(1.50 * tileSize)
        );

        hardDifficulty.setText("HARD");
        hardDifficulty.setForeground(Color.BLACK);
        hardDifficulty.setBackground(Color.WHITE);
        hardDifficulty.setFocusPainted(false);
        hardDifficulty.setBorderPainted(false);
        hardDifficulty.setFont(mediumFont);
        hardDifficulty.setVisible(true);

        insaneDifficulty.setBounds(
                (int)(0.25 * tileSize),
                (int)(7.25 * tileSize),
                (int)(3.25 * tileSize),
                (int)(1.50 * tileSize)
        );

        insaneDifficulty.setText("INSANE");
        insaneDifficulty.setForeground(Color.BLACK);
        insaneDifficulty.setBackground(Color.WHITE);
        insaneDifficulty.setFocusPainted(false);
        insaneDifficulty.setBorderPainted(false);
        insaneDifficulty.setFont(mediumFont);
        insaneDifficulty.setVisible(true);

        difficultyWindowDisplayedButtons.add(closeButton);
        difficultyWindowDisplayedButtons.add(easyDifficulty);
        difficultyWindowDisplayedButtons.add(mediumDifficulty);
        difficultyWindowDisplayedButtons.add(hardDifficulty);
        difficultyWindowDisplayedButtons.add(insaneDifficulty);

        difficultyWindow.add(closeButton);
        difficultyWindow.add(easyDifficulty);
        difficultyWindow.add(mediumDifficulty);
        difficultyWindow.add(hardDifficulty);
        difficultyWindow.add(insaneDifficulty);
    }

    public void revealAdjacentZeroes(int row, int col){
        reveal(displayedTileButtons[row][col]);

        for(int i = row - 1; i < row + 2; i++){
            for(int j = col - 1; j < col + 2; j++){
                if(i == row && j == col){
                    continue;
                }

                if(i >= 0 && i <= displayedTileButtons.length - 1 && j >= 0 && j <= displayedTileButtons.length - 1){
                    if((int)(displayedTileButtons[i][j].getClientProperty("value")) == 0 && !(boolean)(displayedTileButtons[i][j].getClientProperty("reveal"))){
                         revealAdjacentZeroes(i, j);
                    }

                    if(!(boolean)(displayedTileButtons[i][j].getClientProperty("reveal"))){
                        reveal(displayedTileButtons[i][j]);
                    }
                }
            }
        }
    }

    public void reveal(JButton inButton){
        if((boolean)(inButton.getClientProperty("reveal"))){
            return;
        }

        inButton.putClientProperty("reveal", true);

        Color myBackgroundColor = new Color(
                inButton.getBackground().getRed(),
                inButton.getBackground().getRed(),
                inButton.getBackground().getRed()
        );

        switch ((int)(inButton.getClientProperty("value"))) {
            case 0:
                inButton.setBackground(myBackgroundColor);
                inButton.setForeground(new Color(0,0,0,0));
                break;
            case 1:
                inButton.setBackground(myBackgroundColor);
                inButton.setForeground(Color.BLUE);
                break;
            case 2:
                inButton.setBackground(myBackgroundColor);
                inButton.setForeground(Color.GREEN);
                break;
            case 3:
                inButton.setBackground(myBackgroundColor);
                inButton.setForeground(lessOffensiveRed);
                break;
            case 4:
                inButton.setBackground(myBackgroundColor);
                inButton.setForeground(Color.MAGENTA);
                break;
            case 5:
                inButton.setBackground(myBackgroundColor);
                inButton.setForeground(Color.ORANGE);
                break;
            case 6:
                inButton.setBackground(myBackgroundColor);
                inButton.setForeground(Color.CYAN);
                break;
            case 7:
                inButton.setBackground(myBackgroundColor);
                inButton.setForeground(Color.YELLOW);
                break;
            case 8:
                inButton.setBackground(myBackgroundColor);
                inButton.setForeground(Color.BLACK);
                break;
            case -1:
                inButton.setBackground(Color.BLACK);
                inButton.setForeground(new Color(0,0,0,0));
        }
    }

    public void flag(JButton inButton){
        int flagFlipFlop = (int)(inButton.getClientProperty("flag"));

        if((boolean)(inButton.getClientProperty("reveal"))){
            return;
        }

        if(flagFlipFlop == -1){
            inButton.setBackground(lessOffensiveRed);
            inButton.setForeground(lessOffensiveRed);
            changeFlagCount(true);
        }
        if(flagFlipFlop == 1){
            if((boolean)(inButton.getClientProperty("dark"))){
                inButton.setForeground(tileColor);
                inButton.setBackground(tileColor);
            }
            else{
                inButton.setForeground(lightTileColor);
                inButton.setBackground(lightTileColor);
            }
            changeFlagCount(false);
        }

        flagFlipFlop *= -1;

        inButton.putClientProperty("flag", flagFlipFlop);
    }

    public void changeFlagCount(boolean value){
        gameWindow.remove(flagInfo);

        if(value){
            flagInfo.setText(String.valueOf(Integer.parseInt(flagInfo.getText()) - 1));
        }
        else{
            flagInfo.setText(String.valueOf(Integer.parseInt(flagInfo.getText()) + 1));
        }

        displayedMenuButtons.remove(2);
        displayedMenuButtons.add(2, flagInfo);

        gameWindow.add(flagInfo);
        gameWindow.repaint();
    }

    public void mineHit(){
        for(int i = 0; i < dimensions - 1; i++){
            for(int j = 0; j < dimensions - 1; j++){
                if((int)(displayedTileButtons[i][j].getClientProperty("value")) == -1){
                    reveal(displayedTileButtons[i][j]);
                } else {
                    displayedTileButtons[i][j].putClientProperty("reveal", true);
                }
            }
        }

        gameWindow.remove(flagInfo);

        flagInfo.setText("YOU LOST :P");

        gameWindow.add(flagInfo);
        gameWindow.repaint();
    }

    public void remove(JComponent inComponent){
        gameWindow.remove(inComponent);
        gameWindow.repaint();
    }

    public void clearTileButtons(){
        for(int i = 0; i < displayedTileButtons.length; i++){
            for(int j = 0; j < displayedTileButtons.length; j++){
                remove(displayedTileButtons[i][j]);
            }
        }
    }
    
    public void clearMenuButtons(){
        for(JComponent b : displayedMenuButtons){
            remove(b);
        }
    }
}