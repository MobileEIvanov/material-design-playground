package com.example.xyzreader.presentation.article_details;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.transition.TransitionInflater;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ArticleModel;
import com.example.xyzreader.databinding.FragmentArticleDetailBinding;

import com.example.xyzreader.presentation.articles_collection.ArticleListActivity;
import com.example.xyzreader.utils.ImageLoader;
import com.example.xyzreader.utils.UtilsDate;
import com.example.xyzreader.utils.UtilsTransitions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import static android.os.Build.*;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link ArticleListActivity} in two-pane mode (on
 * tablets) or a {@link ArticleDetailActivity} on handsets.
 * http://mikescamell.com/shared-element-transitions-part-4-recyclerview/
 */
public class ArticleDetailFragment extends Fragment {
    private static final String TAG = "ArticleDetailFragment";

    private Cursor mCursor;
    private ArticleModel mArticle;
    private int mMutedColor = 0xFF333333;

    private FragmentArticleDetailBinding mBinding;

    private AppBarLayout.OnOffsetChangedListener mCollapsingToolbarListener = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

            if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                // Collapsed
                mBinding.collapsingToolbar.setTitle(mArticle.getTitle());

            } else {
                //Expanded
                mBinding.collapsingToolbar.setTitle("");

                mBinding.toolbar.setBackground(null);
            }
        }
    };


    View.OnClickListener mShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                    .setType("text/plain")
                    .setText(getString(R.string.text_share_message))
                    .getIntent(), getString(R.string.action_share)));
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
    }

    public static ArticleDetailFragment newInstance(ArticleModel articleModel) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(ArticleModel.DATA, articleModel);
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postponeEnterTransition();
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
//            setEnterTransition(TransitionInflater
//                    .from(getContext())
//                    .inflateTransition(android.R.transition.slide_right));
            setSharedElementEnterTransition(TransitionInflater.from(getContext())
                    .inflateTransition(android.R.transition.move));
        }

        setExitTransition(null);
        if (getArguments().containsKey(ArticleModel.DATA)) {
            mArticle = getArguments().getParcelable(ArticleModel.DATA);
        }
        setHasOptionsMenu(true);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() instanceof AppCompatActivity) {
            initToolbar((AppCompatActivity) getActivity());
        }
    }

    private void initToolbar(AppCompatActivity activity) {

        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });
        activity.setSupportActionBar(mBinding.toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_detail, container, false);
        mBinding.appBar.addOnOffsetChangedListener(mCollapsingToolbarListener);
        mBinding.fabShare.setOnClickListener(mShareListener);
        bindViews();
        return mBinding.getRoot();
    }


    private void bindViews() {
        if (mBinding.getRoot() == null) {
            return;
        }


        // TODO: 5/26/18 Move to method based on videos

        mBinding.headerArticle.articleTitle.setText(mArticle.getTitle());


        String publishDate = mArticle.getPublishedDate();
        if (!TextUtils.isEmpty(publishDate)) {
            Date date = UtilsDate.parsePublishedDate(publishDate);
            SpannableStringBuilder articleSubtitle = new SpannableStringBuilder();
            if (!date.before(UtilsDate.START_OF_EPOCH.getTime())) {
                articleSubtitle.append(DateUtils.getRelativeTimeSpanString(
                        date.getTime(),
                        System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                        DateUtils.FORMAT_ABBREV_ALL).toString());
            } else {
                articleSubtitle.append(UtilsDate.outputFormat.format(date));
            }

            articleSubtitle.append("\n");
            articleSubtitle.append(getString(R.string.label_by));
            articleSubtitle.append("\t");
            articleSubtitle.append(mArticle.getAuthor());
            mBinding.headerArticle.articleSubtitle.setText(articleSubtitle.toString());
        }


        if (!TextUtils.isEmpty(mArticle.getBody())) {
            mBinding.articleBody.setText(mArticle.getBody().substring(0, 1000));
        }

        ImageLoader.loadImage(getActivity(), mArticle.getThumbUrl(), mBinding.headerArticle.thumbnail);
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
