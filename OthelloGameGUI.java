import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OthelloGameGUI extends JFrame {
    private JButton[][] buttons;
    private char currentPlayer = '●';
    private char[][] board;

    public OthelloGameGUI() {
        setTitle("オセロゲーム");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 8));

        int windowSize = 600;
        setSize(windowSize, windowSize);

        buttons = new JButton[8][8];
        initializeBoard();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                buttons[row][col] = new JButton();
                buttons[row][col].setActionCommand(row + "," + col);
                buttons[row][col].addActionListener(new ButtonClickListener());
                add(buttons[row][col]);
            }
        }

        updateButtons();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeBoard() {
        board = new char[8][8];

        board[3][3] = '●';
        board[4][4] = '●';
        board[3][4] = '○';
        board[4][3] = '○';
    }

    private void updateButtons() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                buttons[row][col].setText(Character.toString(board[row][col]));
            }
        }
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            String[] position = button.getActionCommand().split(",");
            int row = Integer.parseInt(position[0]);
            int col = Integer.parseInt(position[1]);

            if (isValidMove(row, col)) {
                board[row][col] = currentPlayer;
                flipOpponentStones(row, col);
                switchPlayer();
                updateButtons();
            } else {
                JOptionPane.showMessageDialog(null, "ほかのマスを選択してください", "Invalid Move", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean isValidMove(int row, int col) {
        if (board[row][col] != '\u0000') {
            return false;
        }

        int[][] directions = {
                { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 },
                { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 }
        };

        for (int[] direction : directions) {
            int dx = direction[0];
            int dy = direction[1];
            int newRow = row + dx;
            int newCol = col + dy;

            if (!isValidPosition(newRow, newCol)) {
                continue;
            }

            if (board[newRow][newCol] != getOpponentPlayer()) {
                continue;
            }

            while (isValidPosition(newRow, newCol) && board[newRow][newCol] == getOpponentPlayer()) {
                newRow += dx;
                newCol += dy;
            }

            if (isValidPosition(newRow, newCol) && board[newRow][newCol] == currentPlayer) {
                return true;
            }
        }

        return false;
    }

    private char getOpponentPlayer() {
        return (currentPlayer == '●') ? '○' : '●';
    }

    private void flipOpponentStones(int row, int col) {
        char opponentColor = (currentPlayer == '●') ? '○' : '●';

        int[][] directions = {
                { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 },
                { -1, -1 }, { -1, 1 }, { 1, -1 }, { 1, 1 }
        };

        for (int[] dir : directions) {
            int dx = dir[0];
            int dy = dir[1];
            int newRow = row + dx;
            int newCol = col + dy;

            boolean validDirection = false;
            while (isValidPosition(newRow, newCol) && board[newRow][newCol] == opponentColor) {
                newRow += dx;
                newCol += dy;
                if (isValidPosition(newRow, newCol) && board[newRow][newCol] == currentPlayer) {
                    validDirection = true;
                    break;
                }
            }

            if (validDirection) {
                newRow -= dx;
                newCol -= dy;
                while (newRow != row || newCol != col) {
                    board[newRow][newCol] = currentPlayer;
                    newRow -= dx;
                    newCol -= dy;
                }
            }
        }
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == '●') ? '○' : '●';
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OthelloGameGUI());
    }
}
