import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import java.util.Timer;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

/* Date 12/20/2018
 * Author: Juefei Lu 
 * Description: MineSweeper game with AI to help play the game for you. It includes difficulty selection, custom sizes, flag count, and timer
 */

public class MineSweeper implements ActionListener,MouseListener {
    //initalize variables
        public int difficulty;
        private JFrame mainFrame;
        private JPanel gamePanel, menuPanel, mainPanel;
        private JLabel title, flagDisplay, timer;
        private JButton easy,medium,hard,customSize,play, AI, introduction;
        private Block[][] blocks;
        private int numberOfBombs, numberOfClicks = 0;
        private int width = 0,length = 0;
        private int bombCounter = 0;
        int secondPassed = 0;
        private boolean timerReset = false;
        private boolean firstTime = true;
        String Instructions; 
        String player = null;
        FileInputStream imageFile = null;
        ImageIcon image = null;
        ImageIcon icon = null;
    public MineSweeper() throws AWTException
    {
        //intialize GUI components
        mainFrame = new JFrame();
        mainFrame.setSize(750,750);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        menuPanel = new JPanel();
        menuPanel.setSize(750,750);
        menuPanel.setLayout(null);
        
        //font sizes
        Font f = new Font("Helvetica", Font.BOLD, 14);
        Font smallF = new Font("Helvetica", Font.BOLD, 12);
        
        title = new JLabel("MineSweeper");
        title.setBounds(300,100,150,100);
        title.setFont(new Font("Helvetica", Font.BOLD, 22));
        
        easy = new JButton("Easy");
        easy.setBounds(175,600,100,100);
        easy.setFont(f);
        easy.addActionListener(this);
        
        medium = new JButton("Medium");
        medium.setBounds(275,600,100,100);
        medium.setFont(f);
        medium.addActionListener(this);
        
        hard = new JButton("Hard");
        hard.setBounds(375,600,100,100);
        hard.setFont(f);
        hard.addActionListener(this);
        
        customSize = new JButton("Custom");
        customSize.setBounds(475,600,100,100);
        customSize.setFont(f);
        customSize.addActionListener(this);
        
        introduction = new JButton("Instructions");
        introduction.setBounds(300,400,150,100);
        introduction.setFont(f);
        introduction.addActionListener(this);
        
        play = new JButton("Play");
        play.setBounds(300,300,150,100);
        play.setFont(new Font("Helvetica", Font.BOLD, 22));
        play.addActionListener(this);
        
        mainPanel = new JPanel();
        mainPanel.setSize(750,750);
        mainPanel.setLayout(null);
        
        flagDisplay = new JLabel("Flags Left:");
        flagDisplay.setBounds(300,650,100,50);
        flagDisplay.setFont(smallF);
        
        timer = new JLabel("0" + "Seconds");
        timer.setBounds(300,650,100,30);
        timer.setFont(smallF);
        
        AI = new JButton("AI");
        AI.setBounds(100,650,100,70);
        AI.setFont(smallF);
        AI.addActionListener(this);
        
        menuPanel.add(title);
        menuPanel.add(easy);
        menuPanel.add(medium);
        menuPanel.add(hard);
        menuPanel.add(customSize);
        menuPanel.add(play);
        menuPanel.add(introduction);
        
        mainPanel.add(flagDisplay);
        mainPanel.add(timer);
        mainPanel.add(AI);
        
        mainFrame.setContentPane(menuPanel);
        mainFrame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == easy)
        {
        difficulty = 0;
        width = 8;
        length = 8;
        }
        
        if(e.getSource() == medium)
        {
        difficulty = 1;
        width = 10;
        length = 10;
        }
        
        if(e.getSource() == hard)
        {
        difficulty = 2;
        width = 12;
        length = 12;
        }
        
        if(e.getSource() == customSize)
        {
            difficulty = 3;
            String temp = JOptionPane.showInputDialog(mainFrame, "Please enter width and length:");
            try
                {
                    //gets user input for width and length, restricted to same width and length for flag image to be resized properly
                    width = Integer.parseInt(temp);
                    length = width;
                    //if user input is greater than 10 or less than 3, automatically set width and length to 8
                    if(width < 3 || width > 10)
                    {
                        difficulty = 2;
                        JOptionPane.showMessageDialog(mainFrame, "Invalid input, width and length set to 8");
                        width = 8;
                        length = 8;
                    }
                }
            catch(Exception fail)
                {
                    //if user input is greater than 10 or less than 3, automatically set width and length to 8
                    difficulty = 2;
                    JOptionPane.showMessageDialog(mainFrame, "Invalid input, width and length set to 8");
                    width = 8;
                    length = 8;
                }
        }
        
        if(e.getSource() == play) 
        {
            //checks if width and length has been chosen
            if(width > 0 && length > 0)
            {
                setUpGrid();
                mainFrame.setContentPane(mainPanel);
                mainFrame.setVisible(true);
            }
        }
        // displays how to play minesweeper to beginners
        if(e.getSource() == introduction)
        {
            JOptionPane.showMessageDialog(mainFrame,"The purpose of the game is to open all the cells of the board which do not contain a bomb. You lose if you set off a bomb cell.\n"
                    + "Every non-bomb cell you open will tell you the total number of bombs in the eight neighboring cells. Once you are sure that a cell contains a bomb, \n you can right-click to put a flag on it." 
                    + " You must flag all bomb cells and reveal all cells on the board not containing a bomb to win");
        }
        //run AI if user clicks AI
        if(e.getSource() == AI)
        {
            autoSolver();
        }
    }
    //mouse click function, checks for left or right click for flag or reveal block
        @Override
    public void mouseClicked(MouseEvent e) {
        //checks mouse clicks for all blocks
        for(int j = 0; j < width; j++)
        {
            for(int i = 0; i < length; i++)
            {
                if(e.getSource() == blocks[j][i].getButton())
                {
                    //checks for left click and reveals block
                    if(isLeftMouseButton(e) && blocks[j][i].flaggedOrNot() == false)
                    {
                       blocks[j][i].checkCell();
                       numberOfClicks++;
                       if(blocks[j][i].getBomb())
                       {
                            JOptionPane.showMessageDialog(mainFrame, "Unforunately you lost");
                            reset();
                            mainFrame.setContentPane(menuPanel);
                       }
                       checkSurroundingCells(j,i);
                        //checks if you have won, doesn't check when it resets
                        if(width != 0)
                        winCheck();
                    }
                    //checks for right click and flags the block if it is not revealed
                    else if(isRightMouseButton(e) && blocks[j][i].isRevealed() == false)
                    {
                        if(blocks[j][i].flaggedOrNot() == false)
                        {
                            setFlag(j,i);                    
                        }
                        
                        else
                        {
                            unFlag(j,i);
                        }
                        numberOfClicks++;
                    }
                    //help reset the timer
                    if(numberOfClicks == 1 && firstTime)
                    {
                        startNewTimer();
                    }
                    //controls timer to be reset or not
                    else if (numberOfClicks == 1)
                    {
                        timerReset = false;
                    }
                }
            }
        }
    }
    
    //sets up the grid with buttons and amount of bombs
    public void setUpGrid()
    {
        //initalizes gamePanel 
        gamePanel = new JPanel();
        gamePanel.setSize(650,650);
        gamePanel.setLayout(new GridLayout(width,length,0,0));
        
        blocks = new Block[width][length];
        
        //uses difficulty to determine number of bombs present in the game
        if(difficulty == 0)
            numberOfBombs = 5;
        if(difficulty == 1) 
            numberOfBombs = 10;        
        if(difficulty == 2) 
            numberOfBombs = 20;
        if(difficulty == 3)
            numberOfBombs = (width*length)/4;
        
        Block.setDifficulty(difficulty);
        //initalizes the blocks 
        for(int j = 0; j < width; j++)
        {
            for(int i = 0; i < length; i++)
            {
                blocks[j][i] = new Block();
                blocks[j][i].getButton().addMouseListener(this);
                gamePanel.add(blocks[j][i].getButton());
            }
        }
        //completely random bomb generation
        while(bombCounter < numberOfBombs)
        {
            int random = (int)(Math.random()*width);
            int random2 = (int)(Math.random()*length);
            if(blocks[random][random2].getBomb() == false)
            {
                blocks[random][random2].setBombTrue();
                bombCounter++;
            }
        }
        //sets flag image
        setImage();
        imageResize(image);
        //sets block value
        setBlockValue();
        //adds gamePanel onto GUI
        mainPanel.add(gamePanel);
        gamePanel.setVisible(true);
    }
    
    //calculates block value 
    public void setBlockValue()
    {
        for(int j = 0; j < width; j++)
        {
            for(int i = 0; i< length; i++)
            {
                for(int counter = -1; counter < 2; counter++)
                {
                    for(int secondCounter = -1; secondCounter < 2; secondCounter++)
                    {
                        if((j+counter) >= 0 && (j+counter) < width && (i+secondCounter) >= 0 && (i+secondCounter) < length)
                        {
                            if(blocks[j+counter][i+secondCounter].getBomb())
                            {
                                if(blocks[j][i].getBomb() == false)
                                    blocks[j][i].incrementValue();
                            }
                        }
                    }
                }    
            }
        }
    }
    
    //checks surrounding cells if it is empty space or just reveal a cell that has value
    public void checkSurroundingCells(int j, int i)
    {
        for(int counter = -1; counter <= 1; counter++)
        {
            for(int secondCounter = -1; secondCounter <= 1; secondCounter++)
            {
                if((j+counter) >= 0 && (j+counter) < width && (i+secondCounter) >= 0 && (i+secondCounter) < length)
                {
                    if(blocks[j+counter][i+secondCounter].checked() == false && blocks[j][i].isEmpty())
                    {
                        //sets block to checked true to be more efficent
                        blocks[j+counter][i+secondCounter].setCheckedTrue();
                        //if block is empty reveal surrounding cells
                        if(blocks[j+counter][i+secondCounter].isEmpty())
                        {
                            blocks[j+counter][i+secondCounter].checkCell();
                            checkSurroundingCells(j+counter,i+secondCounter);
                        }
                        //if block has a value then reveal it
                        if(blocks[j+counter][i+secondCounter].getBomb() == false)
                        {
                                blocks[j+counter][i+secondCounter].checkCell();
                        }
                    }
                }
            }
        }
    }
    
    //reset variable to intial value and resets game to initial state
    public void reset()
    {
        width = 0;
        length = 0;
        bombCounter = 0;
        numberOfClicks = 0;
        numberOfBombs = 0;
        timerReset = true;
        secondPassed = 0;
        timer.setText(Integer.toString(secondPassed) + "Seconds");
        firstTime = false;
        flagDisplay.setText("Flags left:");
        mainPanel.removeAll();
        mainPanel.add(timer);
        mainPanel.add(AI);
        mainPanel.add(flagDisplay);   
    }
    
    //resize images to fit Minesweeper grid for various sizes
    private void imageResize(ImageIcon imgs)
    {
        Image img = imgs.getImage();
        Image scaledImage = img.getScaledInstance((800/width),(720/length), Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImage);
    }
    
    //flag function
    public void setFlag(int j,int i)
    {
        blocks[j][i].flag();
        //counts number of flags left
        numberOfBombs--;
        blocks[j][i].getButton().setIcon(icon);
        flagDisplay.setText("Flags Left: " + String.valueOf(numberOfBombs));
        //checks if you have won, doesn't check when it resets
        if(width != 0)
            winCheck();
    }
    
    //unflag function
    public void unFlag(int j, int i)
    {
        blocks[j][i].unFlag();
        blocks[j][i].getButton().setIcon(null);
        //count number of flags left 
        numberOfBombs++;
        flagDisplay.setText("Flags Left: " + String.valueOf(numberOfBombs));
        //checks if you have won, doesn't check when it resets
        if(width != 0)
        winCheck();
    }
    
    //checls if you have won
    public void winCheck()
    {
        int winCounter = 0;
        for(int j = 0; j < width; j++)
        {
            for(int i = 0; i < length; i++)
            {
                //if block needs to be revealed and is revealed then winCounter +1
                if(blocks[j][i].getBomb() == false)
                {
                    if(blocks[j][i].isRevealed())
                        winCounter++;
                }
                //if block is a bomb and is flagged then winCounter +1
                else
                {
                    if(blocks[j][i].flaggedOrNot())
                        winCounter++;
                }
            }
        }
        //if all bomb blocks are flagged and normal blocks revealed then display win message
        if(winCounter == width*length)
        {
            JOptionPane.showMessageDialog(mainFrame, "Congratulations, you win!!!");
            reset();
            mainFrame.setContentPane(menuPanel);
        }
    }
    //necessary methods to implement for mouseListener
    @Override
    public void mouseExited(MouseEvent arg0) {

    }
    //necessary methods to implement for mouseListener
    public void mouseEntered(MouseEvent arg0) {

    }
    //necessary methods to implement for mouseListener
    @Override
    public void mousePressed(MouseEvent arg0) {

    }
    //necessary methods to implement for mouseListener
    @Override
    public void mouseReleased(MouseEvent arg0) {
        
    }
    
    //get Image using Image IO
    public void setImage()
    {
        try 
        {
            imageFile = new FileInputStream("Images/flag.png");
        }    
        catch (Exception ex) 
        {
            System.out.println("This should never be reached.");
        }                        
        try 
        {
            image = new ImageIcon(ImageIO.read(imageFile));
        } 
        catch (Exception ex)
        {
            System.out.println("This should never be reached.");
        }    
    }
    //Timer Section
    //Timer to display time passed since game started
    public void startNewTimer()
    {
        Timer newTimer = new Timer();
        TimerTask task = new TimerTask()
        {        
            @Override
            public void run()
            {
                //if timer is not reset, display seconds passed on timer 
                if(timerReset == false){
                secondPassed++;
                timer.setText(Integer.toString(secondPassed) + "Seconds");
                }
            }
        };
        newTimer.scheduleAtFixedRate(task, 1000, 1000);
    }
       
    //Minesweeper AI Section
    //sends value to methods to solve MineSweeper
    public void autoSolver()
    {
        if(numberOfBombs == 0)
        {
            for(int j = 0; j < width; j++)
            {
                for(int i = 0; i < length; i++)
                {
                blocks[j][i].checkCell();
                }
            }
            winCheck();
        }
        
        for(int j = 0; j < width; j++)
        {
            for(int i = 0; i < length; i++)
            {
                if(blocks[j][i].isRevealed())
                {
                    deduceBombs(j,i,blocks[j][i].getBombsAround());
                    clickSurroundingCells(j,i,blocks[j][i].getBombsAround()); 
                }
            }
        }
    }

    //check cell for if block has the same amount of blocks unrevealed as there are bombs around it
    public void deduceBombs(int a, int b, int bombsLeft)
    {
        int unrevealed = 0;
        for(int counter = -1; counter < 2; counter++)
        {
            for(int secondCounter = -1; secondCounter < 2; secondCounter++)
            {
                if((a+counter) >= 0 && (a+counter) < width && (b+secondCounter) >= 0 && (b+secondCounter) < length)
                {
                    if(blocks[a+counter][b+secondCounter].isRevealed() == false)
                    {
                    unrevealed++;
                    }
                }
            }
        }
        //flags bomb if the block has same amount of blocks unrevealed as there are bombs around it left
        if(unrevealed == bombsLeft)
        {
            for(int counter = -1; counter < 2; counter++)
            {   
                for(int secondCounter = -1; secondCounter < 2; secondCounter++)
                {
                    if((a+counter) >= 0 && (a+counter) < width && (b+secondCounter) >= 0 && (b+secondCounter) < length)
                    {
                        if(blocks[a+counter][b+secondCounter].isRevealed() == false && blocks[a+counter][b+secondCounter].flaggedOrNot() == false)
                        {
                            setFlag(a+counter,b+secondCounter);
                            numberOfClicks++;
                        }
                    }
                }
            }
        }
    }
    
    //used to reveal surrounding cells that are already flagged for bombs
    public void clickSurroundingCells(int a,int b,int bombsLeft)
    {
        for(int counter = -1; counter < 2; counter++)
        {
            for(int secondCounter = -1; secondCounter < 2; secondCounter++)
            {
                if((a+counter) >= 0 && (a+counter) < width && (b+secondCounter) >= 0 && (b+secondCounter) < length)
                {
                    if(blocks[a+counter][b+secondCounter].flaggedOrNot())
                    {
                    bombsLeft--;
                    }
                }
            }
        }
        //checks if bomb has been all flagged and reveals all adjacent tiles
        if(bombsLeft == 0)
        {
            for(int counter = -1; counter < 2; counter++)
            {
                for(int secondCounter = -1; secondCounter < 2; secondCounter++)
                {
                    if((a+counter) >= 0 && (a+counter) < width && (b+secondCounter) >= 0 && (b+secondCounter) < length && blocks[a+counter][b+secondCounter].flaggedOrNot() == false)
                    {
                        //simulates a click
                        blocks[a+counter][b+secondCounter].checkCell();
                        if(blocks[a+counter][b+secondCounter].getBomb())
                        {
                            JOptionPane.showMessageDialog(mainFrame, "Unforunately, you lost");
                            reset();
                            mainFrame.setContentPane(menuPanel);
                        }
                        numberOfClicks++;
                        checkSurroundingCells(a+counter,b+secondCounter);
                        if(width != 0)
                            winCheck();
                    }
                }
            }
        }
    }
}