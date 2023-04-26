package Entities;

import texasScramble.Player;
import texasScramble.PotOfMoney;

public class guiComputerScramblePlayer extends guiPlayer {
    //TODO CHANGE FROM TEXT INPUT TO GUI using Override
    public guiComputerScramblePlayer(int x, int y, Player PlayerComponent) {
        super(x, y, PlayerComponent);
    }

    @Override
    public void nextAction(PotOfMoney mainPot) {

        playerComponent.nextAction(mainPot);
        
    }

    @Override
    boolean shouldOpen(PotOfMoney pot) {
        return playerComponent.shouldOpen(pot);
    }

    @Override
    boolean shouldSee(PotOfMoney pot) {
        return playerComponent.shouldSee(pot);
    }

    @Override
    boolean shouldRaise(PotOfMoney pot) {
        return playerComponent.shouldRaise(pot);
    }

    @Override
    boolean shouldAllIn(PotOfMoney pot) {
        return playerComponent.shouldAllIn(pot);
    }

}
