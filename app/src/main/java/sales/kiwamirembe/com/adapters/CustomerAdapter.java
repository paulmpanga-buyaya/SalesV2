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
import sales.kiwamirembe.com.classes.Customer;


public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<Customer> customerList;
    private List<Customer> customerListFiltered;
    private CustomerAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView customerName, businessName, email, phone;

        public MyViewHolder(View view) {
            super(view);
            customerName = view.findViewById(R.id.textViewCustomerName);
            businessName = view.findViewById(R.id.textViewBusinessName);
            email = view.findViewById(R.id.textViewEmail);
            phone = view.findViewById(R.id.textViewPhone);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getContext() instanceof MainActivity) {
                        listener.onCustomerSelected(customerListFiltered.get(getAdapterPosition()), 1);
                    }
                }
            });
        }
    }

    public CustomerAdapter(Context context, List<Customer> customerList, CustomerAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.customerList = customerList;
        this.customerListFiltered = customerList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_row_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Customer customer = customerListFiltered.get(position);
        holder.customerName.setText(customer.getCustomer_name());
        holder.businessName.setText(customer.getCustomer_business_name());
        holder.email.setText(customer.getCustomer_email());
        holder.phone.setText(customer.getCustomer_phone());
    }

    @Override
    public int getItemCount() {
        return customerListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    customerListFiltered = customerList;
                } else {
                    List<Customer> filteredList = new ArrayList<>();
                    for (Customer row : customerList) {
                        if (row.getCustomer_name().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getCustomer_business_name().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    customerListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = customerListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                customerListFiltered = (ArrayList<Customer>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    // Method to update the adapter's data
    public void updateData(List<Customer> newData) {
        customerListFiltered.clear();
        customerListFiltered.addAll(newData);
        notifyDataSetChanged();
    }

    public interface CustomerAdapterListener {
        void onCustomerSelected(Customer customer, int count);
    }
}

