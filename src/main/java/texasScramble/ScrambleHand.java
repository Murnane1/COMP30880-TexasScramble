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
    public void setTile(int num, Tile tile){ //sets tile at index
        if (num >= 0 && num < TOTAL_TILES){
            playerTiles.set(num, tile);
        }
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

    public int getValue() {
        return getTile(0).getValue(); //this has to work for letter values
    }

    public int getBestHandValue(){
        return this.bestHandValue;
    }


    public List<Tile> getBestHand(){
        List<Tile> tiles = new ArrayList<>();
        tiles.addAll(playerTiles); //add player tiles to the tiles list
        if (!communityTiles.isEmpty() && communityTiles != null){
            tiles.addAll(communityTiles);

            //List<List<Tile>> possibleHands = generateHands().....
            List<Tile> bestHand = null;
            // for (List<Tile> hand : possibleHands) {
            //     int handValue = .....evalhand()
            //     if (handValue > bestHandValue) {
            //         bestHandValue = handValue;
            //         bestHand = ...... .getHand()
            //     }
            // }
            return bestHand;
        }
        return tiles;
    }

    public int getRiskWorthiness() {
        return 0;
    }




    /*TODO methods for round
                    int getBestHandValue(),
                    List<Tile> getBestHand(),
                    List<Tile> getHand()
     */

    /*TODO methods for computerPlayer
                    int getRiskWortiness() - might need to do it a different way
     */
}
