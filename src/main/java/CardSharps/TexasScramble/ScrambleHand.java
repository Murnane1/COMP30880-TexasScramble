package CardSharps.TexasScramble;

import java.util.*;

import java.io.File;
import java.io.IOException;

public class ScrambleHand {
    private List<Tile> playerTiles;
    private List<Tile> communityTiles;
    private TrieDictionary trieDictionary;
    private final ScrabbleDictionary dictionary;
    private final BagOfTiles bag;

    public static final int TOTAL_TILES = 7;
    public static final int ALL_LETTER_BONUS = 50;
    private static final int BEST_WORD = 79;

    public ScrambleHand(BagOfTiles tiles, ScrabbleDictionary scrabbleDictionary){
        this.playerTiles = new ArrayList<>(); //init player tiles
        this.playerTiles.add(tiles.dealNext()); //add 2 inital tiles
        this.playerTiles.add(tiles.dealNext());
        
        this.communityTiles = new ArrayList<>(); //init community tiles
        this.trieDictionary = scrabbleDictionary.getTrie();

        dictionary = scrabbleDictionary;
        bag = tiles;
    }

    public void reset() {
        playerTiles = null;
        communityTiles = null;
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

    public List<String> getPossibleWords() {
        List<String> possibleWords = new ArrayList<>();
        List<Tile> hand = getHand();
        generatePossibleWords(hand, "", possibleWords);
        return possibleWords;
    }

    private void generatePossibleWords(List<Tile> tiles, String wordSoFar, List<String> possibleWords) {
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
            generatePossibleWords(remainingTiles, wordSoFar + tile.getLetter(), possibleWords);
        }

        //try not using any tile in the word
        generatePossibleWords(tiles.subList(1, tiles.size()), wordSoFar, possibleWords);

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

    public int getHandQuality(String word) {
        int wordValue = calculateWordValue(word);

        //maybe calculate 7-letter words still possible
        //average tile 1.87 points
        //players have about a 15% chance of having a 7-letter word assuming perfect knowledge (1 in 6.67)
        //so aiming for 7 letters


        if(communityTiles.size() == 0){
            for (Tile t: playerTiles) {
                wordValue += t.getValue()/2;
            }
         }
        else if(communityTiles.size() == 3){
            //have useful pre/suf fixes
            if(getBestCommunityWordValue() >= wordValue){
                wordValue -= 8; //every player can use this word
            }
        }
        else if(communityTiles.size() == 4){
            //have useful pre/suf fixes
            if(getBestCommunityWordValue() >= wordValue){
                wordValue -= 12; //every player can use this word
            }
        }
        else {                              //have all tiles best word should be evaluated
            if(getBestCommunityWordValue() >= wordValue){
                return 0; //every player can use this word
            }
            if(wordValue == BEST_WORD){
                return Integer.MAX_VALUE;   //best possible word
            }
            if(word.length() == TOTAL_TILES){
                return wordValue + 20;
            } else {
                return wordValue * 3;
            }
        }

        for(Tile t : playerTiles){
            char c = t.getLetter();
            if(c ==  'A' || c ==  'E' || c ==  'I' || c ==  'T' || c ==  'N' || c ==  'R' || c ==  'S')                     //most useful
                wordValue += 5;
            else if(c ==  'B' || c ==  'V' || c ==  'U' || c ==  'Z' || c ==  'X' || c ==  'W' || c ==  'K' || c ==  'J')   //biggest hinderance
                wordValue -= 5;
            else if (c == 'Q') {
                wordValue -= 9;
            }
        }

        return wordValue;
    }

    public int getBestCommunityWordValue() {
        List<String> possibleWords = getCommunityWords(dictionary);
        int bestCommunityWordValue = 0;
        for (String word : possibleWords){
            int wordValue = calculateWordValue(word);
            if(wordValue > bestCommunityWordValue){
                bestCommunityWordValue = wordValue;
            }
        }
        return bestCommunityWordValue;
    }

    public List<String> getCommunityWords(ScrabbleDictionary dictionary) {
        List<String> possibleWords = new ArrayList<>();
        List<Tile> hand = communityTiles;
        generatePossibleWords(hand, "", possibleWords);
        return possibleWords;
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


    public List<String> getPotentialSevenLetterWords(List<Tile> hand, List<Tile> communityTiles) {
        List<String> potentialWords = new ArrayList<>();
        for (int i = hand.size() + communityTiles.size(); i <= 7; i++) {
            List<String> sevenLetterWords = getSevenLetterWordsContainingTiles(hand, communityTiles, i);
            potentialWords.addAll(sevenLetterWords);
        }
        return potentialWords;
    }

    public List<String> getSevenLetterWordsContainingTiles(List<Tile> hand, List<Tile> communityTiles, int numTiles) {
        List<String> possibleWords = new ArrayList<>();
        generatePossibleSevenLetterWords(hand, communityTiles, "", numTiles, possibleWords, numTiles);
        return possibleWords;
    }

    private void generatePossibleSevenLetterWords(List<Tile> hand, List<Tile> communityTiles, String wordSoFar, int numTilesLeft, List<String> possibleWords, int maxTiles) {
        if (numTilesLeft == 0) {
            if (dictionary.contains(wordSoFar)) {
                if (!possibleWords.contains(wordSoFar)) {
                    possibleWords.add(wordSoFar);
                }
            }
            return;
        }
        List<Tile> allTiles = new ArrayList<>(hand);
        allTiles.addAll(communityTiles);
        int tilesAdded = hand.size() + communityTiles.size() - numTilesLeft;
        for (int i = 0; i < allTiles.size(); i++) {
            Tile tile = allTiles.get(i);
            if (tilesAdded < 7 - hand.size()) {
                List<Tile> remainingTiles = new ArrayList<>(allTiles);
                remainingTiles.remove(i);
                generatePossibleSevenLetterWords(hand, communityTiles, wordSoFar + tile.getLetter(), numTilesLeft - 1, possibleWords, maxTiles);
            }
        }
    }
}
