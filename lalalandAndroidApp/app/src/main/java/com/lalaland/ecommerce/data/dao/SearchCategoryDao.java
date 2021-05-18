package com.lalaland.ecommerce.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.lalaland.ecommerce.data.models.globalSearch.SearchCategory;

import java.util.List;

@Dao
public interface SearchCategoryDao {

    @Query("SELECT * FROM search_category")
    LiveData<List<SearchCategory>> getAllCategory();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategory(SearchCategory... categories);

    @Delete
    void deleteCategory(SearchCategory categories);


    @Query("DELETE from search_category")
    void deleteAllSearches();

}