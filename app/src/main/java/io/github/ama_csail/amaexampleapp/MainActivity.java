package io.github.ama_csail.amaexampleapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import io.github.ama_csail.amaexampleapp.news.Article;
import io.github.ama_csail.amaexampleapp.news.NewsApi;

public class MainActivity extends AppCompatActivity {

    private LinearLayout newsLayout;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Loads the layout for news item
        newsLayout = findViewById(R.id.news_layout);
        new NewsApiTask().execute(this);

        // Save an instance of the context for news loading capabilities
        activity = this;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Starts the process of downloading data from the News API
     */
    @SuppressLint("StaticFieldLeak")
    private class NewsApiTask extends AsyncTask<Context, Void, Void> {

        // Headlines is a list of headline articles from Ars Technica
        List<Article> headlines;
        Context context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Context... params) {

            // Grab information from the news API
            context = params[0];
            headlines = NewsApi.getArsNews();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // First, clear the news view of any existing articles
            newsLayout.removeAllViews();

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            for (Article a : headlines) {

                // For each article, create a card to display information

                // This is the overall container for the card
                View cardContainer = inflater.inflate(R.layout.news_headline, null);

                // Add text content for the article title and author
                String byLine = "Written by " + a.getAuthor();
                ((TextView) cardContainer.findViewById(R.id.headline_title)).setText(a.getTitle());
                ((TextView) cardContainer.findViewById(R.id.headline_subline)).setText(byLine);

                // Load an image into an ImageView within this card container
                Picasso.with(context)
                        .load(a.getUrlToImage())
                        .into((ImageView) cardContainer.findViewById(R.id.headline_image));

                // For each article cards, set a click listener to open the web page
                // NOTE: You shouldn't have to touch this code for the purpose of this project
                final Article finalArticle = a;
                cardContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Uri uri = Uri.parse(finalArticle.getUrl());
                        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
                        intentBuilder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
                        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                        CustomTabsIntent customTabsIntent = intentBuilder.build();
                        customTabsIntent.launchUrl(activity, uri);

                    }
                });

                // Finally, add this new article information to the news layout
                newsLayout.addView(cardContainer);
            }

        }

    }

}
