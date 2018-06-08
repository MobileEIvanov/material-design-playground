package com.example.xyzreader.utils;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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
        Glide.with(context)
                .load(uri)
                .thumbnail(0.1f)
//                .placeholder(ContextCompat.getColor(context, R.color.primary))
//                .error(ContextCompat.getDrawable(context, R.drawable.logo))
                .into(container);
    }
}
