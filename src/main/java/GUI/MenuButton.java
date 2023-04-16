package GUI;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import gameStates.GameState;

public class MenuButton {
    private int xPos, yPos, rowIndex, index;
    private final int HEIGHT = 56, WIDTH = 140;
    private final int yOffsetCenter = HEIGHT/2, xOffsetCenter = WIDTH/2; 
    private GameState state;
    private BufferedImage[] imgs;
    private boolean mouseOver, mousePressed;
    private Rectangle bounds;
    
    public MenuButton(int x, int y, int rowIndex, GameState state){
        this.xPos = x;
        this.yPos = y;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImages();
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(xPos - xOffsetCenter, yPos - yOffsetCenter, WIDTH, HEIGHT);
    }

    private void loadImages() {
        imgs = new BufferedImage[3];
        BufferedImage temp = importImg();
        for(int i = 0; i<imgs.length;i++){
            imgs[i] = temp.getSubimage(i*WIDTH, rowIndex*HEIGHT, WIDTH, HEIGHT);
        } 
    }

    private BufferedImage importImg() {
        InputStream is = getClass().getResourceAsStream("/Images/button_atlas.png");
        
        try {
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("UNABLE TO READ MENU BUTTON", e);
        }
        
    }

    public void draw(Graphics g){
        g.drawImage(imgs[index], xPos  -xOffsetCenter, yPos-yOffsetCenter ,WIDTH, HEIGHT, null); 
    }
    public void update(){
        index = 0;
        if(mouseOver)
            index = 1;
        if(mousePressed)
            index = 2;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public Rectangle getBounds(){
        return bounds;
    }
    public void applyGameState(){
        GameState.state = state;
    }
    
    public void resetBoolean(){
        mouseOver = false;
        mousePressed = false;
    }
}
