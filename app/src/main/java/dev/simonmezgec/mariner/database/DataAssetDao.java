package dev.simonmezgec.mariner.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/** Class containing all SQLite queries. */
@SuppressWarnings("unused")
@Dao
public interface DataAssetDao {

    @Query("SELECT * FROM data_assets")
    LiveData<List<DataAsset>> loadAllDataAssets();

    @Query("SELECT * FROM data_assets ORDER BY name COLLATE NOCASE ASC")
    LiveData<List<DataAsset>> loadAllDataAssetsByName();

    @Query("SELECT * FROM data_assets ORDER BY date_created COLLATE NOCASE DESC")
    LiveData<List<DataAsset>> loadAllDataAssetsByDate();

    @Query("SELECT * FROM data_assets ORDER BY author COLLATE NOCASE ASC")
    LiveData<List<DataAsset>> loadAllDataAssetsByAuthor();

    @Query("SELECT * FROM data_assets ORDER BY price COLLATE NOCASE ASC")
    LiveData<List<DataAsset>> loadAllDataAssetsByPrice();

    @Insert
    void insertDataAsset(DataAsset dataAsset);

    @Query("DELETE FROM data_assets")
    void nukeDataAssetsTable();

    @Query("SELECT * FROM data_assets_favorites")
    LiveData<List<DataAssetFavorite>> loadAllDataAssetsFavorites();

    @Query("SELECT * FROM data_assets_favorites ORDER BY name COLLATE NOCASE ASC")
    LiveData<List<DataAssetFavorite>> loadAllDataAssetsFavoritesByName();

    @Query("SELECT * FROM data_assets_favorites ORDER BY date_created COLLATE NOCASE DESC")
    LiveData<List<DataAssetFavorite>> loadAllDataAssetsFavoritesByDate();

    @Query("SELECT * FROM data_assets_favorites ORDER BY author COLLATE NOCASE ASC")
    LiveData<List<DataAssetFavorite>> loadAllDataAssetsFavoritesByAuthor();

    @Query("SELECT * FROM data_assets_favorites ORDER BY price COLLATE NOCASE ASC")
    LiveData<List<DataAssetFavorite>> loadAllDataAssetsFavoritesByPrice();

    @Query("SELECT * FROM data_assets_favorites WHERE oceanId LIKE :id")
    List<DataAssetFavorite> findDataAssetFavoriteById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDataAssetFavorite(DataAssetFavorite dataAssetFavorite);

    @Delete
    void deleteDataAssetFavorite(DataAssetFavorite dataAssetFavorite);
}