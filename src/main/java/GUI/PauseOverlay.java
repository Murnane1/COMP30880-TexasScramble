package GUI;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class PauseOverlay {
    
    private int bgX, bgY, bgWidth, bgHeight;
    private SoundButton musicButton, sfxButton;
    private BufferedImage background;

    public PauseOverlay(){
        loadBackground();
        createSoundButton();
    }

    private void createSoundButton() {

        musicButton = new SoundButton(520, 190, 40, 40);
        sfxButton = new SoundButton(520, 240, 40, 40);   
    }

    private void loadBackground() {
        background = importImg();
        bgWidth = background.getWidth();
        bgHeight = background.getHeight();
        bgX = 960/2 - bgWidth/2;
        bgY = 540/2 - bgHeight/2;
    }
    public void update(){
        musicButton.update();
        sfxButton.update();

    }
    public void draw(Graphics g){

        //Background
        g.drawImage(background, bgX, bgY,bgWidth, bgHeight, null);

        //Sound Buttons
        musicButton.draw(g);
        sfxButton.draw(g);
    }
    public void mouseDragged(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if(isIn(e, musicButton)){
            musicButton.setMousePressed(true);
        }if(isIn(e, sfxButton)){
            sfxButton.setMousePressed(true);
        }
    }
    
    public void mouseReleased(MouseEvent e) {
        if(isIn(e, musicButton)){
            if(musicButton.isMousePressed()){
                musicButton.setMuted(!musicButton.isMuted());
            }
        }else if(isIn(e, sfxButton)){
            if(sfxButton.isMousePressed()){
                sfxButton.setMuted(!sfxButton.isMuted());
            }
        }
        musicButton.resetBool();
        sfxButton.resetBool();
    }
    
    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        if(isIn(e, musicButton)){
            musicButton.setMouseOver(true);
        }if(isIn(e, sfxButton)){
            sfxButton.setMouseOver(true);
        }
    }


    private boolean isIn(MouseEvent e, PauseButton pb){
        return pb.getBounds().contains(e.getX(), e.getY());
    }

    private BufferedImage importImg() {
        InputStream is = getClass().getResourceAsStream("/Images/pause_menu.png");
        
        try {
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("UNABLE TO READ MENU BUTTON", e);
        }
        
    }
}
