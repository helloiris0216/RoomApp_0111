package com.example.roomapp_0111.fragments.list;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomapp_0111.R;
import com.example.roomapp_0111.data.MyData;
import com.example.roomapp_0111.data.UserDatabase;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<MyData> myData;
    private Activity activity;

    /**實現RecyclerView點選事件: 02.外部宣告我們的定義的介面 */
    private OnItemClickListener myItemClickListener;


    public MyAdapter(Activity activity, List<MyData> myData) {
        this.activity = activity;
        this.myData = myData;
    }


    /**實現RecyclerView點選事件: 01.建立對外接口*/
    public interface OnItemClickListener {
        void onItemClick(MyData myData);
    }

    /**更新資料*/
    // 重新抓取資料庫內所有資料並顯示
    public void refreshView() {
        new Thread(()->{
            List<MyData> data = UserDatabase.getInstance(activity).getDataDao().displayAll();
            this.myData = data;
            activity.runOnUiThread(() -> {
                notifyDataSetChanged();
            });
        }).start();
    }

    /**刪除資料*/
    public void deleteData(int position){
        new Thread(()->{
            UserDatabase.getInstance(activity).getDataDao().deleteData(myData.get(position).getId());
            activity.runOnUiThread(()->{
                notifyItemRemoved(position);
                refreshView();
            });
        }).start();
    }


    // 建立ViewHolder，在這個內部類別內控制元件
    public class ViewHolder extends RecyclerView.ViewHolder {
        // 宣告元件
        private final TextView tv_name, tv_birthday, tv_age, tv_constellation;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_birthday = (TextView) itemView.findViewById(R.id.tv_birthday);
            tv_age = (TextView) itemView.findViewById(R.id.tv_age);
            tv_constellation = (TextView) itemView.findViewById(R.id.tv_constellation);

            view = itemView;
        }
    } //end ViewHolder()


    // 連接剛才寫的layout檔案，return一個View
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    // 在這裡取得元件的控制(每個item內的控制)
    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {

        holder.tv_name.setText(myData.get(position).getName());
        holder.tv_birthday.setText(myData.get(position).getBirthday());
        holder.tv_age.setText(myData.get(position).getAge());

        String bth_m = myData.get(position).getBirthday();
        String month = bth_m.substring(8, 10);
        int day = Integer.parseInt(bth_m.substring(11));
        Log.d("constellation", "string_len:"+bth_m.length());
        Log.d("constellation", "month:"+month);
        Log.d("constellation", "day:"+day);
        String constellation = "";
        switch (month){
            case "01":
                constellation = day<21?"摩羯座":"水瓶座";
                holder.tv_constellation.setText(constellation);
                break;
            case "02":
                constellation = day<20? "水瓶座":"雙魚座";
                holder.tv_constellation.setText(constellation);
                break;
            case "03":
                constellation = day<21?"雙魚座":"白羊座";
                holder.tv_constellation.setText(constellation);
                break;
            case "04":
                constellation = day<21?"白羊座":"金牛座";
                holder.tv_constellation.setText("牡羊座");
                break;
            case "05":
                constellation = day<22?"金牛座":"雙子座";
                holder.tv_constellation.setText(constellation);
                break;
            case "06":
                constellation = day<22?"雙子座":"巨蟹座";
                holder.tv_constellation.setText(constellation);
                break;
            case "07":
                constellation = day<23?"巨蟹座":"獅子座";
                holder.tv_constellation.setText(constellation);
                break;
            case "08":
                constellation = day<24?"獅子座":"處女座";
                holder.tv_constellation.setText(constellation);
                break;
            case "09":
                constellation = day<24?"處女座":"天秤座";
                holder.tv_constellation.setText(constellation);
                break;
            case "10":
                constellation = day<24?"天秤座":"天蠍座";
                holder.tv_constellation.setText(constellation);
                break;
            case "11":
                constellation = day<23?"天蠍座":"射手座";
                holder.tv_constellation.setText(constellation);
                break;
            case "12":
                constellation = day<22?"射手座":"摩羯座";
                holder.tv_constellation.setText(constellation);
                break;
        }



        /**實現RecyclerView點選事件: 03.為ItemView新增點選事件*/
        holder.view.setOnClickListener(v -> myItemClickListener.onItemClick(myData.get(position)));

    }

    /**實現RecyclerView點選事件: 04.定義方法，給外部呼叫*/
    /**item的點選事件處理，這裡採用了介面回撥的方法實現*/
    /**這裡採用了介面回撥的方法實現，將該方法暴露給外部，便於外部呼叫*/
    public void setMyItemClickListener(OnItemClickListener myItemClickListener){
        this.myItemClickListener = myItemClickListener;
    }


    // 取得顯示數量，return一個int，通常都會return陣列長度(arrayList.size)
    @Override
    public int getItemCount() {
        Log.d("myData_size", "getItemCount: "+myData.size());
        return myData.size();
    }


}
