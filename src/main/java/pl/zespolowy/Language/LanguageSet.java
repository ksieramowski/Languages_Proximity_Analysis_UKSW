package pl.zespolowy.Language;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import pl.zespolowy.Language.Language;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class LanguageSet {
    private List<Language> languages;

    public LanguageSet(String jsonString) {
        Deserialize(jsonString);
    }

    private void Deserialize(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            languages = objectMapper.readValue(jsonString, objectMapper.getTypeFactory().constructCollectionType(List.class, Language.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Serialize() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String rootPath = System.getProperty("user.dir");
            String fileName = "languages.json";
            String path = rootPath + "/src/main/resources/";

            File file = new File(path + fileName);
            objectMapper.writeValue(file, this.languages );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Language get(int index) {
        return this.languages.get(index);
    }

    public void print() {
        System.out.println("---- Languages ----");
        for (Language l : languages) {
            System.out.println(l.getName());
        }
        System.out.println();
    }
}
