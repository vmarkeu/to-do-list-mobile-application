package com.example.assignment1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class KIV extends AppCompatActivity{

    RecyclerView recyclerView;
    FloatingActionButton add_button;
    ImageView empty_imageview;
    TextView no_data;
    Spinner spinner;
    ImageView back_button;

    MyDatabaseHelper myDB;
    ArrayList<String> task_id, task_title, task_desc, task_cat, task_status;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kiv);

        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        empty_imageview = findViewById(R.id.empty_imageview);
        no_data = findViewById(R.id.no_task);
        spinner = findViewById(R.id.spinner_category);
        back_button = findViewById(R.id.back_button);

        // spinner list
        List<String> category_list = new ArrayList<>();
        category_list.add(0, "KIV");
        category_list.add("General");
        category_list.add("Important");
        category_list.add("Urgent");

        ArrayAdapter<String> categoryAdapter;
        categoryAdapter = new ArrayAdapter<>(this, R.layout.category_spinner, category_list);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(categoryAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                if (parent.getItemAtPosition(i).equals("KIV")) {
                    // do nothing
                } else {
                    // on selecting a spinner
                    String item = parent.getItemAtPosition(i).toString();

                    // link to another activity
                    if (parent.getItemAtPosition(i).equals("General")) {
                        Intent intent = new Intent(KIV.this, General.class);
                        startActivity(intent);
                    } else if (parent.getItemAtPosition(i).equals("Important")) {
                        Intent intent = new Intent(KIV.this, Important.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(KIV.this, Urgent.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KIV.this, AddActivity.class);
                startActivity(intent);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KIV.this, MainActivity.class);
                startActivity(intent);
            }
        });

        myDB = new MyDatabaseHelper(KIV.this);
        task_id = new ArrayList<>();
        task_title = new ArrayList<>();
        task_desc = new ArrayList<>();
        task_cat = new ArrayList<>();
        task_status = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter(KIV.this, this, task_id, task_title, task_desc, task_cat, task_status, this::remoteUpdate);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(KIV.this));

    }

    private Object remoteUpdate(Object o) {
        recreate();
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }

    void storeDataInArrays() {
        Cursor cursor = myDB.readKIVData();
        if (cursor.getCount() == 0) {
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        } else {
            while (cursor.moveToNext()) {
                task_id.add(cursor.getString(0));
                task_title.add(cursor.getString(1));
                task_desc.add(cursor.getString(2));
                task_cat.add(cursor.getString(3));
                task_status.add(cursor.getString(4));
            }
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
    }

    KIV showPopupMenu() {
        ImageView task_status_options = findViewById(R.id.task_status_options);

        PopupMenu popup = new PopupMenu(KIV.this, task_status_options);
        popup.getMenuInflater().inflate(R.menu.status_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(KIV.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        return null;
    }
}