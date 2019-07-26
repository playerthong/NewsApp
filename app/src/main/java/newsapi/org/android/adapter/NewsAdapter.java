package newsapi.org.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import newsapi.org.android.NewsApp;
import newsapi.org.android.R;
import newsapi.org.android.adapter.holder.LoadingViewHolder;
import newsapi.org.android.adapter.holder.NewsViewHolder;
import newsapi.org.android.database.News;
import newsapi.org.android.model.NewsModel;
import newsapi.org.android.utils.ActivityHelper;
import newsapi.org.android.utils.Utils;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    int totalResult;
    public List<NewsModel> newsModelList;

    public List<NewsModel> getNewsModelList() {
        return newsModelList;
    }

    public void setNewsModelList(List<NewsModel> newsModelList,int totalResult) {
        this.newsModelList = newsModelList;
        this.totalResult=totalResult;
        NewsApp.getInstance().newsModelList=newsModelList;
    }

    public void addMoreNewsModelList(List<NewsModel> list){
        if(newsModelList==null){
            newsModelList=new ArrayList<>();
        }
        newsModelList.addAll(list);
        //cache data
        NewsApp.getInstance().newsModelList=newsModelList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_view, parent, false);
            return new NewsViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_item_view, parent, false);
            return new LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof LoadingViewHolder){

        }else{
            final NewsViewHolder newsViewHolder= (NewsViewHolder) holder;
            final NewsModel newsModel=getNewsModelList().get(position);
            Picasso.get()
                    .load(newsModel.getUrlToImage())
                    .placeholder(R.drawable.ic_photo)
                    .error(R.drawable.ic_photo)
                    .into(newsViewHolder.binding.imageView);
            newsViewHolder.binding.tvTitle.setText(newsModel.getTitle());
            newsViewHolder.binding.tvDescription.setText(newsModel.getDescription());
            newsViewHolder.binding.tvDate.setText(Utils.getSimpleDate(newsModel.getPublishedAt()));
            if(newsModel.isFavorite()){
                newsViewHolder.binding.icFavorite.setImageResource(R.drawable.ic_favorite_red);
            }else{
                newsViewHolder.binding.icFavorite.setImageResource(R.drawable.ic_favorite);
            }
            newsViewHolder.binding.layNews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityHelper.startNewsDetailActivity(newsViewHolder.binding.layNews.getContext(),newsModel);
                }
            });
            newsViewHolder.binding.icFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newsModel.setFavorite(!newsModel.isFavorite());
                    if(newsModel.isFavorite()) {
                        NewsApp.getInstance().hashMapFavoriteNews.put(newsModel.getTitle(),newsModel);
                        Single.fromCallable(new Callable<NewsModel>() {
                            @Override
                            public NewsModel call() throws Exception {
                                NewsApp.getInstance().getNewsDatabase().newsDao().insertAll(newsModel.getNews());
                                return newsModel;
                            }
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableSingleObserver<NewsModel>() {
                            @Override
                            public void onSuccess(NewsModel newsModel) {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
                    }else{
                        NewsApp.getInstance().hashMapFavoriteNews.remove(newsModel.getTitle());
                        Single.fromCallable(new Callable<NewsModel>() {
                            @Override
                            public NewsModel call() throws Exception {
                                NewsApp.getInstance().getNewsDatabase().newsDao().delete(newsModel.getNews());
                                return newsModel;
                            }
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableSingleObserver<NewsModel>() {
                            @Override
                            public void onSuccess(NewsModel newsModel) {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
                    }
                    notifyItemChanged(position);
                }
            });
        }
    }



    @Override
    public int getItemViewType(int position) {
        int total=getItemCount();
        if(position==total-1){
            if(total==totalResult){
                return VIEW_TYPE_ITEM;
            }else{
                return VIEW_TYPE_LOADING;
            }
        }else{
            return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        if(newsModelList==null){
            return  0;
        }else if(newsModelList.size()<totalResult){
            return (newsModelList.size()+1);
        }else{
            return newsModelList.size();
        }
    }

    public boolean beCanLoadingMore(){
        return (newsModelList!=null && getItemCount()>newsModelList.size());
    }
}
