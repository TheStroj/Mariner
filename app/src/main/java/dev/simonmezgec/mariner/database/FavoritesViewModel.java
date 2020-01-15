package dev.simonmezgec.mariner.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/** ViewModel class for displaying favorite data assets. */
public class FavoritesViewModel extends AndroidViewModel {

    private final LiveData<List<DataAssetFavorite>> mAllDataAssets;
    private final LiveData<List<DataAssetFavorite>> mAllDataAssetsByName;
    private final LiveData<List<DataAssetFavorite>> mAllDataAssetsByDate;
    private final LiveData<List<DataAssetFavorite>> mAllDataAssetsByAuthor;
    private final LiveData<List<DataAssetFavorite>> mAllDataAssetsByPrice;

    public FavoritesViewModel(Application application) {
        super(application);
        MarinerDatabase database = MarinerDatabase.getInstance(this.getApplication());
        mAllDataAssets = database.dataAssetDao().loadAllDataAssetsFavorites();
        mAllDataAssetsByName = database.dataAssetDao().loadAllDataAssetsFavoritesByName();
        mAllDataAssetsByDate = database.dataAssetDao().loadAllDataAssetsFavoritesByDate();
        mAllDataAssetsByAuthor = database.dataAssetDao().loadAllDataAssetsFavoritesByAuthor();
        mAllDataAssetsByPrice = database.dataAssetDao().loadAllDataAssetsFavoritesByPrice();
    }

    public LiveData<List<DataAssetFavorite>> getAllDataAssets() {
        return mAllDataAssets;
    }
    public LiveData<List<DataAssetFavorite>> getAllDataAssetsByName() {
        return mAllDataAssetsByName;
    }
    public LiveData<List<DataAssetFavorite>> getAllDataAssetsByDate() {
        return mAllDataAssetsByDate;
    }
    public LiveData<List<DataAssetFavorite>> getAllDataAssetsByAuthor() {
        return mAllDataAssetsByAuthor;
    }
    public LiveData<List<DataAssetFavorite>> getAllDataAssetsByPrice() {
        return mAllDataAssetsByPrice;
    }
}