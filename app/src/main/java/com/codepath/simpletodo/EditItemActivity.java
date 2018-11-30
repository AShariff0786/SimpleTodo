package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import static com.codepath.simpletodo.MainActivity.ITEM_POSITION;
import static com.codepath.simpletodo.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {
    //tracks edit text
    EditText etItemtxt;
    //position of edited item
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etItemtxt= findViewById(R.id.etmulti);
        //set edit text value from intent
        etItemtxt.setText(getIntent().getStringExtra(ITEM_TEXT));
        //Update postion from intent
        position= getIntent().getIntExtra(ITEM_POSITION, 0);
        //update title bar of activity
        getSupportActionBar().setTitle("Edit Item: ");
    }

    //handler for save
    public void onSaveItem(View v){
        //new intent for result
        Intent i= new Intent();
        //passses updated item
        i.putExtra(ITEM_TEXT, etItemtxt.getText().toString());
        //passes original position
        i.putExtra(ITEM_POSITION, position);
        //sets intent as result
        setResult(RESULT_OK, i);
        //close activity and redirect to main
        finish();
    }
}
