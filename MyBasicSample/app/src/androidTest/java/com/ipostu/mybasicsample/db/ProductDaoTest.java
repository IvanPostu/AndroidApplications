package com.ipostu.mybasicsample.db;


import static com.ipostu.mybasicsample.db.TestData.PRODUCTS;
import static com.ipostu.mybasicsample.db.TestData.PRODUCT_ENTITY;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.ipostu.mybasicsample.LiveDataTestUtil;
import com.ipostu.mybasicsample.db.dao.ProductDao;
import com.ipostu.mybasicsample.db.entity.ProductEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Test the implementation of {@link ProductDao}
 */
@RunWith(AndroidJUnit4.class)
public class ProductDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AppDatabase mDatabase;

    private ProductDao mProductDao;

    @Before
    public void initDb() throws Exception {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        mDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                        AppDatabase.class)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .build();

        mProductDao = mDatabase.productDao();
    }

    @After
    public void closeDb() throws Exception {
        mDatabase.close();
    }

    @Test
    public void getProductsWhenNoProductInserted() throws InterruptedException {
        List<ProductEntity> products = LiveDataTestUtil.getValue(mProductDao.loadAllProducts());

        assertTrue(products.isEmpty());
    }

    @Test
    public void getProductsAfterInserted() throws InterruptedException {
        mProductDao.insertAll(PRODUCTS);

        List<ProductEntity> products = LiveDataTestUtil.getValue(mProductDao.loadAllProducts());

        assertThat(products.size(), is(PRODUCTS.size()));
    }

    @Test
    public void getProductById() throws InterruptedException {
        mProductDao.insertAll(PRODUCTS);

        ProductEntity product = LiveDataTestUtil.getValue(mProductDao.loadProduct
                (PRODUCT_ENTITY.getId()));

        assertThat(product.getId(), is(PRODUCT_ENTITY.getId()));
        assertThat(product.getName(), is(PRODUCT_ENTITY.getName()));
        assertThat(product.getDescription(), is(PRODUCT_ENTITY.getDescription()));
        assertThat(product.getPrice(), is(PRODUCT_ENTITY.getPrice()));
    }

}
