package com.example.sharenlearn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    public static  final String SEARCH_TITLE = "title";
    public static  final String SEARCH_UPLOADER = "uploader";
    public static  final String SEARCH_CATEGORY = "category";
    public static  final String FROM_SEARCH_ACTIVITY = "fromSearch";
    EditText et_title,et_uploader;
    Spinner spinner;
    String title,uploader,category;
    Button bt_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        et_title = findViewById(R.id.et_search_title);
        et_uploader = findViewById(R.id.et_search_uploader);
        spinner = findViewById(R.id.spinner_search_category);
        bt_search = findViewById(R.id.bt_search_searchbt);
        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.interests, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        //TODO 16 : add searching function by title , category and uploader
        title="";
        category="";
        uploader="";
        spinner.setOnItemSelectedListener(
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
        bt_search.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if( title.equals("") && uploader.equals("") && category.equals(""))
                        {
                            Toast.makeText(SearchActivity.this,"Atlest one field is mandatory !!!",Toast.LENGTH_SHORT).show();
                        } else
                        {
                            Intent intent = new Intent(SearchActivity.this,NotesActivity.class);
                            intent.putExtra(FROM_SEARCH_ACTIVITY,true);
//                            intent.putExtra(SEARCH_TITLE,title.trim().toLowerCase());
//                            intent.putExtra(SEARCH_UPLOADER,uploader.trim().toLowerCase());
//                            intent.putExtra(SEARCH_CATEGORY,category.trim().toLowerCase());


                            intent.putExtra(SEARCH_TITLE,title.trim());
                            intent.putExtra(SEARCH_UPLOADER,uploader.trim());
                            intent.putExtra(SEARCH_CATEGORY,category.trim());
                            startActivity(intent);
                        }
                    }
                }
        );
    }
}
