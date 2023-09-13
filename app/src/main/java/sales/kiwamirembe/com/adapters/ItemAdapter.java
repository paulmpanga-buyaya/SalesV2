package sales.kiwamirembe.com.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sales.kiwamirembe.com.MainActivity;
import sales.kiwamirembe.com.R;
import sales.kiwamirembe.com.classes.Item;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<Item> itemList;
    private List<Item> itemListFiltered;
    private ItemAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, sku;
        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.nameTextView);
            sku = view.findViewById(R.id.skuTextView);
            price = view.findViewById(R.id.priceTextView);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //listener.onItemSelected(itemListFiltered.get(getAdapterPosition()));
                    if (view.getContext() instanceof MainActivity) {
                        //itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_layout, parent, false);
                        listener.onItemSelected(itemListFiltered.get(getAdapterPosition()),1);
                    } else {
                    }
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return false;
                }
            });
        }
    }

    public ItemAdapter(Context context, List<Item> itemList, ItemAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.itemList = itemList;
        this.itemListFiltered = itemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_row_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Item item = itemListFiltered.get(position);
        holder.name.setText(item.getItem_name());
        holder.sku.setText(item.getItem_sku());
        holder.price.setText("Price : " + " " +String.valueOf(item.getItem_price()));
    }

    @Override
    public int getItemCount() {
        return itemListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    itemListFiltered = itemList;
                } else {
                    List<Item> filteredList = new ArrayList<>();
                    for (Item row : itemList) {
                        if ( (row.getItem_name().replace(",","")).toLowerCase().contains(charString.toLowerCase()) ||
                                row.getItem_sku().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    itemListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = itemListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itemListFiltered = (ArrayList<Item>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ItemAdapterListener {
        void onItemSelected(Item item,int count);
    }

}



