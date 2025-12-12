/**
 * this class represents one square on the minesweeper board
 * each tile knows if it has a mine, if it's been revealed,
 * if it's flagged, and who its neighboring tiles are
 *
 * @author Omar Firdaus, Peyton Dao
 * @version .29alphaOmegagammaEpsilon
 */
import java.util.ArrayList;

/**
 * represents one square on the minesweeper board, and it knows if it has a mine, 
 * if it's been clicked, and who its neighbors are
 */
public class Tile {
    /** which row this tile is in */
    private final int tileRow;
    /** which column this tile is in */
    private final int tileColumn;
    /** the tiles next to this one (for counting nearby mines) */
    private final ArrayList<Tile> neighbors = new ArrayList<>(); 
    /** whether this tile has a mine hidden under it */
    private boolean mine = false;
    /** whether the player has clicked on this tile yet */
    private boolean revealed = false;
    /** whether the player has put a flag on this tile */
    private boolean flagged = false;


    /**
     * creates a new tile at a specific spot on the board
     *
     * @param tileRow    which row to put it in
     * @param tileColumn which column to put it in
     */
    public Tile(int tileRow, int tileColumn) {
        this.tileRow = tileRow;
        this.tileColumn = tileColumn;
    }


    /**
     * adds a tile as a neighbor so we can count mines around this one
     *
     * @param neighbor the tile next to this one
     */
    public void addNeighbor(Tile neighbor) {
        if (neighbor == null || neighbor == this) {
            return;
        }
        for (int i = 0; i < neighbors.size(); i++) {
            if (neighbors.get(i) == neighbor) {
                return;
            }
        }
        neighbors.add(neighbor); 
    }


    /**
     * gets all the tiles that are next to this one
     *
     * @return the list of neighboring tiles
     */
    public ArrayList<Tile> getNeighbors() {
        return neighbors; 
    }


    /**
     * gets which row this tile is in
     *
     * @return the row number
     */
    public int getTileRow() {
        return tileRow;
    }


    /**
     * gets which column this tile is in
     *
     * @return the column number
     */
    public int getTileColumn() {
        return tileColumn;
    }


    /**
     * sets whether this tile has a mine or not
     *
     * @param mine true if you want to put a mine here
     */
    public void setMine(boolean mine) {
        this.mine = mine;
    }


    /**
     * checks if this tile has a mine hidden under it
     *
     * @return true if there's a mine here
     */
    public boolean isMine() {
        return mine;
    }


    /** marks this tile as clicked/revealed */
    public void reveal() {
        revealed = true;
    }


    /**
     * checks if the player has clicked on this tile yet
     *
     * @return true if it's been revealed
     */
    public boolean isRevealed() {
        return revealed;
    }


    /**
     * checks if the player has flagged this tile
     *
     * @return true if there's a flag on it
     */
    public boolean isFlagged() {
        return flagged;
    }


    /** puts a flag on this tile if it doesn't have one, or removes it if it does */
    public void toggleFlag() {
        flagged = !flagged;
    }


    /**
     * counts how many mines are hiding in the tiles next to this one
     *
     * @return how many neighboring tiles have mines
     */
    public int getAdjacentMineCount() {
        int count = 0;
        for (Tile neighbor : neighbors) {
            if (neighbor.isMine()) {
                count++;
            }
        }
        return count;
    }
}