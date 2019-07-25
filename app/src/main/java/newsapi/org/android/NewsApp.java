package newsapi.org.android;

import android.app.Application;

import androidx.room.Room;

import newsapi.org.android.database.NewsDatabase;

public class NewsApp extends Application {
    static NewsApp app;
    public static NewsApp getInstance(){
        if(app==null){
            app=new NewsApp();
        }
        return app;
    }

    NewsDatabase newsDatabase ;

    public NewsDatabase getNewsDatabase(){
            if(newsDatabase==null){
                newsDatabase = Room.databaseBuilder(getApplicationContext(),
                        NewsDatabase.class, "news-database").build();
            }
            return newsDatabase;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
