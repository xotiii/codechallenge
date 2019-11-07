package com.danchua.codechallenge.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.danchua.codechallenge.db.dao.ResultDao;
import com.danchua.codechallenge.db.entity.Result;

/**
 * ROOM DATABASE
 *
 *
 * **/
@Database(entities = { Result.class}, version = 1 )
public abstract class AppDatabase extends RoomDatabase {
    public abstract ResultDao resultDao();
}
