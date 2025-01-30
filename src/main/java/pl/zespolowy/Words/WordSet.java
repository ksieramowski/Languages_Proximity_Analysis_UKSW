package pl.zespolowy.Words;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import lombok.Getter;
import lombok.Setter;
import pl.zespolowy.Language.SimpleBooleanPropertyDeserializer;
import pl.zespolowy.Language.SimpleBooleanPropertySerializer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Setter
@Getter
public class WordSet {
    private String title;
    private List<Word> words;
    @JsonDeserialize(using = SimpleBooleanPropertyDeserializer.class)
    @JsonSerialize(using = SimpleBooleanPropertySerializer.class)
    private BooleanProperty enabled;

    public WordSet() {
        this.title = "UNTITLED";
        this.words = new ArrayList<>();
    }

    public WordSet(String title, String jsonString) {
        System.out.println("JSON STRING: " + jsonString);
        this.title = title;
        Deserialize(jsonString);
    }



    public WordSet(String title, String[] words) {
        this.title = title;
        this.words = Arrays.stream(words).map(Word::new).toList();
    }

    private void Deserialize(String jsonString) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            var ws = objectMapper.readValue(jsonString, WordSet.class);
            this.words = ws.words;
            this.title = ws.title;
            this.enabled = ws.enabled;

            this.print();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Serialize() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String rootPath = System.getProperty("user.dir");
            String fileName = this.title + ".json";
            String path = rootPath + "/src/main/resources/wordSets/";

            File file = new File(path + fileName);
            objectMapper.writeValue(file, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void print() {
        System.out.println("---- WordSet \"" + title + "\" (enabled=" + enabled + "): ----");
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");

        for (Word word : this.words) {
            sb.append(word.getText());
            sb.append(", ");
        }
        sb.append(" }");
        System.out.println(sb.toString());
        System.out.println();
    }

}
