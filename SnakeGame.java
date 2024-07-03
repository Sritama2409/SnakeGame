package Practice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private static final int GRID_SIZE = 20;
    private static final int CELL_SIZE = 20;
    private static final int BOARD_SIZE = GRID_SIZE * CELL_SIZE;
    private static final int INITIAL_SNAKE_LENGTH = 3;

    private ArrayList<Point> snake;
    private Point food;
    private int direction;
    private boolean running;
    private Timer timer;
    private int score;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(BOARD_SIZE, BOARD_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        snake = new ArrayList<>();
        snake.add(new Point(GRID_SIZE / 2, GRID_SIZE / 2));
        for (int i = 1; i < INITIAL_SNAKE_LENGTH; i++) {
            snake.add(new Point(GRID_SIZE / 2 - i, GRID_SIZE / 2));
        }

        food = generateFood();

        direction = KeyEvent.VK_RIGHT;
        running = true;
        timer = new Timer(700, this);
        timer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int newDirection = e.getKeyCode();
                if ((newDirection == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT) ||
                        (newDirection == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT) ||
                        (newDirection == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN) ||
                        (newDirection == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP)) {
                    direction = newDirection;
                }
            }
        });

        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw grid
        for (int x = 0; x <= BOARD_SIZE; x += CELL_SIZE) {
            g.drawLine(x, 0, x, BOARD_SIZE);
        }
        for (int y = 0; y <= BOARD_SIZE; y += CELL_SIZE) {
            g.drawLine(0, y, BOARD_SIZE, y);
        }

        // Draw snake
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }

        // Draw food
        g.setColor(Color.RED);
        g.fillRect(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        // Draw score
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 20);
    }

    private Point generateFood() {
        Random random = new Random();
        int x = random.nextInt(GRID_SIZE);
        int y = random.nextInt(GRID_SIZE);
        return new Point(x, y);
    }

    private void move() {
        Point head = snake.get(0);
        Point newHead = new Point(head);
        switch (direction) {
            case KeyEvent.VK_LEFT:
                newHead.x--;
                break;
            case KeyEvent.VK_RIGHT:
                newHead.x++;
                break;
            case KeyEvent.VK_UP:
                newHead.y--;
                break;
            case KeyEvent.VK_DOWN:
                newHead.y++;
                break;
        }

        // Check if snake hits walls or itself
        if (newHead.x < 0 || newHead.x >= GRID_SIZE || newHead.y < 0 || newHead.y >= GRID_SIZE ||
                snake.contains(newHead)) {
            running = false;
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over! Your score is: " + score, "Game Over",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        snake.add(0, newHead);

        // Check if snake eats food
        if (newHead.equals(food)) {
            score++;
            food = generateFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            repaint();
        }
    }

    public static void main(String[] args) {
        new SnakeGame();
    }
}
