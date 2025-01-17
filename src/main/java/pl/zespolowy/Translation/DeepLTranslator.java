package pl.zespolowy.Translation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import pl.zespolowy.AppConfig;
import pl.zespolowy.Language.Language;
import pl.zespolowy.Words.WordSet;

import java.io.File;

public class DeepLTranslator extends Translator {

    public DeepLTranslator() {}

    private String format(WordSet wordSet) {
        StringBuilder sb = new StringBuilder();
        sb.append("This is list of ");
        sb.append(wordSet.getTitle());
        sb.append(": ");
        for (int i = 0; i < wordSet.getWords().size(); i++) {
            sb.append(wordSet.getWords().get(i).getText());
            if (i != wordSet.getWords().size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(".");
        return sb.toString();
    }

    public Translation translate(WordSet wordSet, Language sourceLanguage, Language targetLanguage) {
        if (AppConfig.getUseCache()) { // skip using web driver
            String filePath = AppConfig.ROOT_PATH + "/Cache/Translations/" + targetLanguage.getName() + "/" + wordSet.getTitle() + ".json";
            if (new File(filePath).exists()) { //  unless cache file is missing
                Translation t = new Translation();
                t.readCache(targetLanguage.getName(), wordSet.getTitle());
                return t;
            }
        }

        String content = format(wordSet);
        String baseUrl = "https://www.deepl.com/" + sourceLanguage.getCode() + "/translator#" + sourceLanguage.getCode() + "/" + targetLanguage.getCode() + "/" + content;
        driver.get(baseUrl);

        System.out.println(content);

        WebElement output;
        while (true) {
            try {
                Thread.sleep(10); // Sleep for 10 milliseconds
                output = driver.findElement(By.name("target"));
                if (output.getText().length() > 50) {
                    break;
                }
            } catch (Exception _) {

            }
        }

        Translation t = null;
        try {
            String[] source = content.split(": ")[1].replace(".", "").toLowerCase().split(", ");
            String[] paragraphs = output.getText().split(": ");
            String[] target = paragraphs[paragraphs.length - 1].replace(".", "").toLowerCase().split(", ");
            String desc = paragraphs[0];
            System.out.println("DESC: \"" + desc + "\"");

            t = new Translation(source, target);
            if (AppConfig.getAllowCache()) {
                t.writeCache(targetLanguage.getName(), wordSet.getTitle());
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return t;
    }

}