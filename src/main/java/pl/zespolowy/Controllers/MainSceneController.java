package pl.zespolowy.Controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import pl.zespolowy.Words.Word;
import pl.zespolowy.Language.Language;
import pl.zespolowy.Language.LanguageSet;
import pl.zespolowy.Translation.Translation;
import pl.zespolowy.Translation.Translator;
import pl.zespolowy.Words.WordSet;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
public class MainSceneController {
//    @FXML
//    private AnchorPane mainSceneAnchorPane;
//
//    @FXML
//    private TextArea textArea;
//
//    @FXML
//    private VBox subjectBox;
//
//    @FXML
//    private ListView subjectList;
//
//    private static final String ROOT_PATH = System.getProperty("user.dir");
//
//    private Translator translator;
//    private LanguageSet languageSet;
//    private boolean useCache;
//
//    public List<Language> languageList;
//    public List<WordSet> wordSetList;
//
//    public void initialize() {
//        String languagesPath = ROOT_PATH + "/src/main/resources/languages.json";
//        initLanguages(languagesPath);
//        languageSet.print();
//
//        String wordSetPath = ROOT_PATH + "/src/main/resources/wordsets/";
//        initWordSets(wordSetPath);
//
//        useCache = false;
//    }
//
//    public void initLanguages(String path) {
//        try {
//            String content = Files.readString(Paths.get(path));
//            languageSet = new LanguageSet(content);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void initWordSets(String path) {
//        File dir = new File(path);
//        if (dir.exists() && dir.isDirectory()) {
//            String[] fileNames = dir.list();
//
//            if (fileNames != null) {
//                for (String fileName : fileNames) {
//                    try {
//                        String title = fileName.split(".json")[0];
//                        String content = Files.readString(Paths.get(path + fileName));
//                        SimpleBooleanProperty falseBoolProp = new SimpleBooleanProperty(false);
//                        WordSet wordSet = new WordSet(title, content, falseBoolProp);
//
//                        wordSetList.add(wordSet);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println(fileName);
//                }
//            } else {
//                System.out.println("The current directory is empty or an error occurred.");
//            }
//        } else {
//            System.out.println("The current directory does not exist or is not a directory.");
//        }
//    }
//
//    @FXML
//    public void handleTranslate() {
//        for (WordSet wordSet : wordSetList) {
//            String folderName = "Cache\\Translation\\Polish\\";
//            String fileName = "Transportation methods.json";
//
//            System.out.println(wordSet.getTitle());
//            Language sourceLanguage = new Language("English", "en");
//            Language targetLanguage = new Language("Test", "es");
//            Translation t = translator.translate(wordSet, sourceLanguage, targetLanguage);
//            System.out.println(t.targetText());
//
//        }
//    }
}
