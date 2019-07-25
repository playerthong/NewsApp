package newsapi.org.android.api;

import java.util.List;

import newsapi.org.android.BuildConfig;
import newsapi.org.android.model.NewsApiResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET("top-headlines")
    Call<NewsApiResponse> getTopHeadLines(
            @Query(BuildConfig.API_KEY) String apiKey ,@Query(BuildConfig.API_COUNTRY) String country,@Query("page") int page
    );
}
