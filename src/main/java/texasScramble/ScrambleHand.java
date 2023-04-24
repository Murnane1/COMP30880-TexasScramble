package texasScramble;

import java.util.*;

public class ScrambleHand {
    private List<Tile> playerTiles;
    private List<Tile> communityTiles;
    private int bestHandValue;

    public static final int TOTAL_TILES = 7;

    public ScrambleHand(BagOfTiles tiles){
        this.playerTiles = new ArrayList<>(); //init player tiles
        this.playerTiles.add(tiles.dealNext()); //add 2 inital tiles
        this.playerTiles.add(tiles.dealNext());
        
        this.communityTiles = new ArrayList<>(); //init community tiles
        this.bestHandValue = 0;
    }

    //modifiers
    public void addCommunityTiles(List<Tile> tiles){ //append 
        this.communityTiles.addAll(tiles);
    }


    //setters
    public void setPlayerTiles(List<Tile> tiles){ //sets tile at index
        playerTiles.clear();
        playerTiles.addAll(tiles);
    }

    public void setCommunityTiles(int num, Tile tile) {
        communityTiles.set(num, tile);
    }

    //getters
    public List<Tile> getCommunityTiles() { // gets all current community tiles
        return communityTiles;
    }

    public Tile getTile(int num){ //gets tile at index
        if (num >= 0 && num < TOTAL_TILES){
            return playerTiles.get(num);
        } else {
            return null;
        }
    }

    public List<Tile> getHand(){ //returns player hand
        return new ArrayList<>(playerTiles);
    }

    public int getValue() { //get value of single tile
        return getTile(0).getValue(); //this has to work for letter values
    }
    
    public int getBestHandValue(){
        return this.bestHandValue;
    }

    public void setBestHandValueToZero(){
        bestHandValue = 0;
    }

    public int getRiskWorthiness() {
        return 0;
    }
}
