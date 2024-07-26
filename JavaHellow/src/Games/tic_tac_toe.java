package Games;

import java.util.Scanner;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class tic_tac_toe {
    private byte[][] area;
    private final int len;

    public tic_tac_toe() {
        this.area = new byte[][]
                {
                        {0, 0, 0},
                        {0, 0, 0},
                        {0, 0, 0}
                };
        this.len = area.length;

    }

    public void arena_reset() {
        for (int i = 0; i < len; i++)
            for (int j = 0; j < len; j++)
                area[i][j] = 0;
    }

    public void put_cross(int line, int column) {
        if (errorChecking(line, column)) {
            area[line][column] = 1;
        } else {
            System.out.print("Error: This cell borrow");
        }
    }

    public void put_zero(int line, int column) {
        if (errorChecking(line, column)) {
            area[line][column] = -1;
        } else {
            System.out.print("Error: This cell borrow");
        }
    }

    public boolean errorChecking(int line, int column) {
        return area[line][column] == 0;
    }

    public void check_arena() {
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                if (area[i][j] == 1) System.out.print("X\t");
                else if (area[i][j] == -1) System.out.print("O\t");
                else System.out.print(" \t");
                if (j <= 1) System.out.print("|\t");
            }
            System.out.print("\n");
        }
    }

    private byte check_win_diagonal() {
        byte sum1 = 0;
        byte sum2 = 0;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                if (i == j) sum1 += area[i][j];
                if (i + j == len - 1) sum2 += area[i][j];
            }
        }
        if (sum1 == 3 || sum2 == 3) return 1;
        else if (sum1 == -3 || sum2 == -3) return -1;
        else return 0;
    }

    private byte check_win_line() {
        byte sum = 0;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                sum += area[i][j];
            }
            if (sum == 3) return 1;
            if (sum == -3) return -1;
            sum = 0;
        }
        return 0;
    }

    private byte check_win_column() {
        byte sum = 0;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                sum += area[j][i];
            }
            if (sum == 3) return 1;
            if (sum == -3) return -1;
            sum = 0;
        }
        return 0;
    }

    public boolean check_win() {
        return check_win_column() != 0 || check_win_line() != 0 || check_win_diagonal() != 0;
    }

    public boolean check_who_win() {
        return check_win_column() == 1 || check_win_line() == 1 || check_win_diagonal() == 1;
    }

    public static class EngineTTT {
        Scanner in = new Scanner(System.in);
        private String name1;
        private String name2;
        private tic_tac_toe game;
        private static int counter;
        private static int win_name1;
        private static int win_name2;

        static {
            counter = 0;
            win_name1 = 0;
            win_name2 = 0;
        }

        public EngineTTT() {
            counter++;
            System.out.print("Input name first player: ");
            this.name1 = in.nextLine();
            System.out.print("Input name second player: ");
            this.name2 = in.nextLine();
            this.game = new tic_tac_toe();
        }

        public void menuTTT() {
            JFrame menu = new JFrame("TicTacToe");
            JLabel b1 = new JLabel("If you want start play in TTT click 'START'");
            b1.setBounds(10, 60, 250, 20);
            menu.add(b1);
            JLabel b2 = new JLabel("If you want finish play in TTT click 'END'");
            b2.setBounds(10, 90, 250, 20);
            menu.add(b2);
            menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            menu.setSize(300, 300);
            menu.setLayout(null);

            JButton butStart = new JButton("START");
            butStart.setBounds(40, 20, 85, 20);
            butStart.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    playTTT();
                }
            });
            menu.add(butStart);

            JButton butEnd = new JButton("END");
            butEnd.setBounds(150, 20, 85, 20);
            butEnd.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(menu, "Игра завершена!");
                    System.exit(0); // Закрыть приложение
                }
            });
            menu.add(butEnd);
            menu.setVisible(true);
        }

        public class TicTacToeGame extends JFrame {
            public TicTacToeGame() {
                setTitle("Tic Tac Toe");
                setSize(400, 400);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                add(new GameBoardPanel());
                setVisible(true);
            }

            private class GameBoardPanel extends JPanel {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(Color.BLACK);

                    // Рисуем вертикальные линии
                    g.drawLine(133, 0, 133, 400);
                    g.drawLine(266, 0, 266, 400);

                    // Рисуем горизонтальные линии
                    g.drawLine(0, 133, 400, 133);
                    g.drawLine(0, 266, 400, 266);

                    // Рисуем крестики и нолики
                    for (int i = 0; i < game.len; i++) {
                        for (int j = 0; j < game.len; j++) {
                            if (game.area[i][j] == 1) {
                                drawCross(g, i, j);
                            } else if (game.area[i][j] == -1) {
                                drawZero(g, i, j);
                            }
                        }
                    }
                }

                private void drawCross(Graphics g, int i, int j) {
                    int x = j * 133;
                    int y = i * 133;
                    g.drawLine(x + 10, y + 10, x + 123, y + 123);
                    g.drawLine(x + 123, y + 10, x + 10, y + 123);
                }

                private void drawZero(Graphics g, int i, int j) {
                    int x = j * 133;
                    int y = i * 133;
                    g.drawOval(x + 10, y + 10, 113, 113);
                }
            }
        }

        public void playTTT() {
            int step = 0;
            TicTacToeGame gameFrame = new TicTacToeGame();
            boolean gameWon = false;

            while (!gameWon && step < 9) {
                game.check_arena();
                int x, y;
                if (step % 2 == 0) {
                    System.out.print("First player's turn\n Enter x and y: ");
                    x = in.nextInt();
                    y = in.nextInt();
                    game.put_cross(x, y);
                } else {
                    System.out.print("Second player's turn\n Enter x and y: ");
                    x = in.nextInt();
                    y = in.nextInt();
                    game.put_zero(x, y);
                }

                gameFrame.repaint();

                if (game.check_win()) {
                    if (game.check_who_win()) {
                        win_name1++;
                        System.out.printf("First player was winner!\n Check %d : %d\n", win_name1, win_name2);
                    } else {
                        win_name2++;
                        System.out.printf("Second player was winner!\n Check %d : %d \n", win_name1, win_name2);
                    }
                    return;
                }
                step++;
            }
            System.out.print("Draw\n");
        }
    }
}