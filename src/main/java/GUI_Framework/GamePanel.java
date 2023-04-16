package GUI_Framework;
import Input.*;

import GUI_TEST.Game;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Graphics;

public class GamePanel extends JPanel{
    
    private final int WIDTH = 960, HEIGHT = 540;


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
        Dimension size = new Dimension(WIDTH, HEIGHT);   //16:9 1920x1080
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

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    
}
