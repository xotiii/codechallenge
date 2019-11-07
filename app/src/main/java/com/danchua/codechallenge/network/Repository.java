package com.danchua.codechallenge.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.provider.MediaStore;

import com.danchua.codechallenge.db.AppDatabase;
import com.danchua.codechallenge.db.entity.Result;
import com.danchua.codechallenge.network.utils.response.ResultList;
import com.danchua.codechallenge.utils.AppExecutors;
import com.danchua.codechallenge.utils.Resource;

import java.util.Date;
import java.util.List;

import static com.danchua.codechallenge.utils.Constants.PARAM_COUNTRY_VALUE;
import static com.danchua.codechallenge.utils.Constants.PARAM_MEDIA_VALUE;
import static com.danchua.codechallenge.utils.Constants.PARAM_TERM_VALUE;
import static com.danchua.codechallenge.utils.Constants.SPREF_LAST_VISIT_KEY;

public class Repository {

    private AppExecutors mAppExecutors;
    private AppDatabase mAppDatabase;
    private SharedPreferences mSharedPrefs;
    private CodeChallengeService mCodeChallengeService;

    /**
     *
     *  Initialize Repository for data manipulation
     *
     * @param appDatabase Database dependency for complex data saving
     * @param appExecutors Mutli-threading dependency for optimize tasks
     * @param sharedPreferences Shared Preferences for saving single value datas
     * @param codeChallengeService Retrofit client service dependency for API services
     *
     * */
    public Repository(AppExecutors appExecutors, AppDatabase appDatabase,
                      SharedPreferences sharedPreferences,
                      CodeChallengeService codeChallengeService) {
        this.mAppExecutors = appExecutors;
        this.mAppDatabase = appDatabase;
        this.mSharedPrefs = sharedPreferences;
        this.mCodeChallengeService = codeChallengeService;
    }

    /**
     *
     * Get List of items from iTunes Apple API
     * using Retrofit Client
     *
     * */
    public LiveData<Resource<ResultList>> getResultList() {
        MediatorLiveData<Resource<ResultList>> mld = new MediatorLiveData<>();
        mAppExecutors.networkIO().execute(() -> {
            mld.postValue(Resource.loading(null));
            mld.addSource(mCodeChallengeService.searchItem(PARAM_TERM_VALUE,
                    PARAM_COUNTRY_VALUE, PARAM_MEDIA_VALUE), requestResult -> {
                if(requestResult.isSuccessful()) {
                    mld.postValue(Resource.success(requestResult.body));
                } else {
                    mld.postValue(Resource.serverError(requestResult.errorMessage, null));
                }
            });
        });
        return mld;
    }

    /**
     *
     * Save last visit of user to app
     *
     * @param date Date of last visit
     *
     * */
    public void setLastVisit(Date date) {
        mSharedPrefs.edit()
                .putLong(SPREF_LAST_VISIT_KEY, date.getTime())
                .apply();
    }

    /**
     *
     * Get last visit of user to app
     * @return date today if the user first opens the app
     * @return date of last visit
     *
     *
     * */
    public Date getLastVisit() {
        if(mSharedPrefs.contains(SPREF_LAST_VISIT_KEY)) {
            return new Date(mSharedPrefs.getLong(SPREF_LAST_VISIT_KEY, 0));
        } else {
            return new Date();
        }
    }

    /**
     *
     * Save Track to wishlist
     * @param result Track to be saved
     *
     * */
    public LiveData<Resource> saveResult(Result result) throws SQLiteConstraintException {
        MediatorLiveData<Resource> mld = new MediatorLiveData<>();
        mld.postValue(Resource.loading(null));
        mAppExecutors.diskIO().execute(() -> {
            mAppDatabase.resultDao().save(result);
            mld.postValue(Resource.success(null));
        });
        return mld;
    }

    /**
     *
     * Get Track from wishlist
     * @param trackId Id of track
     *
     * */
    public LiveData<Result> getResult(int trackId) {
        MediatorLiveData mld = new MediatorLiveData();
        mAppExecutors.diskIO().execute(() -> {
            mld.postValue(mAppDatabase.resultDao().select(trackId));
        });
        return mld;
    }

    /**
     *
     * Get Wishlist from local database
     * @return Wishlist
     *
     * */
    public LiveData<List<Result>> getAllResult() {
        return mAppDatabase.resultDao().getAllResult();
    }

    /**
     *
     * Remove a Track from wishlist
     * @param result Track to be removed
     *
     * */
    public LiveData<Resource> removeResult(Result result) {
        MediatorLiveData<Resource> mld = new MediatorLiveData<>();
        mld.postValue(Resource.loading(null));
        mAppExecutors.diskIO().execute(() -> {
            mAppDatabase.resultDao().remove(result);
            mld.postValue(Resource.success(null));
        });
        return mld;
    }


}
