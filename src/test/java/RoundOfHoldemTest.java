import CardSharps.TexasHoldem.ComputerHoldemPlayer;
import CardSharps.TexasHoldem.HumanHoldemPlayer;
import CardSharps.TexasHoldem.PlayerInterface;
import CardSharps.TexasHoldem.PotTexasHoldem;
import CardSharps.TexasHoldem.RoundOfTexasHoldem;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import CardSharps.Poker.*;
import java.util.*;

public class RoundOfHoldemTest {


    private int numPlayers = 5;
    private HumanHoldemPlayer player1 = new HumanHoldemPlayer("p1",5);
    private ComputerHoldemPlayer player2 = new ComputerHoldemPlayer("p2",4);
    private ComputerHoldemPlayer player3 = new ComputerHoldemPlayer("p3",3);
    private ComputerHoldemPlayer player4 = new ComputerHoldemPlayer("p4",4);
    private ComputerHoldemPlayer player5 = new ComputerHoldemPlayer("p5",1);

    private PlayerInterface[] players = {player1,player2,player3,player4,player5};

    private RoundOfTexasHoldem round = new RoundOfTexasHoldem(new DeckOfCards(), players, 1, 0);

    ArrayList<PotTexasHoldem> pots = new ArrayList<PotTexasHoldem>();

    @Before
    public void setUp() {
        ArrayList<PlayerInterface> listPlayers = new ArrayList<>(Arrays.asList(players));
        PotTexasHoldem mainPot = new PotTexasHoldem(listPlayers);
        pots.add(mainPot);
        //RoundOfTexasHoldem round = new RoundOfTexasHoldem(new DeckOfCards(), players);
    }

    @Test
    public void testAllIn() {
        round.setPots(pots);

        //1st cycle
        player1.raiseBet(pots, 0);
        player2.seeBet(pots, 0);
        player3.seeBet(pots,0);
        player4.seeBet(pots, 0);
        player5.seeBet(pots,0);
        System.out.println("pot0 total: " + pots.get(0).getTotal());
        assertEquals(1, pots.get(0).getCurrentStake());
        assertEquals(5, pots.get(0).getTotal());

        //2nd cycle
        player1.raiseBet(pots, 0);
        player2.seeBet(pots,0);
        player3.seeBet(pots,0);
        player4.seeBet(pots,0);
        System.out.println("pot0 total: " + pots.get(0).getTotal());
        player5.allIn(pots.get(0));
        round.newSidePot(player5, 0);
        System.out.println("pot0 total: " + pots.get(0).getTotal());
        System.out.println("pot1 total: " + pots.get(1).getTotal());

        assertEquals(1, pots.get(0).getMaxStake());
        assertEquals(5,  pots.get(0).getTotal());
        assertEquals(4,  pots.get(1).getTotal());

        //3rd cycle
        player1.raiseBet(pots, 1);
        player2.seeBet(pots, 1);
        player3.seeBet(pots, 1);
        player4.seeBet(pots, 1);

        assertEquals(3, pots.get(1).getCurrentStake());
        assertEquals(5, pots.get(0).getTotal());
        assertEquals(8, pots.get(1).getTotal());

        //4th cycle
        player1.raiseBet(pots, 1);
        player2.seeBet(pots, 1);
        //player3.nextAction(pots, 1);
        player3.allIn(pots.get(0));
        assertTrue(player3.isAllIn());
        round.addSidePot(player3, 1);
        player4.seeBet(pots, 1);

        assertEquals(4, pots.get(2).getCurrentStake());
        assertEquals(3, pots.get(2).getTotal());
        assertEquals(8, pots.get(1).getTotal());

        //5th cycle
        player1.raiseBet(pots, 2);
        player2.allIn(pots.get(2));
        round.addSidePot(player2, 2);
        player4.allIn(pots.get(2));

        //round.bettingCycle(2,1,0);
        round.newSidePot(player2, 0);


        System.out.println("-----");
        for(int i=0;i<round.getPots().size();i++){
            System.out.println(" Pot"+i+": " + round.getPots().get(i).getTotal());
        }
        System.out.println("-----");

    }

    @Test
    public void testNewSidePot() {
        round.setPots(pots);
        assertEquals(5, pots.get(0).getNumPlayers());
        round.newSidePot(player4, 0);
        assertEquals(5, pots.get(0).getNumPlayers());
        assertEquals(4, pots.get(1).getNumPlayers());
    }

    @Test
    public void testAddSidePot() {
        round.setPots(pots);
        assertEquals(pots.get(0).getNumPlayers(), 5);
        round.addSidePot(player4, 0);
        assertEquals(pots.get(0).getNumPlayers(), 4);
        round.addSidePot(player2, 0);
        assertEquals(pots.get(0).getNumPlayers(), 5);
    }
}