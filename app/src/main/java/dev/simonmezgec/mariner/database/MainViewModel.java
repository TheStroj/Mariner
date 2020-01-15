package dev.simonmezgec.mariner.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/** ViewModel class for displaying the data assets from searching. */
public class MainViewModel extends AndroidViewModel {

    private final LiveData<List<DataAsset>> mAllDataAssets;
    private final LiveData<List<DataAsset>> mAllDataAssetsByName;
    private final LiveData<List<DataAsset>> mAllDataAssetsByDate;
    private final LiveData<List<DataAsset>> mAllDataAssetsByAuthor;
    private final LiveData<List<DataAsset>> mAllDataAssetsByPrice;

    public MainViewModel(Application application) {
        super(application);
        MarinerDatabase database = MarinerDatabase.getInstance(this.getApplication());
        mAllDataAssets = database.dataAssetDao().loadAllDataAssets();
        mAllDataAssetsByName = database.dataAssetDao().loadAllDataAssetsByName();
        mAllDataAssetsByDate = database.dataAssetDao().loadAllDataAssetsByDate();
        mAllDataAssetsByAuthor = database.dataAssetDao().loadAllDataAssetsByAuthor();
        mAllDataAssetsByPrice = database.dataAssetDao().loadAllDataAssetsByPrice();
    }

    public LiveData<List<DataAsset>> getAllDataAssets() {
        return mAllDataAssets;
    }
    public LiveData<List<DataAsset>> getAllDataAssetsByName() {
        return mAllDataAssetsByName;
    }
    public LiveData<List<DataAsset>> getAllDataAssetsByDate() {
        return mAllDataAssetsByDate;
    }
    public LiveData<List<DataAsset>> getAllDataAssetsByAuthor() {
        return mAllDataAssetsByAuthor;
    }
    public LiveData<List<DataAsset>> getAllDataAssetsByPrice() {
        return mAllDataAssetsByPrice;
    }
}