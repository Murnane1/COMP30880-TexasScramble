package guiTexasScramble;

import java.awt.Graphics;
import java.util.ArrayList;


import Entities.*;
import GUI.PauseButton;
import GUI.PlayButton;
import texasScramble.*;
import java.awt.event.MouseEvent;

public class guiRoundOfTexasScramble extends RoundOfTexasScramble{



    private ArrayList<guiPlayer> guiPlayers = new ArrayList<>();

    public boolean flag = false;

    public guiRoundOfTexasScramble(BagOfTiles bag, Player[] players, int smallBlind, int button) {
        super(bag, players, smallBlind, button);

        init(players);
    }

 

    private void init(Player[] players) {
        int i = 100;
        int j = 200;
        
        for(Player player : players){
            if(player.getClass()==HumanScramblePlayer.class){
                guiPlayers.add(new guiHumanScramblePlayer(i,j, player));
                i+=100;
            } 
            else{
                guiPlayers.add(new guiComputerScramblePlayer(i,j,player));
                i+=70;
                j+=70;
            }
        }
    }
    

    public void update(){
        for(guiPlayer player: guiPlayers){
            if(player.getClass()==guiHumanScramblePlayer.class){
                guiHumanScramblePlayer Hplayer = (guiHumanScramblePlayer)player;
                Hplayer.update();
            }else{
                guiComputerScramblePlayer Cplayer = (guiComputerScramblePlayer)player;
                Cplayer.update(); 
            }
        }
      
    }
    public void draw(Graphics g){
        //TODO DRAW BUTTON (YES AND NO)??

        for(guiPlayer player: guiPlayers){
            if(player.getClass()==guiHumanScramblePlayer.class){
                guiHumanScramblePlayer Hplayer = (guiHumanScramblePlayer)player;
                Hplayer.render(g);
            }else{
                guiComputerScramblePlayer Cplayer = (guiComputerScramblePlayer)player;
                Cplayer.render(g); 
            }
        }
      
    }  
    @Override
    public void bettingCycle(PotOfMoney mainPot, int playerStart) {
        int stake = -1;
        int numActive = mainPot.getNumPlayers();

        while (stake < mainPot.getCurrentStake() && numActive > 1) {

            stake = mainPot.getCurrentStake();

            for (int i = 0; i < getNumPlayers(); i++) {
                Player currentPlayer = mainPot.getPlayer((playerStart + i) % mainPot.getNumPlayers());

                if (currentPlayer == null || currentPlayer.hasFolded() || currentPlayer.isAllIn()) {
                    continue;
                }

                delay(DELAY_BETWEEN_ACTIONS);

                int index = 0;
                for(index = 0; i< players.length;i++){
                    if(currentPlayer==players[i]){
                        break;
                    }
                }

                guiPlayers.get(index).nextAction(mainPot);
                // currentPlayer.nextAction(mainPot);

                //actions after player's move
                if (currentPlayer.isAllIn() || currentPlayer.hasFolded()) {
                    numActive--;
                }
            }
        }
        flag = true;
    }
    
    public void mousePressed(MouseEvent e) {
        for(guiPlayer player: guiPlayers){
            if(player.getClass()==guiHumanScramblePlayer.class){
                guiHumanScramblePlayer Hplayer = (guiHumanScramblePlayer)player;
                Hplayer.mousePressed(e);
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        for(guiPlayer player: guiPlayers){
            if(player.getClass()==guiHumanScramblePlayer.class){
                guiHumanScramblePlayer Hplayer = (guiHumanScramblePlayer)player;
                Hplayer.mouseReleased(e);
            }
        }
    }

    public void mouseMoved(MouseEvent e) {
        for(guiPlayer player: guiPlayers){
            if(player.getClass()==guiHumanScramblePlayer.class){
                guiHumanScramblePlayer Hplayer = (guiHumanScramblePlayer)player;
                Hplayer.mouseMoved(e);
            }
        }
    }

    public ArrayList<guiPlayer> getGuiPlayers(){
        return guiPlayers;
    }
    
}
