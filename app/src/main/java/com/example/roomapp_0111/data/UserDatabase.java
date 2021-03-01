package com.example.roomapp_0111.data;
//TODO:建立資料庫

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


// entity:表示資料庫內的表
@Database(entities = {MyData.class}, version = 1, exportSchema = true)
public abstract class UserDatabase extends RoomDatabase {

    public static final String DB_NAME = "RecordData.db";   //資料庫名稱
    private static volatile UserDatabase instance;

    public static synchronized UserDatabase getInstance(Context context){
        if (instance == null){
            instance = create(context); //建立新的資料庫
        }
        return instance;
    }

    private static UserDatabase create(final Context context) {
        return Room.databaseBuilder(context, UserDatabase.class, DB_NAME).build();
    }

    public abstract DataDao getDataDao();  //設置對外的接口


}
