package com.example.sharenlearn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {


    EditText et_username,et_password;
    TextView tv_register,tv_forgot_password;
    public SessionManager session;
    public SQLiteHandler db;
    public ProgressDialog pDialog;
    Button bt_login;

    private static final String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // SQLite database handler
        db = new SQLiteHandler(LoginActivity.this);

        // Session manager
        session = new SessionManager(LoginActivity.this);

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);

            startActivity(intent);
            finish();
            Toast.makeText(LoginActivity.this,"isLoggedIn is true",Toast.LENGTH_SHORT).show();
        }

        tv_register = findViewById(R.id.tv_not_account);
        tv_forgot_password = findViewById(R.id.tv_forgot_pass);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        bt_login = findViewById(R.id.bt_login);

        tv_register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                        startActivity(intent);
                    }
                }
        );
        tv_forgot_password.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(LoginActivity.this,"Password is send to your E-mail ID !!!",Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        bt_login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = et_username.getText().toString().trim();
                        String password = et_password.getText().toString().trim();

                        // Check for empty data in the form
                        if (!email.isEmpty() && !password.isEmpty()) {
                            // login user
                            checkLogin(email, password);
                        } else {
                            // Prompt user to enter credentials
                            Toast.makeText(getApplicationContext(),
                                    "Please enter the credentials!", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
        );




//onState bracket
    }

    public void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                Toast.makeText(LoginActivity.this,"Login Response: " + response.toString(),Toast.LENGTH_SHORT).show();
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");
                        int downloads = user.getInt("downloads");
                        int uploads = user.getInt("uploads");

                        // Inserting row in users table
                        db.addUserAll(name, email, uid, created_at,downloads,uploads);

                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(LoginActivity.this,"Login successfully profile ",Toast.LENGTH_SHORT).show();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(LoginActivity.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }

        })
        {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));




        // Adding request to request queue
        AppController appController = new AppController(LoginActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);


    }

    public void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    public void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    //class bracket
}
