package io.vital.app.json;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.vital.app.entities.File;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class JsonParser {
    private final Gson gson = new Gson();

    public List<File> parseFilesFromFile(String filename){
        try(JsonReader jsonReader = new JsonReader(new FileReader(filename))){
            return Arrays.asList(gson.fromJson(jsonReader, File[].class));
        }catch (IOException f){
            f.printStackTrace();
        }
        throw new IllegalArgumentException("Illegal json format");
    }
}
