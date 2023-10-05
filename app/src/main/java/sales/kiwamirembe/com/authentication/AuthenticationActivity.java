package sales.kiwamirembe.com.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sales.kiwamirembe.com.MainActivity;
import sales.kiwamirembe.com.R;
import sales.kiwamirembe.com.SalesFragment;
import sales.kiwamirembe.com.classes.Category;
import sales.kiwamirembe.com.classes.Inventory;
import sales.kiwamirembe.com.classes.SessionManager;
import sales.kiwamirembe.com.classes.User;
import sales.kiwamirembe.com.databases.InventoryDatabase;

public class AuthenticationActivity extends AppCompatActivity {

    SessionManager sessionManager;
    TextInputEditText textInputUsername, textInputPassword;
    private static final String USER_LOGIN_URL = "https://kiwamirembe.com/auth/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        sessionManager = new SessionManager(this);

        textInputUsername = findViewById(R.id.textInputUsername);
        textInputPassword = findViewById(R.id.textInputPassword);

        Button buttonLogIn = findViewById(R.id.buttonLogIn);
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = textInputUsername.getText().toString();
                String password = textInputPassword.getText().toString();

              /*  if(TextUtils.isEmpty(username)) {
                    textInputUsername.setError("Please enter your username");
                    textInputUsername.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(password)) {
                    textInputPassword.setError("Please enter your password");
                    textInputPassword.requestFocus();
                    return;
                }*/

                userLogin(username,password);
            }
        });
    }

    public void userLogin(String username, String password) {
        ProgressDialog progressDialog = ProgressDialog.show(AuthenticationActivity.this, "", "Fetching items...", true);

        StringRequest request = new StringRequest(
                Request.Method.POST, USER_LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            handleInventoryResponse(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            //getCustomers(1);
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(AuthenticationActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AuthenticationActivity.this);
        requestQueue.add(request);
    }

    private void handleInventoryResponse(String response) throws JSONException {
        //inventoryList.clear();
        JSONObject jsonObject = new JSONObject(response);
        int success = jsonObject.getInt("success");
        String message = jsonObject.getString("message");

        if (success == 1) {
            //getting the user from the response
            JSONObject userJson = jsonObject.getJSONObject("user");
            //creating a new user object
            User user = new User(userJson.getInt("user_id"), userJson.getString("username"),3);
            //sessionManager.userLogin(user);
            sessionManager.createLoginSession(user.getUser_id(),user.getUsername(),user.getLocation_id());
        } else {
            Toast.makeText(AuthenticationActivity.this, message, Toast.LENGTH_LONG).show();
        }
    }
}