package com.danchua.codechallenge.wishlist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.danchua.codechallenge.db.entity.Result;
import com.danchua.codechallenge.network.Repository;

import java.util.List;

public class WishlistViewModel extends ViewModel {

    private Repository mRepository;

    public void init(Repository repository) {
        this.mRepository = repository;
    }

    public LiveData<List<Result>> getAllWishList() {
        return mRepository.getAllResult();
    }

}
