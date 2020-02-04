import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/* Date: December 22,2018
 * Description: The block class for minesweeper, storing the atrributes of the block 
 * Author: Juefei Lu
 */
public class Block {
    //intialize variables
    private boolean bomb;
    private int bombsAround;
    private boolean checked;
    private boolean flagged;
    private boolean isRevealed;
    private JButton block;
    private static int difficulty;
    
    //intializes the block
    public Block()
    {
    block = new JButton();
    bombsAround = 0;
    bomb = false;
    checked = false;
    flagged = false;
    isRevealed = false;
    }
    
    //returns the button of the block
    public JButton getButton(){
        return block;
    }
    
    //set the block as a bomb
    public void setBombTrue(){
        bomb = true;
        bombsAround--;
    }
    //whether block is a bomb block
    public boolean getBomb(){
        return bomb;
    }
    //gets number of bombs around this block
    public int getBombsAround(){
        return bombsAround;
    }
    //sets checked to true
    public void setCheckedTrue(){
        checked = true;
    }
    
    //reveals the cell and sets font size accordingly 
    public void checkCell()
    {
        //enables block
        this.getButton().setEnabled(true);
        if(difficulty == 0)
            block.setFont(new Font("Helvetica", Font.BOLD, 28));
        if(difficulty == 1)
            block.setFont(new Font("Helvetica", Font.BOLD, 26));
        if(difficulty >= 2)
            block.setFont(new Font("Helvetica", Font.BOLD, 24));
        //if block is not flagged reveal it
        if(flagged == false)
        {
            //different colours for different bombsAround values
            if(bombsAround == 0)
            {
                block.setText("");
                block.setBackground(Color.WHITE);
                isRevealed = true;
            }
            if(bombsAround >= 1)
            {
                block.setForeground(Color.RED);
                block.setBackground(Color.WHITE);
                block.setText(String.valueOf(bombsAround));
                isRevealed = true;
            }
            if(bombsAround == 2)
            {
                block.setForeground(Color.BLUE);
            }
            if(bombsAround == 3)
            {
                block.setForeground(Color.YELLOW);
            }
            if(bombsAround == 4)
            {
                block.setForeground(Color.CYAN);
            }
            if(bombsAround == 5)
            {
                block.setForeground(Color.ORANGE);
            }
            if(bombsAround == 6)
            {
                block.setForeground(Color.GREEN);
            }
            if(bombsAround == 7)
            {
                block.setForeground(Color.MAGENTA);
            }
            if(bombsAround == 8)
            {
                block.setForeground(Color.PINK);
            }
            else if(bomb)
            {
                block.setBackground(Color.RED);            
                block.setText("B");
                isRevealed = true;
            }
        }
        checked = true;
    }
    //increments value of bombsAround
    public void incrementValue() {
        bombsAround++;        
    }
    //state that the block is checked
    public boolean checked() {
        return checked;
    }
    //the block's bombAround == 0 
    public boolean isEmpty(){
        if(bombsAround == 0)
            return true;
        return false;
    }
    //block is flagged
    public void flag()
    {
    flagged = true;
    }
    //block is not flagged
    public void unFlag()
    {
    flagged = false;
    }
    //returns whether or not block is flagged
    public boolean flaggedOrNot()
    {
    return flagged;
    }
    //returns whether the block is revealed
    public boolean isRevealed()
    {
    return isRevealed;
    }
    
    public static void setDifficulty(int level)
    {
    difficulty = level;
    }
    
}