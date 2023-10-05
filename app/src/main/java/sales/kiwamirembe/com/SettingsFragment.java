package sales.kiwamirembe.com;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sales.kiwamirembe.com.classes.SessionManager;


public class SettingsFragment extends Fragment {

    SessionManager sessionManager;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        // Set up the Toolbar
        Toolbar toolbar = rootView.findViewById(R.id.settings_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_settings));
        setHasOptionsMenu(true); // Enable options menu in this fragment

        // Reference to the settingsParentLayout
        LinearLayout settingsParentLayout = rootView.findViewById(R.id.settingsParentLayout);

        // Example data (you can replace this with your actual data)
        List<MenuItemData> menuItemDataList = new ArrayList<>();
        menuItemDataList.add(new MenuItemData(R.drawable.ic_baseline_routes_24, "Routes"));
        menuItemDataList.add(new MenuItemData(R.drawable.baseline_print_24, "Printers"));
        menuItemDataList.add(new MenuItemData(R.drawable.ic_baseline_settings_24, "General settings"));

        // Inflate and add child views for each data item
        for (final MenuItemData menuItemData : menuItemDataList) {
            View childView = inflater.inflate(R.layout.settings_child_layout, settingsParentLayout, false);
            Toolbar childToolbar = childView.findViewById(R.id.settings_child_toolbar);

            childToolbar.setNavigationIcon(menuItemData.getIconResource());
            childToolbar.setTitle(menuItemData.getName());
            childToolbar.setTitleTextColor(getResources().getColor(R.color.grey_600));

            childToolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the click event and show the corresponding DialogFragment
                    String menuItemName = menuItemData.getName();
                    switch (menuItemName) {
                        case "Routes":
                            RoutesDialogFragment routesDialogFragment = new RoutesDialogFragment();
                            routesDialogFragment.show(getChildFragmentManager(), "routes_dialog");
                            break;
                        case "Printers":
                            PrintersDialogFragment printersDialogFragment = new PrintersDialogFragment();
                            printersDialogFragment.show(getChildFragmentManager(), "printers_dialog");
                            break;
                        case "General settings":
                            GeneralSettingsDialogFragment generalSettingsDialogFragment = new GeneralSettingsDialogFragment();
                            generalSettingsDialogFragment.show(getChildFragmentManager(), "general_settings_dialog");
                            break;
                        default:
                            // Handle other cases if needed
                            break;
                    }
                }
            });


            settingsParentLayout.addView(childView);
        }


        sessionManager = new SessionManager(getActivity());

        Button signOutButton = rootView.findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sessionManager.logoutUser();
            }
        });


        return rootView;
    }

    private static class MenuItemData {
        private int iconResource;
        private String name;

        public MenuItemData(int iconResource, String name) {
            this.iconResource = iconResource;
            this.name = name;
        }

        public int getIconResource() {
            return iconResource;
        }

        public String getName() {
            return name;
        }
    }

}