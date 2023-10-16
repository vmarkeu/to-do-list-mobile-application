package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

public class AddActivity extends AppCompatActivity {

    EditText title_input, desc_input;
    RadioButton kiv_input, general_input, important_input, urgent_input;
    Button add_button;
    String cat_input, status_input;
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        title_input = findViewById(R.id.title_input);
        desc_input = findViewById(R.id.desc_input);
        kiv_input = findViewById(R.id.kiv_input);
        general_input = findViewById(R.id.general_input);
        important_input = findViewById(R.id.important_input);
        urgent_input = findViewById(R.id.urgent_input);
        add_button = findViewById(R.id.add_button);
        back_button = findViewById(R.id.back_button);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get category
                if(kiv_input.isChecked()){
                    cat_input = kiv_input.getText().toString();
                }else if(general_input.isChecked()){
                    cat_input = general_input.getText().toString();
                }else if(important_input.isChecked()){
                    cat_input = important_input.getText().toString();
                }else{
                    cat_input = urgent_input.getText().toString();
                }

                status_input = "PENDING"; // set status = pending

                if (title_input.getText().toString().length() == 0 ) {
                    title_input.setError("Task title is required!");
                } else {
                    MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                    myDB.addTask(title_input.getText().toString().trim(),
                            desc_input.getText().toString().trim(),
                            cat_input, status_input);

                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        // back button
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}