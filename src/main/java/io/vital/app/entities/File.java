package io.vital.app.entities;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vital.app.jackson_adapters.FileDeserializer;
import io.vital.app.jackson_adapters.FileSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(using = FileDeserializer.class)
@JsonSerialize(using = FileSerializer.class)
public class File {
    private Long id;
    private String fileName;
    private Long size;
    private String extension;
    private List<Language> languages;
    private int publishYear;
    private String owner;

    public enum Language {
        ENGLISH, UKRAINIAN, RUSSIAN, GERMAN,
        FRENCH, SPANISH, DUTCH, POLISH, JAPANESE,
        CHINESE, KOREAN, ITALIAN, PORTUGUESE;

        public String getPrettyName() {
            String str = this.toString().toLowerCase();
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
    }
}
