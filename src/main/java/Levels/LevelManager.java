package Levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import GUI_TEST.Game;

public class LevelManager {
    //TODO
    /*
     * 
     * Inventory for community tiles 
     * Player positions
     * 
     */
    private Game game;
    private BufferedImage levelSprite;

    public LevelManager(Game game){
        this.game = game;
        importImg();

    }

    private void importImg() {
        InputStream is = getClass().getResourceAsStream("/Images/table_STAND-IN.jpg");
        
        try {
            levelSprite = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public void update(){

    }

    public void draw(Graphics g){
        
        //Drawing the table
        g.drawImage(levelSprite, 96, 4,768, 432, null);


    }
    

}
