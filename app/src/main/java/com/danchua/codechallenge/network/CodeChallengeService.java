package com.danchua.codechallenge.network;

import android.arch.lifecycle.LiveData;

import com.danchua.codechallenge.network.utils.ApiResponse;
import com.danchua.codechallenge.network.utils.response.ResultList;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *
 * Retrofit API Service
 * Define all API requests here
 *
 * */
public interface CodeChallengeService {

    @GET("/search?")
    LiveData<ApiResponse<ResultList>> searchItem(@Query("term") String term,
                                                 @Query("country") String country,
                                                 @Query("media") String media);

}
