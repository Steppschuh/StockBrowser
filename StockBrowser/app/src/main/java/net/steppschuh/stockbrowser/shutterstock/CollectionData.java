
package net.steppschuh.stockbrowser.shutterstock;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class CollectionData {

    public static final String KEY_ID = "key_id";

    @SerializedName("total_item_count") private int totalItemCount;
    @SerializedName("items_updated_time") private String itemsUpdatedTime;
    private String name;
    private String id;
    @SerializedName("created_time") private String createdTime;
    @SerializedName("updated_time") private String updatedTime;
    @SerializedName("cover_item") private CoverItem coverItem;
    @SerializedName("hero_item") private HeroItem heroItem;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * Checks if all required fields contain valid values
     */
    public boolean isValidCollection() {
        if (id == null || id.length() < 1) {
            return false;
        }

        if (name == null || name.length() < 1) {
            return false;
        }

        if (coverItem == null || coverItem.getUrl() == null || coverItem.getUrl().length() < 1) {
            return false;
        }

        if (totalItemCount < 1) {
            return false;
        }

        return true;
    }

    /**
     * 
     * @return
     *     The totalItemCount
     */
    public int getTotalItemCount() {
        return totalItemCount;
    }

    /**
     * 
     * @param totalItemCount
     *     The total_item_count
     */
    public void setTotalItemCount(int totalItemCount) {
        this.totalItemCount = totalItemCount;
    }

    /**
     * 
     * @return
     *     The itemsUpdatedTime
     */
    public String getItemsUpdatedTime() {
        return itemsUpdatedTime;
    }

    /**
     * 
     * @param itemsUpdatedTime
     *     The items_updated_time
     */
    public void setItemsUpdatedTime(String itemsUpdatedTime) {
        this.itemsUpdatedTime = itemsUpdatedTime;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The createdTime
     */
    public String getCreatedTime() {
        return createdTime;
    }

    /**
     * 
     * @param createdTime
     *     The created_time
     */
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * 
     * @return
     *     The updatedTime
     */
    public String getUpdatedTime() {
        return updatedTime;
    }

    /**
     * 
     * @param updatedTime
     *     The updated_time
     */
    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    /**
     * 
     * @return
     *     The coverItem
     */
    public CoverItem getCoverItem() {
        return coverItem;
    }

    /**
     * 
     * @param coverItem
     *     The cover_item
     */
    public void setCoverItem(CoverItem coverItem) {
        this.coverItem = coverItem;
    }

    /**
     * 
     * @return
     *     The heroItem
     */
    public HeroItem getHeroItem() {
        return heroItem;
    }

    /**
     * 
     * @param heroItem
     *     The hero_item
     */
    public void setHeroItem(HeroItem heroItem) {
        this.heroItem = heroItem;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
