package com.danchua.codechallenge.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.danchua.codechallenge.network.Repository;
import com.danchua.codechallenge.network.utils.response.ResultList;
import com.danchua.codechallenge.utils.Resource;

import java.util.Date;


/**
 *
 * ViewModel for Home
 *
 * ViewModel connects the View to the Repository
 * for data manipulation
 *
 * */
public class HomeViewModel extends ViewModel {

    private Repository mRepository;

    public void init(Repository repository) {
        this.mRepository = repository;
    }

    public LiveData<Resource<ResultList>> getResultList() {
        return mRepository.getResultList();
    }

    public Date getLastVisit() {
        return mRepository.getLastVisit();
    }

}
