package sales.kiwamirembe.com;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import sales.kiwamirembe.com.adapters.CustomerAdapter;
import sales.kiwamirembe.com.adapters.ReceiptAdapter;
import sales.kiwamirembe.com.classes.Customer;
import sales.kiwamirembe.com.classes.Sale;
import sales.kiwamirembe.com.databases.SalesDatabase;

public class ReceiptsFragment extends Fragment implements ReceiptAdapter.ReceiptAdapterListener {

    private RecyclerView recyclerView;
    private List<Sale> saleList = new ArrayList<>();
    private ReceiptAdapter receiptAdapter;
    private SearchView searchView;
    private ProgressDialog progressDialog;

    private MainActivity mainActivity;

    public ReceiptsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_receipts, container, false);
        View rootView = inflater.inflate(R.layout.fragment_receipts, container, false);
        // Set up the Toolbar
        Toolbar toolbar = rootView.findViewById(R.id.receipts_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_receipts));
        setHasOptionsMenu(true); // Enable options menu in this fragment

        recyclerView = rootView.findViewById(R.id.receiptsRecycler);
        setupRecyclerView();

        searchView = rootView.findViewById(R.id.searchReceipts);
        setupSearchView();

        return rootView;
    }

    @Override
    public void onReceiptSelected(Sale sale) {
        // Handle item selection as needed
        //Toast.makeText(getActivity(), product.getSaleTransactionID(),Toast.LENGTH_SHORT).show();
        ViewSaleDetailsFragment dialogFragment = new ViewSaleDetailsFragment();
        Bundle args = new Bundle();
        args.putString("transactionCode", sale.getSale_number());
        dialogFragment.setArguments(args);
        dialogFragment.show(getFragmentManager(), "ViewSaleDetailsFragment");
    }

    private void setupRecyclerView() {
        // Initialize and set up your RecyclerView and mAdapter here
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SalesDatabase databaseHelper1 = new SalesDatabase(getActivity());
        saleList = databaseHelper1.getAllSales();
        receiptAdapter = new ReceiptAdapter(getActivity(), saleList, this);
        recyclerView.setAdapter(receiptAdapter);
    }

    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                receiptAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                receiptAdapter.getFilter().filter(query);
                return false;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.receipts_menu, menu);
        // Get the specific menu item by ID and set its icon programmatically
        MenuItem filterReceipts = menu.findItem(R.id.filter_receipts);
        if (filterReceipts != null) {
            filterReceipts.setIcon(R.drawable.baseline_filter_list_24);
        }

        MenuItem syncReceipts = menu.findItem(R.id.sync_receipts);
        if (syncReceipts != null) {
            syncReceipts.setIcon(R.drawable.baseline_cloud_sync_24);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

}