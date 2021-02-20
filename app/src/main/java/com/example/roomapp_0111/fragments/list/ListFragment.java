package com.example.roomapp_0111.fragments.list;

import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roomapp_0111.MainActivity;
import com.example.roomapp_0111.R;
import com.example.roomapp_0111.data.MyData;
import com.example.roomapp_0111.data.UserDatabase;
import com.facebook.stetho.Stetho;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class ListFragment extends Fragment {


    private FloatingActionButton btn_actionBar;
    private RecyclerView recyclerView_listF;
    private List<MyData> myData;
    MyData nowSelectedData;//取得在畫面上顯示中的資料內容
    private MyAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        Stetho.initializeWithDefaults(getActivity());//設置資料庫監視

        btn_actionBar = (FloatingActionButton) view.findViewById(R.id.btn_floatingAction_bar_listF);
        btn_actionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_listFragment_to_addFragment);
            }
        });

        //TODO: 將資料顯示在 recyclerView
        //02.宣告元件
        recyclerView_listF = (RecyclerView) view.findViewById(R.id.recyclerView_listF);
        //03.設置列表型態、格線
        recyclerView_listF.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView_listF.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        //04.將資料交給adapter
        new Thread(){
            @Override
            public void run() {
                super.run();
                //01.準備資料
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
                                Toast.makeText(getContext(), "你點選了", Toast.LENGTH_SHORT).show();
//                                nowSelectedData = myData;
//                                TextView tv_id_f = (TextView) view.findViewById(R.id.textView_id);
//                                TextView tv_lastName_f = (TextView) view.findViewById(R.id.textView_lastName);
//                                TextView tv_firstName_f = (TextView) view.findViewById(R.id.textView_firstName);
//                                TextView tv_age_f = (TextView) view.findViewById(R.id.tv_age);
//                                tv_id_f.setText(Integer.toString(myData.getId()));
//                                tv_lastName_f.setText(myData.getLastName());
//                                tv_firstName_f.setText(myData.getFirstName());
//                                tv_age_f.setText(myData.getAge());

                            }
                        });

                    }
                });

            }
        }.start();

        setRecyclerFunction(recyclerView_listF);//設置RecyclerView左滑刪除

        return view;
    }

    /**設置RecyclerView的左滑刪除行為*/
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
    }

}