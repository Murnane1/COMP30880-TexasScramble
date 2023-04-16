package Levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import GUI_TEST.Game;

public class LevelManager {
    
    private Game game;
    private BufferedImage levelSprite;

    public LevelManager(Game game){
        this.game = game;
        importImg();

    }

    private void importImg() {
        InputStream is = getClass().getResourceAsStream("/Images/blank-tile.png");
        
        try {
            levelSprite = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public void draw(Graphics g){
        g.drawImage(levelSprite, 0, 0, null);
    }
    public void update(){

    }

}
