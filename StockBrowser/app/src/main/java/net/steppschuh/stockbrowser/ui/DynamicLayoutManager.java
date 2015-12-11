package net.steppschuh.stockbrowser.ui;

import android.support.v7.widget.StaggeredGridLayoutManager;

public class DynamicLayoutManager extends StaggeredGridLayoutManager {

    public DynamicLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
        setGapStrategy(GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
    }

}
