package newsapi.org.android;


import android.os.Bundle;
import android.view.Menu;

import androidx.databinding.DataBindingUtil;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableObserver;
import newsapi.org.android.api.ApiRetrofit;
import newsapi.org.android.api.ApiService;
import newsapi.org.android.databinding.ActivityMainBinding;
import newsapi.org.android.model.NewsApiResponse;
import newsapi.org.android.model.NewsModel;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void loadNews(){
        processTask(new Observable<NewsApiResponse>() {
            @Override
            protected void subscribeActual(Observer observer) {
                //implement background thread
                try {
                    Response<NewsApiResponse> response= ApiRetrofit.getService().getTopHeadLines(BuildConfig.API_KEY_VALUE,BuildConfig.API_COUNTRY_VALUE,0).execute();
                    if(response.isSuccessful()){
                        NewsApiResponse newsApiResponse=response.body();
                        observer.onNext(newsApiResponse);
                    }else{
                        observer.onError(new Exception(response.errorBody().string()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    observer.onError(e);
                }
            }
        }, new DisposableObserver<NewsApiResponse>() {
            @Override
            public void onNext(NewsApiResponse newsApiResponse) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                hideProgress();
            }
        });
    }

    public void showProgress(){
        binding.swRefresh.setRefreshing(true);
    }

    public void hideProgress(){
        binding.swRefresh.setRefreshing(false);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
