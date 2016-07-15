package shift.puzzle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Math;
import java.io.*;
import sun.audio.*;

public class ShiftPuzzle implements MouseListener {
    Drawing draw = new Drawing();
    int[][] board = {{1,2,3,4}, {5,6,7,8}, {9,10,11,12}, {13,14,15,0}};
    int [][] originalBoard = new int [board.length][board[0].length];
    ImageIcon[] boardPictures  = new ImageIcon[16];
    ImageIcon background;
    ImageIcon full;
    ImageIcon banner;   //you win
    ImageIcon[] cheer = new ImageIcon[16];    //patrick cheering gif
    int blankRow, blankCol;
    int prev = 50;
    int maxx=700;
    int maxy=590;
    boolean done = false;
    
    public ShiftPuzzle() {  //constructor
        System.out.println("constructor");
        background = new ImageIcon ("background.jpg");  //background
        full = new ImageIcon ("full.jpg");  //full picture
        banner = new ImageIcon ("banner.jpg");  //ending banner
        for (int i =0; i<boardPictures.length;i++)
        {
            boardPictures[i] = new ImageIcon (i+".jpg");
            cheer[i] = new ImageIcon ("cheer.gif");    //patrick ending cheer gif
        }
        JFrame frame = new JFrame ("Puzzle");
        frame.add(draw);
        draw.addMouseListener(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(maxx, maxy);
        System.out.println("hi");
        store();
        initializeBoard();
        
        System.out.println("HI");
        
        //button to reshuffle board (Start over)
        JPanel panel = new JPanel();
        panel.setLayout (new FlowLayout(FlowLayout.CENTER,7,7));
        frame.add(panel, "South");
        JButton reshuffle = new JButton ("Start Over");
        reshuffle.addMouseListener(
                new MouseAdapter(){
                    public void mouseClicked (MouseEvent e){
                        reset();
                    }
                }
        );
        reshuffle.addMouseListener(this);
        panel.add(reshuffle);
        
        //button to shuffle more - increases difficulty
        panel.setLayout(new FlowLayout (FlowLayout.CENTER,7,15));
        frame.add(panel,"South");
        JButton again = new JButton ("Shuffle More!");
        again.addMouseListener(
                new MouseAdapter(){
                    public void mouseClicked (MouseEvent e){
                        done = false;
                        System.out.println("MORE");
                        initializeBoard();
                    }
                }
        );
        again.addMouseListener(this);
        panel.add(again);
        frame.setVisible(true);
    }//ShiftPuzzle method
    
    public void reset(){
        for (int i=0; i<board.length; i++)
            for (int j=0; j<board[0].length;j++)
                board[i][j] = originalBoard[i][j];
        done = false;
        System.out.println("reset");
        initializeBoard();
    }//reset method
    
    public void store(){
        //stores original value
        for (int i=0; i<board.length; i++)
            for (int j=0; j<board[0].length; j++)
                originalBoard[i][j] = board[i][j];
    }//store method
    
    //gets the 4X4 puzzle board ready to play
    public void initializeBoard(){
        //sets the number of times the board shuffles
        for (int i=0; i<5; i++)
        {
            shuffleBoard();
            System.out.println("initialize FOR");
        }
        System.out.println("in DONE");
    }//initlizeBoard method
    
    //finds a blank spot and picks a tile randomely to move into it
    public void shuffleBoard(){
        findBlank();
        int chosenRow = blankRow;
        int chosenCol = blankCol;
        int chance;
System.out.println("shuffle");
        do{ //prevents repetition in shuffle
            chosenRow = blankRow;
            chosenCol = blankCol;
            
            //if blank spot is in CORNER - 2 possible moves
            if ((blankRow==0 && (blankCol==0 || blankCol==3)) || (blankRow ==3 && (blankCol==0 || blankCol==3))){
                if (blankRow==0){
                    if (blankRow ==blankCol){   //blank at (0,0)
                        chosenCol  = (int)(Math.random()*((blankCol+1)-blankCol+1)+blankCol);
                        if (chosenCol==blankCol)    //moves down 1 row
                            chosenRow++;
                    }
                    else if (blankCol==3){  //blank at (0,3)
                        chosenCol = (int)(Math.random()*(blankCol-(blankCol-1)+1)+(blankCol-1));
                        if (chosenCol==blankCol)    //moves down 1
                            chosenRow++;
                    }
                }
                else if (blankRow==3){  //row 4
                    if (blankCol==0){   //blank at (3,0)
                        chosenCol = (int)(Math.random()*((blankCol+1)-blankCol+1)+blankCol);
                        if (chosenCol==blankCol)    //move up 1
                            chosenRow--;
                    }
                    else if (blankCol==3){    //blank at (3,3)
                        chosenCol = (int)(Math.random()*(blankCol-(blankCol-1)+1)+(blankCol-1));
                        if (chosenCol==blankCol)    //moves up 1
                            chosenRow--;
                    }
                }
            }
            
            //if blank spot is on the SIDES - 3 possible moves
            else if (((blankCol==1 || blankCol==2) && (blankRow==0 || blankRow==3)) || ((blankRow==1 || blankRow==2) && blankCol==0 || blankCol==3)){
                if (blankRow==0 || blankRow==3){    //blank at row 0 or 3
                    chosenCol = (int)(Math.random()*((blankCol+1)-(blankCol-1)+1)+(blankCol-1));
                    if (chosenCol==blankCol&& blankRow==0)  //row 0
                        chosenRow++;
                    else if (chosenCol==blankCol && blankRow==3)    //row 3
                        chosenRow--;
                }
                else if (blankCol==0 || blankCol==3){    //blank at col 0 or 3
                    chosenRow =(int)(Math.random()*((blankRow+1)-(blankRow-1)+1)+(blankRow-1));
                    if (chosenRow==blankRow && blankCol==0) //at row 0
                        chosenCol++;
                    else if (chosenRow == blankRow && blankCol==3)  //row 3
                        chosenCol--;
                }
            }
            
            //if blank spot is in MIDDLE - 4 possible moves
            else{
                //evens out chances or moving along col vs along row
                chance = (int)(Math.random()*3);
                
                //determines row first if chosen number is 1
                if (chance==1){ //determines row first if chosen number is 1
                    chosenRow = (int)(Math.random()*((blankRow+1)-(blankRow-1)+1)+(blankRow-1));
                    if (chosenRow==blankRow)
                        chosenCol=(int)(Math.random()*((blankCol+1)-(blankCol-1)+1)+(blankCol-1));
                }
                
                //determines column first if chosen number is 2
                else{
                    chosenCol = (int)(Math.random()*((blankCol+1)-(blankCol-1)+1)+(blankCol-1));
                    if (chosenCol==blankCol)
                        chosenRow=(int)(Math.random()*((blankRow+1)-(blankRow-1)+1)+(blankRow-1));
                }
            }
        } while ((board[chosenRow][chosenCol]) == prev || board[chosenRow][chosenCol]==0);
        
        //stores previously chosen tile to prevent it from being chosen again
        prev = board[chosenRow][chosenCol];
        
        //switches values of blank space and chosen space
        board[blankRow][blankCol]=board[chosenRow][chosenCol];
        board[chosenRow][chosenCol]=0;
        
        System.out.println("Shuffle end");
    }//shuffleBoard
    
    //finds where blank spot is and stores its row and col into blankRow and blankCol
    public void findBlank(){
        //find blank spot 
        for (int r=0; r<board.length; r++){
            for (int c=0; c<board[0].length;c++){
                if (board[r][c]==0){
                    //stores blanks row and col
                    blankRow=r;
                    blankCol=c;
                }
            }
        }
        System.out.println("blank");
    }//findBlank method
    
    //moves the chosen (mouse-clicked) tile into a blank spot if possible
    public void moveTile(int row, int col){
        findBlank();
        
        //if blank space is beside and vertically or horizontally touching the selected space
        if ((row==blankRow && (col==blankCol-1 || col == blankCol+1)) || (col==blankCol && (row == blankRow-1 || row == blankRow+1))){
            //switches values of clicked space and blank space
            board[blankRow][blankCol] = board[row][col];
            board[row][col] = 0;
            
            //calls to check if player has won
            check();
        }
    }//moveTile
    
    public void check(){
        int count = 0;  //counter for correct tiles
        for (int r=0; r<board.length; r++){
            for (int c=0; c<board[0].length; c++){
                //if tile is equal to original value (before shuffling)
                if (board[r][c]==originalBoard[r][c])
                    count++;
            }
        }
        if (count ==16){   //if all 16 btiles are in correct locations
            done = true;    //player has won and game has ended
        }
    }//check
    
    class Drawing extends JComponent{
        public void paint (Graphics g){
            g.drawImage(background.getImage(),0,0, maxx, maxy, this);   //background
            g.drawImage(full.getImage(),maxx-275, maxy-550, 235, 235, this);    //full picture
            
            for (int row=0; row<4; row++)
                for (int col=0; col<4; col++)
                    g.drawImage(boardPictures[board[row][col]].getImage(),col*100, row*100, 100,100, this);
            
            //if all blocks are correct, output banner
            if (done==true)
            {
                for (int row=0; row<4; row++)
                    for (int col=0; col<4; col++)
                        g.drawImage(cheer[board[row][col]].getImage(),col*100, row*100, 100,100, this);
                g.drawImage(banner.getImage(),300, 300, 100,100, this);  //you win
            }
        }
    }
    
    //starts implementing MouseListener - 5 methods
    public void mousePressed(MouseEvent e){
        
    }
    
    public void mouseReleased (MouseEvent e){
        //find coords of mouse click
        int row=e.getY()/100;
        int col=e.getX()/100;
        
        moveTile(row, col);
        
        //get paint to be called to reflect mouse work
        draw.repaint();
    }
    
    public void mouseClicked(MouseEvent e){
        
    }
    
    public void mouseEntered (MouseEvent e){
        
    }
    
    public void mouseExited(MouseEvent e){
        
    }
    //finishing implementing MouseListener
    
    public static void main(String[] args) 
    throws Exception{
        
        new ShiftPuzzle();
        
        //plays background music
        String bgMusic = "/Users/aquag/OneDrive/Documents/Shift Puzzle/bgMusic.au";
        InputStream in = new FileInputStream(bgMusic);
        
        AudioStream audioStream = new AudioStream(in);
        
        AudioPlayer.player.start(audioStream);
    }//main method
    
}//ShiftPuzzle class
