package GUI;

import Input.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class GamePanel extends JPanel{
    
    private MouseInputs mouseInputs;
    private int xDelta = 100, yDelta = 100;

    private BufferedImage image;

    public GamePanel(){

        importImg();

        setPanelSize();

        addKeyListener(new KeyboardInputs(this));   //Keyboard Input

        this.mouseInputs = new MouseInputs(this);   // Mouse Input listener      
        addMouseListener(mouseInputs);              // Mouse Click Input 
        addMouseMotionListener(mouseInputs);        // Mouse Move and Drag Input

    }

    private void importImg() {
        InputStream is = getClass().getResourceAsStream("/Images/blank-tile.png");
        
        try {
            image = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    private void setPanelSize(){
        Dimension size = new Dimension(960, 540);   //16:9 1920x1080
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    public void changeXDelta(int value){
        this.xDelta += value;
    }

    public void changeYDelta(int value){
        this.yDelta +=value;
    }

    public void setRectPosition(int x, int y){
        this.xDelta = x;
        this.yDelta = y;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.drawImage(image, 0,0,75,75, null);
    }
}
