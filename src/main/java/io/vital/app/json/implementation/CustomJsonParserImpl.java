package io.vital.app.json.implementation;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.vital.app.concurrent.FileParseTask;
import io.vital.app.entities.File;
import io.vital.app.entities.Statistics;
import io.vital.app.json.CustomJsonParser;
import io.vital.app.statistics.FileStatistics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CustomJsonParserImpl implements CustomJsonParser {
    private final ObjectMapper mapper = new ObjectMapper();
    private final ObjectMapper xmlMapper = new XmlMapper();
    private final ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

    public CustomJsonParserImpl() {
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Converts an array of json file objects into a Java List of Files @{@link File}
     * Method uses basic capabilities of Jackson library
     * @see com.fasterxml.jackson.databind.ObjectMapper
     * @see io.vital.app.jackson_adapters.FileDeserializer
     * @param filename File location to deserialize
     * @return Java list of deserialized file objects
     * @throws IOException
     */
    public List<File> parseFilesFromFile(String filename) throws IOException {
        return Arrays.asList(mapper.readValue(new BufferedReader(new FileReader(filename)), File[].class));
    }

    /**
     * Returns a pretty string of a json serialized Java object
     * @see io.vital.app.jackson_adapters.FileSerializer
     * @param json Object to serialize
     * @return Pretty JSON string
     * @throws JsonProcessingException
     */
    public String prettyJsonPrint(Object json) throws JsonProcessingException {
        return writer.writeValueAsString(json);
    }

    public void prettyXmlPrintInFile(String rootPath, Object xml, String value) throws IOException {
        xmlMapper.writeValue(new java.io.File(String.format("%s/statistics_by_%s.xml", rootPath, value)), xml);
    }

    public Statistics parseFilesInFolderEfficient(String folderPath, String value) {
        Statistics statistics = Statistics.empty();

        Arrays.stream(Objects.requireNonNull(new java.io.File(folderPath).listFiles()))
                .filter(CustomJsonParserImpl::isJsonFile)
                .forEach(file -> parseFileEfficient(statistics, file, value));

        return statistics;
    }

    public Statistics parseFilesInFolderEfficientConcurrent(String folderPath, String value, int numOfThreads) {
        Statistics statistics = Statistics.empty();
        ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);

        Arrays.stream(Objects.requireNonNull(new java.io.File(folderPath).listFiles()))
                .filter(CustomJsonParserImpl::isJsonFile)
                .forEach(file -> executorService.submit(new FileParseTask(this, statistics, file, value)));

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(15, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        return statistics;
    }

    @SuppressWarnings("deprecation")
    public void parseFileEfficient(Statistics statistics, java.io.File file, String value) {
        JsonFactory factory = mapper.getFactory();
        try(JsonParser jsonParser = factory.createParser(file)){
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                    //get the current token
                    String fieldName = jsonParser.getCurrentName();
                    if ("size".equals(fieldName) && "size".equals(value)) {
                        //move to next token
                        jsonParser.nextToken();
                        File f = new File();
                        f.setSize(jsonParser.getLongValue());
                        FileStatistics.getStatisticsByValue(statistics, f, value);
                    }
                    if("extension".equals(fieldName) && "extension".equals(value)){
                        //move to next token
                        jsonParser.nextToken();
                        File f = new File();
                        f.setExtension(jsonParser.getText());
                        FileStatistics.getStatisticsByValue(statistics, f, value);
                    }
                    if("owner".equals(fieldName) && "owner".equals(value)){
                        //move to next token
                        jsonParser.nextToken();
                        File f = new File();
                        f.setOwner(jsonParser.getText());
                        FileStatistics.getStatisticsByValue(statistics, f, value);
                    }
                    if("year_published".equals(fieldName) && "year_published".equals(value)){
                        //move to [
                        jsonParser.nextToken();
                        File f = new File();
                        f.setPublishYear(jsonParser.getIntValue());
                        FileStatistics.getStatisticsByValue(statistics, f, value);

                    }
                    if("language".equals(fieldName) && "language".equals(value)){
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

    /**
     * Checks if a {@link java.io.File} is not a directory and has .json extension
     * @param file File obj to check
     * @return true if file corresponds to requirements
     */
    private static boolean isJsonFile(java.io.File file){
        return !file.isDirectory() && file.getName().endsWith(".json");
    }

}
