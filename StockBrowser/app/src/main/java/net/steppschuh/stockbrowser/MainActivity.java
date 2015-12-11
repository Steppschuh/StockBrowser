package net.steppschuh.stockbrowser;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.steppschuh.stockbrowser.shutterstock.CollectionData;
import net.steppschuh.stockbrowser.shutterstock.ShutterStockApi;
import net.steppschuh.stockbrowser.shutterstock.CollectionList;
import net.steppschuh.stockbrowser.ui.CollectionAdapter;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private StockBrowser app;
    private Subscription featuredCollectionsSubscription;
    private CollectionAdapter collectionAdapter;

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

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // RecylerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        // Create an empty adapter for the recycler view
        collectionAdapter = new CollectionAdapter(this, new ArrayList<CollectionData>());
        recyclerView.setAdapter(collectionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void renderCollections(CollectionList collectionList) {
        if (collectionList != null && collectionList.getData() != null) {
            collectionAdapter.setCollections(collectionList.getData());
        }
    }

    private void subscribeToFeaturedCollections() {
        Observable<CollectionList> call = app.getShutterStockApi().getEndpoints().getFeaturedCollections();
        featuredCollectionsSubscription = call
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CollectionList>() {

                    @Override
                    public void onCompleted() {
                        Log.d(ShutterStockApi.class.getSimpleName(), "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(ShutterStockApi.class.getSimpleName(), "onError");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(CollectionList collectionList) {
                        Log.d(ShutterStockApi.class.getSimpleName(), "onNext");
                        renderCollections(collectionList);
                    }

                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
