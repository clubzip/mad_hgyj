package com.example.project1;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MemoSource {
    @PrimaryKey(autoGenerate = true)
    private  int id;

    @ColumnInfo
    private String year;
    private String month;
    private String day;
    private String memo;
    private String date;

    public MemoSource(String year, String month, String day, String memo) {
        this.memo = memo;
        this.year = year;
        this.month = month;
        this.day = day;
        this.date = year+" "+month+" "+day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        final  StringBuffer sb = new StringBuffer("");
        sb.append(memo);
        return sb.toString();
    }


}
