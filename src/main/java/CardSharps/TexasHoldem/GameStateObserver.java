package CardSharps.TexasHoldem;


//game state observer exists as globally accessible singleton class so AI players can check the state of the game
public class GameStateObserver {

    private static GameStateObserver gameObserver = null;

    private enum gameStage {
        PREFLOP,
        FLOP,
        TURN,
        RIVER
    }

    private enum holdemMoves {
        BET,
        CHECK,
        FOLD,
        CONTINUATIONBET,
        CALL,
        ALLIN,
        RAISE,
    }

    private gameStage stage = gameStage.PREFLOP;

    private GameStateObserver() {
        //private constructor as per singleton class design pattern
    }

    //static method controlling access to singleton
    public static GameStateObserver GameStateObserver() {
        if(gameObserver == null) {
            gameObserver = new GameStateObserver();
        }

        return gameObserver;
    }

    public void advanceGameStage() {
        if(stage == gameStage.PREFLOP) {
            stage = gameStage.FLOP;
        }
        else if(stage == gameStage.FLOP) {
            stage = gameStage.TURN;
        }
        else if(stage == gameStage.TURN) {
            stage = gameStage.RIVER;
        }
        else {
            stage = gameStage.PREFLOP;
        }
    }
}
