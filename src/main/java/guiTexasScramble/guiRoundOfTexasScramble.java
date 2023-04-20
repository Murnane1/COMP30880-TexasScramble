package guiTexasScramble;

import java.awt.Graphics;
import java.util.ArrayList;

import Entities.*;
import texasScramble.*;

public class guiRoundOfTexasScramble extends RoundOfTexasScramble{

    private ArrayList<guiPlayer> guiPlayers = new ArrayList<>();

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
                j+=50;
            }
        }
    }

    public void update(){

    }
    public void draw(Graphics g){
        //TODO DRAW BUTTON (YES AND NO)??
        for(guiPlayer player: guiPlayers){
            player.render(g);
        }
    }  

    // @Override
    // public void printPlayerHand(){
    //     //TODO Draw hand 

    // }

    // @Override
    // public void bettingCycle(PotOfMoney pot, int playerStart){
    //     //TODO
    // }

    public ArrayList<guiPlayer> getGuiPlayers(){
        return guiPlayers;
    }
    
}
