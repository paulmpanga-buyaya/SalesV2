package sales.kiwamirembe.com;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sales.kiwamirembe.com.adapters.CartItemAdapter;
import sales.kiwamirembe.com.classes.Cart;
import sales.kiwamirembe.com.classes.CartItem;

import sales.kiwamirembe.com.databases.SalesDatabase;

public class ViewShoppingCartFragment extends Fragment implements
        CartItemAdapter.CartItemAdapterListener, SelectCustomerFragment.OnCustomerSelectedListener {

    // Declare member variables
    private RecyclerView recyclerView;
    private CartItemAdapter cartItemAdapter;
    private SearchView searchView;
    private TextView totalAmountTextView, customerNameTextView, dateTextView;
    private Handler handler;
    private LinearLayout layout;
    private MainActivity mainActivity;
    private AlertDialog alertDialog;
    private SalesDatabase salesDatabase;
    private Toolbar toolbar;

    // Required empty public constructor
    public ViewShoppingCartFragment() {}

    // Called when fragment attaches to MainActivity
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }

    // Inflate the fragment's layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_shopping_cart, container, false);
        setupToolbar(rootView);

        // Initialize views and components
        salesDatabase = new SalesDatabase(getContext());
        setupViews(rootView);

        refreshData();

        return rootView;
    }

    // Sets up the Toolbar with title and navigation
    private void setupToolbar(View rootView) {
        toolbar = rootView.findViewById(R.id.view_shopping_cart_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_shopping_cart));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.baseline_arrow_back_24));
        toolbar.setNavigationOnClickListener(v -> goBack());
        setHasOptionsMenu(true); // Enable options menu in this fragment
    }

    // Initialize views and components
    private void setupViews(View rootView) {
        searchView = rootView.findViewById(R.id.searchCartItems);
        setupSearchView();

        recyclerView = rootView.findViewById(R.id.cartSaleItemsRecyclerView);
        setupRecyclerView();

        totalAmountTextView = rootView.findViewById(R.id.totalAmountTextView);
        //totalAmountTextView.setText(getString(R.string.total_amount, formatAmount(salesDatabase.getTotalCartItemsSum())));
        totalAmountTextView.setText("Total: " + String.valueOf(formatAmount(salesDatabase.getTotalCartItemsSum())));

        customerNameTextView = rootView.findViewById(R.id.customerNameTextView);
        //customerNameTextView.setText(getString(R.string.customer_label, salesDatabase.getCustomerNameByCartId(1)));
        customerNameTextView.setText("Customer: " + salesDatabase.getCustomerNameByCartId(1));

        dateTextView = rootView.findViewById(R.id.dateTextView);
        handler = new Handler(Looper.getMainLooper());
        startUpdatingTime();

        // Inside setupViews method
        Button payButton = rootView.findViewById(R.id.payButton);
        payButton.setOnClickListener(v -> showBottomPaymentDialog());

        layout = mainActivity.findViewById(R.id.buttons);
        layout.setVisibility(View.GONE);
    }

    private void showBottomPaymentDialog() {
        PaymentDialogFragment paymentDialogFragment = new PaymentDialogFragment();
        paymentDialogFragment.show(getParentFragmentManager(), paymentDialogFragment.getTag());
    }

    // Method to handle payment selection
    private void handlePayment(String paymentMethod) {
        // Implement the logic to handle the selected payment method
        alertDialog.dismiss(); // Close the dialog after handling the payment
    }

    // Sets up the RecyclerView
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cartItemAdapter = new CartItemAdapter(getActivity(), salesDatabase.getAllCartItems(), this);
        recyclerView.setAdapter(cartItemAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    // Sets up the SearchView
    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                cartItemAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                cartItemAdapter.getFilter().filter(query);
                return false;
            }
        });
    }

    // Formats the amount with a specified decimal format
    private String formatAmount(double amount) {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        return formatter.format(amount);
    }

    // Starts updating the time
    private void startUpdatingTime() {
        handler.post(updateTimeRunnable);
    }

    // Runnable to update the time
    private Runnable updateTimeRunnable = new Runnable() {
        @Override
        public void run() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss", Locale.getDefault());
            String currentDateTime = dateFormat.format(new Date());
            dateTextView.setText("Date: " + currentDateTime);
            handler.postDelayed(this, 1000); // Update every 1 second
        }
    };

    // Handles cart item clicks
    @Override
    public void onCartItemClicked(CartItem cartItem) {
        // Implement handling of cart item clicks
        // Toast.makeText(getActivity(), cartItem.getCart_item_name(), Toast.LENGTH_SHORT).show();
        showEditCartItemDialog(cartItem);
    }

    // Creates the options menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.shopping_cart_menu, menu);
        updateAssignCustomerMenuItem(menu.findItem(R.id.assign_customer));
        super.onCreateOptionsMenu(menu, inflater);
    }

    // Handles options menu item selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.clear_cart) {
            salesDatabase.clearCart();
            goBack();
            return true;
        }
        if (id == R.id.assign_customer) {
            showAssignCustomerDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Shows the assign customer dialog
    private void showAssignCustomerDialog() {

        SelectCustomerFragment dialogFragment = new SelectCustomerFragment();
        dialogFragment.setCustomerSelectionListener(new SelectCustomerFragment.CustomerSelectionListener() {
            @Override
            public void onCustomerSelected(String customerName, int customerId) {
                // Update the customer name text view
                customerNameTextView.setText("Customer: " + customerName);

                // Update the assign customer menu icon
                Menu menu = toolbar.getMenu();
                MenuItem assignCustomerItem = menu.findItem(R.id.assign_customer);
                updateAssignCustomerMenuItem(assignCustomerItem);

            }
        });
        dialogFragment.show(getFragmentManager(), "select_customer");
    }

    // Shows the fragment to edit the quantity or price of item already in cart
    private void showEditCartItemDialog(CartItem cartItem){
        EditCartItemFragment dialogFragment = new EditCartItemFragment();
        // Create a Bundle to pass data
        Bundle args = new Bundle();
        args.putString("sku", cartItem.getCart_item_sku());
        dialogFragment.setArguments(args);
        dialogFragment.show(getParentFragmentManager(),"edit_cart_item");
    }

    // Updates the assign customer menu item icon
    private void updateAssignCustomerMenuItem(MenuItem assignCustomerItem) {
        String customerName = salesDatabase.getCustomerNameByCartId(1);
        int iconResId = (customerName == null) ? R.drawable.baseline_person_add_24 : R.drawable.ic_baseline_person_added_24;
        assignCustomerItem.setIcon(iconResId);
    }

    // Handles customer selection from SelectCustomerFragment
    @Override
    public void onCustomerSelected(String customerName, int customerId) {
        customerNameTextView.setText(customerName);
        updateAssignCustomerMenuItem(toolbar.getMenu().findItem(R.id.assign_customer));
    }

    // Navigates back to the previous fragment
    private void goBack() {
        /*mainActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, new SalesFragment())
                .commit();*/

        mainActivity.loadFragment(0, "sales_fragment_tag");
    }

    // Method to refresh the data for the RecyclerView
    private void refreshData() {
        // Update your dataset or fetch fresh data here
        List<CartItem> updatedData = salesDatabase.getAllCartItems();
        cartItemAdapter.setData(updatedData);

        // Notify the adapter that the data has changed
        cartItemAdapter.notifyDataSetChanged();
    }


    // Cleans up when fragment is destroyed
    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateTimeRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Call Reload method when the fragment is resumed.
        setupRecyclerView();
    }

}