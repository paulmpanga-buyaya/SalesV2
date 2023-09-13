package sales.kiwamirembe.com;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sales.kiwamirembe.com.adapters.CustomerAdapter;
import sales.kiwamirembe.com.classes.Customer;
import sales.kiwamirembe.com.databases.SalesDatabase;

public class SelectCustomerFragment extends DialogFragment {

    private SalesDatabase salesDatabase;
    private List<Customer> originalCustomerList;
    private SearchView searchView;
    private LinearLayout selectCustomerParentLayout;
    private CustomerSelectionListener customerSelectionListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.select_customer_dialog_layout, container, false);

        setupToolbar(rootView);
        setupSearchView(rootView);

        selectCustomerParentLayout = rootView.findViewById(R.id.selectCustomerParentLayout);

        // Initialize database and original customer list
        salesDatabase = new SalesDatabase(getActivity());
        originalCustomerList = salesDatabase.getAllCustomers();

        // Populate and filter customer views
        filterCustomerViews("");

        return rootView;
    }

    private void setupToolbar(View rootView) {
        Toolbar toolbar = rootView.findViewById(R.id.select_customer_toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_select_customer));
        toolbar.setNavigationIcon(R.drawable.baseline_close_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void setupSearchView(View rootView) {
        searchView = rootView.findViewById(R.id.searchAssignCustomers);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterCustomerViews(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                filterCustomerViews(query);
                return false;
            }
        });
    }

    private void filterCustomerViews(String query) {
        // Clear existing views
        selectCustomerParentLayout.removeAllViews();

        // Filter customers based on query
        List<Customer> filteredCustomers = new ArrayList<>();
        for (Customer customer : originalCustomerList) {
            if (customerMatchesQuery(customer, query)) {
                filteredCustomers.add(customer);
            }
        }

        // Create and add customer views
        for (Customer customer : filteredCustomers) {
            createAndAddCustomerView(customer);
        }
    }

    private boolean customerMatchesQuery(Customer customer, String query) {
        // Implement your logic to match customer data with query
        return customer.getCustomer_name().toLowerCase().contains(query.toLowerCase())
                || customer.getCustomer_business_name().toLowerCase().contains(query.toLowerCase())
                || customer.getCustomer_email().toLowerCase().contains(query.toLowerCase())
                || customer.getCustomer_phone().toLowerCase().contains(query.toLowerCase());
    }

    private void createAndAddCustomerView(Customer customer) {
        View customerView = LayoutInflater.from(getActivity()).inflate(R.layout.customer_row_layout, selectCustomerParentLayout, false);

        TextView textViewCustomerName = customerView.findViewById(R.id.textViewCustomerName);
        TextView textViewBusinessName = customerView.findViewById(R.id.textViewBusinessName);
        TextView textViewEmail = customerView.findViewById(R.id.textViewEmail);
        TextView textViewPhone = customerView.findViewById(R.id.textViewPhone);

        textViewCustomerName.setText(customer.getCustomer_name());
        textViewBusinessName.setText(customer.getCustomer_business_name());
        textViewEmail.setText(customer.getCustomer_email());
        textViewPhone.setText(customer.getCustomer_phone());

        customerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCustomerClick(customer);
            }
        });

        selectCustomerParentLayout.addView(customerView);
    }

    private void handleCustomerClick(Customer customer) {
        String selectedCustomerName = customer.getCustomer_name();
        int selectedCustomerId = customer.getCustomer_id();
        //Toast.makeText(getActivity(), "Selected: " + selectedCustomerName + " (ID: " + selectedCustomerId + ")", Toast.LENGTH_SHORT).show();
        salesDatabase.updateCartCustomerId(1,selectedCustomerId);
        //dismiss();

        if (customerSelectionListener != null) {
            customerSelectionListener.onCustomerSelected(customer.getCustomer_name(), customer.getCustomer_id());
        }
        dismiss(); // Close the dialog
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
    }

    public interface OnCustomerSelectedListener {
        void onCustomerSelected(String customerName, int customerId);
    }

    public interface CustomerSelectionListener {
        void onCustomerSelected(String customerName, int customerId);
    }

    public void setCustomerSelectionListener(CustomerSelectionListener listener) {
        this.customerSelectionListener = listener;
    }

}
