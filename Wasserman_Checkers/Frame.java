import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
/**
 * Graphically represents a checkers game between two people. Provides buttons that
 * allow players to click to move pieces. The class additionally provides various
 * methods that help the game run internally, such as methods to double jump or find
 * the available spaces to which a piece can move.
 *
 * @author (Adam Wasserman)
 * @version (1/21/20)
 */

public class Frame extends JFrame
{
    private final int ROWS = 8;
    private final int COLS = 8;
    private final Color myGREEN = new Color(119,149,86);
    private final Color myWHITE = new Color(235,236,208);
    private final Color myYELLOW = new Color(224, 196, 67);
    private final ImageIcon redPiece= new ImageIcon("red.png");
    private final ImageIcon whitePiece= new ImageIcon("white.png");
    private final ImageIcon redKing = new ImageIcon("red_king.png");
    private final ImageIcon whiteKing = new ImageIcon("white_king.png");
    private ArrayList<Integer> input = new ArrayList<Integer>();
    private JButton[][] bBoard;
    private Board game;
    private ArrayList<Integer> doubleJumps;

    /**
     * Initializes a graphical representation of the 8x8 board as a JFrame. The pieces
     * are represented as buttons to make the board interactive.
     */
    public Frame(){
        setSize(750,750);
        setResizable(false);
        bBoard = new JButton[ROWS][COLS];
        setLayout(new GridLayout(ROWS,COLS,0,0));
        game = new Board();
        doubleJumps = new ArrayList<Integer>();
        Piece[][] board = game.getBoard();
        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                bBoard[r][c] = new JButton();
                bBoard[r][c].setOpaque(true);
                if((r+c)%2 == 1) bBoard[r][c].setBackground(myWHITE);
                else bBoard[r][c].setBackground(myGREEN);
                if(board[r][c].team() == 1) bBoard[r][c].setIcon(whitePiece);
                else if(board[r][c].team() == 2) bBoard[r][c].setIcon(redPiece);
                add(bBoard[r][c]);
                bBoard[r][c].addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e){
                            ArrayList<Integer> toAdd = scan(e);
                            if(doubleJumps.size() > 0){
                                if(toAdd.get(0)==input.get(0) && toAdd.get(1)==input.get(1)){
                                    doubleJumps.clear();
                                    if(game.getPlayer() == 1) game.setPlayer(2);
                                    else game.setPlayer(1);
                                    resetColors();
                                }
                                for(int i =0; i < doubleJumps.size()-1;i+=2){
                                    if(doubleJumps.get(i)==toAdd.get(0) && doubleJumps.get(i+1)==toAdd.get(1)){
                                        game.movePiece(input.get(0),input.get(1),toAdd.get(0),toAdd.get(1));
                                        resetColors();
                                        if(!canDouble(toAdd.get(0),toAdd.get(1))){
                                            if(game.getPlayer() == 1) game.setPlayer(2);
                                            else game.setPlayer(1);
                                        }
                                        else{
                                            input.set(0,toAdd.get(0));
                                            input.set(1,toAdd.get(1));
                                        }
                                    }
                                }

                            }
                            else{
                                if(input.size() == 2){
                                    boolean n = game.movePiece(input.get(0),input.get(1),toAdd.get(0),toAdd.get(1));
                                    resetColors();
                                    if(Math.abs(input.get(0)-toAdd.get(0))==2 && canDouble(toAdd.get(0),toAdd.get(1))){
                                        input.set(0,toAdd.get(0));
                                        input.set(1,toAdd.get(1));
                                        updateBoard();
                                        revalidate();
                                        return;
                                    }
                                    if(n && doubleJumps.size() == 0){
                                        if(game.getPlayer() == 1) game.setPlayer(2);
                                        else game.setPlayer(1);
                                    }
                                    resetColors();
                                    input.clear();
                                }
                                if(board[toAdd.get(0)][toAdd.get(1)].team() == game.getPlayer()){
                                    resetColors();
                                    input.clear();
                                    input.add(toAdd.get(0));
                                    input.add(toAdd.get(1));
                                    ArrayList<Integer> available = findAvailable(toAdd.get(0),toAdd.get(1));
                                    for(int i = 0; i<available.size(); i+=2)
                                        bBoard[available.get(i)][available.get(i+1)].setBackground(myYELLOW);
                                }
                            }
                            updateBoard();
                        }
                    });
            }
        }
        setVisible(true);
        repaint();
        revalidate();
    }

    /**
     * Starts the program by instantiating the Frame class.
     * @param args Unused.
     */
    public static void main(String[] args){
        try{
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Frame jFrame = new Frame();
    }

    private void updateBoard(){
        Piece[][] board = game.getBoard();
        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                if(board[r][c].team() == 1){
                    bBoard[r][c].setIcon(whitePiece);
                    if(board[r][c].isKing()) bBoard[r][c].setIcon(whiteKing);
                }
                else if(board[r][c].team() == 2){
                    bBoard[r][c].setIcon(redPiece);
                    if(board[r][c].isKing()) bBoard[r][c].setIcon(redKing);
                }
                else bBoard[r][c].setIcon(null);
            }
        }
        repaint();
        revalidate();
    }

    private ArrayList<Integer> scan(ActionEvent e){
        Object source = e.getSource();
        ArrayList<Integer> toReturn = new ArrayList<Integer>();
        int i, j = 0;
        for(i = 0; i < 8; i++){
            for(j = 0; j < 8; j++){
                if(source == bBoard[i][j]){
                    toReturn.add(i);
                    toReturn.add(j);
                }
            }
        }
        return toReturn;
    }

    /**
     * Returns a list of the spaces a piece can move
     * @param row the row of the piece to move
     * @param col the column of the piece to move
     * @return the ArrayList of the available spaces
     */
    private ArrayList<Integer> findAvailable(int row, int col){
        Piece[][] board = game.getBoard();
        ArrayList<Integer> toReturn = new ArrayList<Integer>();
        for(int i = -2; i <= 2; i++){
            for(int j = -2; j <= 2; j++){
                if(Math.abs(i) != Math.abs(j)) continue;
                if(i == 0) continue; //implies j == 0 as well
                int newRow = row + i;
                int newCol = col + j;
                int forward = -1;
                if(game.getPlayer() == 2) forward = 1;
                if(!board[row][col].isKing() && i * forward < 0) continue; //get rid of not king and not going forward
                if(newRow < 0 || newRow > 7) continue; // get rid of out of board
                if(newCol < 0 || newCol > 7) continue; // see above
                if(board[newRow][newCol].team() != 0) continue; // must move onto empty space
                if(Math.abs(i) == 2 && board[row+i/2][col+j/2].team() * game.getPlayer() != 2){
                        continue; //if not jumping over enemy
                }
                toReturn.add(newRow);
                toReturn.add(newCol); 
                /*
                 * The above method of storing the row and column
                 * as adjacent entries in an ArrayList is clunky
                 * If I were to rewrite this class, I would make doubleJumps
                 * and findAvailable be ArrayLists of size-2 arrays
                 */
            }
        }
        return toReturn;
    }

    private boolean canDouble(int row, int col){
        doubleJumps.clear();
        ArrayList<Integer> available = findAvailable(row, col);
        for(int i = 0; i < available.size()-1; i+=2){
            if(Math.abs(available.get(i) - row) == 2){
                doubleJumps.add(available.get(i));
                doubleJumps.add(available.get(i+1));
            }
        }
        for(int i =0; i < doubleJumps.size()-1; i+=2){
            bBoard[doubleJumps.get(i)][doubleJumps.get(i+1)].setBackground(myYELLOW);
        }
        if(doubleJumps.size() > 0) return true;
        return false;
    }

    private void resetColors(){
        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                if((r + c) % 2 == 0) bBoard[r][c].setBackground(myGREEN);
            }
        }
    }
}
