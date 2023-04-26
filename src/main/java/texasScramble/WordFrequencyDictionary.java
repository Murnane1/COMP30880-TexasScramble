package texasScramble;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class WordFrequencyDictionary {

    private ArrayList<WordFrequency> wordFrequencies;


    public WordFrequencyDictionary(String filename) throws IOException {
        ArrayList<WordFrequency> frequencyList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) { //open file and creates buffer to read file, which is closed after reading in data.
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim().replaceAll("\t", " ");
                line = line.trim().replaceAll(" +", " ");
                String[] parts = line.split(" ");

                String word = parts[0];
                long freq = Long.parseLong(parts[1]);

                WordFrequency wordFreq = new WordFrequency(word, freq);
                frequencyList.add(wordFreq);
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
            if (wordFrequency.getWord() == "word"){
                return wordFrequency.getFreq();
            }
        }
        return 0;
    }


    /*public void readFrequencyFile(String filename)throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) { //open file and creates buffer to read file, which is closed after reading in data.
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim().replaceAll(" +", " ");
                String[] parts = line.split(" ");

                String word = parts[0];
                int freq = Integer.parseInt(parts[1]);

                WordFrequency wordFreq = new WordFrequency(word, freq);
                wordFrequencies.add(wordFreq);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
