package com.example.roomapp_0111.fragments.add;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.roomapp_0111.R;
import com.example.roomapp_0111.data.MyData;
import com.example.roomapp_0111.data.UserDatabase;

import java.util.List;


public class AddFragment extends Fragment {

    private EditText ed_add_firstName, ed_add_lastName, ed_add_age;
    private Button btn_add;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        // find views
        ed_add_firstName = view.findViewById(R.id.ed_add_firstName);
        ed_add_lastName = view.findViewById(R.id.ed_add_lastName);
        ed_add_age = view.findViewById(R.id.ed_add_age);

        btn_add = view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDataToDB();
            }
        });

        return view;
    }

    private void insertDataToDB() {
        final String firstName = ed_add_firstName.getText().toString();
        final String lastName = ed_add_lastName.getText().toString();
        final String age = ed_add_age.getText().toString();

        Log.d("insertDataToDB", "firstName : "+firstName);
        Log.d("insertDataToDB", "age : "+age);

        if (firstName.isEmpty() || lastName.isEmpty() || age.equals("")){
            Toast.makeText(getContext(), "Please fill out the fields", Toast.LENGTH_SHORT).show();
        } else {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    MyData myData = new MyData(firstName, lastName, age);
                    UserDatabase.getInstance(getContext()).getDataDao().insertData(myData);
                }
            }.start();


        }
    } //insetDataToDB


}