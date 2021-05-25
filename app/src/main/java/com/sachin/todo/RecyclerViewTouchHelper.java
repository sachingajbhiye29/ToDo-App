package com.sachin.todo;

import android.content.DialogInterface;
import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.sachin.todo.Adapter.ToDOAdapter;


public class RecyclerViewTouchHelper extends ItemTouchHelper.SimpleCallback{

    private ToDOAdapter adapter;

    public RecyclerViewTouchHelper(ToDOAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove( RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,  RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped( RecyclerView.ViewHolder viewHolder, int direction) {

        //collect the position of the swiped item;
        final int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.RIGHT){
            AlertDialog. Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Delete Task");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user clicks on positive button "yes" then he wants to delete the task;
                    adapter.deleteTask(position);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyItemChanged(position);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else{
                adapter.editTask(position);
        }
    }

    //method to show the background color on swiping;
    //this method needs v7 of recyclerview
  //  @Override
  /*  public void onChildDraw(Canvas c, RecyclerView recyclerView,  RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }*/
}
