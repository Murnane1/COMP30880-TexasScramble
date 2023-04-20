package texasScramble;

import texasScramble.HumanScramblePlayer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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

    List<Tile> playerTiles = new ArrayList<>();
    List<Tile> commTiles = new ArrayList<>();
    BagOfTiles tiles = new BagOfTiles();
    ScrambleHand playerHand;

    @Before
    public void setUp() {
        playerHand = new ScrambleHand(tiles);

        playerTiles.add(tile1);
        playerTiles.add(tile2);
        //player.getHand().addPlayerTiles(playerTiles);
        playerHand.addPlayerTiles(playerTiles);

        commTiles.add(tile3);
        commTiles.add(tile4);
        commTiles.add(tile5);
        commTiles.add(tile6);
        //player.getHand().addCommunityTiles(commTiles);
        playerHand.addCommunityTiles(commTiles);

        player.setHand(playerHand);
    }

    @Test
    public void testCheckWord() {
        boolean check1 = player.checkWord("nascar");
        assertEquals(true, check1);

        boolean check2 = player.checkWord("house");
        assertEquals(false, check2);

        boolean check3 = player.checkWord("aaaaa");
        assertEquals(false, check3);

        boolean check4 = player.checkWord("NasCAr");
        assertEquals(true, check4);

        boolean check5 = player.checkWord("N a s C A r");
        assertEquals(true, check5);
    }
}
