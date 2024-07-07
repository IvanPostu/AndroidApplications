package com.ipostu.mybasicsample;

import android.app.Application;

import com.ipostu.mybasicsample.db.AppDatabase;
import com.ipostu.mybasicsample.repository.DataRepository;

/**
 * Android Application class. Used for accessing singletons.
 */
public class BasicApp extends Application {

    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppExecutors = new AppExecutors();
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this, mAppExecutors);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase());
    }
}

