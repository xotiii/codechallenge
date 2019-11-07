package com.danchua.codechallenge;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;

import com.danchua.codechallenge.db.AppDatabase;
import com.danchua.codechallenge.network.CodeChallengeService;
import com.danchua.codechallenge.network.Repository;
import com.danchua.codechallenge.network.utils.LiveDataCallAdapterFactory;
import com.danchua.codechallenge.utils.AppExecutors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.danchua.codechallenge.utils.Constants.DATABASE_NAME;
import static com.danchua.codechallenge.utils.Constants.ITUNES_SEARCH_BASE_URL;
import static com.danchua.codechallenge.utils.Constants.SPREF_NAME;

public class CodeChallenge extends Application {

    /**
     * App Executor for multi-threading
     * */
    private AppExecutors mAppExecutors;

    /**
     * App Database for Room Persistence
     * */
    private AppDatabase mAppDatabase;

    /**
     *  Repository for MVVM Architecture
     * */
    private Repository mRepository;
    private Gson mGson;

    /**
     * Initialize Dependecies inside
     * onCreate() method of Application
     *
     */
    @Override
    public void onCreate() {
        super.onCreate();

        mAppExecutors = new AppExecutors();

        mAppDatabase = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class,
                        DATABASE_NAME).build();

        mGson = new GsonBuilder().setPrettyPrinting().create();

        mRepository = new Repository(mAppExecutors, mAppDatabase,
                getSharedPreferences(SPREF_NAME, MODE_PRIVATE),
                retrofit().create(CodeChallengeService.class));


    }

    /**
     * Initialize Retrofit w/o AuthInterceptor
     *
     * @return RetrofitClient
     *
     * */
    private Retrofit retrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        return new Retrofit.Builder()
                .baseUrl(ITUNES_SEARCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(builder.build())
                .build();
    }

    public Repository getRepository() {
        return mRepository;
    }
}
