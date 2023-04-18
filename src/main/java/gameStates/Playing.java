package gameStates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import Entities.guiPlayer;
import GUI.PauseOverlay;
import GUI_TEST.Game;
import Levels.LevelManager;

public class Playing extends State implements StateMethods{
    
    //TODO
    /*
     * Round of Texas Scramble
     */

    private guiPlayer player;
    private LevelManager levelMangager;
    private PauseOverlay pauseOverlay;
    private boolean paused = false;
    
    public Playing(Game game) {
        super(game);
        initClasses();
    }

    
    private void initClasses() {
        player = new guiPlayer(200, 200);   //Random Position
        levelMangager = new LevelManager(game);
        pauseOverlay = new PauseOverlay();
    }
    
    @Override
    public void update() {
        // TODO Auto-generated method stub
        levelMangager.update();
        player.update();
        if(paused){
            pauseOverlay.update();
        }
    }
    @Override
    public void draw(Graphics g) {
        // TODO Auto-generated method stub
        levelMangager.draw(g);
        player.render(g);
        if(paused){
            pauseOverlay.draw(g);
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
    }
    @Override
    public void mousePressed(MouseEvent e) {
        if(paused){
            pauseOverlay.mousePressed(e);
            return;
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        if(paused){
            pauseOverlay.mouseReleased(e);
            return;
        }
    }    
    @Override
    public void mouseDragged(MouseEvent e) {
        if(paused){
            pauseOverlay.mouseDragged(e);
            return;
        }
        player.setPosition(e.getX(), e.getY());
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        if(paused){
            pauseOverlay.mouseMoved(e);
            return;
        }
    }    
    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
    }
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {

            case KeyEvent.VK_W:
                System.out.println("W");
                player.changeYDelta(-5);
                break;
            case KeyEvent.VK_A:
                System.out.println("A");
                player.changeXDelta(-5);
                break;
            case KeyEvent.VK_S:
                System.out.println("S");
                player.changeYDelta(-5);
                break;
            case KeyEvent.VK_D:
                System.out.println("D");
                player.changeXDelta(5);
                break;
            case KeyEvent.VK_ESCAPE:
                paused = !paused;
                break;
            default:
                System.out.println("Some key is pressed");
                break;
        }    }
    public void windowFocusLost(){
        // player.reset();
    }

    public guiPlayer getPlayer() {
        return player;
    }
}
