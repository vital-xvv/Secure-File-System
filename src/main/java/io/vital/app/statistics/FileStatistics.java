package io.vital.app.statistics;

import io.vital.app.entities.Item;
import io.vital.app.entities.Statistics;
import io.vital.app.entities.File;
import java.util.List;

/**
 * Utility class for logic of forming a Statistics object
 * Used by {@link io.vital.app.json.CustomJsonParser}
 * @see Statistics
 * @see Item
 * @author Vitalii Huzii
 */
public class FileStatistics {

    /**
     * Fills {@link Statistics} object with the {@link Item} by json property of {@link File} deserialized json object
     * @param statistics Statistics objects to be filled
     * @param file Deserialized File object with json properties and values
     * @param value Json property to work with
     */
    public static void getStatisticsByValue(Statistics statistics, File file, String value){
        switch (value) {
            case "size" ->
                    statistics.addItem(file.getSize() == null ? new Item() : new Item(String.valueOf(file.getSize())));
            case "extension" ->
                    statistics.addItem(new Item(file.getExtension()));
            case "owner" ->
                    statistics.addItem(new Item(file.getOwner()));
            case "year_published" ->
                    statistics.addItem(file.getPublishYear() == null ? new Item() :new Item(String.valueOf(file.getPublishYear())));
            default ->
                    file.getLanguages().forEach(lang -> statistics.addItem(new Item(lang.getPrettyName())));

        }
    }

    /**
     * Fills {@link Statistics} object with the {@link Item} by json property from List<{@link File}> deserialized json objects
     * Uses {@link FileStatistics#getStatisticsByValue(Statistics, File, String)}
     * @param files List of files
     * @param value Json property to works with
     * @return New filled Statistics object
     */
    public static Statistics getStatisticsByValue(List<File> files, String value) {
        Statistics statistics = Statistics.empty();
        files.forEach(file -> getStatisticsByValue(statistics, file, value));
        return statistics;
    }
}
