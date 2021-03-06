package com.example.xyzreader.presentation.articles_collection;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ItemsContract;
import com.example.xyzreader.databinding.ListItemArticleBinding;
import com.example.xyzreader.utils.DynamicHeightNetworkImageView;
import com.example.xyzreader.utils.ImageLoader;
import com.example.xyzreader.utils.ImageLoaderHelper;
import com.example.xyzreader.utils.UtilsDate;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;
import java.util.zip.Inflater;

/**
 * Created by emil.ivanov on 5/24/18.
 */
class AdapterArticles extends RecyclerView.Adapter<AdapterArticles.ViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    private static IArticleInteraction mListener;

    AdapterArticles(Context context, Cursor cursor, IArticleInteraction listener) {
        mContext = context;
        mCursor = cursor;
        mListener = listener;
    }

    @Override
    public long getItemId(int position) {
        mCursor.moveToPosition(position);
        return mCursor.getLong(ArticleLoader.Query._ID);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_article, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.itemView.setTag(getItemId(position));
        holder.bindData(mCursor);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        private final ListItemArticleBinding mBinding;

        View.OnClickListener mListenerItemInteraction = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 5/24/18 Remove this logic from the adapter. Create interface and implement in activity
                long articleId = (Long) v.getTag();
                mListener.onArticleSelected(articleId);

            }
        };

        ViewHolder(View view) {
            super(view);
            mBinding = DataBindingUtil.bind(view);
            itemView.setOnClickListener(mListenerItemInteraction);
        }


        void bindData(Cursor cursor) {

            String title = cursor.getString(ArticleLoader.Query.TITLE);
            if (!TextUtils.isEmpty(title)) {
                mBinding.articleTitle.setText(title);
            }

            String publishDate = cursor.getString(ArticleLoader.Query.PUBLISHED_DATE);
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
                articleSubtitle.append("by");
                articleSubtitle.append("\t");
                articleSubtitle.append(cursor.getString(ArticleLoader.Query.AUTHOR));
                mBinding.articleSubtitle.setText(articleSubtitle.toString());
            }
            String thumbUrl = cursor.getString(ArticleLoader.Query.THUMB_URL);

            if (!TextUtils.isEmpty(thumbUrl)) {
                ImageLoader.loadImage(mContext, thumbUrl, mBinding.thumbnail);
            }

        }
    }

    public interface IArticleInteraction {
        void onArticleSelected(long articleId);
    }
}
