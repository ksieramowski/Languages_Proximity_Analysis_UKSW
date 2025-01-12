package pl.zespolowy.Algorithm;

import lombok.Getter;
import lombok.Setter;
import pl.zespolowy.Language.Language;
import pl.zespolowy.Words.Word;
import pl.zespolowy.Words.WordSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class WordSetsRegrouper {
    private final WordSetsTranslation wordSetsTranslation;
    Map<String, Set<Map<Language, Word>>> regruopedMap = new HashMap<>();

    public WordSetsRegrouper(WordSetsTranslation wordSetsTranslation) {
        this.wordSetsTranslation = wordSetsTranslation;
        convertToRegroupedMap();
    }

    public void convertToRegroupedMap() {
        regruopedMap = wordSetsTranslation.getSetMapOfKeyStringAndValueMapLanguageWordSet().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        v -> regroupGivenMapsToSetOfMapsOfLanguageAndWords(v.getValue())
                ));
    }
    /**
     *
     */
    public Set<Map<Language, Word>> regroupGivenMapsToSetOfMapsOfLanguageAndWords(Map<Language, WordSet> wordSetsInDifferentLanguages) {
        Set<Map<Language, Word>> mapSet = new HashSet<>();
        int size = findSmallestWordSet(wordSetsInDifferentLanguages);
        // przechodzę po każdym wordsecie w różnych językach i biorę np. pierwsze słowo z każdego języka czyli to samo słowo i robie z tego mape jezyk słowo w tym jezyku
        for (int i = 0; i < size; i++) {
            int finalI = i;
            Map<Language, Word> languageWordMap = getLanguageWordMap(wordSetsInDifferentLanguages, finalI);
            mapSet.add(languageWordMap);
        }
        return mapSet;
    }

    private static int findSmallestWordSet(Map<Language, WordSet> wordSetsInDifferentLanguages) {
        int leastLong = 100;
        for (var values : wordSetsInDifferentLanguages.values()) {
            if (values.getWords().size()-1 < leastLong) {
                leastLong = values.getWords().size()-1;
            }
        }

        return leastLong;
    }

    /**
     *
     * @param wordSetsInDifferentLanguages
     * @param finalI
     * @return
     */
    private static Map<Language, Word> getLanguageWordMap(Map<Language, WordSet> wordSetsInDifferentLanguages, int finalI) {
        Map<Language, Word> languageWordMap = wordSetsInDifferentLanguages.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().getWords().get(finalI)
                ));
        return languageWordMap;
    }
}
