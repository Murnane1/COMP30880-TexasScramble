package gameStates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


import GUI.PauseOverlay;
import GUI_TEST.Game;
import Levels.LevelManager;
import guiTexasScramble.guiRoundOfTexasScramble;
import texasScramble.BagOfTiles;
import texasScramble.ComputerScramblePlayer;
import texasScramble.HumanScramblePlayer;
import texasScramble.Player;

public class Playing extends State implements StateMethods{
    
    //TODO
    /*
     * Round of Texas Scramble
     */


    private LevelManager levelMangager;
    private PauseOverlay pauseOverlay;
    private boolean paused = false;
    
    private guiRoundOfTexasScramble round;

    public Playing(Game game) {
        super(game);
        initClasses();
    }

    
    private void initClasses() {
        //player = new guiHumanScramblePlayer(200, 200, "Generic Name - Dan", 10);   //Random Position
        levelMangager = new LevelManager(game);
        pauseOverlay = new PauseOverlay();

        //TODO REFACTOR
        String[] names = {"Human", "Bob", "Dylan", "Mark"};
        int numPlayers = names.length;
        Player[] players = new Player[numPlayers];

        for (int i = 0; i < numPlayers; i++)
            if (i == 0)
                players[i] = new HumanScramblePlayer(names[i].trim(), 10);
            else
                players[i] = new ComputerScramblePlayer(names[i].trim(), 10);
        
        BagOfTiles bag = new BagOfTiles();
        

        round = new guiRoundOfTexasScramble(bag, players, 1, 0);
    }
    
    @Override
    public void update() {
        round.update();

        if(!round.flag)
            round.play();
        round.update();
        levelMangager.update();
        if(paused){
            pauseOverlay.update();
        }
    }
    @Override
    public void draw(Graphics g) {
        levelMangager.draw(g);
        round.draw(g);
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
        round.mousePressed(e);
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        if(paused){
            pauseOverlay.mouseReleased(e);
            return;
        }
        round.mouseReleased(e);
    }    
    @Override
    public void mouseDragged(MouseEvent e) {
        if(paused){
            pauseOverlay.mouseDragged(e);
            return;
        }
        round.getGuiPlayers().get(0).setPosition(e.getX(), e.getY());
        
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        if(paused){
            pauseOverlay.mouseMoved(e);
            return;
        }
        round.mouseMoved(e);
    }    
    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
    }
    @Override
    public void keyReleased(KeyEvent e) {
        
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                round.getGuiPlayers().get(0).changeYDelta(-5);
                break;
            case KeyEvent.VK_A:
                round.getGuiPlayers().get(0).changeXDelta(-5);
                break;
            case KeyEvent.VK_S:
                round.getGuiPlayers().get(0).changeYDelta(-5);
                break;
            case KeyEvent.VK_D:
                round.getGuiPlayers().get(0).changeXDelta(5);
                break;
            case KeyEvent.VK_ESCAPE:
                paused = !paused;
                break;
            
            default:
                System.out.println("Some key is pressed");
                break;
        }    
    }

    public void windowFocusLost(){
        // player.reset();
    }

  
}
