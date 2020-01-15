package dev.simonmezgec.mariner.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.simonmezgec.mariner.Constant;
import dev.simonmezgec.mariner.database.DataAssetFavoriteAdapter;
import dev.simonmezgec.mariner.R;
import dev.simonmezgec.mariner.utils.RecyclerViewItemSpacing;
import dev.simonmezgec.mariner.utils.SwipeToFavorite;
import dev.simonmezgec.mariner.database.DataAssetFavorite;
import dev.simonmezgec.mariner.database.FavoritesViewModel;
import dev.simonmezgec.mariner.database.MarinerDatabase;

/** Fragment that displays favorite data assets. */
@SuppressWarnings({"WeakerAccess", "unused"})
public class FavoritesFragment extends Fragment implements View.OnClickListener,
        PopupMenu.OnMenuItemClickListener,
        DataAssetFavoriteAdapter.DataAssetFavoriteAdapterOnClickHandler {

    @BindView(R.id.favoriteDataAssetsScrollView)
    NestedScrollView mFavoriteDataAssetsScrollView;
    @BindView(R.id.recyclerViewFavorites)
    RecyclerView mRecyclerViewFavorites;
    @BindView(R.id.favoriteDataAssetsTextView)
    TextView mFavoriteDataAssetsTextView;
    @BindView(R.id.sortFavoritesButton)
    ImageButton mSortFavoritesButton;
    @BindView(R.id.emptyFavoritesWarningTextView)
    TextView mEmptyFavoritesWarningTextView;

    private MarinerDatabase mDatabase;
    private DataAssetFavoriteAdapter mDataAssetAdapter;
    private int mCurrentSort = 0;

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = MarinerDatabase.getInstance(getActivity());
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.bind(this, view);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Constant.CURRENT_SORT_FAVORITES)) {
                mCurrentSort = savedInstanceState.getInt(Constant.CURRENT_SORT_FAVORITES);
            }
        }

        // Setup the button functionality.
        mSortFavoritesButton.setOnClickListener(this);
        if (getActivity() != null) {
            AppBarLayout appbar = getActivity().findViewById(R.id.app_bar_layout);
            appbar.setExpanded(true,true);
        }

        // Setup the RecyclerView and ViewModel for favorite data assets.
        if (this.getContext() != null) setupFavoriteDataAssetRecyclerView(this.getContext());
        setupFavoriteDataAssetViewModel();
        return view;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constant.CURRENT_SORT_FAVORITES, mCurrentSort);
    }

    @Override
    public void onClick(DataAssetFavorite dataAsset, View view) {
        // Open the data asset URL in the browser.
        String link = dataAsset.getLink();
        Uri uri = Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sortFavoritesButton) {
            // Setup the sort button.
            PopupMenu popup2 = new PopupMenu(getActivity(), v);
            popup2.setOnMenuItemClickListener(this);
            MenuInflater inflater = popup2.getMenuInflater();
            inflater.inflate(R.menu.actions, popup2.getMenu());
            popup2.show();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // Sort the data assets based on the selected criteria.
        FavoritesViewModel favoritesViewModel
                = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        switch (item.getItemId()) {
            case R.id.action_sort_default:
                favoritesViewModel.getAllDataAssets()
                        .observe(this, this::setViewModelAction);
                mCurrentSort = 0;
                return true;
            case R.id.action_sort_name:
                favoritesViewModel.getAllDataAssetsByName()
                        .observe(this, this::setViewModelAction);
                mCurrentSort = 1;
                return true;
            case R.id.action_sort_date:
                favoritesViewModel.getAllDataAssetsByDate()
                        .observe(this, this::setViewModelAction);
                mCurrentSort = 2;
                return true;
            case R.id.action_sort_author:
                favoritesViewModel.getAllDataAssetsByAuthor()
                        .observe(this, this::setViewModelAction);
                mCurrentSort = 3;
                return true;
            case R.id.action_sort_price:
                favoritesViewModel.getAllDataAssetsByPrice()
                        .observe(this, this::setViewModelAction);
                mCurrentSort = 4;
                return true;
            default:
                return false;
        }
    }

    /** Sets up the favorite data asset RecyclerView. */
    private void setupFavoriteDataAssetRecyclerView(Context context) {
        int spanCount = context.getResources().getInteger(R.integer.span_count_main);
        GridLayoutManager layoutManager = new GridLayoutManager(
                context, spanCount, RecyclerView.VERTICAL, false);
        mDataAssetAdapter = new DataAssetFavoriteAdapter(this, context);
        mRecyclerViewFavorites.setHasFixedSize(true);
        mRecyclerViewFavorites.setLayoutManager(layoutManager);
        mRecyclerViewFavorites.setAdapter(mDataAssetAdapter);
        // The cards in the RecyclerView item CardViews are spaced with the custom item decoration.
        RecyclerViewItemSpacing itemSpacing = new RecyclerViewItemSpacing(context,
                R.dimen.recyclerview_item_spacing, spanCount);
        mRecyclerViewFavorites.addItemDecoration(itemSpacing);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToFavorite(mDataAssetAdapter, mDatabase,
                getActivity()));
        itemTouchHelper.attachToRecyclerView(mRecyclerViewFavorites);
    }

    /** Sets up the favorite data asset ViewModel. */
    private void setupFavoriteDataAssetViewModel() {
        FavoritesViewModel favoritesViewModel
                = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        switch (mCurrentSort) {
            case 0:
                favoritesViewModel.getAllDataAssets()
                        .observe(this, this::setViewModelAction);
                break;
            case 1:
                favoritesViewModel.getAllDataAssetsByName()
                        .observe(this, this::setViewModelAction);
                break;
            case 2:
                favoritesViewModel.getAllDataAssetsByDate()
                        .observe(this, this::setViewModelAction);
                break;
            case 3:
                favoritesViewModel.getAllDataAssetsByAuthor()
                        .observe(this, this::setViewModelAction);
                break;
            case 4:
                favoritesViewModel.getAllDataAssetsByPrice()
                        .observe(this, this::setViewModelAction);
                break;
        }
    }

    /** Sets up the favorite data asset ViewModel action. */
    private void setViewModelAction(List<DataAssetFavorite> dataAssets) {
        if (dataAssets.size() == 0) {
            mFavoriteDataAssetsScrollView.setVisibility(View.GONE);
            mEmptyFavoritesWarningTextView.setVisibility(View.VISIBLE);
        }
        else {
            mEmptyFavoritesWarningTextView.setVisibility(View.GONE);
            mFavoriteDataAssetsScrollView.setVisibility(View.VISIBLE);
            if (mDataAssetAdapter != null) mDataAssetAdapter.setDataAssets(dataAssets);
            String dataAssetsSize;
            if (dataAssets.size() == 1) {
                dataAssetsSize = String.format(getString(
                        R.string.data_assets_favorite_single), dataAssets.size());
            }
            else {
                dataAssetsSize = String.format(getString(
                        R.string.data_assets_favorite_multiple), dataAssets.size());
            }
            mFavoriteDataAssetsTextView.setText(dataAssetsSize);
        }
    }
}