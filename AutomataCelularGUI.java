import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AutomataCelularGUI extends JFrame {
    private static final int CELDA_SIZE = 10;
    private static final int GRID_WIDTH = 60;
    private static final int GRID_HEIGHT = 60;

    private JPanel gridPanel;
    private boolean[][] grid;
    private Timer timer;

    public AutomataCelularGUI() {
        super("Autómata Celular: Juego de la Vida");
        setSize(GRID_WIDTH * CELDA_SIZE, GRID_HEIGHT * CELDA_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        grid = new boolean[GRID_WIDTH][GRID_HEIGHT];
        // Inicializamos la cuadrícula con valores aleatorios
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                grid[x][y] = Math.random() < 0.5; // 50% de probabilidad de que una celda esté viva
            }
        }

        gridPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int x = 0; x < GRID_WIDTH; x++) {
                    for (int y = 0; y < GRID_HEIGHT; y++) {
                        if (grid[x][y]) {
                            g.setColor(Color.BLACK);
                            g.fillRect(x * CELDA_SIZE, y * CELDA_SIZE, CELDA_SIZE, CELDA_SIZE);
                        }
                    }
                }
            }
        };

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSimulation();
            }
        });

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopSimulation();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);

        add(gridPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void startSimulation() {
        if (timer == null) {
            timer = new Timer(100, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    actualizarCelulas();
                    gridPanel.repaint();
                }
            });
            timer.start();
        }
    }

    private void stopSimulation() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    private void actualizarCelulas() {
        boolean[][] newGrid = new boolean[GRID_WIDTH][GRID_HEIGHT];
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                int aliveNeighbours = contarVecinosVivos(x, y);
                if (grid[x][y]) {
                    // Celula viva con menos de 2 vecinos vivos o más de 3 muere
                    newGrid[x][y] = (aliveNeighbours == 2 || aliveNeighbours == 3);
                } else {
                    // Celula muerta con exactamente 3 vecinos vivos revive
                    newGrid[x][y] = (aliveNeighbours == 3);
                }
            }
        }
        grid = newGrid;
    }

    private int contarVecinosVivos(int x, int y) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int neighbourX = (x + i + GRID_WIDTH) % GRID_WIDTH;
                int neighbourY = (y + j + GRID_HEIGHT) % GRID_HEIGHT;
                if (grid[neighbourX][neighbourY]) {
                    count++;
                }
            }
        }
        if (grid[x][y]) {
            count--; // Descuenta la celda actual
        }
        return count;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AutomataCelularGUI::new);
    }
}
