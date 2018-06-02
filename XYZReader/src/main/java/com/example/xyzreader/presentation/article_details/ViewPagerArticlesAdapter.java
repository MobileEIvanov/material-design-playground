package com.example.xyzreader.presentation.article_details;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.ViewGroup;

import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.WrapperArticle;

/**
 * Created by emil.ivanov on 5/25/18.
 */
class ViewPagerArticlesAdapter extends android.support.v4.app.FragmentStatePagerAdapter {

    private Cursor mCursor;

    public ViewPagerArticlesAdapter(FragmentManager fm, Cursor cursor) {
        super(fm);

        mCursor = cursor;
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        mCursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
        return mCursor;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        ArticleDetailFragment fragment = (ArticleDetailFragment) object;
//        if (fragment != null) {
//            articleDetailActivity.mSelectedItemUpButtonFloor = fragment.getUpButtonFloor();
//            articleDetailActivity.updateUpButtonPosition();
//        }
    }

    @Override
    public Fragment getItem(int position) {
        boolean hasCursor = mCursor.moveToPosition(position);
        Log.d("PAGER: ", "has cursor: " + hasCursor + " " + position);
        return ArticleDetailFragment.newInstance(WrapperArticle.parseCursorToArticle(mCursor));
    }

    @Override
    public int getCount() {
        return (mCursor != null) ? mCursor.getCount() : 0;
    }
}
