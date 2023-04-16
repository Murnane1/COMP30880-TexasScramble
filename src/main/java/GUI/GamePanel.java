package GUI;
import Input.*;

import GUI_TEST.Game;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class GamePanel extends JPanel{
    
    private MouseInputs mouseInputs;
 
    private Game game;

    public GamePanel(Game game){

        this.game = game;

        setPanelSize();

        addKeyListener(new KeyboardInputs(this));   //Keyboard Input

        this.mouseInputs = new MouseInputs(this);   // Mouse Input listener      
        addMouseListener(mouseInputs);              // Mouse Click Input 
        addMouseMotionListener(mouseInputs);        // Mouse Move and Drag Input

    }

    private void setPanelSize(){
        Dimension size = new Dimension(960, 540);   //16:9 1920x1080
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);

        game.render(g);
    }

    public Game getGame() {
        return game;
    }
}
