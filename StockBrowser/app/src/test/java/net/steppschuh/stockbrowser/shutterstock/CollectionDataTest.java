package net.steppschuh.stockbrowser.shutterstock;

import org.junit.Test;

import static org.junit.Assert.*;

public class CollectionDataTest {

    @Test
    public void testCollectionData_validation() throws Exception {
        // create a valid collection
        CollectionData validCollection = generateFakeCollection();
        assertTrue(validCollection.isValidCollection());

        // invalidate items
        CollectionData invalidCollection = validCollection;
        invalidCollection.setTotalItemCount(0);
        assertFalse(invalidCollection.isValidCollection());

        // invalidate id
        invalidCollection = validCollection;
        invalidCollection.setId(null);
        assertFalse(invalidCollection.isValidCollection());

        // invalidate name
        invalidCollection = validCollection;
        invalidCollection.setName(null);
        assertFalse(invalidCollection.isValidCollection());

        // invalidate cover
        invalidCollection = validCollection;
        invalidCollection.getCoverItem().setUrl(null);
        assertFalse(invalidCollection.isValidCollection());
    }

    public static CollectionData generateFakeCollection() {
        CollectionData collection = new CollectionData();
        collection.setId("10000000");
        collection.setName("Test Collection");
        collection.setTotalItemCount(50);

        // create a cover
        CoverItem coverItem = new CoverItem();
        coverItem.setUrl("https://ak.picdn.net/assets/cms/" + collection.getId() + ".jpg");
        collection.setCoverItem(coverItem);

        return collection;
    }
}