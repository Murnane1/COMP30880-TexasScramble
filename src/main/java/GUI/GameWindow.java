package GUI;

import javax.swing.JFrame;

public class GameWindow extends JFrame{

    public GameWindow(GamePanel gamePanel){

        super();

        setSize(960,540);    // 16:9 1920x1080
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //Stop program when window is closed

        add(gamePanel);
        setLocationRelativeTo(null);    //window appears in centre

        setVisible(true);   //call at end

    }
}
