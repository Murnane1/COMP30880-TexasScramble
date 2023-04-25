package texasScramble;

import texasScramble.GameOfTexasScramble;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class GameOfScrambleTest {

    String[] names = {"Me","You","Madonna"};
    GameOfTexasScramble game = new GameOfTexasScramble(names,50);
    BagOfTiles englishBag = new BagOfTiles("ENGLISH");
    BagOfTiles frenchBag = new BagOfTiles("FRENCH");

    @Before
    public void setUp() {

    }

    @Test
    public void bagOfLanguageTest(){
        BagOfTiles englishTest = game.bagOfLanguage('e');
        assertEquals(englishBag ,englishTest);
    }
}
