package com.example.inventoryapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inventoryapplication.R;
import com.example.inventoryapplication.data.model.Article;

import java.util.ArrayList;
import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleHolder> {
    private List<Article> articles = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ArticleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemVew = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_item, parent, false);
        return new ArticleHolder(itemVew);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleHolder holder, int position) {
        Article currentArticle = articles.get(position);
        holder.name.setText(currentArticle.getName());
        holder.price.setText(String.valueOf(currentArticle.getPrice()));
        holder.quantity.setText(String.valueOf(currentArticle.getQuantity()));
        holder.supplier.setText(String.valueOf(currentArticle.getSupplier()));
        String imageUri = currentArticle.getImage();
        Glide
                .with(holder.itemView.getContext())
                .load(imageUri)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
        notifyDataSetChanged();
    }

    public Article getArticleAt(int position) {
        return articles.get(position);
    }

    class ArticleHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView price;
        private TextView quantity;
        private TextView supplier;
        private ImageView image;

        public ArticleHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
            supplier = itemView.findViewById(R.id.supplier);
            image = itemView.findViewById(R.id.image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(articles.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Article article);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
