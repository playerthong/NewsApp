package newsapi.org.android;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import com.google.gson.Gson;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import newsapi.org.android.database.News;
import newsapi.org.android.database.NewsDatabase;
import newsapi.org.android.model.NewsModel;

public class NewsApp extends Application {
    static NewsApp app;
    public static NewsApp getInstance(){
        return app;
    }

    NewsDatabase newsDatabase ;

    public NewsDatabase getNewsDatabase(){
            if(newsDatabase==null){
                newsDatabase = Room.databaseBuilder(this,
                        NewsDatabase.class, "news-database").build();
            }
            return newsDatabase;
    }

    public List<NewsModel> newsModelList;
    public int page=0;
    public int totalResults=0;
    public HashMap<String,NewsModel> hashMapFavoriteNews;

    public void clearCache(){
        if(NewsApp.getInstance().newsModelList!=null) NewsApp.getInstance().newsModelList.clear();
        NewsApp.getInstance().newsModelList=null;
        NewsApp.getInstance().page=0;
        NewsApp.getInstance().totalResults=0;
    }

    public void loadFavoriteNews(DisposableSingleObserver<Boolean> callBack){
        if(callBack==null){
            Single.fromCallable(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    List<News> favoriteNews=getNewsDatabase().newsDao().getAll();
                    hashMapFavoriteNews=new HashMap<>();
                    Gson gson=new Gson();
                    for(News news:favoriteNews){
                        NewsModel newsModel=gson.fromJson(news.getData(),NewsModel.class);
                        hashMapFavoriteNews.put(newsModel.getTitle(),newsModel);
                    }
                    return true;
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableSingleObserver<Boolean>() {
                @Override
                public void onSuccess(Boolean newsModel) {

                }

                @Override
                public void onError(Throwable e) {

                }
            });
        }else{
            Single.fromCallable(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    List<News> favoriteNews=getNewsDatabase().newsDao().getAll();
                    hashMapFavoriteNews=new HashMap<>();
                    Gson gson=new Gson();
                    for(News news:favoriteNews){
                        NewsModel newsModel=gson.fromJson(news.getData(),NewsModel.class);
                        hashMapFavoriteNews.put(newsModel.getTitle(),newsModel);
                    }
                    return true;
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(callBack);
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        app=this;
        loadFavoriteNews(null);
    }
}
