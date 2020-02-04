/*
 * 
 * 
 * 
 * 
 * 
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.*;
import java.lang.Math;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class SliderGame extends JFrame implements ActionListener {
        private JFrame mainFrame;
        private JPanel gameboardPanel, mainPanel, menuPanel;
	private JButton[] buttons, selectButton = new JButton[3];
        private JButton scrambleButton, resetButton, playButton, customSizeButton, customImageButton, numberSliderButton,yasuoButton, luxButton, sonaButton, displayImageButton;
        private JTextArea movesTextArea, winsTextArea;
        private JLabel titleLabel;
        //sets the size of the slider
        private int size = 0;
        //Variable will track the empty spot
	private int emptyIndex;
        //number of move counter
        private int moves = 0; 
        // win counter
        private int wins = 0; 
        //allow user to see mini version of image for prepared images
        private int imageSelection = 0;
        //booleans for different functions
        private boolean winnable = true, buttonsSetUp = false, imageSelected = false; 
        //declare an array of BufferedImages
        private BufferedImage[] imgs;
        //File to store images
        File pickedImage;
        public static void main(String[] args) {
            //Run constructor for class
            new SliderGame();      
	}
        
	public SliderGame()
	{
                //sets up JFrame
		mainFrame = new JFrame("Slider Practice GUI");
                mainFrame.setSize(700,700);  
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                //sets up JPanel for user selection
                menuPanel = new JPanel();
                menuPanel.setSize(700,700);
                menuPanel.setLayout(null);
                
                //sets up assortment of gui components
                titleLabel = new JLabel("Slider Game");
                titleLabel.setBounds(275,0,300,100);
                titleLabel.setFont(new Font("Helvetica", Font.BOLD, 26));
                        
                playButton = new JButton("Play");
                playButton.setBounds(300,500,100,50);
                playButton.setFont(new Font("Helvetica", Font.BOLD, 26));
                playButton.addActionListener(this);
                        
                customSizeButton = new JButton("Custom Size");
                customSizeButton.setBounds(375,600,150,50);
                customSizeButton.setFont(new Font("Helvetica", Font.BOLD, 12));
                customSizeButton.addActionListener(this);
                
                customImageButton = new JButton("Custom Image");
                customImageButton.setBounds(525,600,150,50);
                customImageButton.setFont(new Font("Helvetica", Font.BOLD, 12));
                customImageButton.addActionListener(this);
                
                for(int m = 0; m < selectButton.length; m++)
                {
                selectButton[m] = new JButton((m+3) + " by " + (m+3));
                selectButton[m].setBounds(25+(m*100),600,100,50);
                selectButton[m].setFont(new Font("Helvetica", Font.BOLD, 12));
                selectButton[m].addActionListener(this);
                }
                
                yasuoButton = new JButton(new ImageIcon("miniYasuo.jpg"));
                yasuoButton.setBounds(150,100,200,200);
                yasuoButton.addActionListener(this);
                
                numberSliderButton = new JButton(new ImageIcon("numberSlider.jpg"));
                numberSliderButton.setBounds(150,300,200,200);
                numberSliderButton.addActionListener(this);
                
                luxButton = new JButton(new ImageIcon("miniLux.jpg"));
                luxButton.setBounds(350,100,200,200);
                luxButton.addActionListener(this);
                
                sonaButton = new JButton(new ImageIcon("miniSona.jpg"));
                sonaButton.setBounds(350,300,200,200);
                sonaButton.addActionListener(this);
                
                
                //sets up JPanel for the game
		mainPanel = new JPanel();
                mainPanel.setSize(700,700);
                mainPanel.setLayout(null);
                
                //sets up assortment of gui components
                scrambleButton = new JButton("Scramble");
                scrambleButton.setBounds(0,600,100,30);
                scrambleButton.addActionListener(this);
                
                resetButton = new JButton("Reset");
                resetButton.setBounds(100,600,100,30);
                resetButton.addActionListener(this);
                
                movesTextArea = new JTextArea("Moves: " + moves);
                movesTextArea.setBounds(200,600,100,50);
                movesTextArea.setFont(new Font("Helvetica", Font.BOLD, 18));
                movesTextArea.setBackground(new Color(0,0,0,0));
                movesTextArea.setOpaque(false);
                movesTextArea.setEditable(false);
                
                winsTextArea = new JTextArea("Wins: " + wins);
                winsTextArea.setBounds(300,600,100,50);
                winsTextArea.setFont(new Font("Helvetica", Font.BOLD, 18));
                winsTextArea.setBackground(new Color(0,0,0,0));
                winsTextArea.setOpaque(false);
                winsTextArea.setEditable(false);
                
                displayImageButton = new JButton("Original image");
                displayImageButton.setBounds(400,600,140,30);
                displayImageButton.setFont(new Font("Helvetica", Font.BOLD, 14));
                displayImageButton.addActionListener(this);
                
                //adds various components to menuPanel
                menuPanel.add(playButton);
                menuPanel.add(selectButton[0]);
                menuPanel.add(selectButton[1]);
                menuPanel.add(selectButton[2]);
                menuPanel.add(customSizeButton);
                menuPanel.add(customImageButton);
                menuPanel.add(titleLabel);
                menuPanel.add(numberSliderButton);
                menuPanel.add(yasuoButton);
                menuPanel.add(luxButton);
                menuPanel.add(sonaButton);
                
                //adds various components to mainPanel
                mainPanel.add(scrambleButton);
                mainPanel.add(resetButton);
                mainPanel.add(movesTextArea);
                mainPanel.add(winsTextArea);
                mainPanel.add(displayImageButton);
                
                //set contentPane
                mainFrame.setContentPane(menuPanel);           
                //Turn on JFrame
                mainFrame.setVisible(true);
	}
        
	
        @Override
	public void actionPerformed(ActionEvent e) {
            //if buttons are set up
            if(buttonsSetUp)
            {
                for(int n = 0; n < buttons.length; n++)
                {
                    //checks for clicks on buttons
                    if(e.getSource() == buttons[n])    
                    {
                        //swaps button
                        swapPieces(n);	
                        update();
                    }   
                }
                
                if(e.getSource() == scrambleButton)
                    scramble();
                
                if(e.getSource() == resetButton)
                {
                    moves = 0;
                    wins = 0;
                    reset();
                }
                
                //allows user to view the original image
                if(imageSelected) {
                if(e.getSource() == displayImageButton)
                {
                    FileInputStream imageFile = null;
                    ImageIcon image = null;
                    //if user selected yasuo image
                    if(imageSelection == 1){
                    try {
                        imageFile = new FileInputStream("miniYasuo.jpg");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(SliderGame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }
                    //if user selected lux image
                    if(imageSelection == 2){
                    try {
                        imageFile = new FileInputStream("miniLux.jpg");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(SliderGame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }
                    //if user selected sona image
                    if(imageSelection == 3){
                    try {
                        imageFile = new FileInputStream("miniSona.jpg");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(SliderGame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }
                    //if user selected custom image
                    if(imageSelection == 0) {
                    try {
                        imageFile = new FileInputStream(pickedImage);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(SliderGame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }
                    
                    try {
                        image = new ImageIcon(ImageIO.read(imageFile));
                    } catch (IOException ex) {
                        Logger.getLogger(SliderGame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    JOptionPane.showMessageDialog(null, "", "Original Image", JOptionPane.PLAIN_MESSAGE, image);
                }
                }
            }
                //checks for user selecting size
                for(int m = 0; m < selectButton.length; m++)
                {
                    if(e.getSource() == selectButton[m])
                    {
                        size = m+3;
                    }
                }
                
                //allows use to input custom size
                if(e.getSource() == customSizeButton)
                {
                String temp = JOptionPane.showInputDialog(mainFrame, "Please enter size:");
                try
                {
                    size = Integer.parseInt(temp);
                    if(size < 3 || size > 10)
                    {
                    JOptionPane.showMessageDialog(mainFrame, "Invalid Input, size set to 3");
                    size = 3;
                    }
                }
                catch(Exception fail)
                {
                    JOptionPane.showMessageDialog(mainFrame, "Invalid Input, size set to 3");
                    size = 3;
                }
                
                }
                
                //checks user selecting yasuo image
                if(e.getSource() == yasuoButton)
                {
                    imageSelection = 1;
                    imageSelected = true;
                    pickedImage = new File("yasuo.jpg");
                }
                
                //checks user selecting lux image
                if(e.getSource() == luxButton)
                {
                    imageSelection = 2;
                    imageSelected = true;
                    pickedImage = new File("elementalistLux.jpg");
                }
                
                //checks user selecting sona image
                if(e.getSource() == sonaButton)
                {
                    imageSelection = 3;
                    imageSelected = true;
                    pickedImage = new File("sona.jpg");
                }
                
                //allows user to download their own image and use it in the slider game
                if(e.getSource() == customImageButton)
                {
                    imageSelected = true;
                    String input = JOptionPane.showInputDialog(mainFrame, "Please enter image file");
                    try 
                    {
                    pickedImage = new File(input);
                    }
                    catch(Exception fail)
                    {
                    JOptionPane.showMessageDialog(mainFrame, "Invalid Input, image not set");
                    imageSelected = false;
                    }
                }
                //checks user to play regular number slider game
                if(e.getSource() == numberSliderButton)
                {
                    imageSelected = false;
                }
                //start game after user has selected settings
                if(e.getSource() == playButton)
                {
                    if(size!=0)
                    {
                        setUpButtons();
                        buttonsSetUp = true;
                        mainPanel.add(gameboardPanel);
                        mainFrame.setContentPane(mainPanel); //Add mainPanel to JFrame
                    }            
                }
	}
	
        
        private void setUpButtons()
        {
                //sets up gameboard
                gameboardPanel = new JPanel();
                //if image is not selected
                if(imageSelected != true) {
		gameboardPanel.setLayout(new GridLayout(size,size,5,5)); // custom size grid with 5 pixel padding
                gameboardPanel.setSize((700-(size*5)),600);
                }
                //if image is selected 
                if(imageSelected) {
                gameboardPanel.setLayout(new GridLayout(size,size,3,3)); // custom size grid with 3 pixel padding for less distortion
                gameboardPanel.setSize((700-(size*3)),600);
                }
                 //Allows empty space to be black
		gameboardPanel.setBackground(Color.black);
                Font f = new Font("Arial", Font.BOLD, 20);
		Color[] colours = {Color.orange, Color.white};
                buttons = new JButton[size*size];
       		int lineCounter = 0;
                //set i as counter for button
		for (int i = 0; i < buttons.length; i++)  
		{
                        //Start with Orange
			int colourIndex = 0;  
			//Constructor sets text on new buttons
			buttons[i] = new JButton("" + i);
			//default colour of square is orange
                        colourIndex = 0; 
                        //if size is odd
                        if(size%2 != 0)
                        { 
                            if (i%2 == 0)
                            {
                                //make white if necessary
                                colourIndex = 1; 
                            }
                        }
                        
                        // if size is even
                        if(size%2 == 0)
                        {
                            if(i%size == 0)
                            {
                                lineCounter++;
                            }
                            //odd line 
                            if(lineCounter%2 != 0)
                            {
                                colourIndex = 1;
                                if(i%2 == 0)
                                {
                                    colourIndex = 0;
                                }
                            }
                            //even line
                            if(lineCounter%2 == 0)
                            {
                                colourIndex = 0;
                                if(i%2 == 0)
                                {
                                    colourIndex = 1;
                                }
                            }
                        }    
                    //if image selected
                    if(imageSelected)
                    {
                        //add resized images onto button
                        imgs = getImages(); 
                        imageResize(i);
                    }
                    //set up attributes of button
                    buttons[i].setBackground(colours[colourIndex]);
                    buttons[i].setForeground(Color.blue);  
                    buttons[i].setFont(f);
                    buttons[i].addActionListener(this); 
                    buttons[i].setVisible(true);
                    gameboardPanel.add(buttons[i]);    
                }
                // sets last button invisible
		buttons[size*size-1].setVisible(false);
		emptyIndex=size*size-1;
		gameboardPanel.setEnabled(true);
        }
        
        //swaps buttons
	private void swapPieces(int btnIndex)   
	{
                //checks if it is a valid move
                if(isValid(btnIndex))
                {
                    //swap text of the buttons
                    buttons[emptyIndex].setText(buttons[btnIndex].getText()); 
                    if(imageSelected)
                    {
                        //swap image of the buttons
                        buttons[emptyIndex].setIcon(buttons[btnIndex].getIcon());
                    }
                    //set the emptyIndex visible after the swap
                    buttons[emptyIndex].setVisible(true);
                    //set btnIndex to emptyIndex and not visible
                    buttons[btnIndex].setVisible(false); 
                    emptyIndex = btnIndex;
                    //counts move and check for win
                    if(winnable)
                    {
                        moves++;
                        win();
                    }
                }
	}	
        
        private boolean isValid(int index)
        {
        int counter = 0;
        //button is not on the top line checker
        for(int i = 0; i < size; i++)
        {
            if(index != i)
            {
                counter++;
            }
        }
        if(counter == size)
        {
            if(index - emptyIndex == size)
                return true;
        }
        
        //button is not on the bottom line checker
        counter = 0;
        for(int i = size*(size-1); i < size*size; i++)
        {
            if(index != i)
            {
                counter++;
            }
        }
        if(counter == size)
        {
            if(emptyIndex - index == size)
                return true;
        }
        
        //button is not on leftmost side checker
        counter = 0;
        for(int i = 0; i < size; i++)
        {
            if(index != i*size)
            {
                counter++;
            }
        }
        if(counter == size)
        {
            if(index - emptyIndex == 1)
                return true;           
        }
        
        //button is not on rightmost side checker
        counter = 0;
        for(int i = 1; i <= size; i++)
        {
            if(index != i*(size)-1)
            {
                counter++;
            }
        }
        if(counter == size)
        {
            if(emptyIndex - index == 1)
                return true;
        }
        return false;
        }
        
        //scrambles the buttons around randomly
        private void scramble()
        {
            winnable = false;
            for(int i = 0; i < 300; i++)
            {
                int random;
                random = ((int)(Math.random()*(size*size-1)));
                swapPieces(random);
            }
            winnable = true;
        }
        
        //resets the slider back to orginial position
        private void reset()
        {
            //loop to reintialize buttons
            for(int i = 0; i < buttons.length; i++)
            {
                buttons[i].setText("" + i);
                if(i != (size*size)-1)
                    buttons[i].setVisible(true);
                else
                {
                    buttons[i].setVisible(false);
                }
                emptyIndex = i;
                //if image is selected    
                if(imageSelected)
                {
                    imageResize(i);
                }
            }
            moves = 0;
            wins = 0;
        }
        
        //method to determine if user has won
        private void win()
        {
            //while not scrambling
            while(winnable)
            {
            int winIdentify = 0;
            //check the integer in the button match up
            for(int i = 0; i < buttons.length; i++) 
            {
            if(buttons[i].getText().compareTo("" + i) == 0)
            {
                winIdentify++;
            }
            //check the emptyIndex is the last button
            if(emptyIndex != (size*size)-1)
            {
                winIdentify--;
            }
            }
            //compare to makes sure the user won
            if(winIdentify == buttons.length-1)
            {
                JOptionPane.showMessageDialog(null, "Congrats, you have won");
                wins++;
            }
            winnable = false;
            }
            winnable = true;
        }
        
        //updates the moves and wins counter
        private void update()
        {
        movesTextArea.setVisible(false);
        movesTextArea.setText("Moves: " + moves);
        movesTextArea.setBackground(new Color(0,0,0,0));
        movesTextArea.setVisible(true);
        
        winsTextArea.setVisible(false);
        winsTextArea.setText("Wins: " + wins);
        winsTextArea.setBackground(new Color(0,0,0,0));
        winsTextArea.setVisible(true);
        }
        
        // edited code snippet from https://stackoverflow.com/questions/12418618/split-image-into-clickable-regions
        // allows image to be cut into distinct regions
        private BufferedImage[] getImages() 
        {
        FileInputStream imageFile = null;
        try {
            //set picked image to imageFile
            imageFile = new FileInputStream(pickedImage);
        } catch (FileNotFoundException fail) {
            
        }
        BufferedImage image = null;
        try {
             //reading the image file
            image = ImageIO.read(imageFile);
        } catch (IOException fail) {
            
        }
         // determines the chunk width and height
        int chunkWidth = image.getWidth() / size;
        int chunkHeight = image.getHeight() / size;
        int count = 0;
        BufferedImage imgs[] = new BufferedImage[size*size]; //Image array to hold image chunks
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                //Initialize the image array with image chunks
                imgs[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType());

                // draws the image chunk
                Graphics2D gr = imgs[count++].createGraphics();
                gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);
                gr.dispose();
            }
        }
        return imgs;
        }
        
        //resizes the separate images into same size as the buttons
        private void imageResize(int i)
        {
        ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(imgs[i].getSource()));
        Image img = icon.getImage();
        //scales image to fit in button
        Image scaledImage = img.getScaledInstance((int) ((700)/size)+19, (int) ((600)/size), Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImage);
        buttons[i].setIcon(icon);       
        }
}
