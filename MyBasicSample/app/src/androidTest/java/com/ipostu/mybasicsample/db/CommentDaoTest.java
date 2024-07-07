package com.ipostu.mybasicsample.db;


import static com.ipostu.mybasicsample.db.TestData.COMMENTS;
import static com.ipostu.mybasicsample.db.TestData.COMMENT_ENTITY;
import static com.ipostu.mybasicsample.db.TestData.PRODUCTS;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import android.database.sqlite.SQLiteConstraintException;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.ipostu.mybasicsample.LiveDataTestUtil;
import com.ipostu.mybasicsample.db.dao.CommentDao;
import com.ipostu.mybasicsample.db.dao.ProductDao;
import com.ipostu.mybasicsample.db.entity.CommentEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Test the implementation of {@link CommentDao}
 */
@RunWith(AndroidJUnit4.class)
public class CommentDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AppDatabase mDatabase;

    private CommentDao mCommentDao;

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

        mCommentDao = mDatabase.commentDao();
        mProductDao = mDatabase.productDao();
    }

    @After
    public void closeDb() throws Exception {
        mDatabase.close();
    }

    @Test
    public void getCommentsWhenNoCommentInserted() throws InterruptedException {
        List<CommentEntity> comments = LiveDataTestUtil.getValue(mCommentDao.loadComments
                (COMMENT_ENTITY.getProductId()));

        assertTrue(comments.isEmpty());
    }

    @Test
    public void cantInsertCommentWithoutProduct() throws InterruptedException {
        try {
            mCommentDao.insertAll(COMMENTS);
            fail("SQLiteConstraintException expected");
        } catch (SQLiteConstraintException ignored) {

        }
    }

    @Test
    public void getCommentsAfterInserted() throws InterruptedException {
        mProductDao.insertAll(PRODUCTS);
        mCommentDao.insertAll(COMMENTS);

        List<CommentEntity> comments = LiveDataTestUtil.getValue(mCommentDao.loadComments
                (COMMENT_ENTITY.getProductId()));

        assertThat(comments.size(), is(1));
    }

    @Test
    public void getCommentByProductId() throws InterruptedException {
        mProductDao.insertAll(PRODUCTS);
        mCommentDao.insertAll(COMMENTS);

        List<CommentEntity> comments = LiveDataTestUtil.getValue(mCommentDao.loadComments(
                (COMMENT_ENTITY.getProductId())));

        assertThat(comments.size(), is(1));
    }

}
