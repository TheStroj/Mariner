package dev.simonmezgec.mariner.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/** Custom item decoration class for the RecyclerView, which spaces out the individual elements
  * based on the parameters passed to the class. */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class RecyclerViewItemSpacing extends RecyclerView.ItemDecoration {
    // RecyclerView spacing dimension.
    private int mItemOffset;
    // RecyclerView horizontal item span count.
    private int mSpanCount;

    public RecyclerViewItemSpacing(int itemOffset) {
        mItemOffset = itemOffset;
    }

    public RecyclerViewItemSpacing(@NonNull Context context, @DimenRes int itemOffsetId,
                                   int spanCount) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
        mSpanCount = spanCount;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getAdapter() != null) {
            // Set the RecyclerView item offset so that all the RecyclerView margins are equal.
            int itemPosition = parent.getChildAdapterPosition(view);
            if (itemPosition == 0) {
                outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
            }
            else if (itemPosition < mSpanCount) {
                outRect.set(0, mItemOffset, mItemOffset, mItemOffset);
            }
            else if (itemPosition % mSpanCount == 0) {
                outRect.set(mItemOffset, 0, mItemOffset, mItemOffset);
            }
            else {
                outRect.set(0, 0, mItemOffset, mItemOffset);
            }
        }
    }
}
