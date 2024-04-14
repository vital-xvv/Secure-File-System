package io.vital.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The class serves as a collection of unique statistical entities by file json properties
 * @author Vitalii Huzii
 */
@Data
@JacksonXmlRootElement(localName = "statistics")
public class Statistics {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "item")

    /**
     * List of unique items that count statistical occurrences
     */
    private final List<Item> statistics = new ArrayList<>();
    @JsonIgnore
    private final Lock lock = new ReentrantLock();

    /**
     * Pretty handy method of creating a new empty instance of this class
     * @return new Statistics object
     */
    public static Statistics empty(){
        return new Statistics();
    }

    private Statistics(){}

    /**
     * This method makes the Statistics object almost like a Set collection not adding duplicates but counting repeating
     * statistical occurrances.
     *
     * - If Item object with the same json value already exists in the list it increments its' value
     * - Else it adds a new Item obj with a new json value to the list
     * @param item
     */
    @SuppressWarnings("all")
    public void addItem(Item item) {
        if(item.getValue() == null) return;
        lock.lock();
        if (statistics.stream().noneMatch(t -> t.getValue().equals(item.getValue())))
            statistics.add(item);
        else statistics
                .stream()
                .filter(t -> t.getValue().equals(item.getValue()))
                .findFirst().get().incrementCount();
        lock.unlock();
    }

    /**
     * Sorts items list in descending order
     */
    public void sorted() {
        statistics.sort(Item::compareTo);
    }

    /**
     * @return Returns the amount of Item objects in this class
     */
    public int size(){
        return statistics.size();
    }
}