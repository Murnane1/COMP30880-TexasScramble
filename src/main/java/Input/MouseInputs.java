package Input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import GUI_Framework.GamePanel;
import gameStates.GameState;

public class MouseInputs implements MouseListener , MouseMotionListener {

    GamePanel gamePanel;
    public MouseInputs(GamePanel gamePanel){
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        switch(GameState.state){
            case MENU:
                gamePanel.getGame().getMenu().mouseDragged(e); 
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseDragged(e);
                break;
            default:
                break;
            
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch(GameState.state){
            case MENU:
                gamePanel.getGame().getMenu().mouseMoved(e); 
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseMoved(e);
                break;
            default:
                break;
            
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch(GameState.state){
           
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseClicked(e);
                break;
            default:
                break;
            
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch(GameState.state){
            case MENU:
                gamePanel.getGame().getMenu().mousePressed(e); 
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mousePressed(e);
                break;
            default:
                break;
            
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch(GameState.state){
            case MENU:
                gamePanel.getGame().getMenu().mouseReleased(e); 
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseReleased(e);
                break;
            default:
                break;
            
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        //System.out.println("Unimplemented method 'mouseEntered'");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        //System.out.println("Unimplemented method 'mouseExited'");
    }
    
}
