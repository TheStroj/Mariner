package dev.simonmezgec.mariner.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.simonmezgec.mariner.Constant;
import dev.simonmezgec.mariner.database.DataAssetAdapter;
import dev.simonmezgec.mariner.utils.JsonUtils;
import dev.simonmezgec.mariner.utils.MarinerExecutor;
import dev.simonmezgec.mariner.utils.NetworkUtils;
import dev.simonmezgec.mariner.R;
import dev.simonmezgec.mariner.utils.RecyclerViewItemSpacing;
import dev.simonmezgec.mariner.utils.SwipeToFavoriteAll;
import dev.simonmezgec.mariner.database.DataAsset;
import dev.simonmezgec.mariner.database.MainViewModel;
import dev.simonmezgec.mariner.database.MarinerDatabase;

import static android.content.Context.MODE_PRIVATE;

/** Fragment that implements data asset searching. */
@SuppressWarnings({"WeakerAccess", "unused"})
public class SearchFragment extends Fragment implements View.OnClickListener,
        PopupMenu.OnMenuItemClickListener, DataAssetAdapter.DataAssetAdapterOnClickHandler {

    @BindView(R.id.dataAssetsScrollView)
    NestedScrollView mDataAssetsScrollView;
    @BindView(R.id.recyclerViewMain)
    RecyclerView mRecyclerViewMain;
    @BindView(R.id.dataAssetsFoundTextView)
    TextView mDataAssetsFoundTextView;
    @BindView(R.id.sortButton)
    ImageButton mSortButton;
    @BindView(R.id.searchQueryEditText)
    EditText mSearchQueryEditText;
    @BindView(R.id.searchButton)
    Button mSearchButton;
    @BindView(R.id.emptySearchWarningTextView)
    TextView mEmptySearchWarningTextView;
    @BindView(R.id.progressBar)
    ProgressBar mLoadingIndicator;

    private MarinerDatabase mDatabase;
    private DataAssetAdapter mDataAssetAdapter;
    private List<DataAsset> mDataAssets;
    private int mCurrentSort = 0;
    private boolean mReadyToSearch = true;
    private boolean mDisplayWarning = false;
    private FloatingActionButton mFab;

    public static SearchFragment newInstance() {
        return new SearchFragment();
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

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Constant.READY_TO_SEARCH)) {
                mReadyToSearch = savedInstanceState.getBoolean(Constant.READY_TO_SEARCH);
            }
            if (savedInstanceState.containsKey(Constant.DISPLAY_WARNING)) {
                mDisplayWarning = savedInstanceState.getBoolean(Constant.DISPLAY_WARNING);
            }
            if (savedInstanceState.containsKey(Constant.CURRENT_SORT)) {
                mCurrentSort = savedInstanceState.getInt(Constant.CURRENT_SORT);
            }
        }

        // Setup the button functionality.
        mSortButton.setOnClickListener(this);
        mSearchButton.setOnClickListener(this);
        mSearchQueryEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });

        if (getActivity() != null) {
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(
                            Constant.SHARED_PREFERENCES, MODE_PRIVATE).edit();
            editor.putBoolean(Constant.PREFERENCES_READY_TO_SEARCH, mReadyToSearch);
            editor.apply();

            // Setup the Floating Action Button (FAB).
            mFab = getActivity().findViewById(R.id.fab);
            mFab.setOnClickListener(v -> {
                mReadyToSearch = true;
                mDisplayWarning = false;

                SharedPreferences.Editor editorFab = getActivity().getSharedPreferences(
                                Constant.SHARED_PREFERENCES, MODE_PRIVATE).edit();
                editorFab.putBoolean(Constant.PREFERENCES_READY_TO_SEARCH, mReadyToSearch);
                editorFab.apply();
                resetCoordinatorLayout();

                mEmptySearchWarningTextView.setVisibility(View.GONE);
                mDataAssetsScrollView.setVisibility(View.GONE);
                mFab.hide();
                mLoadingIndicator.setVisibility(View.GONE);

                AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(500);
                anim.setRepeatMode(Animation.REVERSE);
                mSearchQueryEditText.startAnimation(anim);
                mSearchButton.startAnimation(anim);
                mSearchQueryEditText.setVisibility(View.VISIBLE);
                mSearchButton.setVisibility(View.VISIBLE);
            });
        }

        // Hide and show views as appropriate.
        if (!mReadyToSearch) {
            mSearchQueryEditText.setVisibility(View.GONE);
            mSearchButton.setVisibility(View.GONE);
            mFab.show();
        }
        else mFab.hide();

        if (mDisplayWarning) {
            mDataAssetsScrollView.setVisibility(View.GONE);
            mLoadingIndicator.setVisibility(View.GONE);
            mEmptySearchWarningTextView.setVisibility(View.VISIBLE);
            resetCoordinatorLayout();
        }

        // Setup the RecyclerView and ViewModel for data assets.
        if (this.getContext() != null) setupDataAssetRecyclerView(this.getContext());
        setupDataAssetViewModel();
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constant.READY_TO_SEARCH, mReadyToSearch);
        outState.putBoolean(Constant.DISPLAY_WARNING, mDisplayWarning);
        outState.putInt(Constant.CURRENT_SORT, mCurrentSort);
    }

    @Override
    public void onClick(DataAsset dataAsset, View view) {
        // When clicked, open the data asset URL in the browser.
        String link = dataAsset.getLink();
        Uri uri = Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchButton:
                performSearch();
                break;

            case R.id.sortButton:
                // Setup the sort button.
                PopupMenu popup2 = new PopupMenu(getActivity(), v);
                popup2.setOnMenuItemClickListener(this);
                MenuInflater inflater = popup2.getMenuInflater();
                inflater.inflate(R.menu.actions, popup2.getMenu());
                popup2.show();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // Sort the data assets based on the selected criteria.
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        switch (item.getItemId()) {
            case R.id.action_sort_default:
                mainViewModel.getAllDataAssets()
                        .observe(this, this::setViewModelAction);
                mCurrentSort = 0;
                return true;
            case R.id.action_sort_name:
                mainViewModel.getAllDataAssetsByName()
                        .observe(this, this::setViewModelAction);
                mCurrentSort = 1;
                return true;
            case R.id.action_sort_date:
                mainViewModel.getAllDataAssetsByDate()
                        .observe(this, this::setViewModelAction);
                mCurrentSort = 2;
                return true;
            case R.id.action_sort_author:
                mainViewModel.getAllDataAssetsByAuthor()
                        .observe(this, this::setViewModelAction);
                mCurrentSort = 3;
                return true;
            case R.id.action_sort_price:
                mainViewModel.getAllDataAssetsByPrice()
                        .observe(this, this::setViewModelAction);
                mCurrentSort = 4;
                return true;
            default:
                return false;
        }
    }

    /** Searches Ocean Protocol's Pacific network for data assets and caches them. */
    private void performSearch() {
        String query = mSearchQueryEditText.getText().toString();
        if (!query.equals("")) {
            if (getActivity() != null && getActivity().getCurrentFocus() != null) {
                InputMethodManager inputManager = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager != null) {
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                            .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }

            // Hide and show views as appropriate.
            AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
            anim.setDuration(500);
            anim.setRepeatMode(Animation.REVERSE);
            mSearchQueryEditText.startAnimation(anim);
            mSearchButton.startAnimation(anim);
            mSearchQueryEditText.setVisibility(View.GONE);
            mSearchButton.setVisibility(View.GONE);
            mFab.show();
            mLoadingIndicator.setVisibility(View.VISIBLE);

            // Reset the cached data assets prior to conducting a new search.
            if (mDatabase != null) {
                MarinerExecutor.getInstance().executor().execute(()
                        -> mDatabase.dataAssetDao().nukeDataAssetsTable());
            }

            // Perform the search.
            MarinerExecutor.getInstance().executor().execute(()
                    -> {
                if (NetworkUtils.isNotOnline()) {
                    if (getView() != null) {
                        getView().post(() -> {
                            displayEmptyWarning();
                            showInternetConnectionError();
                        });
                    }
                }
                else {
                    URL dataAssetRequestUrl = NetworkUtils.buildUrl(query);
                    try {
                        String jsonDataAssetResponse
                                = NetworkUtils.getResponseFromHttpUrl(dataAssetRequestUrl);
                        mDataAssets = JsonUtils.parseDataAssetsJson(jsonDataAssetResponse);
                    } catch (Exception e) {
                        if (getView() != null) getView().post(this::displayEmptyWarning);
                        e.printStackTrace();
                    }

                    if (mDataAssets != null) {
                        if (mDataAssets.size() == 0) {
                            if (getView() != null) getView().post(this::displayEmptyWarning);
                        }
                    }
                    else {
                        if (getView() != null) getView().post(this::displayEmptyWarning);
                    }

                    if (mDataAssets != null) {
                        if (mDataAssets.size() > 0 && mDatabase != null) {
                            if (getActivity() != null) {
                                // Store the total number of returned data assets.
                                SharedPreferences prefs = getActivity().getSharedPreferences(
                                        Constant.SHARED_PREFERENCES, MODE_PRIVATE);
                                int totalDataAssets
                                        = prefs.getInt(Constant.PREFERENCES_TOTAL_DATA_ASSETS, 0);
                                totalDataAssets = totalDataAssets + mDataAssets.size();
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putInt(Constant.PREFERENCES_TOTAL_DATA_ASSETS,
                                        totalDataAssets);
                                editor.apply();
                            }
                            for (int i = 0; i < mDataAssets.size(); i++) {
                                mDatabase.dataAssetDao().insertDataAsset(mDataAssets.get(i));
                            }
                        }
                    }
                }
            });

            mReadyToSearch = false;
            if (getActivity() != null) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(
                        Constant.SHARED_PREFERENCES, MODE_PRIVATE).edit();
                editor.putBoolean(Constant.PREFERENCES_READY_TO_SEARCH, mReadyToSearch);
                editor.apply();
            }
        }
    }

    /** Displays a warning that the search returned no data assets. */
    private void displayEmptyWarning() {
        mDisplayWarning = true;
        mDataAssetsScrollView.setVisibility(View.GONE);
        mLoadingIndicator.setVisibility(View.GONE);
        mEmptySearchWarningTextView.setVisibility(View.VISIBLE);
        resetCoordinatorLayout();
    }

    /** Resets the scrolling AppBar to its default state. */
    private void resetCoordinatorLayout() {
        if (getActivity() != null) {
            AppBarLayout appbar = getActivity().findViewById(R.id.app_bar_layout);
            appbar.setExpanded(true,true);
        }
    }

    /** Shows a Toast with an Internet connection error. */
    private void showInternetConnectionError() {
        Toast toast = Toast.makeText(
                getActivity(), R.string.internet_connection_toast, Toast.LENGTH_LONG);
        TextView toastView = toast.getView().findViewById(android.R.id.message);
        if (toastView != null) toastView.setGravity(Gravity.CENTER);
        toast.show();
    }

    /** Sets up the data asset RecyclerView. */
    private void setupDataAssetRecyclerView(Context context) {
        int spanCount = context.getResources().getInteger(R.integer.span_count_main);
        GridLayoutManager layoutManager = new GridLayoutManager(
                context, spanCount, RecyclerView.VERTICAL, false);
        mDataAssetAdapter = new DataAssetAdapter(this, context);
        mRecyclerViewMain.setHasFixedSize(true);
        mRecyclerViewMain.setLayoutManager(layoutManager);
        mRecyclerViewMain.setAdapter(mDataAssetAdapter);
        // The cards in the RecyclerView item CardViews are spaced with the custom item decoration.
        RecyclerViewItemSpacing itemSpacing = new RecyclerViewItemSpacing(context,
                R.dimen.recyclerview_item_spacing, spanCount);
        mRecyclerViewMain.addItemDecoration(itemSpacing);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToFavoriteAll(mDataAssetAdapter, mDatabase,
                getActivity()));
        itemTouchHelper.attachToRecyclerView(mRecyclerViewMain);
    }

    /** Sets up the data asset ViewModel. */
    private void setupDataAssetViewModel() {
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        switch (mCurrentSort) {
            case 0:
                mainViewModel.getAllDataAssets()
                        .observe(this, this::setViewModelAction);
                break;
            case 1:
                mainViewModel.getAllDataAssetsByName()
                        .observe(this, this::setViewModelAction);
                break;
            case 2:
                mainViewModel.getAllDataAssetsByDate()
                        .observe(this, this::setViewModelAction);
                break;
            case 3:
                mainViewModel.getAllDataAssetsByAuthor()
                        .observe(this, this::setViewModelAction);
                break;
            case 4:
                mainViewModel.getAllDataAssetsByPrice()
                        .observe(this, this::setViewModelAction);
                break;
        }
    }

    /** Sets up the data asset ViewModel action. */
    private void setViewModelAction(List<DataAsset> dataAssets) {
        if (!mReadyToSearch && !mDisplayWarning) {
            if (mDataAssetAdapter != null) mDataAssetAdapter.setDataAssets(dataAssets);
            String dataAssetsSize;
            if (dataAssets.size() == 1) {
                dataAssetsSize = String.format(
                        getString(R.string.data_assets_found_single), dataAssets.size());
            }
            else {
                dataAssetsSize = String.format(
                        getString(R.string.data_assets_found_multiple), dataAssets.size());
            }
            mDataAssetsFoundTextView.setText(dataAssetsSize);
            mEmptySearchWarningTextView.setVisibility(View.GONE);
            mLoadingIndicator.setVisibility(View.GONE);
            mDataAssetsScrollView.setVisibility(View.VISIBLE);
        }
        else {
            mDataAssetsScrollView.setVisibility(View.GONE);
        }
    }
}