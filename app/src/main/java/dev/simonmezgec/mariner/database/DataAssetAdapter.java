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

/** Adapter for the data assets from searching. */
@SuppressWarnings("CanBeFinal")
public class DataAssetAdapter
        extends RecyclerView.Adapter<DataAssetAdapter.DataAssetAdapterViewHolder> {

    private final DataAssetAdapterOnClickHandler mClickHandler;
    private Context mContext;
    private List<DataAsset> mDataAssets;

    @SuppressWarnings("unused")
    public interface DataAssetAdapterOnClickHandler {
        void onClick(DataAsset dataAsset, View view);
    }

    public DataAssetAdapter(DataAssetAdapterOnClickHandler clickHandler, Context context) {
        mClickHandler = clickHandler;
        mContext = context;
    }

    @NonNull
    @Override
    public DataAssetAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,
                                                           int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.data_asset_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new DataAssetAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAssetAdapterViewHolder dataAssetAdapterViewHolder,
                                 int position) {
        DataAsset dataAsset = mDataAssets.get(position);
        TextView nameTextView = dataAssetAdapterViewHolder.mNameTextView;
        TextView dateTextView = dataAssetAdapterViewHolder.mDateTextView;
        TextView priceTextView = dataAssetAdapterViewHolder.mPriceTextView;
        TextView authorTextView = dataAssetAdapterViewHolder.mAuthorTextView;
        String name = dataAsset.getName();
        if (!name.equals("")) nameTextView.setText(name);
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

    public void setDataAssets(List<DataAsset> dataAssets) {
        mDataAssets = dataAssets;
        notifyDataSetChanged();
    }

    public DataAsset getDataAsset(int position) {
        return mDataAssets.get(position);
    }

    @SuppressWarnings("unused")
    public class DataAssetAdapterViewHolder
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

        DataAssetAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            DataAsset dataAsset = mDataAssets.get(adapterPosition);
            mClickHandler.onClick(dataAsset, view);
        }
    }
}