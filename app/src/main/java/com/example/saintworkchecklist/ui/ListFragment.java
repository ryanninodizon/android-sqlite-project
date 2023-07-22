package com.example.saintworkchecklist.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saintworkchecklist.R;
import com.example.saintworkchecklist.viewmodel.ItemViewModel;

public class ListFragment extends  Fragment {

    private ItemViewModel itemViewModel;
    private RecyclerView recyclerView;
    private ItemListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        itemViewModel.getAllItems().observe(getViewLifecycleOwner(), items -> {
            if (adapter == null) {
                adapter = new ItemListAdapter(items, itemViewModel, recyclerView);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.updateData(items); // Update the adapter's data and reapply the filter
            }
        });

        // Set up the SearchView
        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        Button buttonUnselectAll = view.findViewById(R.id.buttonUnselectAll);
        buttonUnselectAll.setOnClickListener(v -> {
            if (adapter != null && itemViewModel != null) {
                adapter.unselectAllItems(itemViewModel);
            }
        });

        return view;
    }
}

