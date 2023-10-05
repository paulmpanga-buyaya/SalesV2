package sales.kiwamirembe.com.classes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import sales.kiwamirembe.com.MainActivity;
import sales.kiwamirembe.com.authentication.AuthenticationActivity;

public class SessionManager {

    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    String KEY_LOCATION_ID;
    String KEY_USER_ID;
    String KEY_USERNAME;

    //SharedPref file name
    private static final String MGMT_PREFS = "MGMT_PREFS";
    // All Shared Preferences Keys: lists only the user login/out status key
    private static final String IS_LOGIN = "IsLoggedIn";

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(MGMT_PREFS, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
   /* public void createLoginSession(int intValueToPass) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        // Storing an integer value in SharedPreferences
        editor.putInt(KEY_INT_VALUE, intValueToPass);
        // user is not logged in redirect him to Login Activity
        Intent i = new Intent(_context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Pass the integer value as an extra to MainActivity
        i.putExtra("INT_EXTRA_NAME", intValueToPass);
        // Starting Login Activity
        _context.startActivity(i);
        // commit changes
        editor.commit();
    }*/

    public void createLoginSession(int user_id, String username, int location_id) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        // Storing user_id in pref
        editor.putInt(KEY_USER_ID, user_id);
        // Storing username in pref
        editor.putString(KEY_USERNAME, username);
        // Storing user_id in pref
        editor.putInt(KEY_LOCATION_ID, location_id);
        // user is not logged in redirect him to Login Activity
        Intent i = new Intent(_context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        _context.startActivity(i);
        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, AuthenticationActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    /**
     * Get stored session data
     */

 /*   public User getUserDetails() {
        int userId = pref.getInt(KEY_USER_ID, -1); // -1 is a default value if the key is not found
        String username = pref.getString(KEY_USERNAME, "null"); // null is a default value if the key is not found
        int locationId = pref.getInt(KEY_LOCATION_ID, -1); // -1 is a default value if the key is not found

        // Check if the user data exists in SharedPreferences
        if (userId != -1 && locationId != -1) {
            // Create and return a User object with the retrieved data
            return new User(userId, username, locationId);
        } else {
            // User data not found in SharedPreferences
            return null;
        }
    }*/

    /**
     * Clear session details
     */
    public void logoutUser() {

        // Clearing all data from Shared Preferences
        editor.clear();

        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, AuthenticationActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);

    }

    /**
     * Quick check for login
     **/

    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

}
