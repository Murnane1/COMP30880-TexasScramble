package CardSharps.TexasScramble;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class WordFrequencyDictionary {

    private ArrayList<WordFrequency> wordFrequencies;
    private final static int MAX_WORD_LENGTH = 7;

    public WordFrequencyDictionary(String filename) throws IOException {
        ArrayList<WordFrequency> frequencyList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) { //open file and creates buffer to read file, which is closed after reading in data.
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim().replaceAll("\t", " ");
                line = line.trim().replaceAll(" +", " ");
                line = line.trim().toUpperCase();
                String[] parts = line.split(" ");

                String word = parts[0];

                if (word.length() <= MAX_WORD_LENGTH){
                    long freq = Long.parseLong(parts[1]);
                    WordFrequency wordFreq = new WordFrequency(word, freq);
                    frequencyList.add(wordFreq);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        wordFrequencies = frequencyList;
    }

    public ArrayList<WordFrequency> getWordFrequencies() {
        return wordFrequencies;
    }

    //if the word is in the frequency dictionary return number of occurrences
    public long getWordFrequency(String word) {
        for (WordFrequency wordFrequency: wordFrequencies) {
            if (wordFrequency.getWord().equals(word)){
                return wordFrequency.getFreq();
            }
        }
        return 0;
    }
}
