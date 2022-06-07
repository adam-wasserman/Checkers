import java.util.ArrayList;
import java.util.Scanner;
/**
 * Represents a checker board and provides functions to play a game with two people.
 *
 * @author (Adam Wasserman)
 * @version (1/17/2020)
 */
public class Board
{
    private Piece[][] board;
    private int player; //whose turn it is

    /**
     * Instantiates an 8x8 board as a 2-D array with the pieces in starting position.
     * Team 1's pieces will start on the bottom. Team 2's pieces will start on the top.
     * Empty spaces will be represented by a piece of team 0. Player will will go first.
     */
    public Board(){
        board = new Piece[8][8];
        for(int r =0; r < 3; r++){
            for(int c=r%2; c < board[r].length; c+=2){
                board[r][c] = new Piece(2);
            }
        }
        for(int r =board.length-3; r < board.length; r++){
            for(int c=r%2; c < board[r].length; c+=2){
                board[r][c] = new Piece(1);
            }
        }
        for(int r=0; r<board.length; r++){
            for(int c=0; c<board[r].length; c++){
                if(board[r][c] == null) board[r][c] = new Piece(0);
            }
        }
        player = 1;
    }

    /**
     * Move the piece at the given location to another given location if that move is valid.
     * Otherwise, it returns false.
     * @param row the piece's current row
     * @param col the piece's current column
     * @param newRow the row of the space to which the piece will be moved
     * @param newCol the column of the space to which the piece will be moved
     * @return whether or not the move was valid
     */
    public boolean movePiece(int row, int col, int newRow, int newCol){
        if(row > 7 || row < 0 || col > 7 || col < 0) return false; //off board
        if(newRow > 7 || newRow < 0 || newCol > 7 || newCol < 0) return false; //off board
        Piece s_piece = board[row][col];
        if(s_piece.team() != player) return false; //wrong team
        int forward = 1;
        if(player == 1) forward = -1; // defines what's forward
        if(!s_piece.isKing() && (newRow - row) * forward < 0) return false; //wrong dir
        if(board[newRow][newCol].team() != 0) return false;//spot not empty

        if(Math.abs(row - newRow) > 1 || Math.abs(col - newCol) > 1){
            if(Math.abs(row - newRow) == 2 && Math.abs(col - newCol) == 2){
                Piece toJump = board[(newRow-row)/2+row][(newCol-col)/2+col];
                if(toJump.team()*player != 2) return false; //ensures jumping opposite team
                toJump.setEmpty();
            }
            else return false;//cannot move more than 1 diagonal unless jumping 2x2
        }
        else if(!(Math.abs(row - newRow) == 1 && Math.abs(col - newCol) == 1)){
            return false; //can only move on a diagonal
        }
        board[row][col] = new Piece(0);
        board[newRow][newCol] = s_piece;
        s_piece.makeKing(newRow);
        return true;
    }

    /**
     * Gets the 2-D array which represents the current board
     * @return the 2-D array that represents the current board
     */
    public Piece[][] getBoard(){
        return board;
    }

    /**
     * Will set the turn to the player corresponding to the given integer
     * @param the integer corresponding to the player whose turn it will be
     */
    public void setPlayer(int newPlayer){
        player = newPlayer;
    }

    /**
     * Gets the player whose turn it currently is
     * @return the integer corresponding to the player whose turn it is
     */
    public int getPlayer(){
        return player;
    }
}
