package dev.simonmezgec.mariner.database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.simonmezgec.mariner.R;

/** Adapter for favorite data assets. */
@SuppressWarnings("CanBeFinal")
public class DataAssetFavoriteAdapter
        extends RecyclerView.Adapter<DataAssetFavoriteAdapter.DataAssetFavoriteAdapterViewHolder> {

    private final DataAssetFavoriteAdapterOnClickHandler mClickHandler;
    private Context mContext;
    private List<DataAssetFavorite> mDataAssets;

    @SuppressWarnings("unused")
    public interface DataAssetFavoriteAdapterOnClickHandler {
        void onClick(DataAssetFavorite dataAssetFavorite, View view);
    }

    public DataAssetFavoriteAdapter(DataAssetFavoriteAdapterOnClickHandler clickHandler,
                                    Context context) {
        mClickHandler = clickHandler;
        mContext = context;
    }

    @NonNull
    @Override
    public DataAssetFavoriteAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,
                                                         int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.data_asset_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new DataAssetFavoriteAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAssetFavoriteAdapterViewHolder
                                             dataAssetFavoriteAdapterViewHolder,
                                 int position) {
        DataAssetFavorite dataAsset = mDataAssets.get(position);
        TextView nameTextView = dataAssetFavoriteAdapterViewHolder.mNameTextView;
        TextView dateTextView = dataAssetFavoriteAdapterViewHolder.mDateTextView;
        TextView priceTextView = dataAssetFavoriteAdapterViewHolder.mPriceTextView;
        TextView authorTextView = dataAssetFavoriteAdapterViewHolder.mAuthorTextView;
        String name = dataAsset.getName();
        if (!name.equals("")) {
            nameTextView.setText(name);
        }
        String date = dataAsset.getParsedDateCreated();
        if (!date.equals("")) dateTextView.setText(date);
        boolean isFree = dataAsset.isFree();
        if (isFree) priceTextView.setText(mContext.getString(R.string.price_free));
        else priceTextView.setText(mContext.getString(R.string.price_paid));
        String author = dataAsset.getAuthor();
        if (!author.equals(""))
            authorTextView.setText(String.format(mContext.getString(R.string.by_author), author));
    }

    @Override
    public int getItemCount() {
        if (mDataAssets == null) return 0;
        return mDataAssets.size();
    }

    public void setDataAssets(List<DataAssetFavorite> dataAssets) {
        mDataAssets = dataAssets;
        notifyDataSetChanged();
    }

    public DataAssetFavorite getDataAsset(int position) {
        return mDataAssets.get(position);
    }

    @SuppressWarnings("unused")
    public class DataAssetFavoriteAdapterViewHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.item_constraint_layout)
        ConstraintLayout mItemConstraintLayout;
        @BindView(R.id.nameTextView)
        TextView mNameTextView;
        @BindView(R.id.dateTextView)
        TextView mDateTextView;
        @BindView(R.id.priceTextView)
        TextView mPriceTextView;
        @BindView(R.id.authorTextView)
        TextView mAuthorTextView;

        DataAssetFavoriteAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            DataAssetFavorite dataAsset = mDataAssets.get(adapterPosition);
            mClickHandler.onClick(dataAsset, view);
        }
    }
}