package com.example.vijaya.myorder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Summary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        TextView summaryTextView = (TextView) findViewById(R.id.summaryText);
        TextView userTextView = (TextView) findViewById(R.id.name);
        Intent mainIntent = getIntent();
        String summaryTxt = mainIntent.getStringExtra("summary");
        summaryTextView.setText(summaryTxt);
        userTextView.setText("Customer's Name: " + mainIntent.getStringExtra("userName"));
        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayList<String> items = mainIntent.getStringArrayListExtra("items");
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);
    }
    public void backtoHome(View v){
        Intent redirect = new Intent(Summary.this, MainActivity.class);
        startActivity(redirect);
    }
}
