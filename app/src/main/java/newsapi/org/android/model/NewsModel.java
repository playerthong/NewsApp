package newsapi.org.android.model;


import com.google.gson.Gson;

import java.io.Serializable;

import newsapi.org.android.database.News;

public class NewsModel implements Serializable {

    SourceModel source;

    String author;

    String title;

    String description;

    String url;

    String urlToImage;

    String publishedAt;

    String content;

    boolean isFavorite;

    public SourceModel getSource() {
        return source;
    }

    public void setSource(SourceModel source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        if(urlToImage!=null){
            if(urlToImage.contains("https") || urlToImage.contains("http")){
                return urlToImage;
            }else if(urlToImage.indexOf("//")==0){
                urlToImage= urlToImage.substring(2);
                return urlToImage;
            }
        }
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public News getNews(){
        News news=new News();
        Gson gson=new Gson();
        news.setTitle(title);
        news.setPublishedAt(publishedAt);
        news.setData(gson.toJson(this));
        return news;
    }
}
