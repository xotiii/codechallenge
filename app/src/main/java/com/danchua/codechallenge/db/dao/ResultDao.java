package com.danchua.codechallenge.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.danchua.codechallenge.db.entity.Result;

import java.util.List;

@Dao
public interface ResultDao {


    @Insert(onConflict = OnConflictStrategy.FAIL)
    void save(Result result);

    @Query("SELECT * FROM Result where trackId=:trackId")
    Result select(int trackId);

    @Query("SELECT * FROM Result")
    LiveData<List<Result>> getAllResult();

    @Delete
    void remove(Result result);

}
