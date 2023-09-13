package sales.kiwamirembe.com;

import static androidx.core.view.ViewKt.isVisible;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sales.kiwamirembe.com.adapters.InventoryAdapter;
import sales.kiwamirembe.com.classes.Cart;
import sales.kiwamirembe.com.classes.CartItem;
import sales.kiwamirembe.com.classes.Inventory;
import sales.kiwamirembe.com.classes.InventoryMovement;
import sales.kiwamirembe.com.classes.Item;
import sales.kiwamirembe.com.databases.InventoryDatabase;
import sales.kiwamirembe.com.databases.ItemsDatabase;
import sales.kiwamirembe.com.databases.SalesDatabase;

public class SalesFragment extends Fragment implements InventoryAdapter.InventoryAdapterListener {

    private RecyclerView recyclerView;
    private List<Inventory> inventoryList = new ArrayList<>();
    private InventoryAdapter iAdapter;
    private SearchView searchView;

    LinearLayout amountTotalText2;
    private InventoryAdapter mAdapter;
    private TextView amount;
    private Menu menu;
    SalesDatabase salesDatabase;
    ItemsDatabase itemsDatabase;
    InventoryDatabase inventoryDatabase;

    private MainActivity mainActivity;

    private androidx.appcompat.app.AlertDialog alertDialog; // Declare the AlertDialog variable

    private static final String GET_INVENTORY_ITEMS_URL = "https://kiwamirembe.com/gpt/get-location-inventory.php";

    public SalesFragment() {
        // Required empty public constructor
    }

    LinearLayout layout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_sales, container, false);
        View rootView = inflater.inflate(R.layout.fragment_sales, container, false);
        // Set up the Toolbar
        Toolbar toolbar = rootView.findViewById(R.id.sales_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_sales));
        setHasOptionsMenu(true); // Enable options menu in this fragment

        layout = mainActivity.findViewById(R.id.buttons);
        layout.setVisibility(View.VISIBLE);

        salesDatabase = new SalesDatabase(getActivity());
        itemsDatabase = new ItemsDatabase(getActivity());
        inventoryDatabase = new InventoryDatabase(getActivity());

        recyclerView = rootView.findViewById(R.id.createSaleInventoryRecycler);
        setupRecyclerView();

        searchView = rootView.findViewById(R.id.searchSaleProducts);
        setupSearchView();

        DecimalFormat formatter = new DecimalFormat("#,###.00");
        amount = rootView.findViewById(R.id.amount);
        amount.setText(String.valueOf(formatter.format(salesDatabase.getTotalCartItemsSum())));

        ItemsDatabase databaseHelper1 = new ItemsDatabase(getActivity());

        int localRowsItems = databaseHelper1.numberOfRows(ItemsDatabase.items_table);
        if (localRowsItems <= 0){
            mainActivity.getItems();
        }

     /*   int localRows = inventoryDatabase.numberOfRows(InventoryDatabase.inventory_table);
        if (localRows <= 0){
            getInventory(1);
        }
*/
        String cart_number = mainActivity.generateUniqueString();
        int active_cart = salesDatabase.numberOfRows(SalesDatabase.carts_table);
        if (active_cart == 0){
            Cart cart = new Cart(0,cart_number,1023);
            salesDatabase.addCart(cart);
        }

        return rootView;
    }

    @Override
    public void onInventorySelected(Inventory inventory) {
         String cart_number = mainActivity.generateUniqueString();
         int active_cart = salesDatabase.numberOfRows(SalesDatabase.carts_table);
         if (active_cart == 0){
             Cart cart = new Cart(0,cart_number,1023);
             salesDatabase.addCart(cart);
         }

         // Retrieve the sku of the selected item
         String selectedItemSku = inventory.getInventory_item_sku();

        // Retrieve the corresponding Item from the database
        Item item = itemsDatabase.getItemBySku(selectedItemSku);

        if (salesDatabase.doesCartItemExist(selectedItemSku)){
            // item exists in the cart
            salesDatabase. updateCartItemQuantityBySku(selectedItemSku,1,1);
        } else {
            // Sale item doesn't exist in the database
            // Create a new sale item based on the selected inventory item
            CartItem cartItem = new CartItem(
                    item.getItem_sku(),
                    item.getItem_name(),
                    item.getItem_price(),
                    1,
                    item.getItem_price(),//we are using price here since one unit multiplied by price will give us a total that is equal to price
                    0
            );
            // Add the new sale item to the database
            salesDatabase.addCartItem(cartItem);
        }
        // Update the total and the TextView
        float total = salesDatabase.getTotalCartItemsSum();
        updateTextView(total);
    }

    private void updateTextView(float newSum) {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        //amount.setText(String.valueOf(formatter.format(salesDatabase.getTotal())));
        //TextView textView = getView().findViewById(R.id.amount);
        amount.setText(String.valueOf(formatter.format(newSum)));
        iAdapter.notifyDataSetChanged();
    }

/*
    public void getInventory(int locationID) {
        ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", "Fetching items...", true);

        StringRequest request = new StringRequest(
                Request.Method.POST, GET_INVENTORY_ITEMS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            handleInventoryResponse(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
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

    private void handleInventoryResponse(String response) throws JSONException {
        inventoryList.clear();
        JSONObject jsonObject = new JSONObject(response);
        int success = jsonObject.getInt("success");
        int onlineRows = jsonObject.getInt("num_of_rows");

        if (success == 1) {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            List<Inventory> inventoryToInsert = parseInventoryItems(jsonArray);

            saveInventoryToDatabase(inventoryToInsert);

            int localRows = inventoryDatabase.numberOfRows(InventoryDatabase.inventory_table);
            if (localRows == onlineRows) {
                setupRecyclerView();
            } else {
                Toast.makeText(getActivity(), "Local rows do not match online rows", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), "No inventory available", Toast.LENGTH_LONG).show();
        }
    }

    private List<Inventory> parseInventoryItems(JSONArray jsonArray) throws JSONException {
        List<Inventory> inventoryToInsert = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            int inventory_id = object.getInt("inventory_id");
            int product_id = object.getInt("product_id");
            int price = object.getInt("price");
            int quantity = object.getInt("quantity");
            String name = object.getString("name");
            String sku = object.getString("sku");

            Inventory inventory = new Inventory(inventory_id, product_id, name, sku, price, quantity, 1);
            inventoryToInsert.add(inventory);
        }
        return inventoryToInsert;
    }

    private void saveInventoryToDatabase(List<Inventory> inventorys) {
        InventoryDatabase dbHelper = new InventoryDatabase(getActivity());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        //exists(String table, String searchItem, String columnName
        try {
            for (Inventory inventory : inventorys) {
                if (!dbHelper.exists("inventory", String.valueOf(inventory.getInventory_item_id()), "inventory_item_id")) {
                    dbHelper.addInventoryItem(inventory);

                    dbHelper.addInventoryMovementWithId(new InventoryMovement(inventory.getInventory_id(),inventory.getInventory_item_id(),"production",inventory.getInventory_item_stock(),"products already in stock",mainActivity.getTodayTimestamp(),
                            "system",3,101,1));
                } else {

                    Item item = itemsDatabase.getItemBySku(inventory.getInventory_item_sku());


                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            dbHelper.close();
        }
    }
*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sales_menu, menu);
        // Get the specific menu item by ID and set its icon programmatically
        MenuItem viewShoppingCart = menu.findItem(R.id.view_shopping_cart);
        MenuItem syncInventory = menu.findItem(R.id.sync_inventory);
        if (viewShoppingCart != null) {
            viewShoppingCart.setIcon(R.drawable.baseline_shopping_cart_24);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void setupRecyclerView() {
        // Initialize and set up your RecyclerView and mAdapter here
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //SalesDatabase databaseHelper1 = new SalesDatabase(getActivity());
        inventoryList = inventoryDatabase.getAllInventoryItems();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        iAdapter = new InventoryAdapter(getActivity(), inventoryList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(iAdapter);
    }

    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                iAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                iAdapter.getFilter().filter(query);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.view_shopping_cart) {
            // Handle the menu item click here
            showViewShoppingCartDialog();
            return true;
        }
        if (id == R.id.sync_inventory) {
            // Handle the menu item click here
            mainActivity.getInventory(1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showViewShoppingCartDialog() {
        mainActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, new ViewShoppingCartFragment())
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        amount.setText(String.valueOf(formatter.format(salesDatabase.getTotalCartItemsSum())));
    }


}