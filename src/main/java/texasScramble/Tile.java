package texasScramble;

public class Tile {
    private char letter;
    private int value;


    public Tile(char letter, int value){
        this.letter = letter;
        this.value = value;
    }

    public char getLetter() {
        return letter;
    }

    public int getValue() {
        return value;
    }
    public String toString(){
        return letter + "";
    }
}
