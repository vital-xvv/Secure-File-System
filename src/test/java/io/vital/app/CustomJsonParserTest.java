package io.vital.app;

import io.vital.app.entities.File;
import io.vital.app.entities.Statistics;
import io.vital.app.json.CustomJsonParser;
import io.vital.app.json.implementation.CustomJsonParserImpl;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class CustomJsonParserTest {
    private final CustomJsonParser parser = new CustomJsonParserImpl();

    @Test
    //Deserialization Test
    public void testParsingArrayOfFilesFromJsonFileDOMParsing() throws IOException {
        //given
        List<File> files;

        //when
        files = ((CustomJsonParserImpl)parser).parseFilesFromFile("src/test/resources/json_files/example_1.json");

        //then
        Assert.assertEquals(3, files.size());
        Assert.assertEquals(2, files.get(0).getLanguages().size());
        Assert.assertEquals(File.Language.SPANISH, files.get(1).getLanguages().get(0));
        Assert.assertEquals("document_one.pdf", files.get(0).getFileName());
        Assert.assertEquals("txt", files.get(2).getExtension());
    }

    @Test
    //Serialization Test
    public void testCorrectJsonSerializationOfFileUsingPrintedString() throws IOException {
        //given
        File file = ((CustomJsonParserImpl)parser)
                .parseFilesFromFile("src/test/resources/json_files/random.json").get(0);
        String jsonStringified =
                            """
                            {
                              "id" : 1,
                              "filename" : "exemple_document.pdf",
                              "size" : 5120,
                              "extension" : "pdf",
                              "language" : "French",
                              "year_published" : 2023,
                              "owner" : "Jean Dupont"
                            }
                            """;
        jsonStringified = jsonStringified.replaceAll("\n", "").replaceAll("\r", "");

        //when
        String result = ((CustomJsonParserImpl)parser).prettyJsonPrint(file);
        result = result.replaceAll("\n", "").replaceAll("\r", "");

        //then
        Assert.assertEquals(jsonStringified, result);
    }

    @Test
    //Serialization Test
    public void testCorrectJsonSerializationOfFileUsingPrintedStringWhenMultipleLanguages() throws IOException {
        //given
        File file = ((CustomJsonParserImpl)parser)
                .parseFilesFromFile("src/test/resources/json_files/double_language.json").get(1);
        String jsonStringified =
                            """
                            {
                              "id" : 82,
                              "filename" : "documento_de_investigacion.docx",
                              "size" : 4096,
                              "extension" : "docx",
                              "language" : "Spanish, English",
                              "year_published" : 2022,
                              "owner" : "Maria Garcia"
                            }
                            """;
        jsonStringified = jsonStringified.replaceAll("\n", "").replaceAll("\r", "");

        //when
        String result = ((CustomJsonParserImpl)parser).prettyJsonPrint(file);
        result = result.replaceAll("\n", "").replaceAll("\r", "");

        //then
        Assert.assertEquals(jsonStringified, result);
    }

    @Test
    public void testJsonFileParsingUsingJacksonStreamingAPIAndSortedStatisticsCreationByLanguageWithoutUsingInMemoryCollections() {
        //given
        Statistics statistics = Statistics.empty();

        //when
        parser.parseFileEfficient(statistics, new java.io.File("src/test/resources/json_files/example_2.json"), "language");
        statistics.sorted();

        //then
        Assert.assertEquals("English", statistics.getStatistics().get(0).getValue());
        Assert.assertEquals(3, (long)statistics.getStatistics().get(0).getCount());
        Assert.assertEquals("Japanese", statistics.getStatistics().get(1).getValue());
        Assert.assertEquals(2, (long)statistics.getStatistics().get(1).getCount());
        Assert.assertEquals("German", statistics.getStatistics().get(2).getValue());
        Assert.assertEquals(1, (long)statistics.getStatistics().get(2).getCount());
        Assert.assertEquals("Italian", statistics.getStatistics().get(3).getValue());
        Assert.assertEquals(1, (long)statistics.getStatistics().get(3).getCount());
        Assert.assertEquals(10, statistics.size());
        //...
    }

    @Test
    public void testJsonFileParsingUsingJacksonStreamingAPIAndStatisticsCreationByExtensionWithoutUsingInMemoryCollections() {
        //given
        Statistics statistics = Statistics.empty();

        //when
        parser.parseFileEfficient(statistics, new java.io.File("src/test/resources/json_files/double_language.json"), "extension");

        //then
        Assert.assertEquals("pdf", statistics.getStatistics().get(0).getValue());
        Assert.assertEquals(3L, (long)statistics.getStatistics().get(0).getCount());
        Assert.assertEquals("docx", statistics.getStatistics().get(1).getValue());
        Assert.assertEquals(3L, (long)statistics.getStatistics().get(1).getCount());
        Assert.assertEquals("doc", statistics.getStatistics().get(2).getValue());
        Assert.assertEquals(1L, (long)statistics.getStatistics().get(2).getCount());
        Assert.assertEquals(3, statistics.size());
    }

    @Test
    public void testFolderWithJsonFilesParsingUsingJacksonStreamingAPIAndSortedStatisticsCreationByExtensionWithoutUsingInMemoryCollections() {
        //given
        String folderPath = "src/test/resources/json_folder_for_test";

        //when
        Statistics statistics = parser.parseFilesInFolderEfficient(folderPath, "extension");
        statistics.sorted();

        //then
            //Positional
        Assert.assertEquals(9, statistics.size());
        Assert.assertEquals("docx", statistics.getStatistics().get(0).getValue());
        Assert.assertEquals("pdf", statistics.getStatistics().get(1).getValue());
        Assert.assertEquals("doc", statistics.getStatistics().get(2).getValue());
        Assert.assertEquals("txt", statistics.getStatistics().get(3).getValue());
        Assert.assertEquals("pptx", statistics.getStatistics().get(4).getValue());
        Assert.assertEquals("tex", statistics.getStatistics().get(5).getValue());
        Assert.assertEquals("py", statistics.getStatistics().get(6).getValue());
        Assert.assertEquals("epub", statistics.getStatistics().get(7).getValue());
        Assert.assertEquals("csv", statistics.getStatistics().get(8).getValue());

            //Counters
        Assert.assertEquals(6, (long)statistics.getStatistics().get(0).getCount());
        Assert.assertEquals(5, (long)statistics.getStatistics().get(1).getCount());
        Assert.assertEquals(2, (long)statistics.getStatistics().get(2).getCount());
        Assert.assertEquals(2, (long)statistics.getStatistics().get(3).getCount());
        Assert.assertEquals(1, (long)statistics.getStatistics().get(4).getCount());
        Assert.assertEquals(1, (long)statistics.getStatistics().get(5).getCount());
        Assert.assertEquals(1, (long)statistics.getStatistics().get(6).getCount());
        Assert.assertEquals(1, (long)statistics.getStatistics().get(7).getCount());
        Assert.assertEquals(1, (long)statistics.getStatistics().get(8).getCount());
    }

    @Test
    public void testFolderWithJsonFilesParsingConcurrentUsingJacksonStreamingAPIAndSortedStatisticsCreationByExtensionWithoutUsingInMemoryCollections() {
        //given
        String folderPath = "src/test/resources/json_folder_for_test";
        int numOfThreadsDefault = 2;

        //when
        Statistics statistics = parser.parseFilesInFolderEfficientConcurrent(folderPath, "extension", numOfThreadsDefault);
        statistics.sorted();

        //then
        //Positional
        Assert.assertEquals(9, statistics.size());
        Assert.assertEquals("docx", statistics.getStatistics().get(0).getValue());
        Assert.assertEquals("pdf", statistics.getStatistics().get(1).getValue());
        Assert.assertEquals("doc", statistics.getStatistics().get(2).getValue());
        Assert.assertEquals("txt", statistics.getStatistics().get(3).getValue());
        Assert.assertEquals("pptx", statistics.getStatistics().get(4).getValue());
        Assert.assertEquals("tex", statistics.getStatistics().get(5).getValue());
        Assert.assertEquals("py", statistics.getStatistics().get(6).getValue());
        Assert.assertEquals("epub", statistics.getStatistics().get(7).getValue());
        Assert.assertEquals("csv", statistics.getStatistics().get(8).getValue());

        //Counters
        Assert.assertEquals(6, (long)statistics.getStatistics().get(0).getCount());
        Assert.assertEquals(5, (long)statistics.getStatistics().get(1).getCount());
        Assert.assertEquals(2, (long)statistics.getStatistics().get(2).getCount());
        Assert.assertEquals(2, (long)statistics.getStatistics().get(3).getCount());
        Assert.assertEquals(1, (long)statistics.getStatistics().get(4).getCount());
        Assert.assertEquals(1, (long)statistics.getStatistics().get(5).getCount());
        Assert.assertEquals(1, (long)statistics.getStatistics().get(6).getCount());
        Assert.assertEquals(1, (long)statistics.getStatistics().get(7).getCount());
        Assert.assertEquals(1, (long)statistics.getStatistics().get(8).getCount());
    }

    @Test
    public void testXMlFileWithSortedStatisticsByLanguageIsCreatedAfterFolderParsing() throws IOException {
        //given
        String folderPath = "src/test/resources/json_folder_for_test";
        Statistics statistics = parser.parseFilesInFolderEfficient(folderPath, "language");
        statistics.sorted();
        String xmlLocationToWrite = "src/test/resources/statistics_xml";

        //when
        parser.prettyXmlPrintInFile(xmlLocationToWrite, statistics, "language");

        //then
        Assert.assertTrue(new java.io.File("src/test/resources/statistics_xml/" + "statistics_by_language.xml").exists());
    }
}
