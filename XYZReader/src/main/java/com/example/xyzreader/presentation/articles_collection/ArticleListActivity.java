package com.example.xyzreader.presentation.articles_collection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ItemsContract;
import com.example.xyzreader.data.UpdaterService;
import com.example.xyzreader.databinding.ActivityArticleListBinding;
import com.example.xyzreader.presentation.article_details.ArticleDetailActivity;
import com.example.xyzreader.utils.DynamicHeightNetworkImageView;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * An activity representing a list of Articles. This activity has different presentations for
 * handset and tablet-size devices. On handsets, the activity presents a list of items, which when
 * touched, lead to a {@link ArticleDetailActivity} representing item details. On tablets, the
 * activity presents a grid of items as cards.
 */
public class ArticleListActivity extends AppCompatActivity implements AdapterArticles.IArticleInteraction,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = ArticleListActivity.class.toString();

    ActivityArticleListBinding mBinding;
    private AppBarLayout.OnOffsetChangedListener mCollapsingToolbarListener = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

            if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                // Collapsed
                mBinding.collapsingToolbar.setTitle(getString(R.string.app_name));
                mBinding.collapsingToolbar.setBackground(getDrawable(R.drawable.header_expanded_gradient));
                mBinding.toolbar.setBackground(getDrawable(R.drawable.header_expanded_gradient));
                mBinding.collapsingToolbar.setCollapsedTitleGravity(Gravity.CENTER_HORIZONTAL);

            } else {
                //Expanded
                mBinding.collapsingToolbar.setTitle("");

                mBinding.toolbar.setBackground(null);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_article_list);

        mBinding.appBar.addOnOffsetChangedListener(mCollapsingToolbarListener);


        getSupportLoaderManager().initLoader(0, null, this);

        if (savedInstanceState == null) {
            refresh();
        }
    }

    private void refresh() {
        startService(new Intent(this, UpdaterService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mRefreshingReceiver,
                new IntentFilter(UpdaterService.BROADCAST_ACTION_STATE_CHANGE));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mRefreshingReceiver);
    }

    private boolean mIsRefreshing = false;

    private BroadcastReceiver mRefreshingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UpdaterService.BROADCAST_ACTION_STATE_CHANGE.equals(intent.getAction())) {
                mIsRefreshing = intent.getBooleanExtra(UpdaterService.EXTRA_REFRESHING, false);
                updateRefreshingUI();
            }
        }
    };

    private void updateRefreshingUI() {
        mBinding.swipeToRefresh.setRefreshing(mIsRefreshing);
        mBinding.swipeToRefresh.setRefreshing(false);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return ArticleLoader.newAllArticlesInstance(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        AdapterArticles adapter = new AdapterArticles(this, cursor, this);
        adapter.setHasStableIds(true);
        mBinding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mBinding.recyclerView.setAdapter(null);
    }


    @Override
    public void onArticleSelected(long articleId) {
        // TODO: 5/25/18 Why use action view ?
        startActivity(new Intent(Intent.ACTION_VIEW,
                ItemsContract.Items.buildItemUri(articleId)));
    }
}
