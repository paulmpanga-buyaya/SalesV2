package sales.kiwamirembe.com;

import static androidx.core.view.ViewKt.isVisible;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import sales.kiwamirembe.com.classes.Category;
import sales.kiwamirembe.com.classes.Customer;
import sales.kiwamirembe.com.classes.Inventory;
import sales.kiwamirembe.com.classes.InventoryMovement;
import sales.kiwamirembe.com.classes.Item;
import sales.kiwamirembe.com.classes.SessionManager;
import sales.kiwamirembe.com.classes.User;
import sales.kiwamirembe.com.databases.InventoryDatabase;
import sales.kiwamirembe.com.databases.ItemsDatabase;
import sales.kiwamirembe.com.databases.SalesDatabase;

public class MainActivity extends AppCompatActivity {

    private static final String SAVE_CUSTOMER_URL = "https://kiwamirembe.com/gpt/save_cust.php";
    private static final String GET_ITEMS_URL = "https://kiwamirembe.com/gpt/get-items.php";
    private static final String GET_CATEGORIES_URL = "https://kiwamirembe.com/gpt/get-categories.php";
    private static final String GET_INVENTORY_ITEMS_URL = "https://kiwamirembe.com/gpt/get-location-inventory.php";
    private static final String GET_CUSTOMERS_URL = "https://kiwamirembe.com/gpt/get-location-customers.php";
    
    private ProgressDialog progressDialog;
    ItemsDatabase itemsDatabase;
    InventoryDatabase inventoryDatabase;
    SessionManager sessionManager;

    private List<Fragment> fragments;
    private LinearLayout buttonsLayout;
    private int[] iconResources = {
            R.drawable.ic_baseline_shopping_basket_24,
            R.drawable.ic_baseline_products_24,
            R.drawable.ic_baseline_receipts_24,
            R.drawable.ic_baseline_customers_24,
            R.drawable.ic_baseline_settings_24
    };
    private int[] selectedIconResources = {
            R.drawable.ic_baseline_shopping_basket_selected,
            R.drawable.ic_baseline_products_selected,
            R.drawable.ic_baseline_receipts_selected,
            R.drawable.ic_baseline_customers_selected,
            R.drawable.ic_baseline_settings_selected
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
       // sessionManager.checkLogin();

        buttonsLayout = findViewById(R.id.buttons);
        itemsDatabase = new ItemsDatabase(this);
        inventoryDatabase = new InventoryDatabase(this);

        String[] buttonLabels = {
                getResources().getString(R.string.title_sales),
                getResources().getString(R.string.title_items),
                getResources().getString(R.string.title_receipts),
                getResources().getString(R.string.title_customers),
                getResources().getString(R.string.title_settings)
        };


        fragments = new ArrayList<>();
        fragments.add(new SalesFragment());
        fragments.add(new ItemsFragment());
        fragments.add(new ReceiptsFragment());
        fragments.add(new CustomersFragment());
        fragments.add(new SettingsFragment());

        for (int i = 0; i < iconResources.length; i++) {
            View buttonView = getLayoutInflater().inflate(R.layout.layout_button, buttonsLayout, false);
            ImageView imageView = buttonView.findViewById(R.id.section_img);
            TextView textView = buttonView.findViewById(R.id.section_text);

            imageView.setImageResource(iconResources[i]);
            textView.setText(buttonLabels[i]);

            final int index = i;
            final String fragmentTag = buttonLabels[i] + "_fragment_tag"; // Replace this with a meaningful tag
            buttonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadFragment(index, fragmentTag.toLowerCase()); // Pass the tag to the loadFragment method
                    //Toast.makeText(getApplicationContext(), fragmentTag, Toast.LENGTH_SHORT).show();
                }
            });

            buttonsLayout.addView(buttonView);
        }

        loadFragment(0, "sales_fragment_tag"); // Load the initial fragment with the corresponding tag

    }

    public void loadFragment(int index, String tag) {
        for (int i = 0; i < buttonsLayout.getChildCount(); i++) {
            View buttonView = buttonsLayout.getChildAt(i);
            ImageView imageView = buttonView.findViewById(R.id.section_img);
            TextView textView = buttonView.findViewById(R.id.section_text);

            imageView.setImageResource(i == index ? selectedIconResources[i] : iconResources[i]);
            textView.setTextColor(getResources().getColor(i == index ? R.color.purple_700 : R.color.grey_600));
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_container, fragments.get(index), tag);
        ft.commit();
    }

    public void saveCustomer(Customer customer, CustomersFragment.OnSaveCustomerListener listener){
        ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "", "Saving customer...", true);
        StringRequest request = new StringRequest(Request.Method.POST, SAVE_CUSTOMER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            if (success ==1){

                                listener.onSaveCustomer(true);
                                progressDialog.dismiss();

                            } else {

                                listener.onSaveCustomer(false);
                                progressDialog.dismiss();

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Unknown Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("customer_id", String.valueOf(customer.getCustomer_id()));
                params.put("customer_business_name", customer.getCustomer_business_name());
                params.put("customer_name", customer.getCustomer_name());
                params.put("customer_email", customer.getCustomer_email());
                params.put("customer_phone", customer.getCustomer_phone());
                params.put("location_id", String.valueOf(1));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);

    }

    public void getItems(int locationID) {
        progressDialog = ProgressDialog.show(MainActivity.this, "", "Fetching items...", true);
        StringRequest request = new StringRequest(Request.Method.POST, GET_ITEMS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //itemList.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            int itemsOnlineRows = jsonObject.getInt("num_of_rows");
                            int categoriesOnlineRows = jsonObject.getInt("category_rows");

                            if (success == 1) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                List<Item> itemsToInsert = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    int itemId = object.getInt("product_id");
                                    String itemName = object.getString("product_name");
                                    String itemSku = object.getString("product_sku");
                                    float itemPrice = (float) object.getDouble("product_price");
                                    int unitId = object.getInt("unit_id");
                                    int categoryId = object.getInt("category_id");

                                    Item item = new Item(itemId, itemName, itemSku, itemPrice, unitId, categoryId, 1);
                                    itemsToInsert.add(item);

                                }

                                // Save items to the database
                                saveItemsToDatabase(itemsToInsert);

                                // Update your adapter or UI as needed
                               // mAdapter.notifyDataSetChanged();

                                ItemsDatabase itemsDatabase1 = new ItemsDatabase(MainActivity.this);
                                int itemsLocalRows = itemsDatabase1.numberOfRows(ItemsDatabase.items_table);
                                int categoriesLocalRows = itemsDatabase1.numberOfRows(ItemsDatabase.categories_table);
                                if (itemsLocalRows == itemsOnlineRows){
                                    //setupRecyclerView();
                                    // Dismiss the progress dialog
                                    progressDialog.dismiss();
                                    getCategories();
                                    getInventory(locationID);
                                }

                            } else {
                                Toast.makeText(MainActivity.this, R.string.no_items_found, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, R.string.error_parsing_response, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MainActivity.this, getString(R.string.error_message_prefix) + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);
    }

    private void saveItemsToDatabase(List<Item> items) {
        ItemsDatabase itemsDatabase1 = new ItemsDatabase(MainActivity.this);

        SQLiteDatabase db = itemsDatabase1.getWritableDatabase();
        db.beginTransaction();

        try {
            for (Item item : items) {
                if (!itemsDatabase1.exists("items", item.getItem_sku(), "item_sku")) {
                    itemsDatabase1.addItem(item);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();  // Close the SQLiteDatabase, not the itemsDatabase instance
        }
    }

    public void getCategories() {
        progressDialog = ProgressDialog.show(MainActivity.this, "", "Fetching categories...", true);
        StringRequest request = new StringRequest(Request.Method.POST, GET_CATEGORIES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // itemList.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            int categoriesOnlineRows = jsonObject.getInt("num_of_rows");
                            int unitsOnlineRows = jsonObject.getInt("unit_rows");

                            if (success == 1) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                List<Category> categoriesToInsert = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    int category_id = object.getInt("category_id");
                                    String category_name = object.getString("category_name");
                                    String category_sku = object.getString("category_sku");

                                    Category category = new Category(category_id, category_name, category_sku, 1);
                                    categoriesToInsert.add(category);

                                }

                                // Save items to the database
                                saveCategoriesToDatabase(categoriesToInsert);

                                // Update your adapter or UI as needed
                              //  mAdapter.notifyDataSetChanged();

                                ItemsDatabase itemsDatabase1 = new ItemsDatabase(MainActivity.this);
                                int categoriesLocalRows = itemsDatabase1.numberOfRows(ItemsDatabase.categories_table);
                                // int categoriesLocalRows = itemsDatabase1.numberOfRows(ItemsDatabase.categories_table);
                                if (categoriesLocalRows == categoriesOnlineRows){
                                  //  setupRecyclerView(0);
                                    // Dismiss the progress dialog
                                    progressDialog.dismiss();
                                }

                            } else {
                                Toast.makeText(MainActivity.this, R.string.no_items_found, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, R.string.error_parsing_response, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MainActivity.this, getString(R.string.error_message_prefix) + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);
    }

    private void saveCategoriesToDatabase(List<Category> categories) {
        ItemsDatabase itemsDatabase1 = new ItemsDatabase(MainActivity.this);

        SQLiteDatabase db = itemsDatabase1.getWritableDatabase();
        db.beginTransaction();

        try {
            for (Category category : categories) {
                if (!itemsDatabase1.exists("categories", category.getCategory_sku(), "category_sku")) {
                    itemsDatabase1.addCategory(category);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();  // Close the SQLiteDatabase, not the itemsDatabase instance
        }
    }

    public String generateUniqueString() {
        List<Integer> digits = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            digits.add(i);
        }
        Collections.shuffle(digits);
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 7; i++) {
            int index = random.nextInt(digits.size());
            stringBuilder.append(digits.get(index));
            digits.remove(index);
        }

        return stringBuilder.toString();
    }

    public String getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public static long getTodayTimestamp() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    // Formats the amount with a specified decimal format
    public String formatAmount(double amount) {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        return formatter.format(amount);
    }

    public void getInventory(int locationID) {
        ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "", "Fetching items...", true);

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
                            getCustomers(1);
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("locationID", String.valueOf(locationID));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);
    }

    private void handleInventoryResponse(String response) throws JSONException {
        //inventoryList.clear();
        JSONObject jsonObject = new JSONObject(response);
        int success = jsonObject.getInt("success");
        int onlineRows = jsonObject.getInt("num_of_rows");

        if (success == 1) {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            List<Inventory> inventoryToInsert = parseInventoryItems(jsonArray);

            saveInventoryToDatabase(inventoryToInsert);

            int localRows = inventoryDatabase.numberOfRows(InventoryDatabase.inventory_table);
            if (localRows == onlineRows) {
                //setupRecyclerView();
                //Toast.makeText(MainActivity.this, "Local match online rows", Toast.LENGTH_LONG).show();
                SalesFragment fragment = (SalesFragment) getSupportFragmentManager().findFragmentByTag("sales_fragment_tag");
                if (fragment != null) {
                    fragment.setupRecyclerView();
                }


            } else {
                Toast.makeText(MainActivity.this, "Local rows do not match online rows", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "No inventory available", Toast.LENGTH_LONG).show();
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
        InventoryDatabase dbHelper = new InventoryDatabase(MainActivity.this);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        //exists(String table, String searchItem, String columnName
        try {
            for (Inventory inventory : inventorys) {
                if (!dbHelper.exists("inventory", String.valueOf(inventory.getInventory_item_id()), "inventory_item_id")) {
                    dbHelper.addInventoryItem(inventory);

                    dbHelper.addInventoryMovementWithId(new InventoryMovement(inventory.getInventory_id(),inventory.getInventory_item_id(),"production",inventory.getInventory_item_stock(),"products already in stock",getTodayTimestamp(),
                            "system",3,101,1));
                } else {

                    Item item = itemsDatabase.getItemBySku(inventory.getInventory_item_sku());
                    dbHelper.updateInventoryItemStockBySku(item.getItem_sku(),inventory.getInventory_item_stock());
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

    public void getCustomers(int locationID) {
        ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "", "Fetching customers...", true);
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
                                //mAdapter.notifyDataSetChanged();

                                SalesDatabase databaseHelper1 = new SalesDatabase(MainActivity.this);
                                int localRows = databaseHelper1.numberOfRows(SalesDatabase.customers_table);
                                if (localRows == onlineRows){
                                    //setupRecyclerView();
                                    CustomersFragment fragment = (CustomersFragment) getSupportFragmentManager().findFragmentByTag("customers_fragment_tag");
                                    if (fragment != null) {
                                        fragment.setupRecyclerView();
                                    }
                                    // Dismiss the progress dialog
                                    progressDialog.dismiss();
                                }

                            } else {
                                Toast.makeText(MainActivity.this, "There are no customers", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Unknown Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("locationID", String.valueOf(locationID));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);
    }

    private void saveCustomersToDatabase(List<Customer> customers) {
        SalesDatabase dbHelper = new SalesDatabase(MainActivity.this);

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
    }

}