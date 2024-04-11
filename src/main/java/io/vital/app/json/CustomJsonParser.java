package io.vital.app.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.vital.app.entities.File;
import io.vital.app.entities.Statistics;
import io.vital.app.statistics.FileStatistics;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class CustomJsonParser {
    private final ObjectMapper mapper = new ObjectMapper();
    private final ObjectMapper xmlMapper = new XmlMapper();
    private final ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

    public CustomJsonParser() {
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

    public void prettyXmlPrintInFile(String rootPath, Object xml, String value) throws IOException {
        xmlMapper.writeValue(new java.io.File(String.format("%s/statistics_by_%s.xml", rootPath, value)), xml);
    }

    public Statistics parseFilesInFolderEfficient(String folderPath, String value) throws IOException {
        Statistics statistics = Statistics.empty();

        Files.list(Path.of(folderPath))
                .filter(p -> {
                    String filename = p.getFileName().toString();
                    return filename.contains(".") && filename.substring(filename.lastIndexOf(".") + 1).equals("json");
                })
                .forEach(p -> parseFileEfficient(statistics, p.toFile(), value));

        return statistics;
    }

    public void parseFileEfficient(Statistics statistics, java.io.File file, String value) {
        JsonFactory factory = mapper.getFactory();
        try(JsonParser jsonParser = factory.createParser(file)){
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                    //get the current token
                    String fieldname = jsonParser.getCurrentName();
                    if ("size".equals(fieldname) && "size".equals(value)) {
                        //move to next token
                        jsonParser.nextToken();
                        File f = new File();
                        f.setSize(jsonParser.getLongValue());
                        FileStatistics.getStatisticsByValue(statistics, f, value);
                    }
                    if("extension".equals(fieldname) && "extension".equals(value)){
                        //move to next token
                        jsonParser.nextToken();
                        File f = new File();
                        f.setExtension(jsonParser.getText());
                        FileStatistics.getStatisticsByValue(statistics, f, value);
                    }
                    if("owner".equals(fieldname) && "owner".equals(value)){
                        //move to next token
                        jsonParser.nextToken();
                        File f = new File();
                        f.setOwner(jsonParser.getText());
                        FileStatistics.getStatisticsByValue(statistics, f, value);
                    }
                    if("year_published".equals(fieldname) && "year_published".equals(value)){
                        //move to [
                        jsonParser.nextToken();
                        File f = new File();
                        f.setPublishYear(jsonParser.getIntValue());
                        FileStatistics.getStatisticsByValue(statistics, f, value);

                    }
                    if("language".equals(fieldname) && "language".equals(value)){
                        //move to next token
                        jsonParser.nextToken();
                        File f = new File();
                        f.setLanguages(File.deserializeLanguages(jsonParser.getText()));
                        FileStatistics.getStatisticsByValue(statistics, f, value);
                    }
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
