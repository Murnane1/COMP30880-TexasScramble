package texasScramble;

import texasScramble.HumanScramblePlayer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static org.junit.Assert.*;

public class WordFrequencyDictionaryTest {
    private WordFrequencyDictionary wordFrequencyDictionary;
    private ArrayList<WordFrequency> wordFrequencies;

    @Before
    public void setUp() {
        String filename = "englishWordFrequency.txt";
        URL url = ScrabbleDictionary.class.getResource("/WordFrequencies/" + filename);
        String filepath = url.getPath();
        try {
            wordFrequencyDictionary = new WordFrequencyDictionary(filepath);
        } catch (IOException e) {
            System.out.println("COULD NOT ADD DICTIONARY");
        }

        wordFrequencies = wordFrequencyDictionary.getWordFrequencies();
    }

    @Test
    public void testWordFrequencyDictionary(){
        assertEquals(wordFrequencies.get(0).getFreq(), 23135851162L);
        assertEquals(wordFrequencies.get(0).getWord(), "THE");

        System.out.println("length: "+wordFrequencyDictionary.getWordFrequencies().get(wordFrequencies.size()-2).getWord());

        System.out.println("frequency of: "+wordFrequencyDictionary.getWordFrequency("THE"));
        System.out.println("frequency of: "+wordFrequencyDictionary.getWordFrequency(wordFrequencies.get(0).getWord()));
        System.out.println("frequency of: "+wordFrequencyDictionary.getWordFrequency("MOUSE")); //33667003
    }
}
