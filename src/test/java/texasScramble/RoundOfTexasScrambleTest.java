package texasScramble;

import texasScramble.HumanScramblePlayer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class RoundOfTexasScrambleTest {


    /*private HumanScramblePlayer player = new HumanScramblePlayer("p1", 10);
    private Tile tile1 = new Tile('A', 1);
    private Tile tile2 = new Tile('A', 1);
    private Tile tile3 = new Tile('N', 1);
    private Tile tile4 = new Tile('S', 1);
    private Tile tile5 = new Tile('C', 1);
    private Tile tile6 = new Tile('R', 1);*/

    List<Tile> playerTiles = new ArrayList<>();
    List<Tile> commTiles = new ArrayList<>();
    BagOfTiles bag = new BagOfTiles("ENGLISH");
    ScrambleHand meHand;
    ScrabbleDictionary dictionary;
    Player me = new HumanScramblePlayer("me", 10);
    Player you = new ComputerScramblePlayer("you", 10);
    Player zack = new ComputerScramblePlayer("zack", 10);

    Player[] players = {me, you, zack};

    RoundOfTexasScramble round;

    ArrayList<Player> ALplayers = new ArrayList<>(List.of(players));
    PotOfMoney pot = new PotOfMoney(ALplayers);


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

        round = new RoundOfTexasScramble(bag, players, 1,0, dictionary);
        meHand = new ScrambleHand(bag, dictionary);
        me.setHand(meHand);
    }

    @Test
    public void testValidWordChallenge(){
        assertEquals(0, pot.getTotal());
        assertEquals(10, me.getBank());

        me.setWord("HORSE");
        round.challenge(me, you, pot);

        assertEquals(3, pot.getTotal());
        assertEquals(7, you.getBank());
    }

    @Test
    public void testInvalidWordChallenge(){
        assertEquals(0, pot.getTotal());
        assertEquals(10, me.getBank());

        me.setWord("XCXYZ");
        round.challenge(me, you, pot);

        assertEquals(0, me.getHand().getBestHandValue());
        assertEquals(10, you.getBank());
    }
}