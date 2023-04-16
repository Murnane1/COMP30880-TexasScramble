package gameStates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import Entities.guiPlayer;
import GUI_TEST.Game;
import Levels.LevelManager;

public class Playing extends State implements StateMethods{
    
    
    public Playing(Game game) {
        super(game);
        initClasses();
    }

    private guiPlayer player;
    private LevelManager levelMangager;
    
    private void initClasses() {
        player = new guiPlayer(200, 200);   //Random Position
        levelMangager = new LevelManager(game);
    }
    
    @Override
    public void update() {
        // TODO Auto-generated method stub
        levelMangager.update();
        player.update();
    }
    @Override
    public void draw(Graphics g) {
        // TODO Auto-generated method stub
        levelMangager.draw(g);
        player.render(g);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseClicked'");
    }
    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mousePressed'");
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseReleased'");
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        player.setPosition(e.getX(), e.getY());
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mouseMoved'");
    }
    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
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
                GameState.state = GameState.MENU;
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
