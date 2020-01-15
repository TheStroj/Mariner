package dev.simonmezgec.mariner.utils;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.simonmezgec.mariner.R;
import dev.simonmezgec.mariner.database.DataAsset;
import dev.simonmezgec.mariner.database.DataAssetAdapter;
import dev.simonmezgec.mariner.database.DataAssetFavorite;
import dev.simonmezgec.mariner.database.MarinerDatabase;

/** Utility class for implementing the swipe-to-favorite functionality. */
@SuppressWarnings("CanBeFinal")
public class SwipeToFavoriteAll extends ItemTouchHelper.SimpleCallback {
    private DataAssetAdapter mAdapter;
    private MarinerDatabase mDatabase;
    private Drawable icon;
    private Drawable background;

    public SwipeToFavoriteAll(DataAssetAdapter adapter, MarinerDatabase database,
                           Activity activity) {
        // Implement the favorite functionality only when swiping right to avoid accidental actions.
        super(0, ItemTouchHelper.RIGHT);
        mAdapter = adapter;
        mDatabase = database;
        background = ContextCompat.getDrawable(activity, R.drawable.favorite_data_asset_background);
        icon = ContextCompat.getDrawable(activity, R.drawable.ic_star);
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
        DataAsset dataAsset = mAdapter.getDataAsset(position);
        String oceanId = dataAsset.getOceanId();
        DataAssetFavorite dataAssetFavorite = new DataAssetFavorite(dataAsset.getId(),
                dataAsset.getOceanId(),
                dataAsset.getOwner(),
                dataAsset.getType(),
                dataAsset.getName(),
                dataAsset.getDateCreated(),
                dataAsset.getDatePublished(),
                dataAsset.getAuthor(),
                dataAsset.getLicense(),
                dataAsset.getPrice(),
                dataAsset.getFileContentTypes(),
                dataAsset.getFileSizes(),
                dataAsset.getTags(),
                dataAsset.getCategories(),
                dataAsset.getDescription(),
                dataAsset.getLink());

        // Add the data asset to favorites.
        if (mDatabase != null) {
            MarinerExecutor.getInstance().executor().execute(()
                    -> {
                List<DataAssetFavorite> dataAssetsWithSameId
                        = mDatabase.dataAssetDao().findDataAssetFavoriteById(oceanId);
                if (dataAssetsWithSameId != null) {
                    if (dataAssetsWithSameId.size() == 0) {
                        mDatabase.dataAssetDao().insertDataAssetFavorite(dataAssetFavorite);
                    }
                }
                else mDatabase.dataAssetDao().insertDataAssetFavorite(dataAssetFavorite);
            });
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

        // Set background bounds when the view is swiped to the right.
        if (dX > 0) {
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

        // Set the icon and background bounds when the view is swiped to the right.
        if (dX > 0) {
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
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
        super.onChildDraw(c, recyclerView, viewHolder, dX/3, dY, actionState, isCurrentlyActive);
    }
}