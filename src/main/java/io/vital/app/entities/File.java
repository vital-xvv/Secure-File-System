package io.vital.app.entities;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vital.app.jackson_adapters.FileDeserializer;
import io.vital.app.jackson_adapters.FileSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Secondary entity in relation Many-to-One with {@link User}
 * This class serves as a holder for deserialized file objects from json files
 * @author Vitalii Huzii
 */
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
    private Integer publishYear;
    private String owner;

    public enum Language {
        ENGLISH, UKRAINIAN, RUSSIAN, GERMAN,
        FRENCH, SPANISH, DUTCH, POLISH, JAPANESE,
        CHINESE, KOREAN, ITALIAN, PORTUGUESE, PYTHON, JAVA;

        /**
         * Converts {@link File.Language} object into pretty string:
         * Example: JAVA -> Java, ENGLISH -> English
         * @return Pretty language string
         */
        public String getPrettyName() {
            String str = this.toString().toLowerCase();
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
    }

    /**
     * Converts a string with the specified format into list of language objects
     * @param array String of sequence of languages like "Japanese, Ukrainian, English"
     * @return Returns List<Language> {@link File.Language}
     */
    public static List<Language> deserializeLanguages(String array) {
        return Arrays.stream(array.split(" *, *"))
                .filter(s -> !s.isBlank())
                .map(el -> File.Language.valueOf(el.toUpperCase()))
                .toList();
    }
}
