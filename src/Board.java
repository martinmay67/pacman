import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private final int SQUARE = 22;
    private final int WIDTH=28;
    private final int HEIGHT=31;
    private final String[] MAZEDATA={   
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                    "                            ",
                                };
 
    private final int B_WIDTH = WIDTH*SQUARE;
    private final int B_HEIGHT = (HEIGHT+3)*SQUARE;
    private final int DELAY = 140;

    private Timer timer;
    private boolean inGame;
    private Image dot;
    
    private char[][] MAZE = new char[WIDTH][HEIGHT];
    
    public Board() throws Exception {
        
        initBoard();
        initGame();
    }
    
    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("src/resources/dot.png");
        dot = iid.getImage();
    }

    private void initGame() throws Exception {

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                switch (MAZEDATA[y].charAt(x)) {
                    case ' ':
                        MAZE[x][y]='.';
                        break;
                    default:
                        throw new Exception("Chybný znak v bludišti");
                }
            }
        }
        
        inGame = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }
    
    private void doDrawing(Graphics g) {
        
        if (inGame) {
            // vykresli bludišě
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    switch (MAZE[x][y]) {
                        case '.':
                            g.drawImage(dot, 
                                        x*SQUARE+(SQUARE-dot.getWidth(null))/2,
                                        (y+1)*SQUARE+(SQUARE-dot.getHeight(null))/2, this);
                            break;
                    }
                }
            }
            // Updatuj obrazovku
            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }        
    }

    private void gameOver(Graphics g) {
        
        String msg = "Konec hry";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            // TODO: move PacMan
            // TODO: move ghost
            // TODO: check colisions
            // TODO: update scores

        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT) {
                // TODO: move pacman left
            }

            if (key == KeyEvent.VK_RIGHT) {
                // TODO: move pacman right
            }

            if (key == KeyEvent.VK_UP) {
                // TODO: move pacman up
            }

            if (key == KeyEvent.VK_DOWN) {
                // TODO: move pacman down
                inGame = false;
            }
        }
    }
}
