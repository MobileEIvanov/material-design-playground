package com.example.xyzreader.data;

import android.database.Cursor;

/**
 * Created by emil.ivanov on 6/2/18.
 */
public class WrapperArticle {

    public static ArticleModel parseCursorToArticle(Cursor cursor) {
        ArticleModel articleModel = new ArticleModel();
        articleModel.setId(cursor.getLong(ArticleLoader.Query._ID));
        articleModel.setTitle(cursor.getString(ArticleLoader.Query.TITLE));
        articleModel.setAuthor(cursor.getString(ArticleLoader.Query.AUTHOR));
        articleModel.setBody(cursor.getString(ArticleLoader.Query.BODY));
        articleModel.setThumbUrl(cursor.getString(ArticleLoader.Query.PHOTO_URL));
        articleModel.setPublishedDate(cursor.getString(ArticleLoader.Query.PUBLISHED_DATE));

        return articleModel;
    }
}
