package GUI_TEST;
import java.awt.Graphics;

import GUI_Framework.*;
import gameStates.GameState;
import gameStates.Menu;
import gameStates.Playing;

public class Game implements Runnable{
    
    private GamePanel gamePanel;
    
    private Playing playing;
    private Menu menu;

    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    public Game(){

        initClasses();

        this.gamePanel = new GamePanel(this);

        new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        this.gamePanel.requestFocus();   //Requests the input focus

        startGameLoop();
    }

    private void initClasses() {
       menu = new Menu(this);
       playing = new Playing(this);

    }

    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    public void update(){
        //Code for the game
        
        switch(GameState.state){
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            
            default:
                break;
            
        }
    }

    public void render(Graphics g){
        
        switch(GameState.state){
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;
            
            default:
                break;
            
        }
    }

    @Override
    public void run() {
        
        double timePerFrame = 1000000000.0 / FPS_SET; //nano seconds
        double timePerUpdate = 1000000000.0 / UPS_SET;


        long previousTime = System.nanoTime();


        int frames = 0;
        long lastCheck = System.currentTimeMillis();
 
        int updates = 0;

        double deltaU = 0;
        double deltaF = 0;

        while(true){
            
            long currentTime = System.nanoTime();

            deltaU += (currentTime-previousTime) / timePerUpdate;
            deltaF += (currentTime-previousTime) / timePerFrame;

            previousTime = currentTime;

            if(deltaU >=1){
                update();
                updates++;
                deltaU--;
            }

            if(deltaF >=1){
                gamePanel.repaint();    //render
                frames++;
                deltaF--;
            }
            if(System.currentTimeMillis() - lastCheck >= 1000){
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }

        }

    }


    public Menu getMenu(){
        return menu;
    }

    public Playing getPlaying(){
        return playing;
    }
    public GamePanel getGamePanel(){
        return gamePanel;
    }

    public static void main(String[] args) {
        new Game();
    }

}
