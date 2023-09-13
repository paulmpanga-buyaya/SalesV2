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

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<CartItem> cartItemList;
    private List<CartItem> cartItemListFiltered;
    private CartItemAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price_and_quantity, total;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.nameTextView);
            total = view.findViewById(R.id.totalTextView);
            price_and_quantity = view.findViewById(R.id.priceQuantityTextView);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCartItemClicked(cartItemListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public CartItemAdapter(Context context, List<CartItem> cartItemList, CartItemAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.cartItemList = cartItemList;
        this.cartItemListFiltered = cartItemList;
    }

    // Method to set the adapter's data
    public void setData(List<CartItem> updatedData) {
        cartItemList.clear(); // Clear the existing data
        cartItemList.addAll(updatedData); // Add the updated data
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item_row_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final CartItem cartItem = cartItemListFiltered.get(position);
        holder.name.setText(cartItem.getCart_item_name());
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        holder.total.setText(String.valueOf(formatter.format(cartItem.getCart_item_total())));
        holder.price_and_quantity.setText("Price : " + " " +String.valueOf(cartItem.getCart_item_price())+" * "+ String.valueOf(cartItem.getCart_item_quantity()));

    }

    @Override
    public int getItemCount() {
        return cartItemListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    cartItemListFiltered = cartItemList;
                } else {
                    List<CartItem> filteredList = new ArrayList<>();
                    for (CartItem row : cartItemList) {
                        // name or SKU match condition
                        if (row.getCart_item_name().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getCart_item_sku().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    cartItemListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = cartItemListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                cartItemListFiltered = (ArrayList<CartItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface CartItemAdapterListener {
        void onCartItemClicked(CartItem cartItem);
    }

}