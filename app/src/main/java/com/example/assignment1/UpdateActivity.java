package com.example.assignment1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    EditText title_input, desc_input;
    RadioButton kiv_input, general_input, important_input, urgent_input;
    Button update_button;
    String cat_input;
    ImageView back_button, delete_button;
    TextView update_title;

    String id, title, desc, cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        title_input = findViewById(R.id.title_input2);
        desc_input = findViewById(R.id.desc_input2);
        kiv_input = findViewById(R.id.kiv_input2);
        general_input = findViewById(R.id.general_input2);
        important_input = findViewById(R.id.important_input2);
        urgent_input = findViewById(R.id.urgent_input2);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);
        back_button = findViewById(R.id.back_button);
        update_title = findViewById(R.id.update_title);

        //First we call this
        getAndSetIntentData();

        update_title.setText(title);

        update_button.setOnClickListener(view -> {
            //And only then we call this
            MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
            title = title_input.getText().toString().trim();
            desc = desc_input.getText().toString().trim();
            if (kiv_input.isChecked()) {
                cat = kiv_input.getText().toString();
            } else if (general_input.isChecked()) {
                cat = general_input.getText().toString();
            } else if (important_input.isChecked()) {
                cat = important_input.getText().toString();
            } else {
                cat = urgent_input.getText().toString();
            }
            myDB.updateData(id, title, desc, cat);

            Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // delete task
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });

        // back button
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("title") &&
                getIntent().hasExtra("desc")) {
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            desc = getIntent().getStringExtra("desc");
            String cat = getIntent().getStringExtra("cat");

            //Setting Intent Data
            title_input.setText(title);
            desc_input.setText(desc);
            switch (cat) {
                case "General":
                    general_input.setChecked(true);
                    break;
                case "Important":
                    important_input.setChecked(true);
                    break;
                case "Urgent":
                    urgent_input.setChecked(true);
                    break;
                default:
                    kiv_input.setChecked(true);
            }
            cat_input = this.cat;
            Log.d("stev", title + " " + desc + " " + this.cat);
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + title + " ?");
        builder.setMessage("Are you sure you want to delete " + title + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                myDB.deleteOneRow(id);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}