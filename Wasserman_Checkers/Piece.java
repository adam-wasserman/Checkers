
/**
 * Represents an single checkers piece. States whether the piece is a king and
 * to what team it belongs
 *
 * @author (Adam Wasserman)
 * @version (1/17/2020)
 */
public class Piece
{
    private int team; //0 is no team
    private boolean king;
    /**
     * Initializes a piece to not be a king and to belong to provided team.
     * A piece belonging to team 0 will function like a blank space.
     * @param theTeam the team to which the piece will belong.
     * If the team is 0, the piece will be treated like an empty space.
     * Precondition: the integer provided must be 0, 1, or 2.
     */
    public Piece(int my_team){
        team = my_team;
        king = false;
    }

    /**
     * Returns the team to which a piece belongs
     * @return the team to which the piece belongs as an integer
     */
    public int team(){
        return team;
    }

    /**
     * Sets the team to 0 and makes it a regular piece (not a king)
     */
    public void setEmpty(){
        team = 0;
        king = false;
    }

    /**
     * If at the proper end of the board, makes the piece a king
     * @param row the piece's current row
     */
    public void makeKing(int row){
        if(team == 1 && row == 0) king = true;
        if(team == 2 && row == 7) king = true;
    }

    /**
     * Checks if a piece is a king
     * @return whether or not the piece is a king
     */
    public boolean isKing(){
        return king;
    }
}
