package com.danchua.codechallenge;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.database.sqlite.SQLiteConstraintException;

import com.danchua.codechallenge.db.entity.Result;
import com.danchua.codechallenge.network.Repository;
import com.danchua.codechallenge.utils.Resource;

import java.util.Date;

public class MainViewModel extends ViewModel {

    private Repository mRepository;

    public void init(Repository repository) {
        this.mRepository = repository;
    }

    public LiveData<Result> selectResult(int trackId) {
        return mRepository.getResult(trackId);
    }

    public LiveData<Resource> saveResult(Result result) throws SQLiteConstraintException {
        return mRepository.saveResult(result);
    }

    public LiveData<Resource> removeResult(Result result) {
        return mRepository.removeResult(result);
    }

    public void setLastVisit(Date date) {
        mRepository.setLastVisit(date);
    }

}
