package com.ipostu.mybasicsample.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;

import com.ipostu.mybasicsample.BasicApp;
import com.ipostu.mybasicsample.db.entity.ProductEntity;
import com.ipostu.mybasicsample.repository.DataRepository;

import java.util.List;

public class ProductListViewModel extends AndroidViewModel {
    private static final String QUERY_KEY = "QUERY";

    private final SavedStateHandle mSavedStateHandler;
    private final DataRepository mRepository;
    private final LiveData<List<ProductEntity>> mProducts;

    public ProductListViewModel(@NonNull Application application,
                                @NonNull SavedStateHandle savedStateHandle) {
        super(application);
        mSavedStateHandler = savedStateHandle;

        mRepository = ((BasicApp) application).getRepository();

        // Use the savedStateHandle.getLiveData() as the input to switchMap,
        // allowing us to recalculate what LiveData to get from the DataRepository
        // based on what query the user has entered
        LiveData<CharSequence> initialLiveData = savedStateHandle
                .getLiveData("QUERY", null);
        mProducts = Transformations.switchMap(
                initialLiveData,
                query -> {
                    if (TextUtils.isEmpty(query)) {
                        return mRepository.getProducts();
                    }
                    return mRepository.searchProducts("*" + query + "*");
                });
    }

    public void setQuery(CharSequence query) {
        // Save the user's query into the SavedStateHandle.
        // This ensures that we retain the value across process death
        // and is used as the input into the Transformations.switchMap above
        mSavedStateHandler.set(QUERY_KEY, query);
    }

    /**
     * Expose the LiveData Products query so the UI can observe it.
     */
    public LiveData<List<ProductEntity>> getProducts() {
        return mProducts;
    }
}
