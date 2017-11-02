package io.github.ama_csail.amaexampleapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.ama_csail.ama.AccessibleActivity;
import io.github.ama_csail.amaexampleapp.news.Article;
import io.github.ama_csail.amaexampleapp.news.NewsApi;

public class MainActivity extends AccessibleActivity {

    private LinearLayout newsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsLayout = (LinearLayout) findViewById(R.id.news_layout);

        new NewsApiTask().execute(this);

    }

    protected void configureAccessibility() {

        //enableMenu();
        //enableDyslexiaFont(true);
        //provideGlossary(AccessibleDefinitions.getGlossary());
        //AMA.increaseFontSize(getRootView(), 30);
        //AMA.setActionClass(newsLayout, ActionClass.DANGER);
        //Log.e("ACTION", AMA.getActionClass(newsLayout).toString());

    }

    public static class AccessibleDefinitions {

        public static Map<String, String> getGlossary() {
            Map<String, String> terms = new HashMap<>();
            terms.put("accessible", "To be usable in different contexts, no matter the operator");
            terms.put("glossary", "A list of terms and definitions");
            return terms;
        }

    }

    /**
     * Starts the process of downloading data from the New API
     */
    private class NewsApiTask extends AsyncTask<Context, Void, Void> {

        List<Article> headlines;
        Context context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Context... params) {

            context = params[0];
            headlines = NewsApi.getArsNews();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            newsLayout.removeAllViews();

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            for (Article a : headlines) {

                View cardContainer = inflater.inflate(R.layout.news_headline, null);
                String byLine = "Written by " + a.getAuthor();
                ((TextView) cardContainer.findViewById(R.id.headline_title)).setText(a.getTitle());
                ((TextView) cardContainer.findViewById(R.id.headline_subline)).setText(byLine);

                Picasso.with(context)
                        .load(a.getUrlToImage())
                        .into((ImageView) cardContainer.findViewById(R.id.headline_image));

                newsLayout.addView(cardContainer);
            }

            configureAccessibility();

        }

    }

}
