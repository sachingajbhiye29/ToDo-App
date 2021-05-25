package com.sachin.todo.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sachin.todo.Database.DataBaseHelper;
import com.sachin.todo.MainActivity;
import com.sachin.todo.Model.ToDoModel;
import com.sachin.todo.R;
import com.sachin.todo.addNewTask;

import java.util.List;

public class ToDOAdapter  extends RecyclerView.Adapter<ToDOAdapter.MyViewHolder>{

    private List<ToDoModel> list;
    private MainActivity mainActivity;
    private DataBaseHelper myDB;

    public ToDOAdapter(DataBaseHelper myDB, MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.myDB = myDB;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ToDOAdapter.MyViewHolder holder, int position) {

        // now set the values is the checkbox;

        holder.checkBox.setText(list.get(position).getTask());
        holder.checkBox.setChecked(tooBoolean(list.get(position).getStatus()));

        //add onCheckListener to the checkbox;
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //if checked status will be 1 as a parameter;
                    myDB.updateStatus(list.get(position).getId(), 1);
                }
                else {
                    // if unchecked status will be 0 as a parameter;
                    myDB.updateStatus(list.get(position).getId(),0);
                }
            }
        });
    }

    //method to make the int to boolean to set checkBox checked
    public boolean tooBoolean(int num){
        return num!=0;
    }

    //method for context to get context when adding the task;
    public Context getContext(){
        return mainActivity;
    }

    //method to set task
    public void setTask(List<ToDoModel> list){
        //this will create our list to ToDoAdapter;
        this.list = list;
        notifyDataSetChanged();
    }

    //method to call delete task method in DataBase class;
    public void deleteTask(int position){
        myDB.deleteTask(list.get(position).getId());
        list.remove(position);
        notifyItemRemoved(position);
    }

    //method to call insert method;
    public void editTask(int position){
        //use Bundle to pass the data to in key value pairs;
        Bundle bundle = new Bundle();
        bundle.putInt("id",list.get(position).getId());
        bundle.putString("task", list.get(position).getTask());

        //now create instance of fragment class which is addNewTask;
        addNewTask task = new addNewTask();
        //passing the data from activity to fragment;
        task.setArguments(bundle);
        task.show(mainActivity.getSupportFragmentManager(), task.getTag());


    }


    @Override
    public int getItemCount() {
        return list.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;
        public MyViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
