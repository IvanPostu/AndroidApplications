package com.ipostu.mybasicsample.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.ipostu.mybasicsample.db.AppDatabase;
import com.ipostu.mybasicsample.db.entity.CommentEntity;
import com.ipostu.mybasicsample.db.entity.ProductEntity;

import java.util.List;

public class DataRepository {
    private static DataRepository instance;

    private final AppDatabase database;
    private final MediatorLiveData<List<ProductEntity>> observableProducts;

    public static DataRepository getInstance(AppDatabase database) {
        if (instance == null) {
            synchronized (DataRepository.class) {
                if (instance == null) {
                    instance = new DataRepository(database);
                }
            }
        }
        return instance;
    }

    private DataRepository(AppDatabase database) {
        this.database = database;
        observableProducts = new MediatorLiveData<>();

        observableProducts.addSource(database.productDao().loadAllProducts(),
                productEntities -> {
                    if (database.getDatabaseCreated().getValue() != null) {
                        observableProducts.postValue(productEntities);
                    }
                });
    }

    /**
     * Get the list of products from the database and get notified when the data changes.
     */
    public LiveData<List<ProductEntity>> getProducts() {
        return observableProducts;
    }

    public LiveData<ProductEntity> loadProduct(final int productId) {
        return database.productDao().loadProduct(productId);
    }

    public LiveData<List<CommentEntity>> loadComments(final int productId) {
        return database.commentDao().loadComments(productId);
    }

    public LiveData<List<ProductEntity>> searchProducts(String query) {
        return database.productDao().searchAllProducts(query);
    }
}
