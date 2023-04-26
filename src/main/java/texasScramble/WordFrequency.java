package texasScramble;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class WordFrequency {

    ArrayList<WordFrequency> wordFrequencies;
    private String word;
    private long freq;


    public WordFrequency(String word, long freq) {
        this.word = word;
        this.freq = freq;
    }

    public String getWord() {
        return word;
    }

    public long getFreq() {
        return freq;
    }
}
