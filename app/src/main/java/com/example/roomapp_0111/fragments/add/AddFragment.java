package com.example.roomapp_0111.fragments.add;

import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.LocaleData;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.roomapp_0111.R;
import com.example.roomapp_0111.data.MyData;
import com.example.roomapp_0111.data.UserDatabase;
import com.example.roomapp_0111.fragments.list.MyAdapter;

import java.time.LocalDate;
import java.time.Period;
import java.util.Locale;


public class AddFragment extends Fragment {

    //返回鍵設定: 1.將 View 設為全域
    View view;
    //返回鍵設定: 2.設定全域 ActionBar
    ActionBar bar;

    private EditText ed_addF_name;
    private TextView tv_addF_birthday, tv_add_age;
    private Button btn_add;
    MyAdapter myAdapter;
    private ImageView img_cake;

    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener datePicker;

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        ed_addF_name = view.findViewById(R.id.ed_addF_name);
        tv_addF_birthday = view.findViewById(R.id.tv_addF_birthday);
        tv_add_age = view.findViewById(R.id.tv_add_age);

        btn_add = view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertDataToDB();
            }
        });

        /**03.設置生日的點擊事件*/
        img_cake = view.findViewById(R.id.img_addF__cake);
        img_cake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(),
                        R.style.MyDialogTheme,
                        datePicker,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                dialog.show();
            }
        });

        /**02.在 onCreateView中呼叫設定生日的方法*/
        setBth();

        return view;
    }

    // 將資料新增到資料庫: 01.抓取使用者輸入
    private void insertDataToDB() {
        final String name = ed_addF_name.getText().toString();
        final String birthday = tv_addF_birthday.getText().toString();
        final String age = tv_add_age.getText().toString();

        Log.d("insertDataToDB", "firstName : "+name);
        Log.d("insertDataToDB", "age : "+age);

        // 將資料新增到資料庫: 判斷使用者是否有輸入
        if (name.isEmpty() || birthday.isEmpty() || age.equals("")){
            Toast.makeText(getContext(), "Please fill out the fields", Toast.LENGTH_SHORT).show();

        } else {
            // 將資料新增到資料庫: 02.Room 一定要在背景執行緒進行動作，因為撈資料是很耗時的事情
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    MyData myData = new MyData(name, birthday, age);
                    UserDatabase.getInstance(getContext()).getDataDao().insertData(myData);

                    // 將資料新增到資料庫: 按下 Add btn 後，即可返回到 listFragment
                    Navigation.findNavController(view).navigate(R.id.listFragment);

                    // 在背景執行緒中更新 ui 元件
                    getActivity().runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            //myAdapter.refreshView();
                            ed_addF_name.setText("");
                            tv_addF_birthday.setText("");
                            tv_add_age.setText("");
                        }
                    });

                } //end run()
            }.start();

        }
    } //end insetDataToDB()



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


    //TODO:利用 datePickerDialog 設定生日輸入
    /**01.建立日曆並取得時間*/
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setBth() {
        calendar = Calendar.getInstance();
        datePicker = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy/MM/dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.TAIWAN);
                tv_addF_birthday.setText("生日：" + sdf.format(calendar.getTime()));

                /**設定年齡*/
                // 01.取得系統當前日期
                LocalDate now = LocalDate.now();

                // 取得輸入的生日
                int bth_y = calendar.get(Calendar.YEAR);
                int bth_m = calendar.get(Calendar.MONTH);
                int bth_d = calendar.get(Calendar.DAY_OF_MONTH);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("bth", "now_year:"+now.getYear());
                        Log.d("bth", "now_m:"+now.getMonth());
                        Log.d("bth", "now_d:"+now.getDayOfMonth());
                        Log.d("bth", "bth_year:"+bth_y);
                        Log.d("bth", "bth_m:"+bth_m);
                        Log.d("bth", "bth_d:"+bth_d);

                    }
                }).start();

                // 03.計算年齡
                Period period = Period.between(LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth()),
                                               LocalDate.of(bth_y, bth_m, bth_d));

                // 04.將計算結果顯示在 tv
                tv_add_age.setText("年齡 : " + Integer.toString(Math.abs(period.getYears())) + "歲");

            }
        };
    } //end setBth()


} //end