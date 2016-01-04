package net.steppschuh.stockbrowser;

import android.Manifest;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.squareup.spoon.Spoon;

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
    MainActivity mainActivity;
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

        // Generate some collections to test with
        fakeCollections = generateFakeCollections(25);

        // Start the main activity of the application under test
        mainActivity = (MainActivity) getActivity();

        // Get a reference to the application
        app = (StockBrowser) mainActivity.getApplicationContext();

        // Get a reference for all the tested views
        decorView = mainActivity.getWindow().getDecorView();
        recyclerView = (RecyclerView) mainActivity.findViewById(R.id.recycler_view);
        fab = (FloatingActionButton) mainActivity.findViewById(R.id.fab);
    }

    public void testPreconditions() {
        assertNotNull(MainActivity.class.getSimpleName() + " is null", mainActivity);
        assertNotNull(StockBrowser.class.getSimpleName() + " is null", app);
    }

    //@Test
    public void testPermission_storage() {
        String grantPermissionCommand = "adb shell pm grant " + app.getPackageName() + " android.permission.WRITE_EXTERNAL_STORAGE";
        String message = "WRITE_EXTERNAL_STORAGE permission is required but not granted. Tests that generate screenshots may fail.\n" + grantPermissionCommand;

        int expectedPermissionStatus = PackageManager.PERMISSION_GRANTED;
        int actualPermissionStatus = ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        assertEquals(message, expectedPermissionStatus, actualPermissionStatus);
    }

    @Test
    public void testPermission_internet() {
        int expectedPermissionStatus = PackageManager.PERMISSION_GRANTED;
        int actualPermissionStatus = ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.INTERNET);
        assertEquals("INTERNET permission is required but not granted",
                expectedPermissionStatus, actualPermissionStatus);
    }

    @Test
    public void testActivity_orientationChange() {
        int currentOrientation = mainActivity.getResources().getConfiguration().orientation;
        int newOrientation;
        if (currentOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            newOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        } else {
            newOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }

        // force an orientation change
        mainActivity.setRequestedOrientation(newOrientation);

        // TODO: check if the layout adapted properly

        // TODO: check if activity is maintaining its state
    }

    @Test
    public void testActivity_noNetwork() {
        // TODO implement this
    }

    @Test
    public void testActivity_lowMemory() {
        // TODO implement this
    }

    @Test
    public void testActivity_dependenciesAvailable() {
        // TODO implement this
    }

    @Test
    public void testFloatingActionButton_layout() {
        ViewAsserts.assertOnScreen(decorView, fab);

        final ViewGroup.LayoutParams layoutParams = fab.getLayoutParams();
        assertNotNull(layoutParams);
        assertEquals(layoutParams.width, WindowManager.LayoutParams.WRAP_CONTENT);
        assertEquals(layoutParams.height, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Test
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

        recordScreenshot(mainActivity, "before_restart");

        // Stop and restart the activity
        mainActivity.finish();
        mainActivity = (MainActivity) getActivity();

        recordScreenshot(mainActivity, "after_restart");

        // Get the adapter
        recyclerView = (RecyclerView) mainActivity.findViewById(R.id.recycler_view);
        CollectionAdapter collectionAdapter = (CollectionAdapter) recyclerView.getAdapter();

        // The adapter should still hold all the fake collections
        assertEquals("The collection adapter didn't restore its items",
                fakeCollections.size(), collectionAdapter.getItemCount());

        // The order of items should be maintained
        String originalFirstItemId = fakeCollections.get(0).getId();
        String restoredFirstItemId = collectionAdapter.getCollections().get(0).getId();
        assertEquals("The collection order has been changed",
                originalFirstItemId, restoredFirstItemId);
    }

    @Test
    public void testAdi_subscription() {
        // TODO implement this
    }

    @Test
    public void testApi_parsing() {
        // TODO implement this
    }

    @Test
    public void testApi_authentication() {
        // TODO implement this
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Returns a list of collections that can be used to test the recyclerview
     */
    public static List<CollectionData> generateFakeCollections(int count) {
        List<CollectionData> collections  = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            // create a collection
            CollectionData collection = new CollectionData();
            collection.setId(String.valueOf(10000000 + i));
            collection.setName("Collection #" + i);
            collection.setTotalItemCount(50);

            // create a cover
            CoverItem coverItem = new CoverItem();
            coverItem.setUrl("https://ak.picdn.net/assets/cms/" + collection.getId() + ".jpg");
            collection.setCoverItem(coverItem);

            // add the collection
            collections.add(collection);
        }

        return collections;
    }

    /**
     * Records a screenshot of the currently visible decor view.
     */
    private void recordScreenshot(Activity activity, String tag) {
        // Due to a currently unsolved bug in Spoon, recording screenshots
        // on devices running Android 6.0+ fails.
        // Related issue on GitHub: https://github.com/square/spoon/issues/292
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Spoon.screenshot(activity, tag);
        } else {
            System.out.println("Unable to record screenshot " + tag);
        }
    }

}