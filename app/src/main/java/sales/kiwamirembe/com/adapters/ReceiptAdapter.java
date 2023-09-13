package sales.kiwamirembe.com.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sales.kiwamirembe.com.R;
import sales.kiwamirembe.com.classes.Customer;
import sales.kiwamirembe.com.classes.Sale;
import sales.kiwamirembe.com.databases.SalesDatabase;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.MyViewHolder> implements Filterable {

   // private final Object ReceiptAdapterListener;
    private Context context;
    private List<Sale> receiptList;
    private List<Sale> receiptListFiltered;
    private ReceiptAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, customer, total;
        public MyViewHolder(View view) {
            super(view);
            //business_name = view.findViewById(R.id.business_name);
            date = view.findViewById(R.id.dateTextView);
            customer = view.findViewById(R.id.customerNameTextView);
            total = view.findViewById(R.id.totalAmountTextView);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onReceiptSelected(receiptListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public ReceiptAdapter(Context context, List<Sale> receiptList, ReceiptAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.receiptList = receiptList;
        this.receiptListFiltered = receiptList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.receipts_recycler_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Sale receipt = receiptListFiltered.get(position);
        final DateFormat dateFormatter = SimpleDateFormat.getDateInstance();
        holder.date.setText(dateFormatter.format(new Date(receipt.getSale_date())));
        SalesDatabase databaseHelper = new SalesDatabase(context);
        Customer customer = databaseHelper.getCustomerById(receipt.getSale_customer_id());
        holder.customer.setText(customer.getCustomer_name());
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        holder.total.setText(String.valueOf(formatter.format(receipt.getSale_total())));
    }

    @Override
    public int getItemCount() {
        return receiptListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    receiptListFiltered = receiptList;
                    SalesDatabase databaseHelper = new SalesDatabase(context);
                } else {
                    List<Sale> filteredList = new ArrayList<>();
                    for (Sale row : receiptList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        SalesDatabase databaseHelper = new SalesDatabase(context);
                        Customer customer = databaseHelper.getCustomerById(row.getSale_customer_id());
                        if (customer.getCustomer_name().toLowerCase().contains(charString.toLowerCase()) || customer.getCustomer_business_name().toLowerCase().contains(charString.toLowerCase()) ||
                                customer.getCustomer_phone().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    receiptListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = receiptListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                receiptListFiltered = (ArrayList<Sale>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ReceiptAdapterListener {
        void onReceiptSelected(Sale receipt);
    }
}
