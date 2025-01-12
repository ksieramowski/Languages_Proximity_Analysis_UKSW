package pl.zespolowy.Algorithm;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.text.similarity.LevenshteinDistance;
import pl.zespolowy.Language.Language;
import pl.zespolowy.Words.Word;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public final class LanguageSimilarityCalculator {
    private LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
    private WordSetsRegrouper wordSetsRegrouper;
    private Map<String, Map<String, LanguageProximityResult>> proximityBetweenTwoLanguagesMapByTopic = new HashMap<>();

    public LanguageSimilarityCalculator(WordSetsRegrouper wordSetsRegrouper) {
        this.wordSetsRegrouper = wordSetsRegrouper;
    }

    /**
     * obliczenie podobienstwa algorytmem levensteina pomiedzy slowem w roznych jezykach
     *
     */
    public void countingProximityForWordInDifferentLanguagesAndPuttingResultToLanguageProximityResult() {
        // mapa (ktora przechowuje 1 set (podzielony na rozne slowa) map ktore maja pary jezyk słowo w tym jezyku (to samo slowo)) pogrupowana po tematach
        var mapOfTopics = wordSetsRegrouper.getRegruopedMap();
        Map<String, LanguageProximityResult> proximityBetweenTwoLanguagesMap;

        for (var map : mapOfTopics.entrySet()) {
            proximityBetweenTwoLanguagesMap = makeSetOfProximityBetweenTwoLanguages(wordSetsRegrouper.getWordSetsTranslation().getLanguageList());
            for (var set : map.getValue()) {
                loopThroughMaps(set, proximityBetweenTwoLanguagesMap);
            }
            proximityBetweenTwoLanguagesMapByTopic.put(map.getKey(), proximityBetweenTwoLanguagesMap);
        }
    }

    /**
     *
     * @param set
     * @param proximityBetweenTwoLanguagesMap
     */
    private void loopThroughMaps(Map<Language, Word> set, Map<String, LanguageProximityResult> proximityBetweenTwoLanguagesMap) {
        for (Map.Entry<Language, Word> outerEntry : set.entrySet()) {
            Language outerKey = outerEntry.getKey();
            Word outerValue = outerEntry.getValue();

            var innerIterator = set.entrySet().iterator();
            // pomija elementy do outerEntry
            while (innerIterator.hasNext() && !innerIterator.next().equals(outerEntry)) {}

            while (innerIterator.hasNext()) {
                var innerEntry = innerIterator.next();
                Language innerKey = innerEntry.getKey();
                Word innerValue = innerEntry.getValue();
                // obliczenie podobienstwa
                Integer countedDistance = levenshteinDistance.apply(outerValue.getText(), innerValue.getText());
                putResultToLanguageProximityResult(outerKey, innerKey, proximityBetweenTwoLanguagesMap, countedDistance);
            }
        }
    }

    /**
     *
     * @param outerKey
     * @param innerKey
     * @param proximityBetweenTwoLanguagesMap
     * @param countedDistance
     */
    private static void putResultToLanguageProximityResult(Language outerKey, Language innerKey, Map<String, LanguageProximityResult> proximityBetweenTwoLanguagesMap, Integer countedDistance) {
        String languagesAbbreviation = outerKey.getCode() + innerKey.getCode();
        String languagesAbbreviationReversed = innerKey.getCode() + outerKey.getCode();

        if (proximityBetweenTwoLanguagesMap.containsKey(languagesAbbreviation)) {
            proximityBetweenTwoLanguagesMap.get(languagesAbbreviation).countedProximityAndNumberOfWordsToNormalizationIncrease(countedDistance, 1);
        } else if (proximityBetweenTwoLanguagesMap.containsKey(languagesAbbreviationReversed)) {
            proximityBetweenTwoLanguagesMap.get(languagesAbbreviationReversed).countedProximityAndNumberOfWordsToNormalizationIncrease(countedDistance, 1);
        } else {
            System.out.println("LanguageProximityError: Abbreviation not found, line: 68");
        }
    }

    /**
     *
     * @return Set<ProximityBetweenTwoLanguages>
     */
    public Map<String, LanguageProximityResult> makeSetOfProximityBetweenTwoLanguages(List<Language> languages) {
        Map<String, LanguageProximityResult> map = new HashMap<>();
        for (int i = 0; i < languages.size(); i++) {
            for (int j = i + 1; j < languages.size(); j++) {
                map.put(languages.get(i).getCode() + languages.get(j).getCode(), new LanguageProximityResult(languages.get(i), languages.get(j)));
            }
        }
        return map;
    }
}

