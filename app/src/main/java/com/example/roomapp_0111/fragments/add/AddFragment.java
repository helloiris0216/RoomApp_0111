package com.example.roomapp_0111.fragments.add;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

    //返回鍵設定: 1.將 View 設為全域
    View view;
    //返回鍵設定: 2.設定全域 ActionBar
    ActionBar bar;

    private EditText ed_add_firstName, ed_add_lastName, ed_add_age;
    private Button btn_add;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //返回鍵設定: 3.使用全域 View
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add, container, false);

        //返回鍵設定: 4.設定顯示 Menu，這樣才能監聽
        setHasOptionsMenu(true);
        //返回鍵設定: 5.設定 ActionBar 顯示返回鍵
        bar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);


        // 將資料新增到資料庫: 1.find views
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

    // 將資料新增到資料庫: 2.設定新增資料的功能
    private void insertDataToDB() {
        final String firstName = ed_add_firstName.getText().toString();
        final String lastName = ed_add_lastName.getText().toString();
        final String age = ed_add_age.getText().toString();

        Log.d("insertDataToDB", "firstName : "+firstName);
        Log.d("insertDataToDB", "age : "+age);

        // 將資料新增到資料庫: 判斷使用者是否有輸入
        if (firstName.isEmpty() || lastName.isEmpty() || age.equals("")){
            Toast.makeText(getContext(), "Please fill out the fields", Toast.LENGTH_SHORT).show();
        } else {
            // 將資料新增到資料庫: Room 一定要在背景執行緒進行動作，因為撈資料是很耗時的事情
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    MyData myData = new MyData(firstName, lastName, age);
                    UserDatabase.getInstance(getContext()).getDataDao().insertData(myData);

                    // 將資料新增到資料庫: 按下 Add btn 後，即可返回到上一頁
                    Navigation.findNavController(view).popBackStack();
                }
            }.start();

        }
    } //end insetDataToDB


    //返回鍵設定: 6.設定返回鍵功能
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Navigation.findNavController(view).popBackStack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //返回鍵設定: 7.在生命週期 onStop 時，將返回鍵關閉
    @Override
    public void onStop() {
        super.onStop();
        bar.setDisplayHomeAsUpEnabled(false);
    }

} //end