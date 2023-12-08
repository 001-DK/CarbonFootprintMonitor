package com.example.o2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class NewsPage extends AppCompatActivity implements NewsAdapter.NewsClickListener {
    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private List<News> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);
        newsRecyclerView = findViewById(R.id.newsRecyclerView);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        newsList = new ArrayList<>();

        newsAdapter = new NewsAdapter(newsList, NewsPage.this);
        newsRecyclerView.setAdapter(newsAdapter);

        newsAdapter.notifyDataSetChanged();

        fetchNewsData();
    }

    private void fetchNewsData() {
        AsyncHttpClient client = new AsyncHttpClient();
        String apiUrl = "https://news-about-climate-change-api.herokuapp.com/news";

        Log.d("NewsPage", "Fetching data from: " + apiUrl); // Log the URL

        client.get(apiUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Process the JSON array and populate the news list
                ArrayList<News> newsList = parseJsonArray(response);

                // Set up the RecyclerView with the adapter
                //newsAdapter = new NewsAdapter(newsList,null);
                //newsRecyclerView.setAdapter(newsAdapter);
                Log.d("NewsPage", "NewsList Size: " + newsList.size());

                newsAdapter.setNewsList(newsList);
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // Handle failure, e.g., show an error message
                Log.e("NewsPage", "HTTP request failed with status code: " + statusCode);
                Log.e("NewsPage", "Error message: " + responseString);
            }
        });
    }

    private ArrayList<News> parseJsonArray(JSONArray jsonArray) {
        ArrayList<News> newsList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject newsObject = jsonArray.getJSONObject(i);

                String title = newsObject.getString("title");
                String url = newsObject.getString("url");
                String source = newsObject.getString("source");

                News news = new News(title, url, source);
                newsList.add(news);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return newsList;
    }

    @Override
    public void onNewsClick(String newsUrl) {

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(newsUrl));
        startActivity(browserIntent);
    }
}