package sales.kiwamirembe.com;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sales.kiwamirembe.com.adapters.ItemAdapter;
import sales.kiwamirembe.com.classes.Category;
import sales.kiwamirembe.com.classes.Item;
import sales.kiwamirembe.com.databases.ItemsDatabase;

public class ItemsFragment extends Fragment implements ItemAdapter.ItemAdapterListener {

   /* private static final String GET_ITEMS_URL = "https://kiwamirembe.com/gpt/get-items.php";
    private static final String GET_CATEGORIES_URL = "https://kiwamirembe.com/gpt/get-categories.php";*/
    private RecyclerView recyclerView;
    private List<Item> itemList = new ArrayList<>();
    private ItemAdapter mAdapter;
    private SearchView searchView;
    private ProgressDialog progressDialog;
    private PopupWindow popupMenu;

    public ItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_items, container, false);
        // Set up the Toolbar
        Toolbar toolbar = rootView.findViewById(R.id.items_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_items));

        recyclerView = rootView.findViewById(R.id.productsRecyclerView);
        setupRecyclerView(0);

        searchView = rootView.findViewById(R.id.searchAllProducts);
        // Call the setupSearchView method from the utility class
        //SearchViewHelper.setupSearchView(searchView, mAdapter);

        setupSearchView();

        //getItems();

       /* ItemsDatabase databaseHelper1 = new ItemsDatabase(getActivity());
        int localRows = databaseHelper1.numberOfRows(ItemsDatabase.items_table);
        if (localRows <= 0){
            getItems();
        }*/

        setHasOptionsMenu(true); // Enable options menu in this fragment
        return rootView;
    }

    private void setupRecyclerView(int num) {
        if (num == 0){

            // Initialize and set up your RecyclerView and mAdapter here
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            ItemsDatabase databaseHelper1 = new ItemsDatabase(getActivity());
            itemList = databaseHelper1.getAllItems();
            mAdapter = new ItemAdapter(getActivity(), itemList, this);
            recyclerView.setAdapter(mAdapter);

        } else {

            ItemsDatabase databaseHelper1 = new ItemsDatabase(getActivity());
            Category category = databaseHelper1.getCategoryById(num);
            // Initialize and set up your RecyclerView and mAdapter here
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            itemList = databaseHelper1.getItemsByCategoryId(Integer.parseInt(category.getCategory_sku()));
            mAdapter = new ItemAdapter(getActivity(), itemList, this);
            recyclerView.setAdapter(mAdapter);

        }

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
    public void onItemSelected(Item item, int count) {
        // Handle item selection as needed
    }

   /* public void getItems() {
        progressDialog = ProgressDialog.show(getActivity(), "", "Fetching items...", true);
        StringRequest request = new StringRequest(Request.Method.POST, GET_ITEMS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        itemList.clear();
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
                                mAdapter.notifyDataSetChanged();

                                ItemsDatabase itemsDatabase1 = new ItemsDatabase(getActivity());
                                int itemsLocalRows = itemsDatabase1.numberOfRows(ItemsDatabase.items_table);
                                int categoriesLocalRows = itemsDatabase1.numberOfRows(ItemsDatabase.categories_table);
                                if (itemsLocalRows == itemsOnlineRows){
                                    //setupRecyclerView();
                                    // Dismiss the progress dialog
                                    progressDialog.dismiss();
                                    getCategories();
                                }

                            } else {
                                Toast.makeText(getActivity(), R.string.no_items_found, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), R.string.error_parsing_response, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getActivity(), getString(R.string.error_message_prefix) + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    private void saveItemsToDatabase(List<Item> items) {
      ItemsDatabase itemsDatabase1 = new ItemsDatabase(getActivity());

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
        progressDialog = ProgressDialog.show(getActivity(), "", "Fetching categories...", true);
        StringRequest request = new StringRequest(Request.Method.POST, GET_CATEGORIES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        itemList.clear();
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
                                mAdapter.notifyDataSetChanged();

                                ItemsDatabase itemsDatabase1 = new ItemsDatabase(getActivity());
                                int categoriesLocalRows = itemsDatabase1.numberOfRows(ItemsDatabase.categories_table);
                               // int categoriesLocalRows = itemsDatabase1.numberOfRows(ItemsDatabase.categories_table);
                                if (categoriesLocalRows == categoriesOnlineRows){
                                    setupRecyclerView(0);
                                    // Dismiss the progress dialog
                                    progressDialog.dismiss();
                                }

                            } else {
                                Toast.makeText(getActivity(), R.string.no_items_found, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), R.string.error_parsing_response, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getActivity(), getString(R.string.error_message_prefix) + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    private void saveCategoriesToDatabase(List<Category> categories) {
        ItemsDatabase itemsDatabase1 = new ItemsDatabase(getActivity());

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
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.items_menu, menu);
        // Get the specific menu item by ID and set its icon programmatically
        MenuItem menuFilterItems = menu.findItem(R.id.filter_items);
        if (menuFilterItems != null) {
            menuFilterItems.setIcon(R.drawable.baseline_filter_list_24);
        }
        MenuItem menuSyncItems = menu.findItem(R.id.sync_items);
        if (menuSyncItems != null) {
            menuSyncItems.setIcon(R.drawable.baseline_cloud_sync_24);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.filter_items) {
            // Handle the menu item click here
            ///showAddCustomerDialog();
            View anchorView = getActivity().findViewById(R.id.filter_items); // Replace with the actual ID of your menu item
            showCategoryPopupMenu(anchorView);
            return true;
        }
        if (id == R.id.sync_items) {
            // Handle the menu item click here
            //getItems();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showCategoryPopupMenu(View anchorView) {
        View popupView = getLayoutInflater().inflate(R.layout.popup_menu_layout, null);
        LinearLayout popmenuLayout = popupView.findViewById(R.id.popmenu_layout);

        ItemsDatabase itemsDatabase = new ItemsDatabase(getActivity());
        List<Category> categories = itemsDatabase.getAllCategories();

        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            View categoryView = getLayoutInflater().inflate(R.layout.category_layout, null);
            TextView categoryIdTextView = categoryView.findViewById(R.id.category_id);
            TextView categoryNameTextView = categoryView.findViewById(R.id.category_name);

            categoryIdTextView.setText(String.valueOf(category.getCategory_id()));
            categoryNameTextView.setText(category.getCategory_name());

            final String categoryId = String.valueOf(category.getCategory_id());
            final String categoryName = category.getCategory_name();

            categoryView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showToast(categoryId, categoryName);
                    popupMenu.dismiss(); // Dismiss the popup menu
                }
            });

            popmenuLayout.addView(categoryView);

            if (i < categories.size() - 1) {
                View dividerLine = getLayoutInflater().inflate(R.layout.divider_line, popmenuLayout, false);
                popmenuLayout.addView(dividerLine);
            }
        }

        PopupWindow popupMenu = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupMenu.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupMenu.showAsDropDown(anchorView);

        // Store the reference to the popupMenu so it can be dismissed later
        this.popupMenu = popupMenu;
    }

    private void showToast(String categoryId, String categoryName) {
        setupRecyclerView(Integer.parseInt(categoryId));
        // Show toast message if needed
       // String toastMessage = "Category ID: " + categoryId + "\nCategory Name: " + categoryName;
        //Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();

    }


}