package com.example.sharenlearn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class NotesUploadActivity extends AppCompatActivity {


    EditText et_title,et_description,et_download_link,et_image_link;
    Spinner spinner_category;
    Button bt_upload;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    String category;

    //TODO 5:upload new Notes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_upload);

        et_title = findViewById(R.id.et_notes_title);
        et_description = findViewById(R.id.et_notes_description);
        et_download_link = findViewById(R.id.et_notes_download_link);
        et_image_link = findViewById(R.id.et_note_image_link);
        bt_upload = findViewById(R.id.bt_notes_upload);

        spinner_category = findViewById(R.id.spinner_notes_category);
        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.interests, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_category.setAdapter(adapter);

//        spinner_category.setOnItemClickListener(
//                new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                        category = parent.getItemAtPosition(position).toString();
//                    }
//                }
//        );

        spinner_category.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        category = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                        category="";
                    }
                }
        );

        db = new SQLiteHandler(NotesUploadActivity.this);

        bt_upload.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = et_title.getText().toString().trim();
                        String description = et_description.getText().toString().trim();
                        String download_link = et_download_link.getText().toString().trim();
                        String image_link = et_image_link.getText().toString().trim();
                        UserDetails userDetails;
                        userDetails = db.getUserProfile();

                        if( title.equals("") || description.equals("") || download_link.equals("") || image_link.equals("") || category.equals(""))
                        {
                            Toast.makeText(NotesUploadActivity.this,"Fill the data correctly !!!",Toast.LENGTH_LONG).show();
                        }
                        else{
                            //TODO 6: request the data
                            uploadNotes(
                                    userDetails.getUid(),
                                    userDetails.getEmail(),
                                    title,
                                    description,
                                    download_link,
                                    category,
                                    image_link
                            );

                        }

                    }
                }
        );

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);



        //end oncreate
    }

    private void uploadNotes( final String user_id, final String email, final String title , final String description , final String download_link , final String category , final String image_link) {
        // Tag used to cancel the request
        String tag_string_req = "req_register_notes";

        pDialog.setMessage("Uploading Notes ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPLOAD_NOTES, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
    //            Log.d(TAG, "Register Response: " + response.toString());
//                Toast.makeText(NotesUploadActivity.this,"Upload Notes Response : " + response.toString(),Toast.LENGTH_SHORT).show();
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
//                        //Todo 7  : get the json data
//                        int notes_id = jObj.getInt("notes_id");
//
//                        JSONObject note = jObj.getJSONObject("note");
//                        String title = note.getString("name");
//                        String email = note.getString("email");
//                        String user_id = note.getString("user_id");
//                        String description = note.getString("description");
//                        String category = note.getString("category");
//                        int downloads = note.getInt("user_id");
//                        String uploaded_at = note.getString("uploaded_at");

//                        // Inserting row in users table
//       //                 db.addUser(name, email, uid, created_at);
//
                        Toast.makeText(NotesUploadActivity.this, "Notes successfully uploaded !!", Toast.LENGTH_LONG).show();
//

                        finish();
//                        // Launch login activity
//                        //TODO 8 : Direct to search the activity
//                        Intent intent = new Intent(
//                                NotesUploadActivity.this,
//                                LoginActivity.class);
//                        startActivity(intent);
//                        finish();
                    } else {

                        // Error occurred in upload. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(NotesUploadActivity.this , errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
   //             Log.e(TAG, "Registration Error: " + error.getMessage());

                Toast.makeText(NotesUploadActivity.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                //TODO 10 : pass these parameters to the post method

                params.put("user_id", user_id);
                params.put("email", email);
                params.put("title", title);
                params.put("description", description);
                params.put("download_link", download_link);
                params.put("category", category);
                params.put("image_link", image_link);

                return params;
            }

        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        // Adding request to request queue
        AppController appController = new AppController(NotesUploadActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    //end class
}
