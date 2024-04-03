package io.vital.app.entities;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {
    private Long id;
    @SerializedName("filename")
    private String fileName;
    private Long size;
    private String extension;
    //@SerializedName("language")
    private List<Language> languages;
    @SerializedName("year_published")
    private int publishYear;
    private String owner;

    enum Language {
        ENGLISH, UKRAINIAN, RUSSIAN,
        GERMAN, FRENCH, SPANISH
    }
}
