package io.github.ama_csail.amaexampleapp.news;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import io.github.ama_csail.amaexampleapp.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Hooks for the News API from newsapi.org
 * @author Aaron Vontell
 */
public class NewsApi {

    // TODO: Invalidate later
    private static final String API_KEY = "d3f0c5c2b1124f8793c61c57639a5a35";

    private static final String HOST = "https://newsapi.org/v1/articles?source=";
    private static final String EXAMPLE = HOST + "ars-technica&sortBy=latest&apiKey=" + API_KEY;

    public static List<Article> getArsNews() {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(EXAMPLE)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            JSONObject jobj = new JSONObject(result);

            LinkedList<Article> headlines = new LinkedList<>();

            JSONArray articles = jobj.getJSONArray("articles");

            for(int i = 0; i < articles.length(); i++) {

                JSONObject article = articles.getJSONObject(i);
                String source = "Ars Technica";
                String author = article.getString("author");
                String title = article.getString("title");
                String description = article.getString("description");
                String url = article.getString("url");
                String urlToImage = article.getString("urlToImage");
                String publishedAt = article.getString("publishedAt");

                Article headline = new Article(source, title, author, description, url, urlToImage, publishedAt);
                headlines.add(headline);

            }

            return headlines;

        } catch (Exception e) {
            Log.e("NEWS ERROR", e.toString());
            return new LinkedList<>();
        }

    }

}
