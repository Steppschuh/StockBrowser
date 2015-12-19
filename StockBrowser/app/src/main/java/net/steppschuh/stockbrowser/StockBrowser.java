package net.steppschuh.stockbrowser;

import android.app.Activity;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import net.steppschuh.stockbrowser.shutterstock.CollectionData;
import net.steppschuh.stockbrowser.shutterstock.ShutterStockApi;

import java.util.List;

public class StockBrowser extends MultiDexApplication {

    public static final String TAG = StockBrowser.class.getSimpleName();

    private boolean initialized = false;
    private Activity contextActivity;

    private ShutterStockApi shutterStockApi;
    private List<CollectionData> collections;

    public void initialize(Activity contextActivity) {
        Log.d(TAG, "Initializing " + TAG);

        this.contextActivity = contextActivity;
        shutterStockApi = new ShutterStockApi();

        initialized = true;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public Activity getContextActivity() {
        return contextActivity;
    }

    public void setContextActivity(Activity contextActivity) {
        this.contextActivity = contextActivity;
    }

    public ShutterStockApi getShutterStockApi() {
        return shutterStockApi;
    }

    public void setShutterStockApi(ShutterStockApi shutterStockApi) {
        this.shutterStockApi = shutterStockApi;
    }

    public List<CollectionData> getCollections() {
        return collections;
    }

    public void setCollections(List<CollectionData> collections) {
        this.collections = collections;
    }
}
