package sales.kiwamirembe.com;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import sales.kiwamirembe.com.classes.CartItem;
import sales.kiwamirembe.com.databases.InventoryDatabase;
import sales.kiwamirembe.com.databases.SalesDatabase;

public class EditCartItemFragment extends DialogFragment {

    TextView itemNameTextView, stockTextView;
    TextInputEditText textInputAmount, textInputQuantity, textInputPrice;
    MaterialButton buttonUpdateItem;
    SalesDatabase salesDatabase;
    InventoryDatabase inventoryDatabase;
    String cartItemSku;

    public EditCartItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_cart_item, container, false);
        salesDatabase = new SalesDatabase(getActivity());
        inventoryDatabase = new InventoryDatabase(getActivity());

        if (getArguments() != null) {
            cartItemSku = getArguments().getString("sku",null);
        }

        CartItem cartItem = salesDatabase.getCartItemBySku(cartItemSku);

        // Example: Accessing the TextView
        itemNameTextView = rootView.findViewById(R.id.itemNameTextView);
        stockTextView = rootView.findViewById(R.id.availableQuantityTextView);

        itemNameTextView.setText("Item name : " + " " + cartItem.getCart_item_name());
        int cart_quantity = cartItem.getCart_item_quantity();
        int stock = inventoryDatabase.getItemStockBySku(cartItemSku);
        int new_stock = stock - cart_quantity;
        stockTextView.setText("Units : " + " " + new_stock);

        // Example: Accessing the TextInputEditText
        textInputAmount = rootView.findViewById(R.id.textInputAmount);
        textInputQuantity = rootView.findViewById(R.id.textInputQuantity);
        textInputPrice = rootView.findViewById(R.id.textInputPrice);

        textInputAmount.setText(String.valueOf(cartItem.getCart_item_total()));
        textInputQuantity.setText(String.valueOf(cartItem.getCart_item_quantity()));
        textInputPrice.setText(String.valueOf(cartItem.getCart_item_price()));

        textInputQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                String quantityText = textInputQuantity.getText().toString();
                if (quantityText.equals("")){
                    int quantity = 0;
                    float price = Float.parseFloat(textInputPrice.getText().toString());
                    float total = quantity * price;
                    textInputAmount.setText(String.valueOf(total));

                    int new_stock = stock - quantity;
                    stockTextView.setText("Units : " + " " + new_stock);

                } else {
                    int quantity = Integer.parseInt(textInputQuantity.getText().toString());

                    if (quantity > stock){

                        Toast.makeText(getActivity(), "Quantity cannot be higher than avaialbel stock",Toast.LENGTH_SHORT).show();

                    } else {

                        float price = Float.parseFloat(textInputPrice.getText().toString());
                        float total = quantity * price;
                        textInputAmount.setText(String.valueOf(total));

                        int new_stock = stock - quantity;
                        stockTextView.setText("Units : " + " " + new_stock);

                    }

                }

            }
        });

        buttonUpdateItem = rootView.findViewById(R.id.updateItem);
        buttonUpdateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(textInputQuantity.getText().toString());
                //int cart_quantity = cartItem.getCart_item_quantity();
                salesDatabase.updateCartItemQuantityAndTotalBySku(cartItemSku,quantity);
                dismiss();
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
    }
}