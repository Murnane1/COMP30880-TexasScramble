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
        System.out.println(":"+wordFrequencies.get(0).getWord() + ":word | freq " + wordFrequencies.get(0).getFreq());
        assertEquals(wordFrequencies.get(0).getFreq(), 23135851162L);
        assertEquals(wordFrequencies.get(0).getWord(), "the");


    }
}
