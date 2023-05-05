package texasScramble;

import texasScramble.HumanScramblePlayer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class HumanScramblePlayerTest {
    private HumanScramblePlayer player = new HumanScramblePlayer("p1", 10);
    private Tile tile1 = new Tile('A', 1);
    private Tile tile2 = new Tile('A', 1);
    private Tile tile3 = new Tile('N', 1);
    private Tile tile4 = new Tile('S', 1);
    private Tile tile5 = new Tile('C', 1);
    private Tile tile6 = new Tile('R', 1);
    private Tile tile7 = new Tile('A', 1);

    List<Tile> playerTiles = new ArrayList<>();
    List<Tile> commTiles = new ArrayList<>();
    BagOfTiles tiles = new BagOfTiles("ENGLISH");
    ScrambleHand playerHand;
    ScrabbleDictionary dictionary;

    @Before
    public void setUp() {
        String filename = "usEnglishScrabbleWordlist.txt";
        URL url = ScrabbleDictionary.class.getResource("/WordLists/" + filename);
        String filepath = url.getPath();
        try {
            dictionary = new ScrabbleDictionary(filepath);
        } catch (IOException e) {
            System.out.println("COULD NOT ADD DICTIONARY");
        }

        playerHand = new ScrambleHand(tiles, dictionary);


        playerHand.setPlayerTiles(0, tile1);
        playerHand.setPlayerTiles(1, tile2);

        commTiles.add(tile3);
        commTiles.add(tile4);
        commTiles.add(tile5);
        commTiles.add(tile6);
        commTiles.add(tile7);
        playerHand.addCommunityTiles(commTiles);
        player.setHand(playerHand);

        System.out.println(playerHand.getHand());
    }

    @Test
    public void testCheckWord() {
        boolean check1 = player.checkWord("NASCAR");
        assertEquals(true, check1);

        boolean check2 = player.checkWord("house");
        assertEquals(false, check2);

        boolean check3 = player.checkWord("aaaaa");
        assertEquals(false, check3);

        boolean check4 = player.checkWord("NasCAr");
        assertEquals(true, check4);

        boolean check5 = player.checkWord("N a s C A r");
        assertEquals(true, check5);

        boolean check6 = player.checkWord("aa");
        assertEquals(true, check6);

        boolean checkMaxSameLetter = player.checkWord("AAA");
        assertEquals(true, checkMaxSameLetter);

        boolean checkManySameLetter = player.checkWord("AAAA");
        assertEquals(false, checkManySameLetter);

        boolean checkNull = player.checkWord("");
        assertEquals(true, checkNull);
    }
}
