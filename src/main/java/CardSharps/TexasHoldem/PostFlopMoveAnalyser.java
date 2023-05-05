package CardSharps.TexasHoldem;

public class PostFlopMoveAnalyser {
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

    PostFlopHandAnalyser handAnalyser;

    private boolean playerHasBet = false;

    private boolean playerHasChecked = false;

    private boolean opponentHasBet = false;

    private boolean opponentHasRaisedAfterPlayer = false;

    private int numberOfPlayers;

    private int amountToBet = 0;

    private int playerFunds;

    private int potSize;

    private int opponentsBet;

    private gameStage stage = gameStage.FLOP;

    private holdemMoves nextMove = holdemMoves.CALL;

    public PostFlopMoveAnalyser(HoldemHand hand, int numberOfPlayers, int playerFunds, int potSize, int opponentsBet) {
        this.handAnalyser = new PostFlopHandAnalyser(hand);
        this.numberOfPlayers = numberOfPlayers;
    }

    public holdemMoves getNextMove() {
        return nextMove;
    }   //returning the analysed move (call decideNextMove() first)

    public int getAmountToBet () {
        return amountToBet;
    }

    public void decideNextMove() {          // post-flop hand analyser
        if(stage == gameStage.FLOP) {
            if(handAnalyser.isDry() && numberOfPlayers == 2) {
                if(handAnalyser.isMonsterHand() || handAnalyser.isOverPair() || handAnalyser.isMonsterDraw()) {
                    if(opponentHasBet) {
                        nextMove = holdemMoves.RAISE;
                    }
                    else {
                        nextMove = holdemMoves.BET;
                    }
                }
                else {
                    if(opponentHasBet) {
                        nextMove = holdemMoves.FOLD;
                    }
                    else {
                        nextMove = holdemMoves.CONTINUATIONBET;
                    }
                }
            }
            else if(handAnalyser.isDrawy() || numberOfPlayers > 2) {
                if(handAnalyser.isMonsterHand() || handAnalyser.isOverPair() || handAnalyser.isMonsterDraw()) {
                    if(opponentHasBet) {
                        nextMove = holdemMoves.RAISE;
                    }
                    else {
                        nextMove = holdemMoves.BET;
                    }
                }
                else if(handAnalyser.isGutshot() || handAnalyser.isOverCard() || handAnalyser.isTrash()) {
                    if(opponentHasBet) {
                        nextMove = holdemMoves.FOLD;
                    }
                    else {
                        nextMove = holdemMoves.CHECK;
                    }
                }
                else {
                    if(opponentHasBet) {
                        nextMove = holdemMoves.FOLD;
                    }
                    else {
                        nextMove = holdemMoves.CONTINUATIONBET;
                    }
                }
            }
        }
        else if(stage == gameStage.TURN) {
            if(handAnalyser.isMonsterHand() || handAnalyser.isOverPair() || handAnalyser.isMonsterDraw()) {
                if(opponentHasRaisedAfterPlayer) {
                    nextMove = holdemMoves.ALLIN;
                }
                else if(opponentHasBet) {
                    nextMove = holdemMoves.RAISE;
                }
                else {
                    nextMove = holdemMoves.BET;
                }
            }
            else if(handAnalyser.isTopPair()) {
                if(opponentHasRaisedAfterPlayer || opponentHasBet) {
                    nextMove = holdemMoves.FOLD;
                }
                else {
                    nextMove = holdemMoves.CONTINUATIONBET;
                }
            }
            else {
                if(opponentHasBet) {
                    nextMove = holdemMoves.FOLD;
                }
                else {
                    nextMove = holdemMoves.CHECK;
                }
            }
        }
        else if(stage == gameStage.RIVER) {
            if(playerHasBet) {
                if(handAnalyser.isMonsterHand() || handAnalyser.isOverPair() || handAnalyser.isTopPair()) {
                    if(opponentHasRaisedAfterPlayer) {
                        nextMove = holdemMoves.ALLIN;
                    }
                    else if(opponentHasBet) {
                        nextMove = holdemMoves.RAISE;
                    }
                    else {
                        nextMove = holdemMoves.BET;
                    }
                }
                else{
                    if(opponentHasBet) {
                        nextMove = holdemMoves.FOLD;
                    }
                    else {
                        nextMove = holdemMoves.CHECK;
                    }
                }
            }
            else if(playerHasChecked) {
                if(handAnalyser.isMonsterHand()) {
                    if(opponentHasRaisedAfterPlayer) {
                        nextMove = holdemMoves.ALLIN;
                    }
                    else if(opponentHasBet) {
                        nextMove = holdemMoves.RAISE;
                    }
                    else {
                        nextMove = holdemMoves.BET;
                    }
                }
                else if(handAnalyser.isTopPair()) {
                    if(opponentHasRaisedAfterPlayer || opponentHasBet) {
                        nextMove = holdemMoves.CALL;
                    }
                }
                else {
                    if(opponentHasBet) {
                        nextMove = holdemMoves.FOLD;
                    }
                    else {
                        nextMove = holdemMoves.CHECK;
                    }
                }
            }
        }
    }


    //TODO write simplified betting as currently oinly raise by 1 available
    public void decideBettingAmount() {
        if(nextMove == holdemMoves.CONTINUATIONBET) {
            if(handAnalyser.isDry() && numberOfPlayers == 2) {
                amountToBet = potSize / 2;
            }
            else {
                amountToBet = (potSize / 4) * 3;
            }
        }
        else if(nextMove == holdemMoves.RAISE) {
            amountToBet = opponentsBet * 3;
        }
        else if(nextMove == holdemMoves.BET) {
            /*Here bluffing is handled for weak hands and card combinations
                The same metric for bet calculations is used as for continuation bet
             */
            if(handAnalyser.isWeakTwoPair() || !(handAnalyser.isMonsterHand()) || handAnalyser.isTrash() || handAnalyser.isGutshot() || handAnalyser.isOverCard()) {
                if(handAnalyser.isDry() && numberOfPlayers == 2) {
                    amountToBet = potSize / 2;
                }
                else {
                    amountToBet = (potSize / 4) * 3;
                }
            }
            else {
                amountToBet = (potSize / 4) * 3;
            }
        }
        else if(nextMove == holdemMoves.ALLIN) {
            amountToBet = playerFunds;
        }
        else if(nextMove == holdemMoves.CALL) {
            amountToBet = opponentsBet;
        }
    }
}
