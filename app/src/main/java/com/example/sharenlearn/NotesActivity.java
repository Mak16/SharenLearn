package com.example.sharenlearn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotesActivity extends AppCompatActivity {

    public static ArrayList<Notes> notesList;
    NotesListAdapter notesListAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rv_notes_list;
    int DeaultTime = 10000;
    String title_search,category_search,uploader_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        notesList = new ArrayList<>();
        Intent intent = getIntent();
        if(intent.getBooleanExtra(SearchActivity.FROM_SEARCH_ACTIVITY,false))
        {
            title_search = intent.getStringExtra(SearchActivity.SEARCH_TITLE);
            uploader_search = intent.getStringExtra(SearchActivity.SEARCH_UPLOADER);
            category_search = intent.getStringExtra(SearchActivity.SEARCH_CATEGORY);
        }
        else {
            title_search="all";
            category_search="all";
            uploader_search="all";

        }

//        getAllNotes("gate","BE");
        getAllNotes(title_search,category_search,uploader_search);

        rv_notes_list = findViewById(R.id.rv_notes_list);
        linearLayoutManager = new LinearLayoutManager(this);
        rv_notes_list.setLayoutManager(linearLayoutManager);
        rv_notes_list.setHasFixedSize(true);
    }

    public void getAllNotes(final String TITLE, final String CATEGORY,final String UPLOADER) {
        // Tag used to cancel the request
        String tag_string_req = "req_Notes_Data";

        //pDialog.setMessage("Logging in ...");
        //showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_BOOKS_DATA, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "Login Response: " + response.toString());
//                Toast.makeText(NotesActivity.this,"Login Response: " + response.toString(),Toast.LENGTH_LONG).show();
                //hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        //session.setLogin(true);

                        // Now store the user in SQLite
                        JSONArray jsonArray = jObj.getJSONArray("notes");
                        // looping through All Notes
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);

                            String title = c.getString("title");
                            String category = c.getString("category");
                            String download_link = c.getString("download_link");
                            String description = c.getString("description");
                            int rating = c.getInt("rating");
                            int downloads = c.getInt("downloads");
                            String user_id = c.getString("user_id");
                            String user_email = c.getString("email");
                            int notes_id = c.getInt("notes_id");
                            String uploaded_at = c.getString("uploaded_at");
                            String image_link = c.getString("image_link");

                            // Phone node is JSON Object
                            Notes notes = new Notes(title,description,download_link,category,downloads,user_email,user_id,notes_id,rating,image_link);
                            notesList.add(notes);
                        }
                        // Inserting row in users table
                        //db.addUser(name, email, uid, created_at);

                        //launch web
//                        // Launch main activity
//                        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
//                        startActivity(intent);
//                        finish();


                        notesListAdapter = new NotesListAdapter(NotesActivity.this,notesList);
                        rv_notes_list.setAdapter(notesListAdapter);
                        Toast.makeText(NotesActivity.this,"download successful !!! ",Toast.LENGTH_LONG).show();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(NotesActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(NotesActivity.this, "Json exc error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
              //  Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(NotesActivity.this, "onErrorResponse"+error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();
            }

        })
        {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("title", TITLE);
                params.put("category", CATEGORY);
                params.put("uploader", UPLOADER);

                return params;
            }

        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));




        // Adding request to request queue
        AppController appController = new AppController(NotesActivity.this);
        appController.addToRequestQueue(strReq, tag_string_req);


    }
}
