package sales.kiwamirembe.com;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sales.kiwamirembe.com.adapters.CartItemAdapter;
import sales.kiwamirembe.com.classes.Cart;
import sales.kiwamirembe.com.classes.CartItem;
import sales.kiwamirembe.com.classes.Inventory;
import sales.kiwamirembe.com.classes.InventoryMovement;
import sales.kiwamirembe.com.classes.Item;
import sales.kiwamirembe.com.classes.Sale;
import sales.kiwamirembe.com.classes.SaleItem;
import sales.kiwamirembe.com.databases.InventoryDatabase;
import sales.kiwamirembe.com.databases.ItemsDatabase;
import sales.kiwamirembe.com.databases.SalesDatabase;

public class CashPaymentFragment extends Fragment {

    // Declare member variables
    private TextView amountDueTextView, customerNameTextView, dateTextView;
    private TextInputEditText cashReceived;
    private SalesDatabase salesDatabase;
    private InventoryDatabase inventoryDatabase;
    private ItemsDatabase itemsDatabase;
    private MaterialButton paymentReceived;
    private List<CartItem> cartItemList = new ArrayList<>();
    private Cart cart;
    private MainActivity mainActivity;

    private static final String SAVE_INVENTORY_MOVEMENT_URL = "https://kiwamirembe.com/gpt/add-inventory-movement.php";

    public CashPaymentFragment() {
        // Required empty public constructor
    }

    // Called when fragment attaches to MainActivity
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
        View rootView = inflater.inflate(R.layout.fragment_cash_payment, container, false);
        setupToolbar(rootView);
        setHasOptionsMenu(true); // Enable options menu in this fragment

        salesDatabase = new SalesDatabase(getActivity());
        inventoryDatabase = new InventoryDatabase(getActivity());
        itemsDatabase = new ItemsDatabase(getActivity());
        setupViews(rootView);

        return rootView;
    }

    private void setupToolbar(View rootView) {
        Toolbar toolbar = rootView.findViewById(R.id.cash_payment_toolbar);
        //toolbar.setTitle(getResources().getString(R.string.title_select_customer));
        toolbar.setNavigationIcon(R.drawable.baseline_close_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               goBack();
            }
        });
    }

    // Initialize views and components
    private void setupViews(View rootView) {

        amountDueTextView = rootView.findViewById(R.id.amountDueTextView);
        amountDueTextView.setText(String.valueOf(formatAmount(salesDatabase.getTotalCartItemsSum())));
        cashReceived = rootView.findViewById(R.id.cashReceived);
        cashReceived.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String originalText = cashReceived.getText().toString();
                String formattedText = formatAmountWithCommas(originalText);
                cashReceived.removeTextChangedListener(this);
                cashReceived.setText(formattedText);
                cashReceived.setSelection(formattedText.length());
                cashReceived.addTextChangedListener(this);

            }
        });
        paymentReceived = rootView.findViewById(R.id.receivedCash);
        paymentReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String formattedText = cashReceived.getText().toString();
                String numericValue = formattedText.replace(",", "");
                recordPaymentAndTransaction(numericValue);

            }
        });

    }

    public void recordPaymentAndTransaction(String numericValue){

        float cashReceived = Float.parseFloat(numericValue);
        float cashDue = salesDatabase.getTotalCartItemsSum();

        if (cashReceived > cashDue) {
            float cashBalance = cashReceived - cashDue;
            cart = salesDatabase.getCartById(1);
            cartItemList = salesDatabase.getAllCartItems();

            salesDatabase.addSale(new Sale(cart.getCart_number(),cart.getCart_customer_id(),salesDatabase.getTotalCartItemsSum(),getTodayTimestamp(),1,0));
            for (int counter = 0; counter < cartItemList.size(); counter++) {
                salesDatabase.addSaleItem(new SaleItem(cart.getCart_number(),cartItemList.get(counter).getCart_item_name(),cartItemList.get(counter).getCart_item_sku(),
                        cartItemList.get(counter).getCart_item_price(),cartItemList.get(counter).getCart_item_quantity(),cartItemList.get(counter).getCart_item_total(),0));
                inventoryDatabase.reduceInventoryItemStock(cartItemList.get(counter).getCart_item_sku(),cartItemList.get(counter).getCart_item_quantity());

                Item item = itemsDatabase.getItemBySku(cartItemList.get(counter).getCart_item_sku());

                InventoryMovement inventoryMovement = new InventoryMovement(item.getItem_id(),"sale",cartItemList.get(counter).getCart_item_quantity(),"sold items",
                        mainActivity.getTodayTimestamp(),"user",1,0,0);

                inventoryDatabase.addInventoryMovement(inventoryMovement);
                setSaveInventoryMovement(inventoryMovement);

            }

            int countItems = cartItemList.size();
            int countSavedItems = salesDatabase.getSaleItemCountBySaleNumber(cart.getCart_number());
            if (salesDatabase.doesSaleExist(cart.getCart_number()) && countItems == countSavedItems){
                salesDatabase.clearCart();
                //Toast.makeText(getActivity(), "Items have been saved and tables cleared",Toast.LENGTH_SHORT).show();
                showFinishedPaymentDialog(String.valueOf(cashDue),String.valueOf(cashBalance));
            }
        } else {
            Toast.makeText(getActivity(), "Cash received is not enough",Toast.LENGTH_SHORT).show();
        }

    }

    // Shows the assign customer dialog
    private void showFinishedPaymentDialog(String argument1, String argument2) {

        FinishedPaymentFragment salesFragment = FinishedPaymentFragment.newInstance(argument1, argument2);
        mainActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, salesFragment)
                .commit();

    }

    // Navigates back to the previous fragment
    private void goBack() {
        mainActivity.loadFragment(0,"sales_fragment_tag");
    }

    public static long getTodayTimestamp() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    // Formats the amount with a specified decimal format
    private String formatAmount(double amount) {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        return formatter.format(amount);
    }

    private String formatAmountWithCommas(String amount) {
        String cleanAmount = amount.replace(",", ""); // Remove existing commas
        int length = cleanAmount.length();
        StringBuilder formattedAmount = new StringBuilder();

        for (int i = 0; i < length; i++) {
            formattedAmount.append(cleanAmount.charAt(i));
            if ((length - i) % 3 == 1 && i != length - 1) {
                formattedAmount.append(",");
            }
        }

        return formattedAmount.toString();
    }


    public void setSaveInventoryMovement(InventoryMovement inventoryMovement){
        ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", "Saving customer...", true);
        StringRequest request = new StringRequest(Request.Method.POST, SAVE_INVENTORY_MOVEMENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            if (success ==1){

                                //listener.onSaveCustomer(true);
                                progressDialog.dismiss();

                            } else {

                               // listener.onSaveCustomer(false);
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
                        Toast.makeText(getActivity(), "Unknown Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
               // params.put("inventoryMovementId", String.valueOf(inventoryMovement.getInventoryMovementId()));
                params.put("inventoryMovementItemId", String.valueOf(inventoryMovement.getInventoryMovementItemId()));
                params.put("inventoryMovementType", inventoryMovement.getInventoryMovementType());
                params.put("inventoryMovementQuantityChanged",String.valueOf(inventoryMovement.getInventoryMovementQuantityChanged()));
                params.put("inventoryMovementReason", String.valueOf(inventoryMovement.getInventoryMovementReason()));
                params.put("inventoryMovementTimestamp", String.valueOf(inventoryMovement.getInventoryMovementTimestamp()));
                params.put("inventoryMovementInitiator", inventoryMovement.getInventoryMovementInitiator());
                params.put("inventoryMovementSourceLocationId", String.valueOf(inventoryMovement.getInventoryMovementSourceLocationId()));
                params.put("inventoryMovementDestinationLocationId", String.valueOf(inventoryMovement.getInventoryMovementDestinationLocationId()));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);

    }

}