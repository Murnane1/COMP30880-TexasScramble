//import CardSharps.TexasScramble.*;
//
//
//import org.junit.Before;
//import org.junit.Test;
//import static org.junit.Assert.*;
//
//import java.util.*;
//
//import static org.junit.Assert.assertEquals;
//
//public class GameOfScrambleTest {
//
//    String[] names = {"Me","You","Madonna"};
//    GameOfTexasScramble game = new GameOfTexasScramble(3,50, "Me");
//    BagOfTiles englishBag = new BagOfTiles("ENGLISH");
//    BagOfTiles frenchBag = new BagOfTiles("FRENCH");
//
//
//    @Before
//    public void setUp() {
//
//    }
//
//    @Test
//    public void bagOfLanguageTest(){
//        BagOfTiles englishTest = game.bagOfLanguage('e');
//        assertEquals(englishBag.getNumtiles() ,englishTest.getNumtiles());
//    }
//
//    @Test
//    public void wordFrequencyDictionaryTest() {
//        WordFrequencyDictionary wordFrequencyDictionary = game.getWordFrequencyDictionary('e');
//        assertEquals(wordFrequencyDictionary.getWordFrequencies().get(0).getWord(), "THE");
//        assertEquals(wordFrequencyDictionary.getWordFrequency("MOUSE"), 33667003);
//
//    }
//
//    @Test
//    public void verifyNamesTest(){
//        String[] takenNames = {"Tom", "Dick", "Harry", "Sarah", "Maggie", "Andrew", "Zoe", "Sadbh", "Mark", "Sean"};
//        String humanName = "Sadbh";
//        assertTrue(Arrays.asList(takenNames).contains(humanName));
//    }
//
//}
