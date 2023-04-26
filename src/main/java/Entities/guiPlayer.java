package Entities;

import texasScramble.Player;
import texasScramble.PotOfMoney;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

//TODO 
/*
* Maybe make this abstract class?
* Display player tiles
*
*/
public abstract class guiPlayer extends Entity{
    
    protected Player playerComponent;

    private BufferedImage image;
    public guiPlayer(int x, int y, Player playerComponent){
        super(x, y);
        this.playerComponent = playerComponent;
        importImg();
    }

    public void update(){

    }
    public void render(Graphics g){
        g.drawImage(image, (int)x, (int)y,75,75, null);
    }
    private void importImg() {
        InputStream is = getClass().getResourceAsStream("/Images/blank-tile.png");
        
        try {
            image = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    
    public void changeXDelta(int value){
        this.x += value;
    }

    public void changeYDelta(int value){
        this.y +=value;
    }

    abstract boolean shouldOpen(PotOfMoney pot);

    abstract boolean shouldSee(PotOfMoney pot);

    abstract boolean shouldRaise(PotOfMoney pot);

    abstract boolean shouldAllIn(PotOfMoney pot);


    public void nextAction(PotOfMoney pot){
        if (playerComponent.hasFolded()) return;  // no longer in the game

        if (playerComponent.isBankrupt() ) {
            // not enough money to cover the bet

            System.out.println("\n> " + playerComponent.getName() + " says: I'm out!\n");

            playerComponent.fold();

            return;
        }
        if(shouldAllIn(pot)) {
            playerComponent.allIn(pot);
            return;
        }

        else if(!playerComponent.isAllIn()){
            if (pot.getCurrentStake() > playerComponent.getStake()) {
                // existing bet must be covered

                if (shouldSee(pot)) {
                    playerComponent.seeBet(pot);
                }
                else{
                    playerComponent.fold();
                    return;
                }
            }
            if (shouldRaise(pot)){
                playerComponent.raiseBet(pot);
                return;
            }

            else {
                System.out.println(pot.getCurrentStake() + " " + playerComponent.getStake());
                System.out.println("\n> " + playerComponent.getName() + " says: I check!\n");
            }
        }
    }

    public Player getPlayerComponent() {
        return playerComponent;
    }

}
