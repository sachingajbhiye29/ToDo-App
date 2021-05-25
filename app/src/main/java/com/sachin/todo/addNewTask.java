package com.sachin.todo;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sachin.todo.Database.DataBaseHelper;
import com.sachin.todo.Model.ToDoModel;

public class addNewTask extends BottomSheetDialogFragment {


    public static final String TAG = "AddNewTask";

    //widgets
    private EditText editText;
    private Button saveButton;

    private DataBaseHelper myDB;

    public static addNewTask newInstance(){
        return new addNewTask();
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_new_task,container, false);
        return view;
    }



    //override onDismiss method to update the recyclerview after the fragment is closed;
    @Override
    public void onDismiss( DialogInterface dialog) {
        super.onDismiss(dialog);

        Activity activity = getActivity();

        //check if
        if(activity instanceof OnDialogCloseListener){
             ((OnDialogCloseListener) activity).onDialogClose(dialog);
        }
    }

    //override one more method onViewCreated;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //find the id of the views;
        editText = view.findViewById(R.id.edtText);
        saveButton = view.findViewById(R.id.btnSave);

        myDB = new DataBaseHelper(getActivity());

        //check if user want to add the data or update the data;
        boolean isUpdate = false;

        //now create a Bundle to retrieve the data coming from the activity;
        Bundle bundle = getArguments();

        //check if bundle has data;
        if(bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            editText.setText(task);

            //check if task length is > 0 to enable the save button false;
            if(task.length()>0){
                saveButton.setEnabled(false);
            }
        }
        //add textChangeListener to enable the save button;
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // we only want to use on text changed;
                if(s.toString().equals("")){
                    saveButton.setEnabled(false);
                    saveButton.setBackgroundColor(getResources().getColor(R.color.gray));
                }
                else{
                    saveButton.setEnabled(true);
                    saveButton.setBackgroundColor(getResources().getColor(R.color.light_red));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //onclick listener to save button;
        boolean finalIsUpdate = isUpdate;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the data from edittext;
                String text = editText.getText().toString();

                //check if user wants to update or insert a new task;

                if(finalIsUpdate){
                    myDB.updateTask(bundle.getInt("id"), text);
                }
                else{
                    ToDoModel item = new ToDoModel();
                    item.setTask(text);
                    item.setStatus(0);
                    myDB.insertTask(item);
                }
                // dismiss the fragment;
                dismiss();
            }
        });



    }
}
