package newsapi.org.android.api;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import newsapi.org.android.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRetrofit {
    private static final String ENDPOINT = BuildConfig.API_SERVICE_URI_BASE + "/v2/";
    private static Retrofit retrofit;
    private static OkHttpClient client = new OkHttpClient();
    private static ApiService service;
    public static Retrofit getRetrofit(){
        if(retrofit == null){
            OkHttpClient.Builder builder;
            builder = new OkHttpClient.Builder();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(BuildConfig.DEBUG ?
                    HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
            builder.addInterceptor(interceptor);
            builder.readTimeout(60, TimeUnit.SECONDS);
            builder.connectTimeout(60, TimeUnit.SECONDS);
            client = builder.build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .client(client)
                    .build();

        }
        return retrofit;
    }

    public static ApiService getService(){
        if(service==null){
            retrofit=getRetrofit();
            service = retrofit.create(ApiService.class);
        }else{

        }
        return service;
    }
}



