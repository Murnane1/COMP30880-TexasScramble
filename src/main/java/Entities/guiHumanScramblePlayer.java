package Entities;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import GUI.PlayButton;
import texasScramble.Player;
import texasScramble.PotOfMoney;

public class guiHumanScramblePlayer extends guiPlayer{
    //TODO CHANGE FROM TEXT INPUT TO GUI

    private PlayButton YesButton, NoButton;


    public guiHumanScramblePlayer(int x, int y, Player PlayerComponent) {
        super(x, y, PlayerComponent);
        createButtons();

    }
    private void createButtons() {
        YesButton = new PlayButton(650, 500, 0);
        NoButton = new PlayButton(800, 500, 1);

    }
    @Override
    public void update() {
        super.update();
        YesButton.update();
        NoButton.update();
    }
    @Override
    public void render(Graphics g) {
        super.render(g);
        YesButton.draw(g);
        NoButton.draw(g);
    }
    private boolean askQuestion(){

        if(YesButton.isClicked()){
            YesButton.setClicked(!YesButton.isClicked());
            return true;
        }else if(NoButton.isClicked()){
            NoButton.setClicked(!NoButton.isClicked());
            return false;
        }
        return true;
    }

    @Override
    public boolean shouldOpen(PotOfMoney pot) {
       return true;
        
    }

    @Override
    boolean shouldSee(PotOfMoney pot) {
        System.out.print("\n>> " + "Do you want to see the bet of " + playerComponent.addCount(pot.getCurrentStake() - playerComponent.getStake(), "chip", "chips") + " (y/n)?  ");

        if (playerComponent.getStake() == 0)
            return true;
        else
            return askQuestion();
    }

    @Override
    boolean shouldRaise(PotOfMoney pot) {
        System.out.println("\n " + "Do you want to raise the bet by 1 chip" + "(y/n)? ");
        return askQuestion();
    }

    @Override
    boolean shouldAllIn(PotOfMoney pot) {
        System.out.println("\n " + "Do you want to raise the bet by 1 chip" + "(y/n)? ");
        return askQuestion();
    }

    public void mousePressed(MouseEvent e) {
        if(isIn(e, YesButton)){
            YesButton.setMousePressed(true);
        }if(isIn(e, NoButton)){
            NoButton.setMousePressed(true);
        }
    }
    
    public void mouseReleased(MouseEvent e) {
        if(isIn(e, YesButton)){
            if(YesButton.isMousePressed() && !YesButton.isClicked() && !NoButton.isClicked()){
                YesButton.setClicked(!YesButton.isClicked());
            }
        }else if(isIn(e, NoButton)){
            if(NoButton.isMousePressed()&& !YesButton.isClicked() && !NoButton.isClicked()){
                NoButton.setClicked(!NoButton.isClicked());
            }
        }
        YesButton.resetBoolean();
        NoButton.resetBoolean();
    }
    
    public void mouseMoved(MouseEvent e) {
        YesButton.setMouseOver(false);
        NoButton.setMouseOver(false);
        if(isIn(e, YesButton)){
            YesButton.setMouseOver(true);
        }if(isIn(e, NoButton)){
            NoButton.setMouseOver(true);
        }
    }


    private boolean isIn(MouseEvent e, PlayButton pb){
        return pb.getBounds().contains(e.getX(), e.getY());
    }

}