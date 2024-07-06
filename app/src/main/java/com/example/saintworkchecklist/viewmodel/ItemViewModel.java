package com.example.saintworkchecklist.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.saintworkchecklist.ItemRepository;
import com.example.saintworkchecklist.data.Item;
import java.util.List;

public class ItemViewModel extends AndroidViewModel {

    private ItemRepository repository;
    private LiveData<List<Item>> allItems;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        repository = new ItemRepository(application);
        allItems = repository.getAllItems();
    }
    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }
    public void updateItemStatus(Item item) {
        repository.updateItem(item);
    }
}

