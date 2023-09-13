package sales.kiwamirembe.com;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import sales.kiwamirembe.com.classes.Customer;
import sales.kiwamirembe.com.databases.SalesDatabase;

public class AddCustomerDialogFragment extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_customer_dialog, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.add_customer_dialog_toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_add_customer));
        toolbar.setNavigationIcon(R.drawable.baseline_close_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // Dismiss the dialog when the back button is clicked
            }
        });

        TextInputEditText firstNameEditText = rootView.findViewById(R.id.textInputFirstName);
        TextInputEditText lastNameEditText = rootView.findViewById(R.id.textInputLastName);
        TextInputEditText businessNameEditText = rootView.findViewById(R.id.textInputBusinessName);
        TextInputEditText emailEditText = rootView.findViewById(R.id.textInputEmail);
        TextInputEditText phoneEditText = rootView.findViewById(R.id.textInputPhone);

        Button saveButton = rootView.findViewById(R.id.squareCornerButton);

        // Handle the click event of the "Save" button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Retrieve the data entered by the user
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String businessName = businessNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();

                // Validate the data (optional)
                if (firstName.isEmpty() || lastName.isEmpty() || businessName.isEmpty() ||
                        email.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a new Customer object with the entered data
                Customer newCustomer = new Customer();
                newCustomer.setCustomer_name(firstName + " " + lastName);
                newCustomer.setCustomer_business_name(businessName);
                newCustomer.setCustomer_email(email);
                newCustomer.setCustomer_phone(phone);
                newCustomer.setCustomer_web_synced(0); // Not synced with web

                // Insert the customer data into the database
                boolean isInserted = new SalesDatabase(getActivity()).addCustomer(newCustomer);
                if (isInserted) {
                    //Toast.makeText(getActivity(), "Customer added successfully", Toast.LENGTH_SHORT).show();
                    //dismiss(); // Close the dialog
                    // Refresh the CustomerFragment
                    CustomersFragment customerFragment = (CustomersFragment) getParentFragmentManager().findFragmentByTag("customers_fragment_tag");
                    if (customerFragment != null) {
                        customerFragment.refreshCustomerList();
                    }
                    //customerFragment.refreshCustomerList();
                    dismiss(); // Close the dialog

                } else {
                    Toast.makeText(getActivity(), "Failed to add customer", Toast.LENGTH_SHORT).show();
                }


            }


        });

        return rootView;
    }
}

