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
        this.trieDictionary = scrabbleDictionary.getTrie();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        List<Tile> hand = getHand();
        sb.append("Tiles: [");
        for (Tile tile : hand) {
            sb.append(tile.getLetter()).append(", ");
        }
        if (hand.size() > 0) {
            sb.delete(sb.length() - 2, sb.length()); // Remove trailing ", "
        }
        sb.append("]");
        return sb.toString(); 
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
    return trieDictionary.contains(word);
    }

    public List<String> getPossibleWords(ScrabbleDictionary dictionary) {
        List<String> possibleWords = new ArrayList<>();
        List<Tile> hand = getHand();
        generatePossibleWords(hand, "", dictionary, possibleWords);
        return possibleWords;
    }

    private void generatePossibleWords(List<Tile> tiles, String wordSoFar, ScrabbleDictionary dictionary,
            List<String> possibleWords) {
        if (tiles.isEmpty()) {
            if (dictionary.contains(wordSoFar)) {
                if (possibleWords.contains(wordSoFar)){
                    return;
                } else {
                    possibleWords.add(wordSoFar);
                }
            }
            return;
        }

        // Try using each tile in the word
        for (int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);
            List<Tile> remainingTiles = new ArrayList<>(tiles);
            remainingTiles.remove(i);
            generatePossibleWords(remainingTiles, wordSoFar + tile.getLetter(), dictionary, possibleWords);
        }

        // Try not using any tile in the word
        generatePossibleWords(tiles.subList(1, tiles.size()), wordSoFar, dictionary, possibleWords);
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
        communityTiles.add(bag.dealNext());
        communityTiles.add(bag.dealNext());
        communityTiles.add(bag.dealNext());
        hand.addCommunityTiles(communityTiles);

        System.out.println(hand.toString());

        List<String> possibleWords = hand.getPossibleWords(dictionary);
        for (String word: possibleWords){
            boolean contains = dictionary.contains(word);
            System.out.println(word + " is " + (contains ? "" : "not ") + "in the dictionary");
        }
        System.out.println(possibleWords.toString());
    }
}
