package pl.zespolowy.Language;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;

public class SimpleBooleanPropertySerializer extends JsonSerializer<SimpleBooleanProperty> {

    @Override
    public void serialize(SimpleBooleanProperty value, JsonGenerator gen, com.fasterxml.jackson.databind.SerializerProvider serializers) throws IOException {
        // Get the boolean value from the SimpleBooleanProperty and write it as a JSON boolean
        gen.writeBoolean(value.get());
    }
}
