package GUI;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class SoundButton extends PauseButton {

    private BufferedImage[][] soundImgs;
    private boolean mouseOver, mousePressed;
    private boolean muted;
    private int rowIndex, colIndex;

    private final int SIZE = 42;
    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);

        loadSoundImgs();
    }
    private void loadSoundImgs() {
        soundImgs = new BufferedImage[2][3];
        BufferedImage temp = importImg();
        System.out.println(soundImgs.length + " " + soundImgs[0].length);
        for(int i = 0; i<soundImgs.length;i++){
            for(int j = 0; j<soundImgs[i].length;j++){
            soundImgs[i][j] = temp.getSubimage(j*SIZE, i*SIZE, SIZE, SIZE);
            }
        }
    }

    private BufferedImage importImg() {
        InputStream is = getClass().getResourceAsStream("/Images/sound_button.png");
        
        try {
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("UNABLE TO READ MENU BUTTON", e);
        }
        
    }

    public void update(){
        if(muted){
            rowIndex = 1;
        }else{
            rowIndex = 0;
        }

        colIndex = 0;
        if(mouseOver){
            colIndex = 1;
        }if(mousePressed){
            colIndex = 2;
        }

    }
    public void resetBool(){
        mouseOver = false;
        mousePressed = false;
    }
    public void draw(Graphics g){
        g.drawImage(soundImgs[rowIndex][colIndex], x, y, width, height, null);
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
    public boolean isMuted() {
        return muted;
    }
    public void setMuted(boolean muted) {
        this.muted = muted;
    }
    
}
