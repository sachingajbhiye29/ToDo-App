package com.sachin.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sachin.todo.Adapter.ToDOAdapter;
import com.sachin.todo.Database.DataBaseHelper;
import com.sachin.todo.Model.ToDoModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListener{

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private DataBaseHelper myDB;
    private List<ToDoModel> list;
    private ToDOAdapter toDOAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        myDB = new DataBaseHelper(MainActivity.this);
        list = new ArrayList<>();
        toDOAdapter = new ToDOAdapter(myDB, MainActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(toDOAdapter);

        list = myDB.getAllTask();
        //database store the data from top to bottom
        // but we want to show the latest data on top so use collection
        Collections.reverse(list);
        toDOAdapter.setTask(list);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewTask.newInstance().show(getSupportFragmentManager(), addNewTask.TAG);
            }
        });

        //call the TouchHelper class
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(toDOAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        list = myDB.getAllTask();
        //database store the data from top to bottom
        // but we want to show the latest data on top so use collection
        Collections.reverse(list);
        toDOAdapter.setTask(list);
        toDOAdapter.notifyDataSetChanged();
    }
}