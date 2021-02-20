package com.example.roomapp_0111.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Dao
public interface DataDao {

    String tableName = "MyTable";

    /**簡易新增所有資料的方法*/
    @Insert(onConflict = OnConflictStrategy.IGNORE)//預設萬一執行出錯怎麼辦，IGNORE
    void insertData(MyData myData);

    /**撈取全部資料*/
    @Query("SELECT * FROM MyTable ORDER BY id ASC")
    List<MyData> displayAll();

    /**簡易更新資料的方法*/
    @Update
    void updateData(MyData myData);

    /**簡單刪除資料的方法*/
    @Delete
    void deleteData(MyData myData);

    /**複雜(?)刪除資料的方法*/
    @Query("DELETE  FROM " + tableName + " WHERE id = :id")
    void deleteData(int id);


}
