package com.example.sycomusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditToDo extends AppCompatActivity {

    private EditText title, desc;
    private Button edit;
    private DbHandler dbHandler;
    private Context context;
    private Long updateDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_to_do);

        //initializing the xml object to android
        context = this;
        dbHandler = new DbHandler(context);

        title = findViewById(R.id.editToDoTextTitle);
        desc = findViewById(R.id.editToDoTextDescription);
        edit = findViewById(R.id.buttonEdit);

        final String id = getIntent().getStringExtra("id");
        ToDo singleToDo = dbHandler.getSingleToDo(Integer.parseInt(id));

        title.setText(singleToDo.getTitle());
        desc.setText(singleToDo.getDescription());

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textText = title.getText().toString();
                String descText = desc.getText().toString();
                updateDate = System.currentTimeMillis();

                ToDo toDo = new ToDo(Integer.parseInt(id), textText, descText, updateDate, 0);
                int status = dbHandler.updateSingleToDo(toDo);
                System.out.println(status);
                if(status > 0){
                    Toast.makeText(context,"Succssefuly Update", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(context,MainActivity.class));
                }else {
                    Toast.makeText(context, "Unable to Update!!!! Please try again!..", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}