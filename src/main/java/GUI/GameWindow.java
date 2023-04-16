package GUI;

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

    }
}
