package com.example.sharenlearn;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class ProfileActivity extends AppCompatActivity {

    UserDetails userDetails;
   // String username,email;
  //  int downloads,uploads;
    Button bt_logout,bt_search,bt_upload,bt_all_notes;

    TextView tv_username,tv_email,tv_upload,tv_download;
    SQLiteHandler sqLiteHandler;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sqLiteHandler = new SQLiteHandler(ProfileActivity.this);

        //TODO 11:make the update option for the profile
        tv_username = findViewById(R.id.tv_pf_username);
        tv_email = findViewById(R.id.tv_pf_email);
        tv_download = findViewById(R.id.tv_pf_no_downloads);
        tv_upload = findViewById(R.id.tv_pf_no_uploads);
        bt_logout = findViewById(R.id.bt_logout);
        bt_all_notes = findViewById(R.id.bt_all_notes);
        bt_upload = findViewById(R.id.bt_upload);
        bt_search = findViewById(R.id.bt_search_notes);



        userDetails = sqLiteHandler.getUserProfile();
    //    checkLogin(userDetails.getEmail());

        if(userDetails == null)
        {
            Toast.makeText(this,"userdetail is null",Toast.LENGTH_LONG).show();
        }
        else {

            tv_username.setText(userDetails.getUsername());
            tv_email.setText(userDetails.getEmail());
            tv_download.setText(String.valueOf(userDetails.getDownloads()));
            tv_upload.setText(String.valueOf(userDetails.getUploads()));
        }
//        tv_upload.setText(String.valueOf(userDetails.getUploads()));
//        tv_download.setText(String.valueOf(userDetails.getUploads()));


        bt_logout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sqLiteHandler.deleteUsers();
                        SessionManager sessionManager = new SessionManager(ProfileActivity.this);
                        sessionManager.setLogin(false);
                        Intent intent = new Intent(ProfileActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        bt_all_notes.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ProfileActivity.this,NotesActivity.class);
                        startActivity(intent);
                    }
                }
        );

        bt_search.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ProfileActivity.this,SearchActivity.class);
                        startActivity(intent);
                    }
                }
        );

        bt_upload.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(ProfileActivity.this,NotesUploadActivity.class);
                        startActivity(intent);
                    }
                }
        );

    }

    public void checkLogin(final String email) {
        // Tag used to cancel the request
        String tag_string_req = "req_profile_data";

   //     pDialog.setMessage("Logging in ...");
    //    showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GET_USER_PROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                Log.d(TAG, "Login Response: " + response.toString());
                Toast.makeText(ProfileActivity.this,"Update Profile: " + response.toString(),Toast.LENGTH_SHORT).show();
//                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
//                        session.setLogin(true);

                        // Now store the user in SQLite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");
                        int downloads = user.getInt("downloads");
                        int uploads = user.getInt("uploads");

                        // Inserting row in users table
//                        sqLiteHandler.deleteUsers();
//
//                        sqLiteHandler.addUserAll(name, email, uid, created_at,downloads,uploads);

                        sqLiteHandler.updateUsersUploadsonDB(uploads);
                        Toast.makeText(ProfileActivity.this,"Profile updated successfully ",Toast.LENGTH_SHORT).show();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(ProfileActivity.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
            }

        })
        {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);

                return params;
            }

        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));




        // Adding request to request queue
        AppController appController = new AppController(ProfileActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);


    }

    @Override
    protected void onPause() {
        super.onPause();
        checkLogin(userDetails.getEmail());

    }

    //    public void showDialog() {
//        if (!pDialog.isShowing())
//            pDialog.show();
//    }
//
//    public void hideDialog() {
//        if (pDialog.isShowing())
//            pDialog.dismiss();
//    }

}
