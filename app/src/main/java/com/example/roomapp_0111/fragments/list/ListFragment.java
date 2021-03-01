package com.example.roomapp_0111.fragments.list;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roomapp_0111.R;
import com.example.roomapp_0111.data.MyData;
import com.example.roomapp_0111.data.UserDatabase;
import com.facebook.stetho.Stetho;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class ListFragment extends Fragment {


    private FloatingActionButton btn_actionBar;
    private RecyclerView recyclerView_listF;
    private List<MyData> myData;
    MyData nowSelectedData;//取得在畫面上顯示中的資料內容
    private MyAdapter myAdapter;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener datePicker;
    private TextView tv_age, tv_birthday, tv_constellation;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO:取得畫面
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        Stetho.initializeWithDefaults(getActivity());//設置資料庫監視

        btn_actionBar = (FloatingActionButton) view.findViewById(R.id.btn_floatingAction_bar_listF);
        btn_actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_listFragment_to_addFragment);
            }
        });


        //TODO:初始化 RecyclerView (顯示資料)
        //02.宣告元件
        recyclerView_listF = (RecyclerView) view.findViewById(R.id.recyclerView_listF);
        //03.設置列表型態、格線
        recyclerView_listF.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView_listF.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
//        recyclerView_listF.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//                super.getItemOffsets(outRect, view, parent, state);
//                outRect.bottom = 10;
//            }
//        });

        //04.將資料交給adapter
        new Thread(){
            @Override
            public void run() {
                super.run();
                //01.準備資料 (撈出在資料庫的資料)
                List<MyData> data = UserDatabase.getInstance(getActivity()).getDataDao().displayAll();
                Log.d("MyData", "data:"+data);
                myAdapter = new MyAdapter(getActivity(), data);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //05.設置adapter給recycler_view
                        recyclerView_listF.setAdapter(myAdapter);

                        /**實現RecyclerView點選事件: 05.外部呼叫定義點擊事件的方法*/
                        myAdapter.setMyItemClickListener(new MyAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(MyData myData) {
                                Toast.makeText(getContext(), "你點選了"+myData.getName() , Toast.LENGTH_SHORT).show();
                                nowSelectedData = myData;
                                Dialog_modify(myData, view);
//
                            }
                        });

                    }
                });
            }
        }.start();

        setRecyclerFunction(recyclerView_listF);//設置RecyclerView左滑刪除

        setBth(); //設定生日

        return view;
    } //end onCreateView()



    //TODO:資料的修改
    private void Dialog_modify(MyData myData, View view){
        /**01.建立對話框*/
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        /**02.取得資料並顯示在畫面*/
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_layout, (ViewGroup) view.findViewById(R.id.dialog));
        EditText ed_name = v.findViewById(R.id.ed_name_d);
        tv_birthday = v.findViewById(R.id.tv_birthday_d);
        tv_age = v.findViewById(R.id.tv_age_d);

        ed_name.setText(myData.getName());
        tv_birthday.setText(myData.getBirthday());
        tv_age.setText(myData.getAge());

        builder.setTitle(ed_name.getText().toString())
               .setView(v)
               .setPositiveButton("修改", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String name = ed_name.getText().toString();
                String birthday = tv_birthday.getText().toString();
                String age = tv_age.getText().toString();

                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        MyData mData = new MyData(myData.getId(), name, birthday, age);
                        UserDatabase.getInstance(getContext()).getDataDao().updateData(mData);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                nowSelectedData = null;
                                myAdapter.refreshView();
                            }
                        });
                    }
                }.start();

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消", null);
        builder.create().show();


        /**03.設置生日的點擊事件*/
        ImageView img_cake = v.findViewById(R.id.img_cake_d);
        img_cake.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
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
    } //end Dialog_modify()


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

                tv_birthday.setText("生日：" + sdf.format(calendar.getTime()));

                /**設定年齡*/
                // 01.取得系統當前日期
                LocalDate now = LocalDate.now();

                // 02.取得輸入的生日
                int bth_y = calendar.get(Calendar.YEAR);
                int bth_m = calendar.get(Calendar.MONTH);
                int bth_d = calendar.get(Calendar.DAY_OF_MONTH);

                // 03.計算年齡
                Period period = Period.between(LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth()),
                        LocalDate.of(bth_y, bth_m, bth_d));

                // 04.將計算結果顯示在 tv
                tv_age.setText("年齡 : " + Integer.toString(Math.abs(period.getYears())) + "歲");

            }
        };
    } //end setBth()




    //TODO:設置RecyclerView的左滑刪除行為
    private void setRecyclerFunction(RecyclerView recyclerView){
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {//設置RecyclerView手勢功能

            //這裡是告訴RecyclerView你想開啟哪些操作
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT);
            }

            //管理上下拖曳
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder       target) {
                return false;
            }

            //管理滑動情形
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                switch (direction){
                    case ItemTouchHelper.LEFT:
                    case ItemTouchHelper.RIGHT:
                        myAdapter.deleteData(position);
                        break;

                }
            }

            // 刪除的紅色底圖和圖標
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(getContext(),android.R.color.holo_red_dark))
                        .addActionIcon(R.drawable.ic_baseline_delete_24)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });
        helper.attachToRecyclerView(recyclerView);
    } //end setRecyclerFunction()

}