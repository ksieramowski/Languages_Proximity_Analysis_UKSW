package pl.zespolowy.Words;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Word {
    private String text;
    private boolean enabled;

    public Word() {
        this.text = null;
        this.enabled = false;
    }

    public Word(String text) {
        this.text = text;
        this.enabled = true;
    }

    public Word(String text, boolean enabled) {
        this.text = text;
        this.enabled = enabled;
    }
}
