package pl.zespolowy.Language;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Language {
    private String name;
    private String code;
    private boolean enabled;

    public Language() {}

    public Language(String name, String code) {
        this.name = name;
        this.code = code;
        this.enabled = false;
    }

    public Language(String name, String code, boolean enabled) {
        this.name = name;
        this.code = code;
        this.enabled = true;
    }
}
