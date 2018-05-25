package com.example.xyzreader.utils;

import android.util.Log;

import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.presentation.articles_collection.ArticleListActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by emil.ivanov on 5/25/18.
 */
public class UtilsDate {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");
    // Use default locale format
    public static final SimpleDateFormat outputFormat = new SimpleDateFormat();
    // Most time functions can only handle 1902 - 2037
    public static final GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2, 1, 1);

    // TODO: 5/24/18 Move to UtilsDate
    public static Date parsePublishedDate(String date) {
        try {
            return dateFormat.parse(date);
        } catch (ParseException ex) {
            return new Date();
        }
    }
}
