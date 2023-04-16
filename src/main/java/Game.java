import GUI.*;

public class Game implements Runnable{
    
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    
    private Thread gameThread;
    private final int FPS_SET = 120;

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

    @Override
    public void run() {
        
        double timePerFrame = 1000000000.0 / FPS_SET; //nano seconds
        long lastFrame = System.nanoTime();
        
        int frames = 0;
        long lastCheck = System.currentTimeMillis();

        while(true){
            
            if(System.nanoTime() - lastFrame >= timePerFrame){
                gamePanel.repaint();

                lastFrame = System.nanoTime();
                frames++;
            }
            if(System.currentTimeMillis() - lastCheck >= 1000){
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames);
                frames = 0;
            }

        }

    }

    public static void main(String[] args) {
        new Game();
    }

}
