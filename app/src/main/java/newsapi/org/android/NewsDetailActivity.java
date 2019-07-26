package newsapi.org.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import newsapi.org.android.databinding.ActivityNewsDetailBinding;
import newsapi.org.android.model.NewsModel;

public class NewsDetailActivity extends BaseActivity {
    ActivityNewsDetailBinding binding;
    NewsModel newsModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_news_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        newsModel= (NewsModel) getIntent().getSerializableExtra("news");
        if(!TextUtils.isEmpty(newsModel.getUrl())){
            binding.webView.loadUrl(newsModel.getUrl());
            getSupportActionBar().setTitle(newsModel.getTitle());
        }
        binding.webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
               binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
