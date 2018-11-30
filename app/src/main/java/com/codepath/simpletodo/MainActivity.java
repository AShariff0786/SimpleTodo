package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //numeric code for identifying edit activity
    public final static int EDIT_REQUEST_CODE=20;
    //keys for passing data between activities
    public final static String ITEM_TEXT="itemText";
    public final static String ITEM_POSITION= "itemPosition";

    ArrayList<String> items;
    ArrayAdapter<String> itmadapt;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readItems();
        itmadapt= new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, items);
        lvItems= (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(itmadapt);
        setupListViewListener();
    }
    public void onAddItems(View v){
        EditText etnewitm= (EditText) findViewById(R.id.etnew);
        String itemtxt= etnewitm.getText().toString();
        itmadapt.add(itemtxt);
        etnewitm.setText("");
        writeItems();
        Toast.makeText(getApplicationContext(), "Item was added to the List", Toast.LENGTH_SHORT).show();
    }

    private void setupListViewListener(){
        Log.i("MainActivity", "Setting up listener on list view");
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("MainActivity", "Item removed from the List: " +position);
                items.remove(position);
                itmadapt.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        //item listener for edit
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //creates new activity
                Intent i= new Intent(MainActivity.this, EditItemActivity.class);
                //pass data being edited
                i.putExtra(ITEM_TEXT, items.get(position));
                i.putExtra(ITEM_POSITION, position);
                //displays activity
                startActivityForResult(i, EDIT_REQUEST_CODE);
            }
        });
    }

    //handles results from edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        //if edit activity completed is ok
        if(resultCode==RESULT_OK && requestCode==EDIT_REQUEST_CODE){
            //extract updated item
            String updatedItem= data.getExtras().getString(ITEM_TEXT);
            //extracts original position
            int postion= data.getExtras().getInt(ITEM_POSITION);
            //update model with new item text at end of edited postion
            items.set(postion, updatedItem);
            //inform adapter
            itmadapt.notifyDataSetChanged();
            //persist changed model
            writeItems();
            //notify user operation was completed ok
            Toast.makeText(this, "Item was successfully updated", Toast.LENGTH_SHORT).show();

        }
    }
    private File getDataFile(){
        return new File(getFilesDir(), "todo.txt");
    }

    private void readItems(){
        try {
            items= new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading file", e);
            items=new ArrayList<>();
        }
    }

    private void writeItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing file", e);
        }
    }
}
