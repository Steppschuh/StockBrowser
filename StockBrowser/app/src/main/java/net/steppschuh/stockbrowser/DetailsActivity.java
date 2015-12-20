package net.steppschuh.stockbrowser;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import net.steppschuh.stockbrowser.shutterstock.CollectionData;
import net.steppschuh.stockbrowser.ui.AnimationHelper;
import net.steppschuh.stockbrowser.ui.ColorHelper;
import net.steppschuh.stockbrowser.ui.ImageHelper;

public class DetailsActivity extends AppCompatActivity {

    public static final String TAG = StockBrowser.TAG + "." + DetailsActivity.class.getSimpleName();

    private StockBrowser app;
    private CollectionData collection;

    private ImageView coverImage;
    private TextView titleText;
    private LinearLayout titleTextContainer;

    private Bitmap originalCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        app = (StockBrowser) getApplicationContext();
        if (!app.isInitialized()) {
            app.initialize(this);
        }

        try {
            collection = restoreCollection(getIntent().getExtras(), savedInstanceState);
        } catch (NullPointerException exception) {
            Log.e(TAG, "Unable to restore the collection");
            exception.printStackTrace();
        }

        setupUi();
        setupTransitions();
    }

    /**
     * Parses the passed bundles and tries to return the desired collection
     */
    private CollectionData restoreCollection(Bundle intentExtras, Bundle savedInstanceExtras) throws NullPointerException {
        if (app.getCollections() == null) {
            // TODO: fetch collections instead of throwing an exception
            throw new NullPointerException("No collections available");
        }

        String collectionId = null;

        if (intentExtras != null && savedInstanceExtras == null) {
            // activity started for the first time, use intent bundle
            collectionId = intentExtras.getString(CollectionData.KEY_ID);
        } else {
            // activity restored, use saved instance bundle
            collectionId = savedInstanceExtras.getString(CollectionData.KEY_ID);
        }

        if (collectionId == null) {
            throw new NullPointerException("No collection ID specified");
        }

        for (CollectionData collection : app.getCollections()) {
            if (collection.getId().equals(collectionId)) {
                return collection;
            }
        }

        throw new NullPointerException("No collection found with ID: " + collectionId);
    }

    private void setupUi() {
        // Views
        coverImage = (ImageView) findViewById(R.id.tile_cover_image);
        titleText = (TextView) findViewById(R.id.tile_title);
        titleTextContainer = (LinearLayout) findViewById(R.id.tile_title_container);

        // Data
        if (collection != null) {
            titleText.setText(collection.getName());

            int imageHeight = (int) getResources().getDimension(R.dimen.tile_height);
            int imageWidth = (int) 1.5 * imageHeight;

            // create a target for Picasso to load the cover image in
            Target coverTarget = new Target() {
                @Override
                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                    // copy the image for later (exit transition)
                    originalCover = bitmap;

                    // set the image as cover
                    coverImage.setImageDrawable(new BitmapDrawable(getResources(), bitmap));

                    // blur the image in a new thread for a transition drawable
                    (new Thread() {
                        @Override
                        public void run() {
                            try {
                                // resize the image for faster processing
                                Bitmap resizedBitmap = ImageHelper.getResizedBitmap(bitmap, bitmap.getWidth() / 4);

                                // invoke a renderscript to blur the image
                                final Bitmap blurredCover = ImageHelper.blurBitmap(resizedBitmap, ImageHelper.RADIUS_SMALL, getApplicationContext());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // update the image view
                                        updateCoverImage(blurredCover);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    // calculate a palette in order to update the UI colors
                    updatePalette(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    Log.w(TAG, "Unable to load cover image");
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            // Load the cover image
            Picasso.with(this)
                    .load(collection.getCoverItem().getUrl())
                    .resize(imageWidth, imageHeight)
                    .centerCrop()
                    .placeholder(R.color.colorPrimary)
                    .into(coverTarget);

        }
    }

    private void setupTransitions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        // Enter transition
        Transition enterTransition = new AutoTransition();
        enterTransition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {

            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        getWindow().setSharedElementEnterTransition(enterTransition);

        // Return transition
        Transition returnTransition = new AutoTransition();
        returnTransition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                // Fade to the original cover image to get a smooth transition
                if (originalCover != null) {
                    updateCoverImage(originalCover);
                }
            }

            @Override
            public void onTransitionEnd(Transition transition) {

            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        getWindow().setSharedElementReturnTransition(returnTransition);
    }

    private void updateCoverImage(Bitmap bitmap) {
        // create a TransitionDrawable to get a smooth fade to the new image
        final TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[] {
                coverImage.getDrawable(),
                new BitmapDrawable(getResources(), bitmap)
        });

        // set and invoke the TransitionDrawable
        coverImage.setImageDrawable(transitionDrawable);
        transitionDrawable.startTransition(AnimationHelper.DEFAULT_FADE_DURATION);
    }

    private void updatePalette(Bitmap bitmap) {
        final Activity contextActivity = this;

        new Palette.Builder(bitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                // get the needed colors from the image
                int muted = palette.getMutedColor(Color.BLACK);
                int mutedDark = palette.getDarkMutedColor(muted);

                // adjust the alpha value and use it as title text background
                int titleBackground = ColorHelper.adjustAlpha(mutedDark, ColorHelper.HARD_OVERLAY_ALPHA);
                ColorHelper.fadeBackgroundColor(titleTextContainer, titleBackground);

                // also tint the status bar
                ColorHelper.fadeStatusBarToColor(contextActivity, mutedDark, AnimationHelper.DEFAULT_FADE_DURATION);
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

        switch (id) {
            case R.id.action_about: {
                // TODO: add about page
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (collection != null) {
            savedInstanceState.putString(CollectionData.KEY_ID, collection.getId());
        }

        super.onSaveInstanceState(savedInstanceState);
    }

}
