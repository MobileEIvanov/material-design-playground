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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.Loader;
import android.support.v7.graphics.Palette;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleModel;
import com.example.xyzreader.databinding.FragmentArticleDetailBinding;

import com.example.xyzreader.presentation.articles_collection.ArticleListActivity;
import com.example.xyzreader.utils.ImageLoader;

import org.w3c.dom.Text;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link ArticleListActivity} in two-pane mode (on
 * tablets) or a {@link ArticleDetailActivity} on handsets.
 */
public class ArticleDetailFragment extends Fragment {
    private static final String TAG = "ArticleDetailFragment";

    public static final String ARG_ITEM_ID = "item_id";
    private static final float PARALLAX_FACTOR = 1.25f;

    private Cursor mCursor;
    private ArticleModel mArticle;
    private int mMutedColor = 0xFF333333;
    private ColorDrawable mStatusBarColorDrawable;

    private boolean mIsCard = false;
    private int mStatusBarFullOpacityBottom;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");
    // Use default locale format
    private SimpleDateFormat outputFormat = new SimpleDateFormat();
    // Most time functions can only handle 1902 - 2037
    private GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2, 1, 1);
    private FragmentArticleDetailBinding mBinding;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ArticleModel.DATA)) {
            mArticle = getArguments().getParcelable(ArticleModel.DATA);
        }

        mIsCard = getResources().getBoolean(R.bool.detail_is_card);
        mStatusBarFullOpacityBottom = getResources().getDimensionPixelSize(
                R.dimen.detail_card_top_margin);
        setHasOptionsMenu(true);
    }

    public ArticleDetailActivity getActivityCast() {
        return (ArticleDetailActivity) getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // In support library r8, calling initLoader for a fragment in a FragmentPagerAdapter in
        // the fragment's onCreate may cause the same LoaderManager to be dealt to multiple
        // fragments because their mIndex is -1 (haven't been added to the activity yet). Thus,
        // we do this in onActivityCreated.
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_article_detail, container, false);


        mStatusBarColorDrawable = new ColorDrawable(0);

//        mRootView.findViewById(R.id.share_fab).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
//                        .setType("text/plain")
//                        .setText("Some sample text")
//                        .getIntent(), getString(R.string.action_share)));
//            }
//        });

        bindViews();
//        updateStatusBar();
        return mBinding.getRoot();
    }

//    private void updateStatusBar() {
//        int color = 0;
//        if (mPhotoView != null && mTopInset != 0 && mScrollY > 0) {
//            float f = progress(mScrollY,
//                    mStatusBarFullOpacityBottom - mTopInset * 3,
//                    mStatusBarFullOpacityBottom - mTopInset);
//            color = Color.argb((int) (255 * f),
//                    (int) (Color.red(mMutedColor) * 0.9),
//                    (int) (Color.green(mMutedColor) * 0.9),
//                    (int) (Color.blue(mMutedColor) * 0.9));
//        }
//        mStatusBarColorDrawable.setColor(color);
//        mDrawInsetsFrameLayout.setInsetBackground(mStatusBarColorDrawable);
//    }

    private Date parsePublishedDate() {
        try {
            return dateFormat.parse(mArticle.getPublishedDate());
        } catch (ParseException ex) {
            Log.e(TAG, ex.getMessage());
            Log.i(TAG, "passing today's date");
            return new Date();
        }
    }


    private void bindViews() {
        if (mBinding.getRoot() == null) {
            return;
        }


        // TODO: 5/26/18 Move to method based on videos
//        mBinding.articleBody.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "Rosario-Regular.ttf"));

//            mRootView.setAlpha(0);
//            mRootView.setVisibility(View.VISIBLE);
//            mRootView.animate().alpha(1);
        mBinding.headerArticle.articleTitle.setText(mArticle.getTitle());
        Date publishedDate = parsePublishedDate();
        if (!publishedDate.before(START_OF_EPOCH.getTime())) {
            mBinding.headerArticle.articleSubtitle.setText(Html.fromHtml(
                    DateUtils.getRelativeTimeSpanString(
                            publishedDate.getTime(),
                            System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                            DateUtils.FORMAT_ABBREV_ALL).toString()
                            + " by <font color='#ffffff'>"
                            + mArticle.getAuthor()
                            + "</font>"));

        } else {
            // If date is before 1902, just show the string
            mBinding.headerArticle.articleSubtitle.setText(Html.fromHtml(
                    outputFormat.format(publishedDate) + " by <font color='#ffffff'>"
                            + mArticle.getAuthor() + "</font>"));

        }

        if (!TextUtils.isEmpty(mArticle.getBody())) {
            mBinding.articleBody.setText(mArticle.getBody());
//                    .setText(Html.fromHtml(mArticle.getBody().replaceAll("(\r\n|\n)", "<br />")));
        }
        ImageLoader.loadImage(getActivity(), mArticle.getThumbUrl(), mBinding.headerArticle.thumbnail);
        // TODO: 6/2/18 Create a color palette based on the image
        //            Palette p = Palette.generate(bitmap, 12);
//            mMutedColor = p.getDarkMutedColor(0xFF333333);

    }


}
