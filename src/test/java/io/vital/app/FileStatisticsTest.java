package io.vital.app;

import io.vital.app.entities.File;
import io.vital.app.entities.Statistics;
import io.vital.app.statistics.FileStatistics;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

public class FileStatisticsTest {

    @Test
    public void testStatisticsHasMultipleLanguages() {
        //given
        Statistics statistics = Statistics.empty();
        File file = new File();
        file.setLanguages(List.of(File.Language.DUTCH, File.Language.JAPANESE));

        //when
        FileStatistics.getStatisticsByValue(statistics, file, "language");

        //then
        Assert.assertEquals("Dutch", statistics.getStatistics().get(0).getValue());
        Assert.assertEquals("Japanese", statistics.getStatistics().get(1).getValue());
    }

    @Test
    public void testStatisticsHasMultipleLanguagesAndCorrectCounters() {
        //given
        File file1 = new File();
        File file2 = new File();
        file1.setLanguages(List.of(File.Language.DUTCH, File.Language.JAPANESE));
        file2.setLanguages(List.of(File.Language.UKRAINIAN, File.Language.JAPANESE));

        //when
        Statistics statistics = FileStatistics.getStatisticsByValue(List.of(file1, file2), "language");

        //then
        Assert.assertEquals("Dutch", statistics.getStatistics().get(0).getValue());
        Assert.assertEquals("Japanese", statistics.getStatistics().get(1).getValue());
        Assert.assertEquals("Ukrainian", statistics.getStatistics().get(2).getValue());
        Assert.assertEquals(2, (long) statistics.getStatistics().get(1).getCount());
    }

    @Test
    public void testStatisticsIsSortedByCount() {
        //given
        File file1 = new File();
        File file2 = new File();
        File file3 = new File();
        file1.setLanguages(List.of(File.Language.RUSSIAN, File.Language.ENGLISH));
        file2.setLanguages(List.of(File.Language.UKRAINIAN, File.Language.SPANISH));
        file3.setLanguages(List.of(File.Language.UKRAINIAN, File.Language.RUSSIAN));

        //when
        Statistics statistics = FileStatistics.getStatisticsByValue(List.of(file1, file2, file3), "language");
        statistics.sorted();

        //then
        Assert.assertEquals("Russian", statistics.getStatistics().get(0).getValue());
        Assert.assertEquals("Ukrainian", statistics.getStatistics().get(1).getValue());
        Assert.assertEquals("English", statistics.getStatistics().get(2).getValue());
        Assert.assertEquals("Spanish", statistics.getStatistics().get(3).getValue());

        Assert.assertEquals(2L, (long) statistics.getStatistics().get(0).getCount());
        Assert.assertEquals(2L, (long) statistics.getStatistics().get(1).getCount());
    }

    @Test
    public void testStatisticsDoesntHaveDuplicatesAndCountsCorrectly() {
        //given
        List<File> files = IntStream.range(1, 11).mapToObj(i -> {
            File file = new File();
            file.setOwner("Daisy");
            return file;
        }).toList();

        //when
        Statistics statistics = FileStatistics.getStatisticsByValue(files, "owner");

        //then
        Assert.assertEquals("Daisy", statistics.getStatistics().get(0).getValue());
        Assert.assertEquals(1, statistics.size());
        Assert.assertEquals(10L, (long)statistics.getStatistics().get(0).getCount());

    }

    @Test
    public void testStatisticsIsEmptyIfEmptyFileField() {
        //given
        List<File> files = IntStream.range(1, 100).mapToObj(i -> {
            File file = new File();
            file.setOwner("Jackson");
            return file;
        }).toList();

        //when
        Statistics statistics = FileStatistics.getStatisticsByValue(files, "year_published");

        //then
        Assert.assertEquals(0, statistics.size());
        Assert.assertTrue(statistics.getStatistics().isEmpty());
    }

    @Test
    public void testStatisticsHasOnlyUniqueValues() {
        //given
        List<File> files = IntStream.range(8000, 8100).mapToObj(i -> {
            File file = new File();
            file.setSize((long)i);
            return file;
        }).toList();

        //when
        Statistics statistics = FileStatistics.getStatisticsByValue(files, "size");

        //then
        Assert.assertEquals(100, statistics.size());
        Assert.assertEquals("8000", statistics.getStatistics().get(0).getValue());
        Assert.assertEquals(1L, (long)statistics.getStatistics().get(0).getCount());
        Assert.assertEquals("8001", statistics.getStatistics().get(1).getValue());
        Assert.assertEquals(1L, (long)statistics.getStatistics().get(1).getCount());
    }
}
