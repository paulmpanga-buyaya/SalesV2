package sales.kiwamirembe.com;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class RoutesDialogFragment extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
    }

    public RoutesDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_routes, container, false);
        View rootView = inflater.inflate(R.layout.fragment_routes, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.routes_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_routes));
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // Dismiss the dialog when the back button is clicked
            }
        });

        setHasOptionsMenu(true); // Enable options menu in this fragment

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.routes_menu, menu);
        // Get the specific menu item by ID and set its icon programmatically
        MenuItem addRoute = menu.findItem(R.id.add_route);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_route) {
            // Handle the menu item click for clearing the cart
            // Implement your logic here to clear the cart
            AddRouteDialogFragment addRouteDialogFragment = new AddRouteDialogFragment();
            addRouteDialogFragment.show(getChildFragmentManager(), "add_routes_dialog");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}