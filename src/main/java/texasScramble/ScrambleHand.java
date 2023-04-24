package texasScramble;

import java.util.*;

import java.net.URL;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ScrambleHand {
    private List<Tile> playerTiles;
    private List<Tile> communityTiles;
    private int bestHandValue;
    private TrieDictionary trieDictionary;

    public static final int TOTAL_TILES = 7;

    public ScrambleHand(BagOfTiles tiles, ScrabbleDictionary scrabbleDictionary){
        this.playerTiles = new ArrayList<>(); //init player tiles
        this.playerTiles.add(tiles.dealNext()); //add 2 inital tiles
        this.playerTiles.add(tiles.dealNext());
        
        this.communityTiles = new ArrayList<>(); //init community tiles
        this.bestHandValue = 0;
        this.trieDictionary = scrabbleDictionary.getTrie();;
    }

    //modifiers
    public void addCommunityTiles(List<Tile> tiles){ //append 
        this.communityTiles.addAll(tiles);
    }

    public void addPlayerTiles(List<Tile> tiles){
        this.playerTiles.addAll(tiles);
    }

    //setters
    public void setPlayerTiles(int num, Tile tile){
        playerTiles.set(num, tile);
    }

    public void setCommunityTiles(int num, Tile tile) {
        communityTiles.set(num, tile);
    }

    public void setBestHandValueToZero() {
        bestHandValue = 0;
    }

    //getters
    public List<Tile> getCommunityTiles() { // gets all current community tiles
        return communityTiles;
    }

    public List<Tile> getPlayerTiles() {
        return playerTiles;
    }

    public Tile getTile(int num){ //gets tile at index
        if (num >= 0 && num < TOTAL_TILES){
            return playerTiles.get(num);
        } else {
            return null;
        }
    }

    public List<Tile> getHand(){ //returns player hand
        List<Tile> mergedHand = new ArrayList<>();
        mergedHand.addAll(playerTiles);
        mergedHand.addAll(communityTiles);
        return mergedHand;
    }

    public int getValue(Tile tile) { //get value of single tile
        return tile.getValue();
    }
    
    public int getBestHandValue(){
        return this.bestHandValue;
    }
    

    public boolean isValidWord(String word, ScrabbleDictionary trieDictionary) {
        List<Tile> hand = getHand();
        Map<Character, Integer> characterFrequency = new HashMap<>();
        for (Tile tile : hand) {
            char c = tile.getLetter();
            characterFrequency.put(c, characterFrequency.getOrDefault(c, 0) + 1);
        }

        for (char c : word.toCharArray()) {
            if (!characterFrequency.containsKey(c) || characterFrequency.get(c) == 0) {
                return false;
            }
            characterFrequency.put(c, characterFrequency.get(c) - 1);
        }

        return trieDictionary.contains(word); // check this return, dont know what state the case is for words atm.
                                              // (Should be uppercase)
    }

    public List<String> getPossibleWords(ScrabbleDictionary dictionary){
        List<String> possibleWords = new ArrayList<>();
        List<Tile> hand = getHand();

        List<List<Tile>> combinations = generateCombinations(hand, TOTAL_TILES);
        for (List<Tile> combination: combinations){
            String word = tilesToString(combination);
            if (isValidWord(word, dictionary)){ //changes to isValid
                possibleWords.add(word);
            }
        }
        return possibleWords;
    }

    private List<List<Tile>> generateCombinations(List<Tile> tiles, int k){
        List<List<Tile>> combinations = new ArrayList<>();
        runBackThroughTree(combinations, new ArrayList<Tile>(), tiles, k, 0);
        return combinations;
    }

    private void runBackThroughTree(List<List<Tile>> combinations, List<Tile> tempCombination, List<Tile> tiles, int k, int start){
        if (tempCombination.size() == k){
            combinations.add(new ArrayList<>(tempCombination));
            return;
        }
        for (int i = start; i < tiles.size(); i++){
            tempCombination.add(tiles.get(i));
            runBackThroughTree(combinations, tempCombination, tiles, k, i+1);
            tempCombination.remove(tempCombination.size()-1);
        }
    }

    private String tilesToString(List<Tile> tiles){
        StringBuilder sb = new StringBuilder();
        for (Tile tile : tiles){
            sb.append(tile.getLetter());
        }
        return sb.toString();
    }

    public boolean isValidWord(String word){
        TrieDictionary dictionary = TrieDictionary.getInstance();
        return dictionary.contains(word);
    }


    public int getRiskWorthiness() {
        return 0;
    }


    public static void main(String[] args) throws IOException{
        BagOfTiles bag = new BagOfTiles();
        String path = "src/main/resources/WordLists/ukEnglishScrabbleWordlist.txt";
        File file = new File(path);
        ScrabbleDictionary dictionary = new ScrabbleDictionary(file.getAbsolutePath());
        ScrambleHand hand = new ScrambleHand(bag, dictionary);

        List<Tile> communityTiles = new ArrayList<>();
        communityTiles.add(new Tile('C', 3));
        communityTiles.add(new Tile('A', 1));
        communityTiles.add(new Tile('T', 1));
        hand.addCommunityTiles(communityTiles);

        List<String> possibleWords = hand.getPossibleWords(dictionary);
        possibleWords.size();
        System.out.println(possibleWords.size());
        System.out.println(Arrays.toString(possibleWords.toArray()));
        
    }
}
