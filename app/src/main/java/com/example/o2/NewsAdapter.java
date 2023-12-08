package com.example.o2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<News> newsList;
    private NewsClickListener newsClickListener;

    public NewsAdapter(List<News> newsList, NewsClickListener newsClickListener) {
        this.newsList = newsList;
        this.newsClickListener = newsClickListener;
    }
    @NonNull
    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsViewHolder holder, int position) {

        News news = newsList.get(position);

        if (news!=null)
        {holder.titleTextView.setText(news.getTitle());
            holder.sourceTextView.setText("Source: "+news.getSource());
        }


        holder.itemView.setOnClickListener(view -> {
            if (newsClickListener != null) {
                newsClickListener.onNewsClick(news.getUrl());
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
        notifyDataSetChanged();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView sourceTextView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            sourceTextView = itemView.findViewById(R.id.sourceTextView);
        }
    }

    public interface NewsClickListener {
        void onNewsClick(String newsUrl);
    }
}
