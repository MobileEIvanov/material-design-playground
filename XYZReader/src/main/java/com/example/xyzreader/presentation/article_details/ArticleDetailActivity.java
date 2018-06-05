package com.example.xyzreader.presentation.article_details;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowInsets;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ItemsContract;
import com.example.xyzreader.databinding.ActivityArticleDetailBinding;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
public class ArticleDetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String SELECTED_PAGE = "selected_page";
    private Cursor mCursor;
    private long mStartId;

    private long mSelectedItemId;

    private ViewPagerArticlesAdapter mPagerAdapter;
    private ActivityArticleDetailBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_article_detail);

        getSupportLoaderManager().initLoader(0, null, this);

        if (savedInstanceState == null) {
            if (getIntent() != null && getIntent().getData() != null) {
                mStartId = ItemsContract.Items.getItemId(getIntent().getData());
                mSelectedItemId = mStartId;
            }
        } else {
            mSelectedItemId = savedInstanceState.getLong(SELECTED_PAGE);
        }
    }

    private void initViewPager(Cursor cursor) {
        mPagerAdapter = new ViewPagerArticlesAdapter(getSupportFragmentManager(), cursor);

        mBinding.pager.setAdapter(mPagerAdapter);

        mBinding.pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }

            @Override
            public void onPageSelected(int position) {
                if (mCursor != null) {
                    if (mCursor.moveToPosition(position)) {
                        mSelectedItemId = mCursor.getLong(ArticleLoader.Query._ID);
                    }
                }
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return ArticleLoader.newAllArticlesInstance(this);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {


        if (mPagerAdapter == null) {
            mCursor = cursor;
            initViewPager(mCursor);
        } else {
            mCursor = mPagerAdapter.swapCursor(cursor);
        }


        if (mCursor == null) {
            return;
        }
        // Select the start ID
        if (mStartId > 0 || mSelectedItemId > 0) {
            mCursor.moveToFirst();
            // TODO: optimize
            while (!mCursor.isAfterLast()) {
                if (mCursor.getLong(ArticleLoader.Query._ID) == mStartId) {
                    final int position = mCursor.getPosition();
                    mBinding.pager.setCurrentItem(position, true);
                    break;
                }
                mCursor.moveToNext();
            }
            mStartId = 0;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mCursor = null;
        mPagerAdapter.notifyDataSetChanged();
    }


}
