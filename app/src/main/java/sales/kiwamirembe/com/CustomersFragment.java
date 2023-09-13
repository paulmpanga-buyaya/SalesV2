package sales.kiwamirembe.com;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sales.kiwamirembe.com.adapters.CustomerAdapter;
import sales.kiwamirembe.com.classes.Customer;
import sales.kiwamirembe.com.databases.SalesDatabase;

public class CustomersFragment extends Fragment implements CustomerAdapter.CustomerAdapterListener {

    private RecyclerView recyclerView;
    private List<Customer> customerList = new ArrayList<>();
    private CustomerAdapter mAdapter;
    private SearchView searchView;
    private ProgressDialog progressDialog;

    private MainActivity mainActivity;

    private static final String SAVE_CUSTOMER_URL = "https://kiwamirembe.com/gpt/save_cust.php";
    private static final String GET_CUSTOMERS_URL = "https://kiwamirembe.com/gpt/get-location-customers.php";


    public CustomersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // This enables the Fragment to handle menu events
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_customers, container, false);
        View root =  inflater.inflate(R.layout.fragment_customers, container, false);
        //Toolbar toolbar = root.findViewById(R.id.customers_toolbar);
        // Find the toolbar inside the fragment layout
        Toolbar toolbar = root.findViewById(R.id.customers_toolbar);
        // Set the toolbar as the activity's action bar
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Customers");

        recyclerView = root.findViewById(R.id.customersRecyclerView);
        setupRecyclerView();

        searchView = root.findViewById(R.id.searchAllCustomers);
        setupSearchView();

        SalesDatabase databaseHelper1 = new SalesDatabase(getActivity());
       /* int localRows = databaseHelper1.numberOfRows(SalesDatabase.customers_table);
        if (localRows <= 0){
            //getItems();
            getCustomers(1);
        }*/

        return root;
    }

    public void setupRecyclerView() {
        // Initialize and set up your RecyclerView and mAdapter here
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SalesDatabase databaseHelper1 = new SalesDatabase(getActivity());
        customerList = databaseHelper1.getAllCustomers();
        mAdapter = new CustomerAdapter(getActivity(), customerList, this);
        recyclerView.setAdapter(mAdapter);
    }

    public void refreshCustomerList() {
        // Fetch updated customer data from the database
        SalesDatabase databaseHelper = new SalesDatabase(requireContext());
        List<Customer> updatedCustomers = databaseHelper.getAllCustomers();

        // Update the adapter's data and notify it
        mAdapter.updateData(updatedCustomers);
        mAdapter.notifyDataSetChanged();
    }


    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
    }

    @Override
    public void onCustomerSelected(Customer customer, int count) {
        // Handle item selection as needed
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.customers_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        menu.getItem(0).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_baseline_person_add_24));
        menu.getItem(1).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.baseline_cloud_sync_24));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.sync_customers){

            mainActivity.getCustomers(1);

            /*SalesDatabase databaseHelper = new SalesDatabase(getActivity());
            //databaseHelper.resetCustomerSyncStatus();
            List<Customer> customersList1 = new ArrayList<>();
            customersList1 = databaseHelper.getUnsyncedCustomers();
            for (Customer customer : customersList1) {
                //Toast.makeText(getActivity(), customer.getCustomer_name(),Toast.LENGTH_SHORT).show();
                mainActivity.saveCustomer(customer, new OnSaveCustomerListener() {
                    @Override
                    public void onSaveCustomer(boolean isSuccess) {
                        if (isSuccess) {
                            // Handle successful fetch
                            // You can return true here or perform any other action
                            databaseHelper.updateCustomerSyncStatus(customer.getCustomer_id(),1);
                        } else {
                            // Handle fetch failure
                            // You can return false here or perform any other error-handling actions
                            databaseHelper.updateCustomerSyncStatus(customer.getCustomer_id(),2);
                        }
                    }
                });
            }*/
        }
        if (id == R.id.add_customers){
            //Toast.makeText(getActivity(), "Add Customer",Toast.LENGTH_SHORT).show();
            AddCustomerDialogFragment dialogFragment = new AddCustomerDialogFragment();
            dialogFragment.show(getFragmentManager(), "AddCustomerDialogFragment");
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    /*public void getCustomers(int locationID) {
        ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", "Fetching customers...", true);
        StringRequest request = new StringRequest(Request.Method.POST, GET_CUSTOMERS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            int onlineRows = jsonObject.getInt("num_of_rows");
                            //JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (success == 1) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                List<Customer> customersToInsert = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    int customer_id = object.getInt("customer_id");
                                    String customer_business_name = object.getString("customer_business_name");
                                    String customer_name = object.getString("customer_name");
                                    String customer_email = object.getString("customer_email");
                                    String customer_phone = object.getString("customer_phone");
                                    //int customer_webSynced = object.getInt("customer_webSynced");

                                    Customer customer = new Customer(customer_id, customer_business_name, customer_name, customer_email, customer_phone, 1);
                                    customersToInsert.add(customer);

                                }

                                // Save customers to the database
                                saveCustomersToDatabase(customersToInsert);

                                // Update your adapter or UI as needed
                                mAdapter.notifyDataSetChanged();

                                SalesDatabase databaseHelper1 = new SalesDatabase(getActivity());
                                int localRows = databaseHelper1.numberOfRows(SalesDatabase.customers_table);
                                if (localRows == onlineRows){
                                    setupRecyclerView();
                                    // Dismiss the progress dialog
                                    progressDialog.dismiss();
                                }

                            } else {
                                Toast.makeText(getActivity(), "There are no customers", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Unknown Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("locationID", String.valueOf(locationID));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    private void saveCustomersToDatabase(List<Customer> customers) {
        SalesDatabase dbHelper = new SalesDatabase(getActivity());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        //exists(String table, String searchItem, String columnName
        try {
            for (Customer customer : customers) {
                if (!dbHelper.exists("customers", String.valueOf(customer.getCustomer_id()), "customer_id")) {
                    //dbHelper.addInventoryItem(db, inventory);
                    dbHelper.addCustomerWithId(customer);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            dbHelper.close();
        }
    }*/

    public interface OnSaveCustomerListener {
        void onSaveCustomer(boolean isSuccess);
    }
}