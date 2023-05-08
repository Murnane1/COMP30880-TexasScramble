# COMP30880-TexasScramble by Card Sharps

## An overview of the noteworthy aspects of our game

### Ronan
 - Trie Strucutre and Dictionary parsing
 - Algorithm to generate all possible words and validating their existance in the dictionary
 - Generating potential words from given player hand
 - Fixed bugs and formatting of Blackjack
 - Merged all game files into singular package with GameHandler to determine which game to play
 
### Conor
 - Fixed side pots, if players tied the pot is split amongst them. If not evenly divisible the remainder goes to the house
 - Redid betting structure, players not able to meet big blind for next round are removed permenately from game
 - Each player decides their word, then go round the table starting from player with button
 - Persistant players, random selection of a user specified number of predefined players
 - Only players with active bets declare words. If only one player left they take the pot without declaring their word.
 - If multiple players still left at the word reveal. First they each choose their word. Kept secret from the other players. Then starting from the button token. Each player going left around the table has an opportunity to challenge.
 - In challenge, If the word is invalid the words score is set to 0. Otherwise if the word is valid the challenger pays a penalty into the pot. They could win this back but there would be no point in challenging a word with a lower score than their own.
 - A player is allowed to go into debt to challenge a word. This is because otherwise there would be no way for richer players to be challenged once they forced their opposition all in.
 - The small blind increases by 25% rounded down each round. Starting with a small blind of 5 and a big blind of 10. To gradually reduce the number of players.
 - A word frequency list was used to assign values to the most used words from the dictionary using Peter Norvig's 1/3 million most frequent words (https://norvig.com/ngrams/)
 - The scrabble dictionaries were found through various sources. Using the SOWPODS wordlist for English and ODS6 for French
 - Most computer players choose words based on the frequency dictionary. Each word in the dictionary found in the frequency list assigned a value. If the player had a word knowledge above this frequency they can use the word
 - A second type of computer player "copycat" that makes its decisions based on other players in the rounds actions
 - Human players input their word. The word is checked to make sure the player has each tile they are using including enough of each letter used.
 - The players word does not need to be valid, that is the purpose of challenging and gives another chance to bluff.
 - The ability to choose the amount to raise by has been added in. Players are warned if they raise up to their all in amount and are asked to confirm they are sure
 - The computer player challenges more as the word value increases with 3 levels of suspicion for each word length.
 - The computer players see, raise and fold based on several factors. There include their risk tolerance, the ratio of other players & their own stake:bank, the quality of hand, the number of players still betting and how much they need to raise by. Along with a random element.
 - Fixed the Holdem game, allowing it to play through in the same way as Scramble. The opposition players are the same with the same characteristics in both games
 - Reconfigured the codebase so Texas Scramble was built off Texas Holdem

### Ryan
- Created the fundamentals of AI word invention and detection, explained in the AI bluffing package



### Prerequisites

Requirements for the software and other tools to build, test and push 
- Java 18
- Maven


## Authors

- Ryan Daly                    16315911
- Conor Murnane                16305716
- Dhruv Gupta                  20200897
- Ronan O'Brien                20404302
