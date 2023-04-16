import GUI.*;

public class Game {
    
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    
    public Game(){

        this.gamePanel = new GamePanel();
        this.gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();   //Requests the input focus
    }


    public static void main(String[] args) {
        new Game();
    }
}
