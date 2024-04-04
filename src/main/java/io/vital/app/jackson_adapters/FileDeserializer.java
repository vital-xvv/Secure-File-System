package io.vital.app.jackson_adapters;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.vital.app.entities.File;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FileDeserializer extends StdDeserializer<File> {

    public FileDeserializer() {
        this(null);
    }

    public FileDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public File deserialize(JsonParser jp, DeserializationContext deserializationContext)
            throws IOException {

        JsonNode node = jp.getCodec().readTree(jp);
        long id = node.get("id").longValue();
        String fileName = node.get("filename").asText();
        long size = node.get("size").longValue();
        String extension = node.get("extension").asText();
        List<File.Language> languages = Arrays
                .stream(node.get("language").asText().split(" *, *"))
                .filter(s -> !s.isBlank())
                .map(el -> File.Language.valueOf(el.toUpperCase()))
                .toList();
        int publishYear = node.get("id").intValue();
        String owner = node.get("owner").asText();

        return new File(id, fileName, size, extension, languages, publishYear, owner);
    }

}
