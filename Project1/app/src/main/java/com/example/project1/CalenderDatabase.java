package com.example.project1;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

//@Database(entities = {MemoSource.class}, version = 1)
@Database(entities = {MemoSource.class}, version = 2, exportSchema = false)
public abstract class CalenderDatabase extends RoomDatabase {
    public abstract MemoSourceDao memoSourceDao();

    private static CalenderDatabase INSTANCE;

    private static CalenderDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (CalenderDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CalenderDatabase.class,
                            "calender_memo-db").build();
                }
            }
        }
        return INSTANCE;
    }
}
