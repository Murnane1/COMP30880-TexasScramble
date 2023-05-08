package CardSharps.TexasScramble.AIBluffing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


//The regexes and the associated algorithms were made with the assistance of chatgpt

public class AIBluffer {

    //regular expression to check to see if a word fits the phonotactic rules of the english language
    String pewgex = "^(?:(?:[bcdfghjklmnpqrstvwxyz](?!h)(?!x))?(?:(?:[aeiouy][bcdfghjklmnpqrstvwxyz])|(?:[bcdfghjklmnpqrstvwxyz][aeiouy]))(?:[bcdfghjklmnpqrstvwxz](?!h)(?!x))?)|(?:[aeiouy](?!h)(?!x))(?:(?:[bcdfghjklmnpqrstvwxyz](?!h)(?!x))|(?:[aeiouy](?!h)(?!x)))?(?:(?:[tdl](?![lr]))?[bcdfghjklmnpqrstvwxyz](?!h)(?!x))?(?:(?:(?:[bcdfghjklmnpqrstvwxyz](?:(?<=[aeiouy])[aeiouy][bcdfghjklmnpqrstvwxyz])|(?:[bcdfghjklmnpqrstvwxyz](?<=[aeiouy])[aeiouy][bcdfghjklmnpqrstvwxyz]?[bcdfghjklmnpqrstvwxyz]))(?:(?<=[aeiouy])[aeiouy](?:(?<=[aeiouy])[bcdfghjklmnpqrstvwxyz]|[tdl](?![lr]))?)?|(?:(?<=[aeiouy])(?:(?:(?<=[bcdfghjklmnpqrstvwxyz])[bcdfghjklmnpqrstvwxyz])?[tdl](?![lr]))?(?<=[tdl])(?:(?:(?<=[aeiouy])[aeiouy])|(?:[bcdfghjklmnpqrstvwxyz](?<=[aeiouy])[aeiouy][bcdfghjklmnpqrstvwxyz]?[bcdfghjklmnpqrstvwxyz](?:(?<=[aeiouy])[aeiouy])?))|(?:(?<=[b-df-hj-np-tv-z])[aeiouy](?:(?<=^[b-df-hj-np-tv-z])[b-df-hj-np-tv-z])?(?<!^(a|e|i|o|u|y)))|(^(a|e|i|o|u|y))|(^(b|c|d|f|g|h|j|k|l|m|n|p|q|r|s|t|v|w|x|y|z)[aeiouy]))(?:n't|'ve|'s|s'|'re|'ll|'d)?\\b))";


    // Define regular expressions for unusual letter combinations
    private static final String PEW_REGEX = ".*[pP][eE][wW].*";
    private static final String QZ_REGEX = ".*[qQ][zZ].*";
    private static final String JKX_REGEX = ".*[jJkKxX].*";
    private static final String CVBDFGMPQU_REGEX = ".*[cCvVbBdDfFgGmMpPqQ][cCvVbBdDfFgGmMpPqQ][uU].*";
    private static final String WXYZ_REGEX = ".*[wWxXyYzZ].*";
    private static final String HYPHEN_REGEX = ".*[-].*";

    // Define unusualness score thresholds for each regular expression
    private static final int PEW_SCORE = 1;
    private static final int QZ_SCORE = 2;
    private static final int JKX_SCORE = 3;
    private static final int CVBDFGMPQU_SCORE = 4;
    private static final int WXYZ_SCORE = 5;
    private static final int HYPHEN_SCORE = 1;

    // Define the minimum score required for a word to be considered unusual
    private static final int MIN_UNUSUALNESS_SCORE = 3;


    public AIBluffer() {

    }

    public Boolean isValidWord(String word) {
        return word.matches(pewgex);
    }


/*  The following is what exists of a not-yet working algorithm to determine how unusual a word seems.
    Each word is given a score based on the presence of certain unusual combinations of letter and various
    other factors.

   A score above 3 would indicate a reasonably unusual word, while any score above 4 indicates a word
   that seems highly unusual.

   This system would be used in tandem with the phonotactic regex to be able to successfully bluff without
   drawing too much attention, and to even detect suspicious bluffs of other players even if the words
   are seemingly valid phonotactically, being able to place such words on a scale to better analyse the risk
   of calling out such a bluff.

   This was extensively testing using chatgpt and was able to successfully estimate the unusualness of hundreds
   of real and fake words that would all pass the pewgex test.

   For example, "house" rates quite low but a word like "chthonic" would rate reasonably high.



        // Remove all non-alphabetic characters
        String cleanWord = word.replaceAll("[^a-zA-Z]", "");

        // If the word is empty after cleaning, return a score of 0
    if (cleanWord.isEmpty()) {
            return 0;
        }

        // Convert the word to lowercase for consistency
        cleanWord = cleanWord.toLowerCase();

        // Initialize a HashMap to store the count of each letter combination
        Map<String, Integer> combinationCount = new HashMap<>();

        // Iterate through each pair of adjacent letters in the word
    for (int i = 0; i < cleanWord.length() - 1; i++) {
            String combination = cleanWord.substring(i, i + 2);

            // If the combination is not already in the map, add it with a count of 1
            if (!combinationCount.containsKey(combination)) {
                combinationCount.put(combination, 1);
            }


            for (int i = 0; i < word.length() - 1; i++) {
                String pair = word.substring(i, i + 2);
                if (pair.matches(pewgex))
                    continue;
                if (pair.matches(unusualRegex))
                    score += 1.5;
            }

            // Check for unusual triplets
            for (int i = 0; i < word.length() - 2; i++) {
                String triplet = word.substring(i, i + 3);
                if (triplet.matches(pewgex))
                    continue;
                if (triplet.matches(unusualRegex))
                    score += 2.0;
            }


            public static double calculateUnusualnessScore(String word) {
                String[] unusualCombinations = getUnusualCombinations(word);
                double score = 0.0;
                double weight = 1.0;
                double sumOfWeights = 0.0;

                for (String combination : unusualCombinations) {
                    double combinationWeight = getCombinationWeight(combination);
                    sumOfWeights += combinationWeight;
                }

                if (sumOfWeights == 0) {
                    return 0.0;
                }

                for (String combination : unusualCombinations) {
                    double combinationWeight = getCombinationWeight(combination);
                    weight = combinationWeight / sumOfWeights;
                    score += weight * getCombinationFrequency(combination);
                }
                return score;
            }
            */


}

/*
The pewgex (phonetic english word regex) was generated and designed based on the principles of english phonotactics and
orthography according to the general principles outlined below:

While the current version does not work java, it was extensively tested using chat gpt to be able to reliably
check the correctness of words and even be used to generate convincing looking english words.
This would provide a firm base to work on creating an AI that is able to bluff fake words and even detect the
bluffs of other players.


Syllable limitations: *repeat steps again for following syllables

CCVCCCC

Onset + Nucleus + Coda

----------------------------------------------------------
Onset

- null
- limited_s or (limited_stop [eg no voiced stops] or null) or (limited_approximate or null)
- any non-final set (eg no "ng")

Nucleus

- any vowel set
- any final set incl. vowel (for simplicity)


Coda

- null
- any letter
- any valid combination that makes single sound excl. vowel

For complex codas:
1 : null, any single sound?
2 : null, any?, not be /r/, /ŋ/, /ʒ/, or /ð/
3 : null, any?
4 : null or s
*be wary of sonorance hierarchy

**silent e

**The second consonant in a complex coda must not be /r/, /ŋ/, /ʒ/, or /ð/
**If the second consonant in a complex coda is voiced, so is the first
**An obstruent following /m/ or /ŋ/ in a coda must be homorganic with the nasal
**Two obstruents in the same coda must share voicing (compare kids /kɪdz/ with kits /kɪts/)


----------------------------------------------------------


 */


