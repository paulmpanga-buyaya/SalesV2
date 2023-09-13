package sales.kiwamirembe.com.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import sales.kiwamirembe.com.R;
import sales.kiwamirembe.com.classes.CartItem;
import sales.kiwamirembe.com.classes.Inventory;
import sales.kiwamirembe.com.classes.SaleItem;
import sales.kiwamirembe.com.databases.SalesDatabase;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<Inventory> inventoryList;
    private List<Inventory> inventoryListFiltered;
    private InventoryAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, stock;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            price = view.findViewById(R.id.price);
            stock = view.findViewById(R.id.quantity);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onInventorySelected(inventoryListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public InventoryAdapter(Context context, List<Inventory> inventoryList, InventoryAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.inventoryList = inventoryList;
        this.inventoryListFiltered = inventoryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_row_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        SalesDatabase databaseHelper = new SalesDatabase(context);
        final Inventory inventory = inventoryListFiltered.get(position);
        if (databaseHelper.getCartItemBySku(inventory.getInventory_item_sku()) != null){
            final CartItem saleItem = databaseHelper.getCartItemBySku(inventory.getInventory_item_sku());
            holder.name.setText(inventory.getInventory_item_name());
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            holder.price.setText(String.valueOf(formatter.format(inventory.getInventory_item_price())));
            int stock = inventory.getInventory_item_stock() - saleItem.getCart_item_quantity();
            //holder.stock.setText("Qty" + " " + String.valueOf(inventory.getInventory_item_stock()));
            holder.stock.setText("Qty" + " " + String.valueOf(stock));
        } else {
            holder.name.setText(inventory.getInventory_item_name());
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            holder.price.setText(String.valueOf(formatter.format(inventory.getInventory_item_price())));
            holder.stock.setText("Qty" + " " + String.valueOf(inventory.getInventory_item_stock()));
        }
    }

    @Override
    public int getItemCount() {
        return inventoryListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    inventoryListFiltered = inventoryList;
                } else {
                    List<Inventory> filteredList = new ArrayList<>();
                    for (Inventory row : inventoryList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getInventory_item_sku().toLowerCase().contains(charString.toLowerCase()) || row.getInventory_item_name().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    inventoryListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = inventoryListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                inventoryListFiltered = (ArrayList<Inventory>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface InventoryAdapterListener {
        void onInventorySelected(Inventory inventory);
    }

}
