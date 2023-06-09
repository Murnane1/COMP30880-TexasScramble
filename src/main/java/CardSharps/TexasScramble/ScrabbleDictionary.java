package CardSharps.TexasScramble;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import CardSharps.TexasScramble.TrieDictionary.TrieNode;

import java.net.URL;

public class ScrabbleDictionary {
    private TrieDictionary trie = new TrieDictionary(); //store words in dictionary

    public ScrabbleDictionary(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) { //open file and creates buffer to read file, which is closed after reading in data.
            String line; //store line temporarily
            while ((line = reader.readLine()) != null) {
                trie.insert(line.trim().toUpperCase());
            } //reads in each line and inserts it into the trie in uppercase
        }
    }

    public boolean contains(String word) { //checks if a word exists (USE THIS METHOD AND NOT THE ONE IN TRIEDICTIONARY (I think))
        return trie.contains(word.toUpperCase());
    }

    public TrieDictionary getTrie() {
        return this.trie;
    }
}

class TrieDictionary {
    private static TrieDictionary instance = null; //instance of TrieDictionary which is used to implement singleton pattern

    public static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>(); //nodes in trie structure
        boolean isWord; //check to see if it is a word
    }

    private TrieNode root = new TrieNode(); //new instance of trienode for root node

    public TrieDictionary() throws IOException { //does nothing, needed for instance
    }

    public static TrieDictionary getInstance() throws IOException { //gets the instance of the trie structure, returns this instance
        if (instance == null) {
            instance = new TrieDictionary();
        }
        return instance;
    }

    public void insert(String word) { //inserts the word into the trie structure
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }

        node.isWord = true;
    }

    public boolean contains(String word) { //checks to see if the trie contains a specific word.
        TrieNode node = root;
        for (char c : word.toCharArray()) { //converts word to char array
            if (!node.children.containsKey(c)) { //checks if it contains char
                return false; 
            }
            node = node.children.get(c); //gets the word and sets it
        }
        return node.isWord; //retuns that its true for being a word
    }

    public TrieNode getSubTrie(char c) { // modified method with char parameter
        TrieNode node = root.children.get(c); // get the child node corresponding to the given character
        if (node == null) { // if the child node does not exist, return null
            return null;
        }
        return node; // return the child node
    }

    public TrieNode getRoot(){
        return this.root;
    }
}