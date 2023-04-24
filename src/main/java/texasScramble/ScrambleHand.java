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

        return trieDictionary.contains(word); // check this return, dont know what state the case is for words atm.
                                              // (Should be uppercase)
    }

    // public boolean isValidWord(List<Tile> hand, String word, ScrabbleDictionary dictionary) {
    //     List<List<Tile>> combinations = generateCombinations(hand);
    //     for (List<Tile> combo : combinations) {
    //         List<String> permutations = generatePermutations(combo);
    //         for (String perm : permutations) {
    //             if (dictionary.contains(perm) && perm.equals(word)) {
    //                 return true;
    //             }
    //         }
    //     }
    //     return false;
    // }

    private List<List<Tile>> generateCombinations(List<Tile> tiles) {
        List<List<Tile>> combinations = new ArrayList<>();
        int size = tiles.size();
        for (int i = 0; i < (1 << size); i++) {
            List<Tile> combo = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                if ((i & (1 << j)) != 0) {
                    combo.add(tiles.get(j));
                }
            }
            combinations.add(combo);
        }
        return combinations;
    }

    private List<List<Character>> generatePermutations(List<Character> letters) {
        List<List<Character>> permutations = new ArrayList<>();
        if (letters.size() == 1) {
            permutations.add(letters);
            return permutations;
        }
        for (int i = 0; i < letters.size(); i++) {
            Character letter = letters.get(i);
            List<Character> remaining = new ArrayList<>(letters.subList(0, i));
            remaining.addAll(letters.subList(i + 1, letters.size()));
            List<List<Character>> subPermutations = generatePermutations(remaining);
            for (List<Character> subPermutation : subPermutations) {
                List<Character> permutation = new ArrayList<>();
                permutation.add(letter);
                permutation.addAll(subPermutation);
                permutations.add(permutation);
            }
        }
        return permutations;
    }

    private void runBackThroughTree(List<String> permutations, StringBuilder tempPermutation, boolean[] used,
            List<Tile> tiles) {
        if (tempPermutation.length() == tiles.size()) {
            permutations.add(tempPermutation.toString());
            return;
        }
        for (int i = 0; i < tiles.size(); i++) {
            if (used[i]) {
                continue;
            }
            used[i] = true;
            tempPermutation.append(tiles.get(i).getLetter());
            runBackThroughTree(permutations, tempPermutation, used, tiles);
            tempPermutation.deleteCharAt(tempPermutation.length() - 1);
            used[i] = false;
        }
    }

    private String tilesToString(List<Tile> tiles){
        StringBuilder sb = new StringBuilder();
        for (Tile tile : tiles){
            sb.append(tile.getLetter());
        }
        return sb.toString();
    }

    private String permutationToString(List<Character> permutation) {
        StringBuilder sb = new StringBuilder();
        for (Character letter : permutation) {
            sb.append(letter);
        }
        return sb.toString();
    }


    public List<String> getPossibleWords(ScrabbleDictionary dictionary) {
        ArrayList<String> possibleWords = new ArrayList<>();
        List<Tile> hand = getHand();
        ArrayList<Character> letters = new ArrayList<>();
        for (Tile tile : hand) {
            letters.add(tile.getLetter());
        }
        List<List<Character>> permutations = generatePermutations(letters);
        for (List<Character> permutation : permutations) {
            String word = permutationToString(permutation);
            if (dictionary.contains(word)) {
                possibleWords.add(word);
            }
        }
        return possibleWords;
    }


    public int getRiskWorthiness() {
        return 0;
    }


    public static void main(String[] args) throws IOException{
        BagOfTiles bag = new BagOfTiles();
        String path = "src/main/resources/WordLists/usEnglishScrabbleWordlist.txt";
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
        System.out.println(possibleWords);
        
    }
}
