/**
 * this is the menu screen class that displays difficulty options
 * and allows the player to choose easy, medium, or hard before
 * starting a minesweeper game
 *
 * @author Omar Firdaus, Peyton Dao
 * @version .29alphaOmegagammaEpsilon
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * this is the menu screen where you pick your difficulty level before starting a game
 */
public class BootMenu extends JFrame implements ActionListener {
    // buttons for difficulty selection
    private JButton easyButton;
    private JButton mediumButton;
    private JButton hardButton;

    /**
     * sets up the menu window with all the difficulty buttons and makes them work
     */
    public BootMenu() {
        setTitle("Minesweeper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 360);
        setLocation(100, 100);
        setResizable(false);

        // layout and background
        Container contentPane = getContentPane();
        contentPane.setLayout(null);
        Color colorBackground = new Color(245, 245, 250);
        contentPane.setBackground(colorBackground);

        // title
        JLabel titleLabel = new JLabel("choose difficulty");
        titleLabel.setBounds(100, 50, 280, 40);
        titleLabel.setForeground(new Color(50, 50, 80));
        contentPane.add(titleLabel);

        // difficulty buttons
        easyButton = new JButton("Easy");
        easyButton.setBounds(150, 120, 180, 40);
        easyButton.setIcon(new ImageIcon("easyDifficulty.png"));
        easyButton.setBackground(new Color(200, 230, 200));
        easyButton.addActionListener(this);
        contentPane.add(easyButton);

        mediumButton = new JButton("Medium");
        mediumButton.setBounds(150, 180, 180, 40);
        mediumButton.setIcon(new ImageIcon("mediumDifficulty.png"));
        mediumButton.setBackground(new Color(230, 220, 180));
        mediumButton.addActionListener(this);
        contentPane.add(mediumButton);

        hardButton = new JButton("Hard");
        hardButton.setBounds(150, 240, 180, 40);
        hardButton.setIcon(new ImageIcon("hardDifficulty.png"));
        hardButton.setBackground(new Color(230, 200, 200));
        hardButton.addActionListener(this);
        contentPane.add(hardButton);

        setVisible(true);
    }

    /**
     * handles button clicks to start games with different difficulty levels
     *
     * @param e the action event from the button click
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == easyButton) {
            launchGrid(9, 9, 10);
        } else if (e.getSource() == mediumButton) {
            launchGrid(16, 16, 40);
        } else if (e.getSource() == hardButton) {
            launchGrid(30, 16, 99);
        }
    }

    /**
     * starts up a new game with the given board size and mine count
     *
     * @param boardColumns how many columns the board should have
     * @param boardRows    how many rows the board should have
     * @param totalMines   how many mines to hide in the board
     */
    private void launchGrid(int boardColumns, int boardRows, int totalMines) {
        new ClickableGrid(boardColumns, boardRows, totalMines);
    }

    /** opens the minesweeper menu */
    public static void main(String[] args) {
        new BootMenu();
    }
}
