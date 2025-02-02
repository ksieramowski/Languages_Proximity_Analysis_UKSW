package pl.zespolowy.Business.Algorithm;

import lombok.Getter;
import lombok.Setter;
import pl.zespolowy.Language.Language;
import pl.zespolowy.Language.LanguageSet;
import pl.zespolowy.Translation.DeepLTranslator;
import pl.zespolowy.Translation.Translation;
import pl.zespolowy.Translation.Translator;
import pl.zespolowy.Words.WordSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Getter
@Setter
public class WordSetsTranslation {
    private Translator translator = new DeepLTranslator();

    private Map<String, Map<Language, WordSet>> topicLangWSmap = new HashMap<>();
    private LanguageSet langSet;
    private List<Language> langList = new ArrayList<>();
    private static final String ROOT_PATH = System.getProperty("user.dir");
    private static final String LANGUAGES_PATH = ROOT_PATH + "/src/main/resources/languages.json";
    WordSetsReader wsr;

    public WordSetsTranslation() {
        this.wsr = new WordSetsReader("./src/main/resources/wordSets/");
        translateWSToDiffLangs();
    }

    /**
     * This method transforms Map containing pairs (Topic, WordSet)
     * into Map holding Topic as a key and as a value Map with
     * Language as a key and WordSet as a value from this point we have
     * access to WordSets translated to different languages grouped by Topics
     */
    public void translateWSToDiffLangs() {
        topicLangWSmap = wsr.getWordSets().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        v -> setLanguages(v.getValue())
                ));
    }

    private Map<Language, WordSet> setLanguages(WordSet wordSet) {
        Map<Language, WordSet> wsInDiffLangs = new HashMap<>();

        // read languages from file
        loadLangsFromFile();

        // translate to other languages
        langList.forEach(a -> translate(wsInDiffLangs, a, wordSet));

        return wsInDiffLangs;
    }

    private void translate(Map<Language, WordSet> wsInDiffLangs, Language language, WordSet wordSet) {
        // omit english because of DeepL mistranslation from en -> en
        if (language.getCode().equals("en") && language.getEnabled().get()) {
            wsInDiffLangs.put(language, wordSet);
        } else {
            if (language.getEnabled().get()) {
                Translation translation = translator.translate(wordSet, new Language("English", "en"), language);
                wsInDiffLangs.put(language, new WordSet(wordSet.getTitle(), translation.getTarget()));
            }
        }
    }

    public void loadLangsFromFile() {
        try {
            String content = Files.readString(Paths.get(LANGUAGES_PATH));

            langSet = new LanguageSet(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        langList = langSet.getLanguages();
    }
}
