package com.ipostu.mybasicsample.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ipostu.mybasicsample.db.entity.ProductEntity;

import java.util.List;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM products")
    LiveData<List<ProductEntity>> loadAllProducts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ProductEntity> products);

    @Query("SELECT * FROM products WHERE id = :productId")
    LiveData<ProductEntity> loadProduct(int productId);


    @Query("SELECT * FROM products WHERE id = :productId")
    ProductEntity loadProductSync(int productId);

    @Query("SELECT p.* FROM products AS p INNER JOIN productsFts ON (p.id=productsFts.rowid) " +
            "WHERE productsFts MATCH :query")
    LiveData<List<ProductEntity>> searchAllProducts(String query);
}
