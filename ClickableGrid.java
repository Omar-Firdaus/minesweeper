/**
 * this is the main game window class where you actually play minesweeper
 * it handles the game board, tile interactions, mine placement, and
 * all the game logic for revealing tiles and checking for wins
 *
 * @author Omar Firdaus, Peyton Dao
 * @version .29alphaOmegagammaEpsilon
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * this is the main game window where you actually play minesweeper
 */
public class ClickableGrid extends JFrame implements ActionListener, MouseListener {
    /** how many columns wide the board is */
    private final int boardColumns;
    /** how many rows tall the board is */
    private final int boardRows;
    /** total number of mines hidden in this game */
    private final int totalMines;
    /** the actual game tiles that hold all the game logic */
    private final Tile[][] tiles;
    /** the buttons you see and click on the screen */
    private final JButton[][] buttons;
    /** the label at the top that shows how many mines and flags there are */
    private final JLabel statusLabel;
    /** whether the game has ended or not */
    private boolean gameOver = false;
    /** how many tiles the player has flagged so far */
    private int flaggedTiles = 0;
    private ImageIcon blankImage;
    private ImageIcon flagImage;
    private ImageIcon mineImage;
    private ImageIcon oneImage;
    private ImageIcon twoImage;
    private ImageIcon threeImage;
    private ImageIcon fourImage;
    private ImageIcon fiveImage;
    private ImageIcon sixImage;
    private ImageIcon sevenImage;
    private ImageIcon eightImage;
    /** Which button was last clicked for mouse events */
    private JButton lastClickedButton = null;
    /** Row of the last clicked button */
    private int lastClickedRow = -1;
    /** Column of the last clicked button */
    private int lastClickedColumn = -1;
    /** Restart button */
    private JButton resetButton;
    /** Difficulty button */
    private JButton difficultyButton;

    /**
     * creates a new minesweeper game window and sets everything up
     *
     * @param boardColumns how many columns you want
     * @param boardRows    how many rows you want
     * @param totalMines   how many mines you want 
     */
    public ClickableGrid(int boardColumns, int boardRows, int totalMines) {
        setTitle("Minesweeper");
        this.boardColumns = boardColumns;
        this.boardRows = boardRows;
        int maxMines = boardColumns * boardRows - 1;
        if (totalMines > maxMines) {
            this.totalMines = maxMines;
        } else {
            this.totalMines = totalMines;
        }
        this.tiles = new Tile[boardRows][boardColumns];
        this.buttons = new JButton[boardRows][boardColumns];

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(220, 220, 235));

        int buttonSize = 30;
        int startX = 20;
        int startY = 60;
        int spacing = buttonSize + 2;

        statusLabel = new JLabel("Mines: " + this.totalMines + " | Flags: 0");
        statusLabel.setBounds(startX, 10, 300, 30);
        statusLabel.setForeground(new Color(40, 40, 60));
        contentPane.add(statusLabel);

        loadImages();

        for (int rowIndex = 0; rowIndex < boardRows; rowIndex++) {
            for (int columnIndex = 0; columnIndex < boardColumns; columnIndex++) {
                JButton tileButton = new JButton("");
                int xPos = startX + columnIndex * spacing;
                int yPos = startY + rowIndex * spacing;
                tileButton.setBounds(xPos, yPos, buttonSize, buttonSize);
                tileButton.addActionListener(this);
                tileButton.addMouseListener(this);
                tileButton.setIcon(blankImage);
                buttons[rowIndex][columnIndex] = tileButton;
                contentPane.add(tileButton);
            }
        }

        resetButton = new JButton("restart");
        resetButton.setBounds(startX, startY + boardRows * spacing + 10, 100, 30);
        resetButton.setBackground(new Color(180, 200, 220));
        resetButton.addActionListener(this);
        contentPane.add(resetButton);

        difficultyButton = new JButton("difficulty");
        difficultyButton.setBounds(startX + 110, startY + boardRows * spacing + 10, 100, 30);
        difficultyButton.setBackground(new Color(200, 190, 220));
        difficultyButton.addActionListener(this);
        contentPane.add(difficultyButton);

        int windowWidth = startX * 2 + boardColumns * spacing;
        int windowHeight = startY + boardRows * spacing + 60;
        setSize(windowWidth, windowHeight);
        setLocation(100, 100);
        setResizable(false);

        initializeTiles();
        placeMines();
        updateStatus();
        setVisible(true);
    }

    /** loads images for tiles from the project root */
    private void loadImages() {
        blankImage = new ImageIcon("blank.png");
        flagImage = new ImageIcon("flag.png");
        mineImage = new ImageIcon("mine.png");
        oneImage = new ImageIcon("one.png");
        twoImage = new ImageIcon("two.png");
        threeImage = new ImageIcon("three.png");
        fourImage = new ImageIcon("four.png");
        fiveImage = new ImageIcon("five.png");
        sixImage = new ImageIcon("six.png");
        sevenImage = new ImageIcon("seven.png");
        eightImage = new ImageIcon("eight.png");
    }

    /**
     * handles button clicks - either tile clicks or control button clicks
     *
     * @param e the action event
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) {
            restart();
        } else if (e.getSource() == difficultyButton) {
            new BootMenu();
        } else {
            for (int rowIndex = 0; rowIndex < boardRows; rowIndex++) {
                for (int columnIndex = 0; columnIndex < boardColumns; columnIndex++) {
                    if (e.getSource() == buttons[rowIndex][columnIndex]) {
                        revealTile(rowIndex, columnIndex);
                        return;
                    }
                }
            }
        }
    }

    /**
     * handles mouse clicks to detect right clicks for flagging
     *
     * @param e the mouse event
     */
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 3) {
            for (int rowIndex = 0; rowIndex < boardRows; rowIndex++) {
                for (int columnIndex = 0; columnIndex < boardColumns; columnIndex++) {
                    if (e.getSource() == buttons[rowIndex][columnIndex]) {
                        toggleFlag(rowIndex, columnIndex);
                        return;
                    }
                }
            }
        }
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    /**
     * sets up all the tiles and connects them so they know who their neighbors are
     */
    private void initializeTiles() {
        for (int rowIndex = 0; rowIndex < boardRows; rowIndex++) {
            for (int columnIndex = 0; columnIndex < boardColumns; columnIndex++) {
                tiles[rowIndex][columnIndex] = new Tile(rowIndex, columnIndex);
            }
        }

        for (int rowIndex = 0; rowIndex < boardRows; rowIndex++) {
            for (int columnIndex = 0; columnIndex < boardColumns; columnIndex++) {
                Tile currentTile = tiles[rowIndex][columnIndex];

                for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
                    for (int colOffset = -1; colOffset <= 1; colOffset++) {
                        if (rowOffset == 0 && colOffset == 0) continue;

                        int neighborRow = rowIndex + rowOffset;
                        int neighborColumn = columnIndex + colOffset;

                        if (isInBounds(neighborRow, neighborColumn)) {
                            currentTile.addNeighbor(tiles[neighborRow][neighborColumn]);
                        }
                    }
                }
            }
        }
    }

    /**
     * randomly scatters mines across the board, making sure we don't put two in the same spot
     */
    private void placeMines() {
        int minesPlaced = 0;
        while (minesPlaced < totalMines) {
            int randomRow = (int)(Math.random() * boardRows);
            int randomColumn = (int)(Math.random() * boardColumns);

            if (!tiles[randomRow][randomColumn].isMine()) {
                tiles[randomRow][randomColumn].setMine(true);
                minesPlaced++;
            }
        }
    }

    /**
     * shows what's under a tile when you click it. if it's empty, it reveals neighbors too
     *
     * @param rowIndex    which row the tile is in
     * @param columnIndex which column the tile is in
     */
    private void revealTile(int rowIndex, int columnIndex) {
        if (!isInBounds(rowIndex, columnIndex) || gameOver) return;
        Tile tile = tiles[rowIndex][columnIndex];
        if (tile.isRevealed() || tile.isFlagged()) return;

        tile.reveal();
        JButton button = buttons[rowIndex][columnIndex];

        if (tile.isMine()) {
            button.setIcon(mineImage);
            button.setBackground(Color.RED);
            endGame(false);
            return;
        }

        int adjacentMines = tile.getAdjacentMineCount();
        if (adjacentMines > 0) {
            button.setText("");
            if (adjacentMines == 1) button.setIcon(oneImage);
            else if (adjacentMines == 2) button.setIcon(twoImage);
            else if (adjacentMines == 3) button.setIcon(threeImage);
            else if (adjacentMines == 4) button.setIcon(fourImage);
            else if (adjacentMines == 5) button.setIcon(fiveImage);
            else if (adjacentMines == 6) button.setIcon(sixImage);
            else if (adjacentMines == 7) button.setIcon(sevenImage);
            else if (adjacentMines == 8) button.setIcon(eightImage);
        } else {
            button.setText("");
            button.setIcon(blankImage);
        }
        button.setEnabled(false);

        if (adjacentMines == 0) {
            ArrayList<Tile> neighbors = tile.getNeighbors();
            for (int i = 0; i < neighbors.size(); i++) {
                Tile neighbor = neighbors.get(i);
                revealTile(neighbor.getTileRow(), neighbor.getTileColumn());
            }
        }

        if (checkWin()) endGame(true);
        else updateStatus();
    }

    /**
     * puts a flag on a tile or removes it if it's already flagged
     *
     * @param rowIndex    which row the tile is in
     * @param columnIndex which column the tile is in
     */
    private void toggleFlag(int rowIndex, int columnIndex) {
        if (!isInBounds(rowIndex, columnIndex) || gameOver) return;

        Tile tile = tiles[rowIndex][columnIndex];
        if (tile.isRevealed()) return;

        tile.toggleFlag();

        JButton button = buttons[rowIndex][columnIndex];
        if (tile.isFlagged()) {
            button.setText("");
            button.setIcon(flagImage);
            flaggedTiles++;
        } else {
            button.setText("");
            button.setIcon(blankImage);
            flaggedTiles--;
        }
        updateStatus();
    }

    /**
     * ends the game and shows you what happened (won or hit a mine)
     *
     * @param won true if you cleared all the safe tiles
     */
    private void endGame(boolean won) {
        gameOver = true;

        for (int rowIndex = 0; rowIndex < boardRows; rowIndex++) {
            for (int columnIndex = 0; columnIndex < boardColumns; columnIndex++) {
                Tile tile = tiles[rowIndex][columnIndex];
                JButton button = buttons[rowIndex][columnIndex];
                if (tile.isMine()) {
                    button.setIcon(mineImage);
                } else if (tile.isFlagged()) {
                    button.setIcon(flagImage);
                } else {
                    button.setIcon(blankImage);
                }
                button.setEnabled(false);
            }
        }
        if (won) {
            statusLabel.setText("You cleared the board! Congratulations!");
        } else {
            statusLabel.setText("Boom! You hit a mine.");
        }
    }

    /**
     * checks if you've revealed all the safe tiles and won the game
     *
     * @return true if you won
     */
    private boolean checkWin() {
        for (int rowIndex = 0; rowIndex < boardRows; rowIndex++) {
            for (int columnIndex = 0; columnIndex < boardColumns; columnIndex++) {
                Tile tile = tiles[rowIndex][columnIndex];
                if (!tile.isMine() && !tile.isRevealed()) return false;
            }
        }
        return true;
    }

    /** starts a new game with the same difficulty settings */
    private void restart() {
        new ClickableGrid(boardColumns, boardRows, totalMines);
    }

    /**
     * checks if a position is actually on the board or if it's out of bounds
     *
     * @param rowIndex    which row we're checking
     * @param columnIndex which column we're checking
     * @return true if it's a valid position on the board
     */
    private boolean isInBounds(int rowIndex, int columnIndex) {
        return rowIndex >= 0 && rowIndex < boardRows && columnIndex >= 0 && columnIndex < boardColumns;
    }

    /** updates the top label to show how many mines and flags there are */
    private void updateStatus() {
        statusLabel.setText("Mines: " + totalMines + " - Flags: " + flaggedTiles);
    }
}
