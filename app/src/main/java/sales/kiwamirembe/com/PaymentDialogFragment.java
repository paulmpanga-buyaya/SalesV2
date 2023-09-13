package sales.kiwamirembe.com;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.widget.Button;
import android.widget.Toast;
import androidx.fragment.app.FragmentManager;

public class PaymentDialogFragment extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bottom_payment_dialog, container, false);

        Button buttonCash = rootView.findViewById(R.id.buttonCash);
        Button buttonMTN = rootView.findViewById(R.id.buttonMTN);
        Button buttonAirtel = rootView.findViewById(R.id.buttonAirtel);
        Button buttonCredit = rootView.findViewById(R.id.buttonCredit);

        buttonMTN.setOnClickListener(v -> {
            showToast("MTN MoMo coming soon");
        });

        buttonAirtel.setOnClickListener(v -> {
            showToast("Airtel Money coming soon");
        });

        buttonCredit.setOnClickListener(v -> {
            showToast("Credit coming soon");
        });

        buttonCash.setOnClickListener(v -> {
            openCashPaymentFragment();
        });

        return rootView;
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void openCashPaymentFragment() {
        // Replace the current fragment with CashPaymentFragment
        CashPaymentFragment cashPaymentFragment = new CashPaymentFragment();
        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_container, cashPaymentFragment)
                .addToBackStack(null)
                .commit();

        dismiss(); // Close the bottom sheet after opening the fragment
    }
}


