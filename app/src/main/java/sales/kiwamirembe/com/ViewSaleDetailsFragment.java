package sales.kiwamirembe.com;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sales.kiwamirembe.com.classes.Sale;
import sales.kiwamirembe.com.classes.SaleItem;
import sales.kiwamirembe.com.databases.SalesDatabase;

public class ViewSaleDetailsFragment extends DialogFragment {

    private List<SaleItem> saleItemList = new ArrayList<>();
    TextView totalAmountTextView, totalHintTextView, dateTextView, dateAndTimeSummaryTExtView,
            receiptNumberSummaryTextView, userTextView, posTextView, routeTextView;
    private SalesDatabase salesDatabase;

    public ViewSaleDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_sale_details, container, false);


        Toolbar toolbar = rootView.findViewById(R.id.view_transaction_details_toolbar);
        // Retrieve data from arguments
        String code = getArguments().getString("transactionCode");

        toolbar.setTitle("Receipt: " + code);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24); // Back arrow icon
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // Close the dialog
            }
        });

        salesDatabase = new SalesDatabase(getActivity());

        Sale sale = salesDatabase.getSaleByNumber(code);

        totalAmountTextView = rootView.findViewById(R.id.totalAmountTextView);
        totalAmountTextView.setText(String.valueOf(sale.getSale_total()));

        receiptNumberSummaryTextView = rootView.findViewById(R.id.receiptNumberSummaryTextView);
        receiptNumberSummaryTextView.setText("Receipt #: " + code);

        long timeStamp = sale.getSale_date();
        Date date = new Date(timeStamp);

        final DateFormat dateFormatter = SimpleDateFormat.getDateInstance();
        dateAndTimeSummaryTExtView = rootView.findViewById(R.id.dateAndTimeSummaryTExtView);
        // dateAndTimeSummaryTExtView.setText(sdf.format(new Date(saleTransaction.getSaleTransactionDate())));



        totalHintTextView = rootView.findViewById(R.id.totalHintTextView);

        //  dateTextView = rootView.findViewById(R.id.dateTextView);
/*
        //String dateString = mainActivity.convertLongToDateTime(saleTransaction.getSaleTransactionDate());
        Date date = new Date(saleTransaction.getSaleTransactionDate());

        // Define the desired date and time format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        dateTextView.setText(sdf.format(date));*/

        userTextView = rootView.findViewById(R.id.userTextView);
        userTextView.setText("User: Kasim Aga");
        posTextView = rootView.findViewById(R.id.posTextView);
        posTextView.setText("Truck: UAX685R");
        routeTextView = rootView.findViewById(R.id.routeTextView);
        routeTextView.setText("Route: Town service");

        //recyclerView = rootView.findViewById(R.id.saleItemsRecyclerView1);
        // setupRecyclerView();

        saleItemList = salesDatabase.getSaleItemsBySaleNumber(code);

        LayoutInflater inflater1 = LayoutInflater.from(getContext());

        LinearLayout parentLayout = rootView.findViewById(R.id.parentLayout);

        for (SaleItem item : saleItemList) {
            View itemView = inflater1.inflate(R.layout.sale_item_row_layout, null);

            TextView nameTextView = itemView.findViewById(R.id.nameTextView);
            TextView priceAndQuantityTextView = itemView.findViewById(R.id.priceQuantityTextView);
            TextView totalTextView = itemView.findViewById(R.id.totalTextView);

            nameTextView.setText(item.getSale_item_name());
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            totalTextView.setText(String.valueOf(formatter.format(item.getSale_item_total())));
            priceAndQuantityTextView.setText("Price : " + " " + String.valueOf(item.getSale_item_price()) + " * " + String.valueOf(item.getSale_item_quantity()));

            parentLayout.addView(itemView);

            // Add a line divider after each item layout
            View dividerView = new View(getContext());
            dividerView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, // Width of the divider (match parent)
                    1 // Height of the divider (you can adjust this value for desired thickness)
            ));
            dividerView.setBackgroundColor(getResources().getColor(android.R.color.darker_gray)); // Color of the divider
            parentLayout.addView(dividerView);
        }


        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
    }
}