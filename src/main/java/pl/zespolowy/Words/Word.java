package pl.zespolowy.Words;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.build.Plugin;
import pl.zespolowy.Language.SimpleBooleanPropertyDeserializer;
import pl.zespolowy.Language.SimpleBooleanPropertySerializer;

@Setter
@Getter
public class Word {
    private String text;
    @JsonDeserialize(using = SimpleBooleanPropertyDeserializer.class)
    @JsonSerialize(using = SimpleBooleanPropertySerializer.class)
    private SimpleBooleanProperty enabled;

    public Word() {
        this.text = null;
        this.enabled = new SimpleBooleanProperty(false);
    }

    public Word(String text) {
        this.text = text;
        this.enabled = new SimpleBooleanProperty(false);
    }

    public Word(String text, SimpleBooleanProperty enabled) {
        this.text = text;
        this.enabled = new SimpleBooleanProperty(false);
    }

}
