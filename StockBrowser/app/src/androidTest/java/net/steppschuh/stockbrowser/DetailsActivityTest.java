package net.steppschuh.stockbrowser;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import net.steppschuh.stockbrowser.shutterstock.CollectionData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DetailsActivityTest extends ActivityInstrumentationTestCase2 {

    StockBrowser app;
    DetailsActivity detailsActivity;
    View decorView;

    FloatingActionButton fab;
    RecyclerView recyclerView;

    List<CollectionData> fakeCollections;
    CollectionData targetCollection;

    public DetailsActivityTest() {
        super(DetailsActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());

        // Prevent UI controls from taking focus
        setActivityInitialTouchMode(true);

        // Generate some collections to test with
        fakeCollections = MainActivityTest.generateFakeCollections(25);
        targetCollection = fakeCollections.get(0);

        // Create an intent that contains the collection id
        Intent intent = new Intent();
        intent.putExtra(CollectionData.KEY_ID, targetCollection.getId());
        setActivityIntent(intent);

        // Start the main activity of the application under test
        detailsActivity = (DetailsActivity) getActivity();

        // Get a reference to the application
        app = (StockBrowser) detailsActivity.getApplicationContext();

        // Get a reference for all the tested views
        decorView = detailsActivity.getWindow().getDecorView();
    }

    public void testPreconditions() {
        assertNotNull(MainActivity.class.getSimpleName() + " is null", detailsActivity);
        assertNotNull(StockBrowser.class.getSimpleName() + " is null", app);
    }

    @Test
    public void testActivity_restoreCollectionFromIntent() {
        Bundle bundle = new Bundle();
        bundle.putString(CollectionData.KEY_ID, targetCollection.getId());

        // make sure that the app doesn't hold any collections
        app.setCollections(null);

        try {
            detailsActivity.restoreCollection(bundle, null);
            fail("Activity didn't detect that there are no collections to restore");
        } catch (NullPointerException ex) {
        }

        // now add the fake collections and try again
        app.setCollections(fakeCollections);

        CollectionData restoredCollection = detailsActivity.restoreCollection(bundle, null);
        assertEquals("Activity didn't restore the collection from intent bundle",
                targetCollection.getId(), restoredCollection.getId());
    }

    @Test
    public void testActivity_restoreCollectionFromSavedInstance() {
        Bundle bundle = new Bundle();
        bundle.putString(CollectionData.KEY_ID, targetCollection.getId());

        // add the fake collections and the target collection
        app.setCollections(fakeCollections);
        detailsActivity.collection = targetCollection;

        // Stop and restart the activity
        detailsActivity.finish();
        detailsActivity = (DetailsActivity) getActivity();

        assertEquals("Activity didn't restore the collection from saved instance bundle",
                targetCollection.getId(), detailsActivity.collection.getId());
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

}