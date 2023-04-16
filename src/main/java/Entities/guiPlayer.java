package Entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

//TODO Extend Player
public class guiPlayer extends Entity {
    
    private BufferedImage image;
    public guiPlayer(int x, int y){
        super(x, y);
        importImg();
    }

    public void update(){

    }
    public void render(Graphics g){
        g.drawImage(image, (int)x, (int)y,75,75, null);
    }
    private void importImg() {
        InputStream is = getClass().getResourceAsStream("/Images/blank-tile.png");
        
        try {
            image = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    
    public void changeXDelta(int value){
        this.x += value;
    }

    public void changeYDelta(int value){
        this.y +=value;
    }


}
