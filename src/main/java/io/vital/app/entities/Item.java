package io.vital.app.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Item implements Comparable<Item>{
    private String value;
    private Long count = 1L;

    public Item(String val){
        value = val;
    }

    public void incrementCount(){
        count++;
    }

    @Override
    public int compareTo(Item o) {
        return -1 * Long.compare(count, o.getCount());
    }
}
