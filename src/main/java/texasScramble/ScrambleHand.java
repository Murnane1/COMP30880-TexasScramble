package texasScramble;

import java.util.*;

import java.io.File;
import java.io.IOException;

public class ScrambleHand {
    private List<Tile> playerTiles;
    private List<Tile> communityTiles;
    private int bestHandValue;
    private TrieDictionary trieDictionary;
    private BagOfTiles bag;

    public static final int TOTAL_TILES = 7;
    private final static int ALL_LETTER_BONUS = 50;

    public ScrambleHand(BagOfTiles tiles, ScrabbleDictionary scrabbleDictionary){
        this.playerTiles = new ArrayList<>(); //init player tiles
        this.playerTiles.add(tiles.dealNext()); //add 2 inital tiles
        this.playerTiles.add(tiles.dealNext());
        
        this.communityTiles = new ArrayList<>(); //init community tiles
        this.bestHandValue = 0;
        this.trieDictionary = scrabbleDictionary.getTrie();
        bag = tiles;
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
        //try using each tile in the word
        for (int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);
            List<Tile> remainingTiles = new ArrayList<>(tiles);
            remainingTiles.remove(i);
            generatePossibleWords(remainingTiles, wordSoFar + tile.getLetter(), dictionary, possibleWords);
        }

        //try not using any tile in the word
        generatePossibleWords(tiles.subList(1, tiles.size()), wordSoFar, dictionary, possibleWords);

        //remove empty string at end of list
        if (!possibleWords.isEmpty() && possibleWords.get(possibleWords.size() - 1).isEmpty()) {
            possibleWords.remove(possibleWords.size() - 1);
        }
    }

    public int calculateWordValue(String word) {
        int value = 0;
        for (char c : word.toCharArray()) {
            for (Tile tile : bag.getBag()) {
                if (tile != null){
                    if (tile.getLetter() == c) {
                        value += tile.getValue();
                        break;
                    }
                }
            }
        }
        if(word.length() == TOTAL_TILES){
            value += ALL_LETTER_BONUS;
        }
        return value;
    }

    public int getRiskWorthiness(String word) {
        int wordValue = calculateWordValue(word);
        if(communityTiles.size() == 0){
            //TODO how will 5 more cards effect current hand (vowels more useful?)
        }
        else if(communityTiles.size() == 3){

        }
        else if(communityTiles.size() == 4){

        }
        else {

        }
        return wordValue; //TODO (max word value is 29+50=79) , how to manage incomplete hands
    }

    public int getBestCommunityWordValue() {

        return 0;
    }

    public int getBettingRound(){
        int betRound = 0;   //preflop
        if(communityTiles.size() == 3){
            betRound = 1;   //flop
        }
        else if(communityTiles.size() == 4){
            betRound = 2;   //turn
        }
        else if(communityTiles.size() == 5){
            betRound = 3;   //river
        }
        return betRound;
    }

    public static void main(String[] args) throws IOException{
        BagOfTiles bag = new BagOfTiles("ENGLISH");
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
            System.out.println(word + " is " + (contains ? "" : "not ") + "in the dictionary!" + " Word value = "  + hand.calculateWordValue(word));
        }
        
        System.out.println(possibleWords + ": Total Words = " + possibleWords.size());
    }
}
