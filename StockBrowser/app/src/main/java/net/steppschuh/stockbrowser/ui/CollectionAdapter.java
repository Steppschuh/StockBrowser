package net.steppschuh.stockbrowser.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.steppschuh.stockbrowser.R;
import net.steppschuh.stockbrowser.shutterstock.CollectionData;

import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CardViewHolder> {

    private Context context;
    private List<CollectionData> collections;

    public CollectionAdapter(Context context, List<CollectionData> collections) {
        this.context = context;
        this.collections = collections;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the card item layout
        View contactView = inflater.inflate(R.layout.tile_list_item, parent, false);

        // Return a new holder instance
        CardViewHolder cardViewHolder = new CardViewHolder(contactView);
        return cardViewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int position) {
        // Get the data based on position
        CollectionData collection = collections.get(position);

        // TODO: validate collection data

        // Set item views based on the data
        ImageView coverImage = cardViewHolder.coverImage;
        Picasso.with(context)
                .load(collection.getCoverItem().getUrl())
                .fit()
                .centerCrop()
                .into(coverImage);

        cardViewHolder.headingText.setText(collection.getName());

        // Dynamically resize the view
        if (false) {
            boolean fullSpan = Math.random() < 0.2;
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) cardViewHolder.itemView.getLayoutParams();
            layoutParams.setFullSpan(fullSpan);
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
    public static class CardViewHolder extends RecyclerView.ViewHolder {

        public ImageView coverImage;
        public TextView headingText;

        public CardViewHolder(View cardView) {
            super(cardView);
            coverImage = (ImageView) cardView.findViewById(R.id.card_cover_image);
            headingText = (TextView) cardView.findViewById(R.id.card_heading);
        }
    }
}