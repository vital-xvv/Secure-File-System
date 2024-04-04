package io.vital.app.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.vital.app.entities.File;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JsonParser {
    private final ObjectMapper mapper = new ObjectMapper();
    private final ObjectMapper xmlMapper = new XmlMapper();
    private final ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

    public JsonParser() {
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public List<File> parseFilesFromFile(String filename) throws IOException {
        return Arrays.asList(mapper.readValue(new FileReader(filename), File[].class));
    }

    public void prettyPrint(Object json) throws JsonProcessingException {
        System.out.println(writer.writeValueAsString(json));
    }

    public void prettyXmlPrint(Object xml) throws JsonProcessingException {
        System.out.println(xmlMapper.writeValueAsString(xml));
    }

}
