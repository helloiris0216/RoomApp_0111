package com.example.roomapp_0111.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "MyTable")  //這邊要先取好table的名字，稍後的table設置必須與他相同
public class MyData {

        @PrimaryKey(autoGenerate = true)    //設置是否使ID自動累加
        private int id;
        private String firstName;
        private String lastName;
        private String age;

        public MyData(String firstName, String lastName, String age) {
                this.firstName = firstName;
                this.lastName = lastName;
                this.age = age;
        }

        @Ignore
        public MyData(int id, String firstName, String lastName, String age) {
                this.id = id;
                this.firstName = firstName;
                this.lastName = lastName;
                this.age = age;
        }

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public String getFirstName() {
                return firstName;
        }

        public void setFirstName(String firstName) {
                this.firstName = firstName;
        }

        public String getLastName() {
                return lastName;
        }

        public void setLastName(String lastName) {
                this.lastName = lastName;
        }

        public String getAge() {
                return age;
        }

        public void setAge(String age) {
                this.age = age;
        }
}
