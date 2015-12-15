package net.steppschuh.stockbrowser.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import net.steppschuh.stockbrowser.R;
import net.steppschuh.stockbrowser.shutterstock.CollectionData;

import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.TileViewHolder> {

    private Context context;
    private List<CollectionData> collections;

    public CollectionAdapter(Context context, List<CollectionData> collections) {
        this.context = context;
        this.collections = collections;
    }

    @Override
    public TileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the card item layout
        View contactView = inflater.inflate(R.layout.tile_list_item, parent, false);

        // Return a new holder instance
        TileViewHolder tileViewHolder = new TileViewHolder(contactView);
        return tileViewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(TileViewHolder tileViewHolder, int position) {
        // Get the data based on position
        CollectionData collection = collections.get(position);

        if (collection.isValidCollection()) {
            // Set item views based on the data
            tileViewHolder.titleText.setText(collection.getName());

            // calculate the maximum dimensions that the ImageView can get
            int imageHeight = (int) context.getResources().getDimension(R.dimen.tile_height);
            int imageWidth = (int) 1.5 * imageHeight;

            // load the cover image into the target
            Picasso.with(context)
                    .load(collection.getCoverItem().getUrl())
                    .resize(imageWidth, imageHeight)
                    .centerCrop()
                    .placeholder(R.color.colorPrimary)
                    .into(tileViewHolder);
        } else {
            // TODO: perform exception handling
        }
    }

    @Override
    public int getItemCount() {
        return collections.size();
    }

    public List<CollectionData> getCollections() {
        return collections;
    }

    public void setCollections(List<CollectionData> collections) {
        this.collections = collections;
    }

    // Direct reference to each view within a collection item
    // Used to cache the views within the item layout for fast access
    public class TileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Target {

        public ImageView coverImage;
        public TextView titleText;
        public LinearLayout titleTextContainer;

        public TileViewHolder(View tileView) {
            super(tileView);
            coverImage = (ImageView) tileView.findViewById(R.id.tile_cover_image);
            titleText = (TextView) tileView.findViewById(R.id.tile_title);
            titleTextContainer = (LinearLayout) tileView.findViewById(R.id.tile_title_container);
            tileView.setOnClickListener(this);
        }

        private void updatePalette(Bitmap bitmap) {
            new Palette.Builder(bitmap).generate(new Palette.PaletteAsyncListener() {
                public void onGenerated(Palette palette) {
                    // get the dark muted color from the image
                    int muted = palette.getMutedColor(Color.BLACK);
                    int mutedDark = palette.getDarkMutedColor(muted);

                    // adjust the alpha value and use it as title text background
                    int titleBackground = ColorHelper.adjustAlpha(mutedDark, 0.66f);
                    ColorHelper.fadeBackgroundColor(titleTextContainer, titleBackground);
                }
            });
        }

        @Override
        public void onClick(View view) {
            CollectionData data = collections.get(getLayoutPosition());
            Toast.makeText(context, data.getName(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            // create a TransitionDrawable to get a smooth fade to the new image
            final TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[] {
                    coverImage.getDrawable(),
                    new BitmapDrawable(context.getResources(), bitmap)
            });

            // set and invoke the TransitionDrawable
            coverImage.setImageDrawable(transitionDrawable);
            transitionDrawable.startTransition(ColorHelper.DEFAULT_FADE_DURATION);

            updatePalette(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.w(TileViewHolder.class.getSimpleName(), "Unable to load cover image");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            coverImage.setImageDrawable(placeHolderDrawable);
        }
    }
}