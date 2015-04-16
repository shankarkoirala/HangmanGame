package com.shankar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by kornsanz on 4/5/15.
 */
public class HangmanUI extends JFrame{

    JButton exit    = null;
    JButton newGameButton = null;

    JLabel wordArea    = null;
    JLabel messageArea = null;
    List alphabetButtonList = new ArrayList();
    Iterator alphaIterator = null;

    boolean reset        = true;
    boolean disable      = false;

    JPanel hangingPanel;

    // list of target words
    String[] targetWords = {
            "diffuse", "district", "color", "value", "helper",
            "best", "charcoal", "smoke", "principal", "video",
            "java", "drink", "homework","shell", "audio",
            "define", "android", "drawing", "picture", "frame",
            "nutshell", "polygon", "circle", "rectangle", "sphere",
            "company", "lotion", "shoes", "trowsers", "belt",
            "blouse", "temple", "cowboy", "engineer", "waiter",
            "wheel", "engine", "pedal", "road", "navigate",
            "boat", "skiing", "cycle", "runner", "bike",
            "deer", "lion", "pseudonym", "tiger", "monkey"
    };


    String winningMessage = "Congratulations!  You are saved!";
    String losingPrefix  = "You are DEAD The answer was ";
    String currentGuess;
    String targetWord;

    int numberWrong       = 0;
    int numberOfBodyParts = 5;
    int next              = 0;

    public HangmanUI()
    {
        setSize(640, 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createLoweredBevelBorder());
        pane.setLayout(new BorderLayout());

        pane.add(createNorthPanel(), BorderLayout.NORTH);

        pane.add(createSouthPanel(), BorderLayout.SOUTH);

        pane.add(createEastPanel(), BorderLayout.EAST);

        pane.add(createWestPanel(), BorderLayout.WEST);

        pane.add(createCenterPanel(), BorderLayout.CENTER);

        add(pane);
        setVisible(true);

    }


    public Component createNorthPanel() {
        JPanel pane = new JPanel();
        pane.setLayout(new BorderLayout());
        pane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));


        wordArea = new JLabel("Press New Game");
        wordArea.setFont( new Font("Helvetica", Font.PLAIN, 24) );
        wordArea.setBackground(Color.lightGray);
        wordArea.setForeground(Color.black);
        pane.add(wordArea, BorderLayout.CENTER);
        //pane.add(Box.createHorizontalGlue() );

        return pane;
    }

    public Component createSouthPanel() {
        JPanel pane = new JPanel();
        pane.setBorder( BorderFactory.createEmptyBorder(10, 10, 10, 10) );

        pane.setLayout(new BorderLayout());

        messageArea = new JLabel("Win or Die!");

        messageArea.setFont( new Font("Helvetica", Font.PLAIN, 28) );
        messageArea.setBackground( Color.lightGray );
        messageArea.setForeground( Color.red );
        pane.add(messageArea,BorderLayout.CENTER);
        return pane;
    }

    public Component createCenterPanel() {
        hangingPanel = new JPanel();

        System.out.println("createCenterPane  is called");
        BufferedImage myPicture = null;

        try {

            myPicture = ImageIO.read(new File("/home/kornsanz/IdeaProjects/HangmanGame/src/images/default.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));

        hangingPanel.add(picLabel);

        return hangingPanel;
    }

    public Component createEastPanel() {

        ActionListener controlButtonListener = new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                JButton buttonPushed = (JButton)e.getSource();
                if( buttonPushed.getText().equals("New Game") ) {
                    setUpNewGame();
                }
                else {
                    System.exit(0);
                }
            }
        }; //listener for controlbutton




        JPanel pane = new JPanel();

        pane.setLayout(new BorderLayout());


        newGameButton = new JButton( "New Game" );
        newGameButton.addActionListener( controlButtonListener );
        pane.add( newGameButton , BorderLayout.SOUTH);


        exit = new JButton( "Exit" );
        exit.addActionListener( controlButtonListener );
        pane.add( exit, BorderLayout.NORTH);

        return pane;
    }


    public void setUpNewGame() {
        numberWrong = 0;
        messageArea.setText("Win or Die!");

        //Enable alphabet buttons
        Iterator alphaIterator = alphabetButtonList.iterator();
        while( alphaIterator.hasNext() ) {
            ( (JButton)alphaIterator.next() ).setEnabled( reset );
        }

        //Disable new game button
        newGameButton.setEnabled( disable );


        wordArea.setBackground(Color.lightGray);


        double number = Math.random();
        next = (int)( number * targetWords.length );
        targetWord  = targetWords[next];

        //Fill the word-to-guess with ???
        currentGuess = "?";
        for( int i=0; i<targetWord.length()-1; i++) {
            currentGuess = currentGuess.concat("?");
        }
        wordArea.setText( currentGuess );




    }//set up a neew game


    public void processAnswer(String answer) {
        char newCharacter = answer.charAt(0);

        String nextGuess    = "";
        boolean foundAMatch = false;

        for( int i=0; i<targetWord.length(); i++ ) {
            char characterToMatch = targetWord.charAt(i);
            if( characterToMatch == newCharacter ) {
                nextGuess = nextGuess.concat( String.valueOf(newCharacter) );
                foundAMatch = true;
            }
            else {
                nextGuess = nextGuess.concat(String.valueOf( currentGuess.charAt(i) ));
            }
        }// for each character

        currentGuess = nextGuess;
        wordArea.setText( currentGuess );

        // for winner
        if( currentGuess.equals( targetWord ) ) {
            //Disable the buttons
            Iterator alphaIterator = alphabetButtonList.iterator();
            while( alphaIterator.hasNext() ) {
                ( (JButton)alphaIterator.next() ).setEnabled( disable );
            }
            messageArea.setText( winningMessage );
            newGameButton.setEnabled( reset );
            exit.setEnabled( reset );
        }
        // Wrong Answer
        //   Set out a new body part to be drawn by repaint()
        else {
            if( !foundAMatch ) {
                numberWrong++;
                hang(numberWrong+1);

            }
            // Is the game over?
            if( numberWrong >= numberOfBodyParts ) {
                //Disable the buttons
                Iterator alphaIterator = alphabetButtonList.iterator();
                while( alphaIterator.hasNext() ) {
                    ( (JButton)alphaIterator.next() ).setEnabled( disable );
                }
                messageArea.setText( losingPrefix + targetWord );
                newGameButton.setEnabled( reset );
                exit.setEnabled( reset );
            }
        }//if else
    }//processAnswer


    public void hang(int numberWrong)
    {
        BufferedImage image = null;
        JLabel picLabel = null;
        switch(numberWrong)
        {
            case 1:
                try {
                    image = ImageIO.read(new File("/home/kornsanz/IdeaProjects/HangmanGame/src/images/default.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                hangingPanel.removeAll();
                picLabel = new JLabel(new ImageIcon(image));
                hangingPanel.add(picLabel);
                hangingPanel.repaint();
                System.out.println("Image 1 ");

                break;
            case 2:
                try {
                    image = ImageIO.read(new File("/home/kornsanz/IdeaProjects/HangmanGame/src/images/1wrong.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                hangingPanel.removeAll();
                picLabel = new JLabel(new ImageIcon(image));
                hangingPanel.add(picLabel);
                hangingPanel.repaint();
                System.out.println("Image 2 ");


                break;
            case 3:
                try {
                    image = ImageIO.read(new File("/home/kornsanz/IdeaProjects/HangmanGame/src/images/2wrong.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                hangingPanel.removeAll();
                picLabel = new JLabel(new ImageIcon(image));
                hangingPanel.add(picLabel);
                hangingPanel.repaint();

                System.out.println("Image 3 ");

                break;
            case 4:

                try {
                    image = ImageIO.read(new File("/home/kornsanz/IdeaProjects/HangmanGame/src/images/3wrong.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                hangingPanel.removeAll();
                picLabel = new JLabel(new ImageIcon(image));
                hangingPanel.add(picLabel);
                hangingPanel.repaint();

                System.out.println("Image 4 ");

                break;
            case 5:

                try {
                    image = ImageIO.read(new File("/home/kornsanz/IdeaProjects/HangmanGame/src/images/4wrong.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                hangingPanel.removeAll();
                picLabel = new JLabel(new ImageIcon(image));
                hangingPanel.add(picLabel);
                hangingPanel.repaint();


                System.out.println("Image 5 ");

                break;
            case 6:
                try {
                    image = ImageIO.read(new File("/home/kornsanz/IdeaProjects/HangmanGame/src/images/5wrong.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                hangingPanel.removeAll();
                picLabel = new JLabel(new ImageIcon(image));
                hangingPanel.add(picLabel);

                System.out.println("Image 6 ");


                break;

        }

    }

    public Component createWestPanel() {
        ActionListener alphabetButtonAction = new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                JButton buttonPushed = (JButton)e.getSource();
                buttonPushed.setEnabled( disable );
                processAnswer( buttonPushed.getText() );
            }
        };

        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createLoweredBevelBorder());
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c  = new GridBagConstraints();
        pane.setLayout( gridbag );
        c.fill = GridBagConstraints.BOTH;

        JButton button = new JButton( "a" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 0;
        c.gridy      = 0;
        c.gridheight = 1;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );

        button = new JButton( "b" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 1;
        c.gridy      = 0;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );

        button = new JButton( "c" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 2;
        c.gridy      = 0;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );

        button = new JButton( "d" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 0;
        c.gridy      = 1;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );

        button = new JButton( "e" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 1;
        c.gridy      = 1;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );

        button = new JButton( "f" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 2;
        c.gridy      = 1;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );


        button = new JButton( "g" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 0;
        c.gridy      = 2;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );

        button = new JButton( "h" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 1;
        c.gridy      = 2;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );

        button = new JButton( "i" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 2;
        c.gridy      = 2;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );

        button = new JButton( "j" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 0;
        c.gridy      = 3;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );

        button = new JButton( "k" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 1;
        c.gridy      = 3;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );

        button = new JButton( "l" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 2;
        c.gridy      = 3;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );

        button = new JButton( "m" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 0;
        c.gridy      = 4;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );

        button = new JButton( "n" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 1;
        c.gridy      = 4;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );

        button = new JButton( "o" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 2;
        c.gridy      = 4;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );

        button = new JButton( "p" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 0;
        c.gridy      = 5;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );

        button = new JButton( "q" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 1;
        c.gridy      = 5;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );

        button = new JButton( "r" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 2;
        c.gridy      = 5;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );

        button = new JButton( "s" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 0;
        c.gridy      = 6;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );

        button = new JButton( "t" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 1;
        c.gridy      = 6;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );

        button = new JButton( "u" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 2;
        c.gridy      = 6;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );

        button = new JButton( "v" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 0;
        c.gridy      = 7;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );

        button = new JButton( "w" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 1;
        c.gridy      = 7;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );

        button = new JButton( "x" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 2;
        c.gridy      = 7;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );

        button = new JButton( "y" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 0;
        c.gridy      = 8;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );

        button = new JButton( "z" );
        c.weightx    = 0.5;
        c.weighty    = 0.5;
        c.gridx      = 1;
        c.gridy      = 8;
        gridbag.setConstraints( button, c );
        button.addActionListener( alphabetButtonAction );
        pane.add( button );
        alphabetButtonList.add( button );

        pane.setEnabled(true);

        alphaIterator = alphabetButtonList.iterator();
        while( alphaIterator.hasNext() ) {
            ( (JButton)alphaIterator.next() ).setEnabled( disable );
        }
        return pane;
    }




}
