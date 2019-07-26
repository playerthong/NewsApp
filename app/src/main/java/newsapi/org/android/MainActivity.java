package newsapi.org.android;


import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import newsapi.org.android.adapter.NewsAdapter;
import newsapi.org.android.api.ApiRetrofit;
import newsapi.org.android.api.ApiService;
import newsapi.org.android.databinding.ActivityMainBinding;
import newsapi.org.android.model.NewsApiResponse;
import newsapi.org.android.model.NewsModel;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;
    NewsAdapter newsAdapter;

    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.rcNews.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                binding.rcNews.getContext(),
                DividerItemDecoration.HORIZONTAL
        );
        binding.rcNews.addItemDecoration(dividerItemDecoration);
        initScrollListener();
        binding.swRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Clear cache
                NewsApp.getInstance().clearCache();
                loadNews(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void loadNews(boolean isLoadingMore){
        //Check cache data in app
        if(NewsApp.getInstance().newsModelList!=null && !isLoadingMore){
            List<NewsModel> listNews=NewsApp.getInstance().newsModelList;
            //Apply Favorite
            applyFavoriteIntoList(listNews);
            if(newsAdapter==null){
                newsAdapter=new NewsAdapter();
                newsAdapter.setNewsModelList(NewsApp.getInstance().newsModelList,NewsApp.getInstance().totalResults);
                binding.rcNews.setAdapter(newsAdapter);
            }else{
                newsAdapter.setNewsModelList(NewsApp.getInstance().newsModelList,NewsApp.getInstance().totalResults);
            }
        }else{
            if(!isLoadingMore) showProgress();
            NewsApp.getInstance().page++;
            processTask(new Observable<NewsApiResponse>() {
                @Override
                protected void subscribeActual(Observer observer) {
                    //implement background thread
                    try {
                        Response<NewsApiResponse> response= ApiRetrofit.getService().getTopHeadLines(BuildConfig.API_KEY_VALUE,BuildConfig.API_COUNTRY_VALUE,NewsApp.getInstance().page).execute();
                        if(response.isSuccessful()){
                            NewsApiResponse newsApiResponse=response.body();
                            List<NewsModel> listNews=newsApiResponse.getArticles();
                            //Apply Favorite
                            applyFavoriteIntoList(listNews);
                            observer.onNext(newsApiResponse);
                        }else{
                            observer.onError(new Exception(response.errorBody().string()));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        observer.onError(e);
                    }
                    observer.onComplete();
                }
            }, new DisposableObserver<NewsApiResponse>() {
                @Override
                public void onNext(NewsApiResponse newsApiResponse) {
                    NewsApp.getInstance().totalResults= newsApiResponse.getTotalResults();
                    List<NewsModel> listNews=newsApiResponse.getArticles();

                    if(newsAdapter==null){
                        newsAdapter=new NewsAdapter();
                        newsAdapter.setNewsModelList(listNews,newsApiResponse.getTotalResults());
                        binding.rcNews.setAdapter(newsAdapter);
                    }else{
                        if(listNews!=null && listNews.size()>0){
                            if(NewsApp.getInstance().page==1){
                                newsAdapter.setNewsModelList(listNews,NewsApp.getInstance().totalResults);
                            }else{
                                newsAdapter.addMoreNewsModelList(listNews);
                            }
                            newsAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onComplete() {
                    hideProgress();
                }
            });
        }

    }

    private void initScrollListener() {
        binding.rcNews.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == newsAdapter.getNewsModelList().size() - 1) {
                        isLoading = true;
                        if(newsAdapter.beCanLoadingMore()){
                            loadNews(true);
                        }else{

                        }
                    }
                }
            }
        });


    }

    public void applyFavoriteIntoList(final List<NewsModel> listNews){
        if(NewsApp.getInstance().hashMapFavoriteNews==null){
            NewsApp.getInstance().loadFavoriteNews(new DisposableSingleObserver<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    for(NewsModel newsModel:listNews){
                        if(NewsApp.getInstance().hashMapFavoriteNews.containsKey(newsModel.getTitle())){
                            newsModel.setFavorite(true);
                        }else{
                            newsModel.setFavorite(false);
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {

                }
            });
        }else{
            for(NewsModel newsModel:listNews){
                if(NewsApp.getInstance().hashMapFavoriteNews.containsKey(newsModel.getTitle())){
                    newsModel.setFavorite(true);
                }else{
                    newsModel.setFavorite(false);
                }
            }
        }

    }

    public void showProgress(){
        if(!binding.swRefresh.isRefreshing()) binding.swRefresh.setRefreshing(true);
    }

    public void hideProgress(){
        if(binding.swRefresh.isRefreshing()) binding.swRefresh.setRefreshing(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadNews(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
