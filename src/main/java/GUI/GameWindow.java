package GUI;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

public class GameWindow extends JFrame{

    public GameWindow(GamePanel gamePanel){

        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //Stop program when window is closed

        add(gamePanel);

        setResizable(false);
        pack();

        setLocationRelativeTo(null);    //window appears in centre
        
        setVisible(true);   //call at end
        addWindowFocusListener(new WindowFocusListener() {

            @Override
            public void windowGainedFocus(WindowEvent e) {
                // TODO Auto-generated method stub
                System.out.println("Window Gained Focus");
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                // TODO Auto-generated method stub
                System.out.println("Window Lost Focus");
            }
            
        });
    }
}
