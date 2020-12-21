package com.example.sycomusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddToDo extends AppCompatActivity {

    private EditText title, desc;
    private Button add;
    private DbHandler dbHandler;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);

        //initializing the xml object to android
        title = findViewById(R.id.editTextTitle);
        desc = findViewById(R.id.editTextDescription);
        add = findViewById(R.id.buttonAdd);
        context = this;

        dbHandler = new DbHandler(context);

        //getting users data to insert that data to database
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userTitle = title.getText().toString();
                String userDesc = desc.getText().toString();
                //getting system current date
                long started = System.currentTimeMillis();

                ToDo toDo = new ToDo(userTitle, userDesc, started, 0);
                Long insert1 = dbHandler.addToDo(toDo);
                /*System.out.println(insert1);*/
                if (insert1 > 0){
                    Toast.makeText(context,"ToDo Added!!!!!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(context,MainActivity.class));
                }else {
                    Toast.makeText(context, "Unable to Add ToDo!!!! Please try again!..", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}