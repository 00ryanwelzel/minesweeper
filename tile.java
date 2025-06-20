import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JDialog;
import java.awt.*;

public class tile {
    private JDialog myBase;
    private JButton myButton;
    private logic fedLogic;
    private Color myBackgroundColor;
    private int myValue;
    private boolean isRevealed;

    public tile(JDialog inBase, JButton inButton, logic inLogic, int inValue){
        myBase = inBase;
        myButton = inButton;
        myValue = inValue;
        fedLogic = inLogic;
        isRevealed = false;

        myBackgroundColor = new  Color(
                myButton.getBackground().getRed(),
                myButton.getBackground().getRed(),
                myButton.getBackground().getRed()
        );
        //myButton.setFocusable(false);

        myBase.add(myButton);
        myBase.repaint();
    }

    public int getValue(){
        return myValue;
    }

    public boolean getReveal(){
        return isRevealed;
    }

    public void removeTile(){
        myBase.remove(myButton);
        myBase.repaint();
    }

    public void reveal(){
        isRevealed = true;
        switch (myValue){
            case 0:
                myButton.setBackground(myBackgroundColor);
                myButton.setForeground(myBackgroundColor);
                //myButton.setText();
                break;
            case 1:
                myButton.setBackground(myBackgroundColor);
                myButton.setForeground(Color.BLUE);
                //myButton.setText(String.valueOf(myValue));
                break;
            case 2:
                myButton.setBackground(myBackgroundColor);
                myButton.setForeground(Color.GREEN);
                //myButton.setText(String.valueOf(myValue));
                break;
            case 3:
                myButton.setBackground(myBackgroundColor);
                myButton.setForeground(Color.RED);
                //myButton.setText(String.valueOf(myValue));
                break;
            case 4:
                myButton.setBackground(myBackgroundColor);
                myButton.setForeground(Color.MAGENTA);
                //myButton.setText(String.valueOf(myValue));
                break;
            case 5:
                myButton.setBackground(myBackgroundColor);
                myButton.setForeground(Color.ORANGE);
                //myButton.setText(String.valueOf(myValue));
                break;
            case 6:
                myButton.setBackground(myBackgroundColor);
                myButton.setForeground(Color.CYAN);
                //myButton.setText(String.valueOf(myValue));
                break;
            case 7:
                myButton.setBackground(myBackgroundColor);
                myButton.setForeground(Color.YELLOW);
                //myButton.setText(String.valueOf(myValue));
                break;
            case 8:
                myButton.setBackground(myBackgroundColor);
                myButton.setForeground(Color.ORANGE);
                //myButton.setText(String.valueOf(myValue));
                break;
        }
        isRevealed = true;
    }
}
