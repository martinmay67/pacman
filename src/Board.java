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
    private final int HEIGHT=30;
    private final String[] MAZEDATA={   
                                    "XXXXXXXXXXXXXXXXXXXXXXXXXXXX",
                                    "X         XX    XX         X",
                                    "X XXXX XX XX XX XX XX XXXX X",
                                    "X XXXX XX XX XX XX XX XXXX X",
                                    "X      XX    XX    XX      X",
                                    "X XXXX XXXXX XX XXXXX XXXX X",
                                    "X XXXX XXXXX XX XXXXX XXXX X",
                                    "X XX         XX         XX X",
                                    "X XX XXXX XXXXXXXX XXXX XX X",
                                    "X XX XXXX XXXXXXXX XXXX XX X",
                                    "                            ",
                                    "X XXXX XX XXXXXXXX XX XXXX X",
                                    "X XXXX XX X      X XX XXXX X",
                                    "X      XX X      X XX      X",
                                    "X XXXX XX XXXXXXXX XX XXXX X",
                                    "X XXXX XX          XX XXXX X",
                                    "X   XX XX XXXXXXXX XX XX   X",
                                    "XXX XX XX XXXXXXXX XX XX XXX",
                                    "XXX XX       XX       XX XXX",
                                    "    XX XXXXX XX XXXXX XX    ",
                                    "X XXXX XXXXX XX XXXXX XXXX X",
                                    "X XXXX XX          XX XXXX X",
                                    "X      XX XX XX XX XX      X",
                                    "X XX XXXX XX XX XX XXXX XX X",
                                    "X XX XXXX XX XX XX XXXX XX X",
                                    "X XX      XX    XX      XX X",
                                    "X XXXXXXX XXXXXXXX XXXXXXX X",
                                    "X XXXXXXX XXXXXXXX XXXXXXX X",
                                    "X                          X",
                                    "XXXXXXXXXXXXXXXXXXXXXXXXXXXX"
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
                    case 'X':
                        MAZE[x][y]='X';
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
    
    private char getMaze(int x, int y){
        if ((x<0)||(x>=WIDTH)||(y<0)||(y>=HEIGHT)) {
            return 'X';
        } else {
            return MAZE[x][y];
        }
    }
    private void doDrawing(Graphics g) {
        
        if (inGame) {
            // vykresli bludišě
            final int s3 = SQUARE/3;
            final int fit = SQUARE - 2*s3;
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    int xc = x*SQUARE;
                    int yc = (y+1)*SQUARE;
                    switch (MAZE[x][y]) {
                        case '.':
                            g.drawImage(dot,
                                        xc+(SQUARE-dot.getWidth(null))/2,
                                        yc+(SQUARE-dot.getHeight(null))/2, this);
                            break;
                        case 'X':
                            g.setColor(java.awt.Color.blue);
                            g.fillRect(xc+s3, yc+s3, s3, s3);
                            if (getMaze(x-1,y)=='X') {
                                g.fillRect(xc,yc+s3,s3,s3);
                                if ((getMaze(x-1,y-1)=='X') && (getMaze(x,y-1)=='X')) g.fillRect(xc,yc,s3,s3);
                                if ((getMaze(x-1,y+1)=='X') && (getMaze(x,y+1)=='X')) g.fillRect(xc,yc+2*s3,s3,fit);
                            };
                            if (getMaze(x+1,y)=='X') {
                                g.fillRect(xc+2*s3,yc+s3,fit,s3);
                                if ((getMaze(x+1,y-1)=='X') && (getMaze(x,y-1)=='X')) g.fillRect(xc+2*s3,yc,fit,s3);
                                if ((getMaze(x+1,y+1)=='X') && (getMaze(x,y+1)=='X')) g.fillRect(xc+2*s3,yc+2*s3,fit,fit);
                            }    
                            if (getMaze(x,y-1)=='X') g.fillRect(xc+s3,yc,s3,s3);
                            if (getMaze(x,y+1)=='X') g.fillRect(xc+s3,yc+2*s3,s3,fit);
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
