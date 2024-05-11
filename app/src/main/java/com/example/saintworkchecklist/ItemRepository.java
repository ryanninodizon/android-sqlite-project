package com.example.saintworkchecklist;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.saintworkchecklist.data.AppDatabase;
import com.example.saintworkchecklist.data.Item;
import com.example.saintworkchecklist.data.ItemDao;

import java.util.List;

public class ItemRepository {
    private ItemDao itemDao;
    private LiveData<List<Item>> allItems;

    public ItemRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        itemDao = db.itemDao();
        allItems = (LiveData<List<Item>>) itemDao.getAllItems();
    }

    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }

    public void insertItem(Item item) {
        // Use Kotlin coroutine to perform database operation on a background thread
        new InsertAsyncTask(itemDao).execute(item);
    }

    public void deleteItem(Item item) {
        // Use Kotlin coroutine to perform database operation on a background thread
        new DeleteAsyncTask(itemDao).execute(item);
    }

    public void updateItem(Item item) {
        new UpdateItemAsyncTask(itemDao).execute(item);
    }

    // Asynchronous calls. This implementation needs to be updated because AsyncTask class is now deprecated. https://developer.android.com/reference/android/os/AsyncTask
    private static class InsertAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDao asyncTaskDao;

        InsertAsyncTask(ItemDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Item... items) {
            asyncTaskDao.insertItem(items[0]);
            return null;
        }
    }
    
    private static class DeleteAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDao asyncTaskDao;

        DeleteAsyncTask(ItemDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Item... items) {
            asyncTaskDao.deleteItem(items[0]);
            return null;
        }
    }

    private static class UpdateItemAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDao itemDao;

        UpdateItemAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDao.update(items[0]);
            return null;
        }
    }
}

