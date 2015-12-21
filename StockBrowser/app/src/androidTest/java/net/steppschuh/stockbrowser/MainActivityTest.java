package net.steppschuh.stockbrowser;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import net.steppschuh.stockbrowser.shutterstock.CollectionData;
import net.steppschuh.stockbrowser.shutterstock.CoverItem;
import net.steppschuh.stockbrowser.ui.CollectionAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends ActivityInstrumentationTestCase2 {

    StockBrowser app;
    Activity mainActivity;
    View decorView;

    FloatingActionButton fab;
    RecyclerView recyclerView;

    List<CollectionData> fakeCollections;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());

        // Prevent UI controls from taking focus
        setActivityInitialTouchMode(true);

        // Start the main activity of the application under test
        mainActivity = getActivity();

        // Get a reference to the application
        app = (StockBrowser) mainActivity.getApplicationContext();

        // Get a reference for all the tested views
        decorView = mainActivity.getWindow().getDecorView();
        recyclerView = (RecyclerView) mainActivity.findViewById(R.id.recycler_view);
        fab = (FloatingActionButton) mainActivity.findViewById(R.id.fab);

        // Generate some collections to test with
        fakeCollections = generateFakeCollections(25);
    }

    public void testPreconditions() {
        assertNotNull(MainActivity.class.getSimpleName() + " is null", mainActivity);
        assertNotNull(StockBrowser.class.getSimpleName() + " is null", app);
    }

    @Test
    public void testPermission_storage() {
        String grantPermissionCommand = "adb shell pm grant " + app.getPackageName() + " android.permission.WRITE_EXTERNAL_STORAGE";
        String message = "WRITE_EXTERNAL_STORAGE permission is required but not granted. Other tests depending on this may fail.\n" + grantPermissionCommand;

        int expectedPermissionStatus = PackageManager.PERMISSION_GRANTED;
        int actualPermissionStatus = ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        assertEquals(message, expectedPermissionStatus, actualPermissionStatus);
    }

    @Test
    public void testPermission_internet() {
        int expectedPermissionStatus = PackageManager.PERMISSION_GRANTED;
        int actualPermissionStatus = ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.INTERNET);
        assertEquals("INTERNET permission is required but not granted", expectedPermissionStatus, actualPermissionStatus);
    }

    @Test
    public void testFloatingActionButton_layout() {
        ViewAsserts.assertOnScreen(decorView, fab);

        final ViewGroup.LayoutParams layoutParams = fab.getLayoutParams();
        assertNotNull(layoutParams);
        assertEquals(layoutParams.width, WindowManager.LayoutParams.WRAP_CONTENT);
        assertEquals(layoutParams.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @SmallTest
    public void testRecyclerView_layout() {
        ViewAsserts.assertOnScreen(decorView, recyclerView);

        final ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        assertNotNull(layoutParams);
        assertEquals(layoutParams.width, WindowManager.LayoutParams.MATCH_PARENT);
        assertEquals(layoutParams.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Test
    public void testRecyclerView_restoreCollections() {
        // Add fake collections to the application
        app.setCollections(fakeCollections);

        // Also update the adapter
        ((CollectionAdapter) recyclerView.getAdapter()).setCollections(fakeCollections);

        //Spoon.screenshot(mainActivity, "before_restart");

        // Stop and restart the activity
        mainActivity.finish();
        mainActivity = getActivity();

        //Spoon.screenshot(mainActivity, "after_restart");

        // Get the adapter
        recyclerView = (RecyclerView) mainActivity.findViewById(R.id.recycler_view);
        CollectionAdapter collectionAdapter = (CollectionAdapter) recyclerView.getAdapter();

        // The adapter should still hold all the fake collections
        assertEquals("The collection adapter didn't restore its items", fakeCollections.size(), collectionAdapter.getItemCount());

        // The order of items should be maintained
        String originalFirstItemId = fakeCollections.get(0).getId();
        String restoredFirstItemId = collectionAdapter.getCollections().get(0).getId();
        assertEquals("The collection order has been changed", originalFirstItemId, restoredFirstItemId);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Returns a list of collections that can be used to test the recyclerview
     */
    private List<CollectionData> generateFakeCollections(int count) {
        List<CollectionData> collections  = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            // create a collection
            CollectionData collection = new CollectionData();
            collection.setId(String.valueOf(10000000 + i));
            collection.setName("Collection #" + i);

            // create a cover
            CoverItem coverItem = new CoverItem();
            coverItem.setUrl("https://ak.picdn.net/assets/cms/" + collection.getId() + ".jpg");
            collection.setCoverItem(coverItem);

            // add the collection
            collections.add(collection);
        }

        return collections;
    }

}