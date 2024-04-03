package io.vital.app;

import io.vital.app.entities.File;
import io.vital.app.json.JsonParser;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        JsonParser jsonParser = new JsonParser();

        List<File> files = jsonParser.parseFilesFromFile("<filePath>");

        System.out.println(files);
    }
}