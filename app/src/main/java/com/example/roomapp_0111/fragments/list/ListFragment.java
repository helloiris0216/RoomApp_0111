package com.example.roomapp_0111.fragments.list;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roomapp_0111.R;
import com.example.roomapp_0111.data.MyData;
import com.example.roomapp_0111.data.UserDatabase;
import com.facebook.stetho.Stetho;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;



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
        //01.準備資料
//        mData = new ArrayList<>();
//        for (int i=0; i<50; i++){
//            HashMap<String,String> map = new HashMap<>();
//            map.put("Id","座號："+String.format("%02d",i+1));
//            map.put("last name", "姓:" + String.valueOf(new Random().nextInt(80) + 20));
//            map.put("first name", "名:" + String.valueOf(new Random().nextInt(80) + 20));
//            map.put("age", "年齡:" + String.valueOf(new Random().nextInt(80) + 20));
//            mData.add(map);
//        }

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
                    }
                });

            }
        }.start();
        return view;
    }

}