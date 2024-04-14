package io.vital.app.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents an item in {@link Statistics}
 */
@Data
@NoArgsConstructor
public class Item implements Comparable<Item>{
    /**
     * Json property value
     */
    private String value;
    private Long count = 1L;

    public Item(String val){
        value = val;
    }

    public void incrementCount(){
        count++;
    }

    /**
     * Serves for sorting collections of Item objects by count in descending order
     * @param o the object to be compared.
     * @return int
     */
    @Override
    public int compareTo(Item o) {
        return -1 * Long.compare(count, o.getCount());
    }
}
