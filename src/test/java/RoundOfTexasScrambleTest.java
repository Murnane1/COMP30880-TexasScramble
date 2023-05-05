import CardSharps.TexasScramble.*;


import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class RoundOfTexasScrambleTest {

    List<Tile> playerTiles = new ArrayList<>();
    List<Tile> commTiles = new ArrayList<>();
    BagOfTiles bag = new BagOfTiles("ENGLISH");
    ScrambleHand meHand;
    ScrabbleDictionary dictionary;
    Player me = new HumanScramblePlayer("me", 10);
    Player you = new ComputerScramblePlayer("you", 10);

    private Player player1 = new HumanScramblePlayer("p1",5);
    private Player player2 = new HumanScramblePlayer("p2",4);
    private Player player3 = new HumanScramblePlayer("p3",3);
    private Player player4 = new HumanScramblePlayer("p4",4);
    private Player player5 = new HumanScramblePlayer("p5", 1);

    private Player player6 = new HumanScramblePlayer("p6", 3);

    private Player[] players = {player1,player2,player3,player4,player5, player6};

    RoundOfTexasScramble round;

    ArrayList<Player> ALplayers = new ArrayList<>(List.of(players));
    PotOfMoney pot = new PotOfMoney(ALplayers);
    ArrayList<PotOfMoney> pots = new ArrayList<>();



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

        ArrayList<Player> listPlayers = new ArrayList<>(Arrays.asList(players));
        PotOfMoney mainPot = new PotOfMoney(listPlayers);
        pots.add(mainPot);
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
        assertEquals(0, pot.getTotal());


        me.setWord("XCXYZ");
        round.challenge(me, you, pot);

        assertEquals(0, me.getWordScore());
        assertEquals(10, you.getBank());
        assertEquals(0, pot.getTotal());

    }

    @Test
    public void testNewSidePot() {
        //1st cycle
        PotOfMoney pot = pots.get(0);
        player1.raiseBet(pot, 1);
        player2.seeBet(pot);
        player3.seeBet(pot);
        player4.seeBet(pot);
        player5.seeBet(pot);
        player6.seeBet(pot);
        System.out.println("pot0 total: " + pots.get(0).getTotal());
        assertEquals(1, pots.get(0).getCurrentStake());
        assertEquals(6, pots.get(0).getTotal());

        //2nd cycle
        player1.raiseBet(pot, 1);
        player2.seeBet(pot);
        player3.seeBet(pot);
        player4.seeBet(pot);
        System.out.println("pot0 total: " + pots.get(0).getTotal());
        player5.allIn(pots.get(0));
        System.out.println("pot0 total: " + pots.get(0).getTotal());
        //System.out.println("pot1 total: " + pots.get(1).getTotal());
        player6.fold(pot);

        //3rd cycle
        player1.raiseBet(pot,1);
        player2.seeBet(pot);
        player3.seeBet(pot);
        player4.seeBet(pot);

        pots = round.newSidePots(pots.get(0));
        for (PotOfMoney aPot:pots) {
            System.out.println(aPot.getTotal() + " total|players " + aPot.getNumPlayers());
        }
        System.out.println("TEST tot size: "+pots.size());

        assertEquals(6, pots.get(0).getTotal());
        assertEquals(8, pots.get(1).getTotal());

    }

    @Test
    public void testAnotherNewSidePot() {
        PotOfMoney pot = pots.get(0);

        //1st cycle - stake 1
        player1.raiseBet(pot,1);  //1
        player2.seeBet(pot);    //1
        player3.seeBet(pot);
        player4.seeBet(pot);
        //player5.seeBet(pot);
        player5.allIn(pot);

        //2nd cycle - stake2
        player1.raiseBet(pot,1);
        player2.seeBet(pot);
        player3.seeBet(pot);
        player4.seeBet(pot);
        //player5.allIn(pot);      //1

        //3rd cycle - stake 3
        player1.raiseBet(pot,1);
        player2.seeBet(pot);
        player3.allIn(pot);
        player4.seeBet(pot);

        //4th cycle - stake 4
        player1.raiseBet(pot,1);
        player2.seeBet(pot);
        player4.seeBet(pot);

        //5th cycle - stake 5
        player1.raiseBet(pot,1);

        pots = round.newSidePots(pots.get(0));

        System.out.println("Num pots: " + pots.size());
        assertEquals(5, pots.get(0).getTotal());
        assertEquals(8, pots.get(1).getTotal());

        assertEquals(3, pots.get(2).getNumPlayers());
        assertEquals(1, pots.get(3).getNumPlayers());

        assertEquals(4, pots.size());
    }

    @Test
    public void testSameValueAllIns(){
        PotOfMoney newPot = pots.get(0);

        player1.raiseBet(newPot,1);
        player3.seeBet(newPot);
        player6.seeBet(newPot);     //stake = 1

        player1.raiseBet(newPot,1);
        player3.seeBet(newPot);
        player6.seeBet(newPot);     //stake = 2

        player1.raiseBet(newPot,1);
        player3.allIn(newPot);

        player1.raiseBet(newPot,1);     //stake = 4

        player6.allIn(newPot);     //stake = 3


        pots.clear();
        pots = round.newSidePots(newPot);

        assertEquals(9, pots.get(0).getTotal());
        assertEquals(1, pots.get(1).getTotal());
        assertEquals(2, pots.size());
    }

    @Test
    public void testFolding(){
        PotOfMoney newPot = pots.get(0);

        player1.raiseBet(newPot,1);
        player3.seeBet(newPot);
        player6.seeBet(newPot);     //pot = 3

        player1.raiseBet(newPot,1);
        player3.fold(newPot);
        player6.seeBet(newPot);     //pot = 5

        player1.raiseBet(newPot,1);     //pot = 6
        player6.fold(newPot);

        pots.clear();
        pots = round.newSidePots(newPot);

        assertEquals(6, pots.get(0).getTotal());
        assertEquals(1, pots.size());
    }
}