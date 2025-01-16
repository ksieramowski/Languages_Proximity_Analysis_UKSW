//package pl.zespolowy;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.List;
//
//@Getter
//@Setter
//public class WordSet {
//    private String title;
//    private List<Word> words;
//    private boolean enabled;
//
//    public WordSet(String title) {
//        this.title = title;
//        this.enabled = false;
//    }
//    public WordSet(String title, List<Word> words) {
//        this.title = title;
//        this.words = words;
//        this.enabled = false;
//    }
//
//    public WordSet(String title, String jsonString) {
//        this.title = title;
//        Deserialize(jsonString);
//        this.enabled = false;
//    }
//
//    public WordSet(String title, String jsonString, boolean enabled) {
//        this.title = title;
//        Deserialize(jsonString);
//        this.enabled = enabled;
//    }
//
//    private void Deserialize(String jsonString) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            words = objectMapper.readValue(jsonString, objectMapper.getTypeFactory().constructCollectionType(List.class, Word.class));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public List<Word> getWords() {
//        return words;
//    }
//
//    //    public String format(TranslatorType translatorType) {
////        StringBuilder sb = new StringBuilder();
////
////        switch (translatorType) {
////            case GOOGLE -> formatGoogle(sb);
////            case CHAT_GPT -> formatChatGPT(sb);
////            case GEMINI -> formatGemini(sb);
////        }
////
////        return sb.toString();
////    }
////
////    private void formatGoogle(StringBuilder sb) {
////        sb.append("This is list of ");
////        sb.append(this.title);
////        sb.append(": ");
////        for (int i = 0; i < words.size(); i++) {
////            sb.append(words.get(i).getText());
////            if (i != words.size() - 1) {
////                sb.append(", ");
////            }
////        }
////    }
////
////    private void formatChatGPT(StringBuilder sb) {
////        for (int i = 0; i < words.size(); i++) {
////            String sep;
////            if (i <= 9) {
////                sep = "\n0" + i + ". ";
////            } else {
////                sep = "\n" + i + ". ";
////            }
////            sb.append(sep);
////            sb.append(words.get(i).getText());
////        }
////    }
////
////    private void formatGemini(StringBuilder sb) {}
//
//    public void addWord(Word word) {
//        words.add(word);
//    }
//
//    public boolean getEnabled() {
//        return enabled;
//    }
//
//    public void setEnabled(boolean enabled) {
//        this.enabled = enabled;
//    }
//
//    // test
//    public void print() {
//        System.out.println("---- Words in set \"" + title + "\": ----");
//        for (Word word : words) {
//            System.out.println(word.getText());
//        }
//        System.out.println();
//    }
//
//    public void initLanguages(String title, String path) {
//        try {
//            String content = Files.readString(Paths.get(path));
//            LanguageSet languageSet = new LanguageSet(title, content);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
