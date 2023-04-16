package gameStates;

import java.awt.event.MouseEvent;

import GUI.MenuButton;
import GUI_TEST.Game;

public class State {
    
    protected Game game;
    public State(Game game){
        this.game = game;
    }
    public boolean isIn(MouseEvent e, MenuButton mb){
        return mb.getBounds().contains(e.getX(),e.getY());  // Returns true if mouse if over the bounds of a button
    }
    public Game getGame(){
        return game;
    }
}
