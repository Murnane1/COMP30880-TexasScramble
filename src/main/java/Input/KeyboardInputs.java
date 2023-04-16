package Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import GUI.GamePanel;

public class KeyboardInputs implements KeyListener {

    private GamePanel gamePanel;
    public KeyboardInputs(GamePanel gamePanel){
        this.gamePanel = gamePanel;                 //Used to read in the inputs and change certain values
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        switch (e.getKeyCode()) {

            case KeyEvent.VK_W:
                System.out.println("W");
                gamePanel.changeYDelta(-5);
                break;
            case KeyEvent.VK_A:
                System.out.println("A");
                gamePanel.changeXDelta(-5);
                break;
            case KeyEvent.VK_S:
                System.out.println("S");
                gamePanel.changeYDelta(-5);
                break;
            case KeyEvent.VK_D:
                System.out.println("D");
                gamePanel.changeXDelta(5);
                break;
            default:
                System.out.println("Some key is pressed");
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
    }

}
