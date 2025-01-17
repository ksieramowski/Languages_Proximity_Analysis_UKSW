package pl.zespolowy.Translation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pl.zespolowy.Language.Language;
import pl.zespolowy.Words.WordSet;

public abstract class Translator {
    protected final WebDriver driver;

    public Translator() {
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("no-sandbox", "headless");
        driver = new ChromeDriver(options);
    }

    public abstract Translation translate(WordSet wordSet, Language sourceLanguage, Language targetLanguage);
}
