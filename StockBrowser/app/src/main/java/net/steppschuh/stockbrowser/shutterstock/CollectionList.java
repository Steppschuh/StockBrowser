
package net.steppschuh.stockbrowser.shutterstock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CollectionList {

    private List<CollectionData> data = new ArrayList<CollectionData>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * Randomizes the CollectionData items. Just for demonstration purposes.
     */
    public void randomizeData() {
        // change the order of items
        Collections.shuffle(data, new Random(System.nanoTime()));
    }

    /**
     * 
     * @return
     *     The data
     */
    public List<CollectionData> getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(List<CollectionData> data) {
        this.data = data;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
