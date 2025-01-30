package pl.zespolowy.Language;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;

public class SimpleBooleanPropertyDeserializer extends JsonDeserializer<SimpleBooleanProperty> {

    @Override
    public SimpleBooleanProperty deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // Parse the JSON value into a boolean and wrap it in a SimpleBooleanProperty
        boolean value = p.getBooleanValue();
        return new SimpleBooleanProperty(value);
    }
}