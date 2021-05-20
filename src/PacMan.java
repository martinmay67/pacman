import java.awt.EventQueue;
import javax.swing.JFrame;

public class PacMan extends JFrame {

    public PacMan() {
        initUI();
    }
    
    private void initUI() {
        try {
            add(new Board());
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
               
        setResizable(false);
        pack();
        
        setTitle("PacMan");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            JFrame ex = new PacMan();
            ex.setVisible(true);
        });
    }
}
