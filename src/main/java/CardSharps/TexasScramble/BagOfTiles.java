package CardSharps.TexasScramble;

import java.util.Random;

public class BagOfTiles {
    //ENGLISH
    private int numTiles;
    private Tile[] bag;

    private int next = 0;

    private final Random dice = new Random(System.currentTimeMillis());


    public BagOfTiles(String language){ //language arg?
        if(language.equals("ENGLISH")){
            numTiles = 98;
            bag = new Tile[numTiles];
            addTiles('A', 1, 9);
            addTiles('B', 3, 2);
            addTiles('C', 3, 2);
            addTiles('D', 2, 4);
            addTiles('E', 1, 12);
            addTiles('F', 4, 2);
            addTiles('G', 2, 3);
            addTiles('H', 4, 2);
            addTiles('I', 1, 9);
            addTiles('J', 8, 1);
            addTiles('K', 5, 1);
            addTiles('L', 5, 4);
            addTiles('M', 1, 2);
            addTiles('N', 3, 6);
            addTiles('O', 1, 8);
            addTiles('P', 1, 2);
            addTiles('Q', 3, 1);
            addTiles('R', 10, 6);
            addTiles('S', 1, 4);
            addTiles('T', 1, 6);
            addTiles('U', 1, 4);
            addTiles('V', 1, 2);
            addTiles('W', 4, 2);
            addTiles('X', 4, 1);
            addTiles('Y', 8, 2);
            addTiles('Z', 4, 1);
        }
        else if (language.equals("FRENCH")) {
            numTiles = 100;
            bag = new Tile[numTiles];
            addTiles('E', 1, 15);
            addTiles('A', 1, 9);
            addTiles('I', 1, 8);
            addTiles('N', 1, 6);
            addTiles('O', 1, 6);
            addTiles('R', 1, 6);
            addTiles('S', 1, 6);
            addTiles('T', 1, 6);
            addTiles('U', 1, 6);
            addTiles('L', 1, 5);
            addTiles('D', 2, 3);
            addTiles('M', 2, 3);
            addTiles('G', 2, 2);
            addTiles('B', 3, 2);
            addTiles('C', 3, 2);
            addTiles('P', 3, 2);
            addTiles('F', 4, 2);
            addTiles('H', 4, 2);
            addTiles('V', 4, 2);
            addTiles('J', 8, 1);
            addTiles('Q', 8, 1);
            addTiles('K', 10, 1);
            addTiles('W', 10, 1);
            addTiles('X', 10, 1);
            addTiles('Y', 10, 1);
            addTiles('Z', 10, 1);
        }
        reset();
    }

    public void reset(){
        next = 0;
        shuffle();
    }

    public void shuffle(){
        Tile palm = null;

        int alpha = 0, beta = 0;

        for (int i = 0; i < numTiles*numTiles; i++) {
            alpha       = Math.abs(dice.nextInt())%numTiles;
            beta        = Math.abs(dice.nextInt())%numTiles;

            palm        = bag[alpha];
            bag[alpha] = bag[beta];
            bag[beta]  = palm;
        }
    }

    public Tile dealNext() {
        if (next >= numTiles)
            return new Tile('0', 0);  // bag is empty
        else
            return bag[next++];
    }

    public String toString(){
        return "Tiles Left in Bag: " + (numTiles-next);
    }

    private void addTiles( char letter, int value, int num){
        for(int i = 0; i < num; i++){
            bag[next++] = new Tile(letter, value);
        }
    }

    public Tile[] getBag(){
        return bag;
    }

    public int getNumtiles(){
        return numTiles;
    }

    public ScrambleHand dealHand(ScrabbleDictionary dictionary) {
        return new ScrambleHand(this, dictionary);
    }
}