package sales.kiwamirembe.com;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class FinishedPaymentFragment extends Fragment {

    private TextInputEditText amountPaid, changeDue;
    private MainActivity mainActivity;
    private Button startAnotherSale;

    public FinishedPaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }

    // Static method to create a new instance of FinishedPaymentFragment with arguments
    public static FinishedPaymentFragment newInstance(String argument1, String argument2) {
        FinishedPaymentFragment fragment = new FinishedPaymentFragment();
        Bundle args = new Bundle();
        args.putString("argument1_key", argument1);
        args.putString("argument2_key", argument2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_finished_payment, container, false);

        // Find the EditText fields in the layout
        amountPaid = rootView.findViewById(R.id.amountPaid); // Replace with your actual EditText ID
        changeDue = rootView.findViewById(R.id.changeDue); // Replace with your actual EditText ID

        startAnotherSale = rootView.findViewById(R.id.startAnotherSale);
        startAnotherSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        // Retrieve arguments
        Bundle args = getArguments();
        if (args != null) {
            String argument1 = args.getString("argument1_key");
            String argument2 = args.getString("argument2_key");

            // Set values to EditText fields
            amountPaid.setText(mainActivity.formatAmount(Double.parseDouble(argument1)));
            changeDue.setText(mainActivity.formatAmount(Double.parseDouble(argument2)));
        }

        return rootView;
    }

  /*  @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
    }*/

    // Navigates back to the previous fragment
    private void goBack() {

        mainActivity.loadFragment(0, "sales_fragment_tag");

      /*  mainActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, new SalesFragment())
                .commit();*/

        //dismiss();
    }

}