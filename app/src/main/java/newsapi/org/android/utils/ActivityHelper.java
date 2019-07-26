package newsapi.org.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import newsapi.org.android.NewsDetailActivity;
import newsapi.org.android.model.NewsModel;

public class ActivityHelper {
    public static void startNewsDetailActivity(Context context, NewsModel newsModel){
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra("news",newsModel);
        context.startActivity(intent);
    }
}
