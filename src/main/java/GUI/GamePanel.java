package GUI;

import Input.*;
import javax.swing.JPanel;

import java.awt.Graphics;

public class GamePanel extends JPanel{
    
    private MouseInputs mouseInputs;
    private int xDelta = 100, yDelta = 100;

    public GamePanel(){

        this.mouseInputs = new MouseInputs(this);

        addKeyListener(new KeyboardInputs(this));

        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    public void changeXDelta(int value){
        this.xDelta += value;
        repaint();
    }

    public void changeYDelta(int value){
        this.yDelta +=value;
        repaint();
    }

    public void setRectPosition(int x, int y){
        this.xDelta = x;
        this.yDelta = y;
        repaint();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.fillRect(xDelta, yDelta, 200, 100);

    }
}
