package com.example.xyzreader.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by emil.ivanov on 6/2/18.
 */
public class ArticleModel implements Parcelable {
    public static final String DATA = "article_data";
    private long id;
    private long serverId;
    private String title;
    private String author;
    private String body;
    private String thumbUrl;
    private String publishedDate;

    protected ArticleModel(Parcel in) {
        id = in.readLong();
        serverId = in.readLong();
        title = in.readString();
        author = in.readString();
        body = in.readString();
        thumbUrl = in.readString();
        publishedDate = in.readString();
    }

    public static final Creator<ArticleModel> CREATOR = new Creator<ArticleModel>() {
        @Override
        public ArticleModel createFromParcel(Parcel in) {
            return new ArticleModel(in);
        }

        @Override
        public ArticleModel[] newArray(int size) {
            return new ArticleModel[size];
        }
    };

    public ArticleModel() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(serverId);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(body);
        dest.writeString(thumbUrl);
        dest.writeString(publishedDate);
    }
}
