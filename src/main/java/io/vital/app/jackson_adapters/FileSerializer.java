package io.vital.app.jackson_adapters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.vital.app.entities.File;

import java.io.IOException;
import java.util.stream.Collectors;

public class FileSerializer extends StdSerializer<File> {

    protected FileSerializer() {
        this(null);
    }

    protected FileSerializer(Class<File> t) {
        super(t);
    }

    @Override
    public void serialize(File file, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {

        jsonGenerator.writeStartObject();

        // "id" : "1"
        jsonGenerator.writeNumberField("id", file.getId());

        // "filename" : "example_document.pdf"
        jsonGenerator.writeStringField("filename", file.getFileName());

        // "size" : 1024
        jsonGenerator.writeNumberField("size", file.getSize());

        // "extension" : "pdf"
        jsonGenerator.writeStringField("extension", file.getExtension());

        // "language" : "French, German"
        jsonGenerator.writeStringField("language",
                file.getLanguages()
                        .stream()
                        .map(File.Language::getPrettyName)
                        .collect(Collectors.joining(", ")));

        // "year_published" : 1024
        jsonGenerator.writeNumberField("year_published", file.getPublishYear());

        // "owner" : "Maria Garcia"
        jsonGenerator.writeStringField("owner", file.getOwner());

        jsonGenerator.writeEndObject();

    }
}
