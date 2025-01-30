package pl.zespolowy.Language;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Language {
    private String name;
    private String code;
    @JsonDeserialize(using = SimpleBooleanPropertyDeserializer.class)
    @JsonSerialize(using = SimpleBooleanPropertySerializer.class)
    private BooleanProperty enabled;

    public Language() {}

    public Language(String name, String code) {
        this.name = name;
        this.code = code;
        this.enabled = new SimpleBooleanProperty(false);
    }

    public Language(String name, String code, BooleanProperty enabled) {
        this.name = name;
        this.code = code;
        this.enabled = new SimpleBooleanProperty(false);
    }

}
