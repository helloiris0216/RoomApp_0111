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
        private final TextView tv_id, tv_firstName, tv_lastName, tv_age;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_id = (TextView) itemView.findViewById(R.id.textView_id);
            tv_lastName = (TextView) itemView.findViewById(R.id.textView_lastName);
            tv_firstName = (TextView) itemView.findViewById(R.id.textView_firstName);
            tv_age = (TextView) itemView.findViewById(R.id.tv_age);

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

        holder.tv_id.setText(Integer.toString(myData.get(position).getId()));
        holder.tv_lastName.setText(myData.get(position).getLastName());
        holder.tv_firstName.setText(myData.get(position).getFirstName());
        holder.tv_age.setText(myData.get(position).getAge());

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
