package pl.zespolowy.Business.Algorithm;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.Setter;
import pl.zespolowy.Words.WordSet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class WordSetsReader {
    private final Map<String, WordSet> wordSets = new HashMap<>();

    public WordSetsReader(String s) {
        // read WordSets from file
        initWordSets(s);
    }

    public void initWordSets(String path) {
        File dir = new File(path);

        if (dir.exists() && dir.isDirectory()) {
            String[] fileNames = dir.list();

            if (fileNames != null) {
                for (String fileName : fileNames) {
                    try {
                        String title = fileName.split(".json")[0];
                        String content = Files.readString(Paths.get(path + fileName));
                        WordSet wordSet = new WordSet(title, content);
                        if (wordSet.getEnabled().get()) {
                            wordSets.put(wordSet.getTitle(), wordSet);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(fileName);
                }
            } else {
                System.out.println("The current directory is empty or an error occurred.");
            }
        } else {
            System.out.println("The current directory does not exist or is not a directory.");
        }
    }
}