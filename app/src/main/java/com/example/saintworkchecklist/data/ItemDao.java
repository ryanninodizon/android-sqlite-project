package com.example.saintworkchecklist.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ItemDao {
    @Query("SELECT * FROM saintdb_Sheet1")
    LiveData<List<Item>> getAllItems();

    @Insert
    void insertItem(Item item);

    @Delete
    void deleteItem(Item item);

    @Update
    void update(Item item);
}

