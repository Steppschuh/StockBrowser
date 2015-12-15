package net.steppschuh.stockbrowser;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.steppschuh.stockbrowser.shutterstock.CollectionData;
import net.steppschuh.stockbrowser.shutterstock.CollectionList;
import net.steppschuh.stockbrowser.ui.CollectionAdapter;
import net.steppschuh.stockbrowser.ui.DynamicLayoutManager;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = StockBrowser.TAG + "." + MainActivity.class.getSimpleName();

    private StockBrowser app;
    private Subscription featuredCollectionsSubscription;
    private CollectionAdapter collectionAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (StockBrowser) getApplicationContext();
        if (!app.isInitialized()) {
            app.initialize(this);
        }

        setupUi();

        subscribeToFeaturedCollections();
    }

    private void setupUi() {
        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subscribeToFeaturedCollections();

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // SwipeRefreshLayout
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                subscribeToFeaturedCollections();
            }
        });

        // RecylerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // Create an empty adapter for the recycler view
        collectionAdapter = new CollectionAdapter(this, new ArrayList<CollectionData>());
        recyclerView.setAdapter(collectionAdapter);

        // Setup a layout manager to make the list more dynamic
        recyclerView.setLayoutManager(new DynamicLayoutManager(2, DynamicLayoutManager.VERTICAL));
    }

    private void subscribeToFeaturedCollections() {
        Observable<CollectionList> call = app.getShutterStockApi().getEndpoints().getFeaturedCollections();
        featuredCollectionsSubscription = call
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CollectionList>() {

                    @Override
                    public void onCompleted() {
                        collectionAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(CollectionList collectionList) {
                        if (collectionList != null && collectionList.getData() != null) {
                            Log.d(TAG, "Received collection list with " + collectionList.getData().size() + " items");

                            // randomize data for demonstration purposes
                            collectionList.randomizeData();

                            // update the adapter data set
                            collectionAdapter.setCollections(collectionList.getData());
                        } else {
                            Log.w(TAG, "Received collection list with invalid data");
                        }
                    }

                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        featuredCollectionsSubscription.unsubscribe();
        super.onDestroy();
    }
}
