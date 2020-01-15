package dev.simonmezgec.mariner.utils;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import dev.simonmezgec.mariner.R;
import dev.simonmezgec.mariner.database.DataAssetFavorite;
import dev.simonmezgec.mariner.database.DataAssetFavoriteAdapter;
import dev.simonmezgec.mariner.database.MarinerDatabase;

/** Utility class for implementing the swipe-to-remove-from-favorites functionality. */
@SuppressWarnings("CanBeFinal")
public class SwipeToFavorite extends ItemTouchHelper.SimpleCallback {
    private DataAssetFavoriteAdapter mAdapter;
    private MarinerDatabase mDatabase;
    private Drawable icon;
    private Drawable background;

    public SwipeToFavorite(DataAssetFavoriteAdapter adapter, MarinerDatabase database,
                           Activity activity) {
        // Implement the remove functionality only when swiping left to avoid accidental actions.
        super(0, ItemTouchHelper.LEFT);
        mAdapter = adapter;
        mDatabase = database;
        background = ContextCompat.getDrawable(activity, R.drawable.favorite_data_asset_background);
        icon = ContextCompat.getDrawable(activity, R.drawable.ic_star_half);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        DataAssetFavorite dataAsset = mAdapter.getDataAsset(position);

        // Remove the data asset from favorites.
        if (mDatabase != null) {
            MarinerExecutor.getInstance().executor().execute(()
                    -> mDatabase.dataAssetDao().deleteDataAssetFavorite(dataAsset));
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c,
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX,
                            float dY,
                            int actionState,
                            boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;

        // Set background bounds when the view is swiped to the left.
        if (dX < 0) {
            background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getRight(),
                    itemView.getBottom());
        }
        // Set background bounds when the view is unswiped.
        else {
            background.setBounds(0, 0, 0, 0);
        }
        background.draw(c);

        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight())/2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight())/2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        // Set the icon and background bounds when the view is swiped to the left.
        if (dX < 0) {
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getRight(),
                    itemView.getBottom());
        }
        // Set the icon and background bounds when the view is unswiped.
        else {
            background.setBounds(0, 0, 0, 0);
        }
        background.draw(c);
        icon.draw(c);
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}