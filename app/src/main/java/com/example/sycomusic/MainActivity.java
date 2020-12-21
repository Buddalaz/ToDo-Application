package com.example.sycomusic;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

 public class MainActivity extends AppCompatActivity {

    private Button add;
    private ListView listView;
    private TextView count;
    Context context;
    private DbHandler dbHandler;
    private List<ToDo> toDos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing all references
        context = this;
        dbHandler = new DbHandler(context);
        add = findViewById(R.id.add);
        listView = findViewById(R.id.todolist);
        count = findViewById(R.id.todocount);
        toDos = new ArrayList<>();

        toDos = dbHandler.getAllToDos();

        //set the adapter to the mainActivity
        ToDoAdapter toDoAdapter = new ToDoAdapter(context, R.layout.single_todo, toDos);
        listView.setAdapter(toDoAdapter);

        /*get todo count from the table*/
        int countToDo = dbHandler.countToDo();
        count.setText("You have "+countToDo+" todos");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AddToDo.class));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ToDo toDo = toDos.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(toDo.getTitle());
                builder.setMessage(toDo.getDescription());


                builder.setPositiveButton("finished", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toDo.setFinished(System.currentTimeMillis());
                        dbHandler.updateSingleToDo(toDo);
                        startActivity(new Intent(context,MainActivity.class));
                    }
                });
                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int deleteToDo = dbHandler.deleteToDo(toDo.getId());
                        if (deleteToDo > 0){
                            Toast.makeText(context,"ToDo Deleted!!!!!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(context,MainActivity.class));
                        }else {
                            Toast.makeText(context, "Unable to Delete ToDo!!!! Please try again!..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNeutralButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, EditToDo.class);
                        intent.putExtra("id",String.valueOf(toDo.getId()));
                        startActivity(intent);
                    }
                });

                builder.show();
            }
        });




    }
}