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
import java.util.EnumMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.graalvm.compiler.loop.InductionVariable.Direction;

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
                                    "X XXXX XX XX    XX XX XXXX X",
                                    "X      XX XXXXXXXX XX      X",
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

    enum Dir {L,R,U,D};
    enum State {ready,running,dead};

    private final int B_WIDTH = WIDTH*SQUARE;
    private final int B_HEIGHT = (HEIGHT+3)*SQUARE;
    private final int DELAY = 140;

    private Timer timer;
    private boolean inGame;
    private Image dot;
    private Map <Dir,Image> pacMan;
    private Font small;
    private FontMetrics smallM;
    
    private char[][] MAZE = new char[WIDTH][HEIGHT];
    private long score;
    private long dotcount;
    private int lives;
    private int pacX,pacY;
    private Dir pacDir;

    private class Ghost {
        private int x,y;
        private State state;
        private Dir dir;
        private Image image;
        private char color;

        public Ghost(int newX,int newY,char newColor) {
            // výchozí pozice ducha
            x=newX;y=newY;
            state=State.ready;
            color=newColor;
            dir=Dir.U;

            // load resources
            ImageIcon iid = new ImageIcon("src/resources/ghost"+color+".png");
            image = iid.getImage();
        }

        public void draw(Graphics g) {
            int shift = (22-image.getWidth(null))/2;
            g.drawImage(image,x*SQUARE+shift,(y+1)*SQUARE+shift,null);
        }
    }

    private Ghost[] ghosts = new Ghost[1];

    public Board() throws Exception {
        
        initBoard();
        initGame();
    }
    
    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadAssets();
    }

    private void loadAssets() {

        ImageIcon iid = new ImageIcon("src/resources/dot.png");
        dot = iid.getImage();
        pacMan = new EnumMap<Dir,Image>(Dir.class);
        iid = new ImageIcon("src/resources/pacWL.png");
        pacMan.put(Dir.L,iid.getImage());
        iid = new ImageIcon("src/resources/pacWR.png");
        pacMan.put(Dir.R,iid.getImage());
        iid = new ImageIcon("src/resources/pacWU.png");
        pacMan.put(Dir.U,iid.getImage());
        iid = new ImageIcon("src/resources/pacWD.png");
        pacMan.put(Dir.D,iid.getImage());
        small = new Font("Helvetica", Font.BOLD, 14);
        smallM = getFontMetrics(small);
    }

    private void initGame() throws Exception {
        dotcount=0;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                switch (MAZEDATA[y].charAt(x)) {
                    case ' ':
                        MAZE[x][y]='.';
                        dotcount++;
                        break;
                    case 'X':
                        MAZE[x][y]='X';
                        break;
                    default:
                        throw new Exception("Chybný znak v bludišti");
                }
            }
        }
        dotcount=dotcount-12;
        dotcount=100;
        score = 0;
        pacX=14;
        pacY=10;
        pacDir=Dir.U;
        lives=5;

        inGame = true;
        timer = new Timer(DELAY, this);
        timer.start();

        ghosts[0]=new Ghost(13, 12,'R');
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

    private void writeMessage(Graphics g,String message) {
        final Font big = new Font("Helvetica", Font.BOLD, 36);
        final FontMetrics bigM = getFontMetrics(big);
        g.setColor(Color.white);
        g.setFont(big);
        g.drawString(message,
                     (B_WIDTH-bigM.stringWidth(message))/2,
                     (B_HEIGHT-bigM.getHeight())/2);
    }

    private void doDrawing(Graphics g) {
        
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
                        g.setColor(Color.blue);
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

        // vytiskni score
        g.setColor(Color.white);
        g.setFont(small);
        String msg = String.format("SCORE: %8d",score);
        g.drawString(msg,0, 20);

        // nakresli pacmana
        int shift = (22-pacMan.get(pacDir).getWidth(null))/2;
        g.drawImage(pacMan.get(pacDir),pacX*SQUARE+shift,(pacY+1)*SQUARE+shift,this);

        if (!inGame){
            if (lives>0) writeMessage(g,"LEVEL UP"); 
            else writeMessage(g,"GAME OVER");
        }

        // nakresli duchy
        ghosts[0].draw(g);

        // Updatuj obrazovku
        Toolkit.getDefaultToolkit().sync();
       
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

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
            if (inGame) {
                switch (key) {
                    case KeyEvent.VK_LEFT:
                        pacDir=Dir.L;
                        if (pacX==0){
                            pacX=WIDTH-1;
                        } else {
                            if (MAZE[pacX-1][pacY]!='X') pacX--;
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        pacDir=Dir.R;
                        if (pacX==WIDTH-1) {
                            pacX=0;
                        } else {
                            if (MAZE[pacX+1][pacY]!='X') pacX++;
                        }
                        break;
                    case KeyEvent.VK_UP:
                        pacDir=Dir.U;
                        if (pacY==0){
                            pacY=HEIGHT-1;
                        } else {
                            if (MAZE[pacX][pacY-1]!='X') pacY--;
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        pacDir=Dir.D;
                        if (pacY==HEIGHT-1) {
                            pacY=0;
                        } else {
                            if (MAZE[pacX][pacY+1]!='X') pacY++;
                        }
                        break;
                }    
                // Eat the food
                switch (MAZE[pacX][pacY]) {
                    case '.':
                        MAZE[pacX][pacY]=' ';
                        score=score+10;
                        dotcount--;
                        break;
                }
                if (dotcount==0) 
                    inGame=false;
            }
        }
    }
}
