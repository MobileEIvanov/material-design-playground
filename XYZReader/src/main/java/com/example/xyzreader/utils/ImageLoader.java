package com.example.xyzreader.utils;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.example.xyzreader.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by emil.ivanov on 6/2/18.
 */
public class ImageLoader {


    public static void loadImage(Context context, String url, ImageView container) {
        Uri uri = Uri.parse(url);
        Picasso.with(context)
                .load(uri)
                .networkPolicy(NetworkPolicy.NO_CACHE)
//                .placeholder(ContextCompat.getColor(context, R.color.primary))
//                .error(ContextCompat.getDrawable(context, R.drawable.logo))
                .noFade()
                .into(container);
    }
}
