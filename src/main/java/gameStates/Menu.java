package gameStates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import GUI.MenuButton;
import GUI_TEST.Game;

public class Menu extends State implements StateMethods{

    private MenuButton[] buttons = new MenuButton[3];
    private BufferedImage background;
    private int menuX, menuY, menuWidth, menuHeight;

    public Menu(Game game) {
        super(game);
        
        loadBackground();
        loadButtons();
    }

    private void loadBackground() {
        background = importImg();
        menuWidth = background.getWidth();
        menuHeight = background.getHeight();
        menuX = 960/2 - menuWidth/2;
        menuY = 540/2 - menuHeight/2;
    }
    private BufferedImage importImg() {
        InputStream is = getClass().getResourceAsStream("/Images/menu_background.png");
        
        try {
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("UNABLE TO READ MENU BUTTON", e);
        }
        
    }

    private void loadButtons() {
        buttons[0] = new MenuButton(960/2, 540/2 - 40, 0, GameState.PLAYING);
        buttons[1] = new MenuButton(960/2, 540/2 + 30    , 1, GameState.OPTIONS);
        buttons[2] = new MenuButton(960/2, 540/2 + 100, 2, GameState.QUIT);
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        for(MenuButton mb : buttons){
            mb.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        // TODO Auto-generated method stub
        g.drawImage(background, menuX, menuY, menuWidth, menuHeight, null);
        for(MenuButton mb : buttons)
        {
            mb.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        for(MenuButton mb : buttons){
            if(isIn(e, mb)){
                mb.setMousePressed(true);
                break;
            }
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        for(MenuButton mb : buttons){
            if(isIn(e,mb)){
                if(mb.isMousePressed()){
                    mb.applyGameState();
                }
                break;
            }
        }
        resetButtons();
    }
    private void resetButtons() {
        for(MenuButton mb : buttons){
            mb.resetBoolean();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
       
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for(MenuButton mb : buttons){
            mb.setMouseOver(false);
        }

        for(MenuButton mb : buttons){
            if(isIn(e, mb)){
                mb.setMouseOver(true);
                break;
            }
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
       
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
    
}
