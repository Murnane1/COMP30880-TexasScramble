import GUI.*;

public class Game implements Runnable{
    
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    public Game(){

        this.gamePanel = new GamePanel();
        this.gameWindow = new GameWindow(gamePanel);
        this.gamePanel.requestFocus();   //Requests the input focus

        startGameLoop();
    }

    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    public void update(){
        //Code for the game
        gamePanel.updateGame();
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

    public static void main(String[] args) {
        new Game();
    }

}
