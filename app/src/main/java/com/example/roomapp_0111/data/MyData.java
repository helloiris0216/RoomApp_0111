package com.example.roomapp_0111.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "MyTable")  //這邊要先取好table的名字，稍後的table設置必須與他相同
public class MyData {

        @PrimaryKey(autoGenerate = true)    //設置是否使ID自動累加
        private int id;
        private String name;
        private String birthday;
        private String age;

        public MyData(String name, String birthday, String age) {
                this.name = name;
                this.birthday = birthday;
                this.age = age;
        }

        @Ignore
        public MyData(int id, String name, String birthday, String age) {
                this.id = id;
                this.name = name;
                this.birthday = birthday;
                this.age = age;
        }

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getBirthday() {
                return birthday;
        }

        public void setBirthday(String birthday) {
                this.birthday = birthday;
        }

        public String getAge() {
                return age;
        }

        public void setAge(String age) {
                this.age = age;
        }
}
