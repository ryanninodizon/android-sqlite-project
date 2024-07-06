package com.example.saintworkchecklist.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saintworkchecklist.R;
import com.example.saintworkchecklist.data.CheckedItemsTracker;
import com.example.saintworkchecklist.data.Item;
import com.example.saintworkchecklist.viewmodel.ItemViewModel;
import java.util.ArrayList;
import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> implements Filterable {

    private final CheckedItemsTracker checkedItemsTracker;
    private List<Item> itemList;
    private List<Item> filteredItemList;
    private ItemViewModel itemViewModel; // ViewModel reference
    private String currentFilterQuery = "";
    private RecyclerView recyclerView;

    public ItemListAdapter(List<Item> itemList, ItemViewModel itemViewModel, RecyclerView recyclerView) {
        this.itemList = itemList;
        this.filteredItemList = new ArrayList<>(itemList);
        this.checkedItemsTracker = new CheckedItemsTracker();
        this.itemViewModel = itemViewModel;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = filteredItemList.get(position);

        holder.textFirstName.setText(item.FirstName);
        holder.textLastName.setText(item.LastName);
        holder.textStreetName.setText(item.StreetName);
        holder.textStreetNumber.setText(String.valueOf(item.StreetNumber));
        // Set the checkbox state based on the item's isDone value
        holder.checkboxDone.setChecked(item.isDone == 1);

        holder.checkboxDone.setOnClickListener(view -> {
            CheckBox checkbox = holder.checkboxDone;
            Item clickedItem = filteredItemList.get(holder.getAdapterPosition()); // Get the correct item from the filtered list
            boolean newCheckedState = checkbox.isChecked();

            // Show a confirmation dialog before updating the checkbox state
            showConfirmationDialog(clickedItem, newCheckedState, holder.itemView.getContext(), position);
        });
    }

    @Override
    public int getItemCount() {
        return filteredItemList.size(); // Return the size of the filtered list
    }
    public void updateData(List<Item> itemList) {
        this.itemList = itemList;
        getFilter().filter(currentFilterQuery); // Reapply the filter with the current query
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textFirstName;
        TextView textLastName;
        TextView textStreetName;
        TextView textStreetNumber;
        CheckBox checkboxDone;

        ViewHolder(View itemView) {
            super(itemView);
            textFirstName = itemView.findViewById(R.id.textFirstName);
            textLastName = itemView.findViewById(R.id.textLastName);
            textStreetName = itemView.findViewById(R.id.textStreetName);
            textStreetNumber = itemView.findViewById(R.id.textStreetNumber);
            checkboxDone = itemView.findViewById(R.id.checkboxDone);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchText = charSequence.toString().toLowerCase().trim();

                List<Item> filteredList = new ArrayList<>();
                if (searchText.isEmpty()) {
                    filteredList.addAll(itemList); // If search text is empty, show all items
                } else {
                    for (Item item : itemList) {
                        if (item.StreetName.toLowerCase().contains(searchText)) {
                            filteredList.add(item);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;

                // Store the current filter query
                currentFilterQuery = searchText;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                List<Item> filteredList = (List<Item>) filterResults.values;

                // Clear the previous filtered list and add only the filtered items
                filteredItemList.clear();
                filteredItemList.addAll(filteredList);

                // Check if the filter query is empty or same as the previous one,
                // if yes, notify only the filtered items have changed.
                if (currentFilterQuery.isEmpty() || currentFilterQuery.equals(charSequence.toString())) {
                    notifyDataSetChanged();
                } else {
                    // If the filter query has changed, notifyDataSetChanged will reload the whole list
                    // but it may cause inconsistencies. Instead, we can use notifyItemRangeChanged() to
                    // update the specific items that have changed.
                    notifyItemRangeChanged(0, filteredItemList.size());
                }
            }
        };
    }

    private void showConfirmationDialog(Item item, boolean newCheckedState, Context context, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmation");

        String confirmationMessage = "Is this the correct reciever?\n\n";
        confirmationMessage +=item.FirstName + " " + item.LastName + " \n";
        confirmationMessage +=item.StreetName + " " + item.StreetNumber + " \n";
        confirmationMessage +="\n If yes, Good job Saint!  ";

        if(!newCheckedState){
            confirmationMessage = "Do you want to continue unchecking this";
        }

        builder.setMessage(confirmationMessage);
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // User confirmed, update the checkbox state
            updateCheckBoxState(item, newCheckedState, context, position);
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // User canceled, reset the checkbox state
            CheckBox checkbox = ((ViewHolder) recyclerView.findViewHolderForAdapterPosition(position)).checkboxDone;
            checkbox.setChecked(!newCheckedState);
        });
        builder.show();
    }

    private void updateCheckBoxState(Item item, boolean newCheckedState, Context context, int position) {
        if (item.isDone == (newCheckedState ? 1 : 0)) {
            // No need to update the database if the checkbox state is not changed
            return;
        }
        // Update the item status in the database
        item.isDone = newCheckedState ? 1 : 0;
        ItemViewModel itemViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(ItemViewModel.class);
        itemViewModel.updateItemStatus(item);
    }
    public void unselectAllItems(ItemViewModel itemViewModel) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this.recyclerView.getContext());
        builder.setTitle("Confirmation");
        builder.setMessage("Start checking all over again? Kung oo, edi wow!");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            checkedItemsTracker.clear(); // Clear all tracked checkbox states
            notifyDataSetChanged(); // Notify the adapter to update the UI
            // Update the database for each item in the list
            for (Item item : itemList) {
                item.isDone = 0; // Set isDone to 0 (unchecked) for all items
                itemViewModel.updateItemStatus(item); // Update the item in the database
            }
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            return;
        });
        builder.show();
    }

}
