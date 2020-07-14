package com.example.project1;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MemoSourceDao {
    @Query("SELECT * FROM MemoSource")
    List<MemoSource> getAll();

    @Query("SELECT * FROM MemoSource WHERE date LIKE :search")
    MemoSource findByDate(String search);

    @Insert
    void insert(MemoSource memoSource);

    @Update
    void update(MemoSource memoSource);

    @Delete
    void delete(MemoSource memoSource);
}