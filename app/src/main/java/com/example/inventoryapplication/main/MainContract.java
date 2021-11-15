package com.example.inventoryapplication.main;

import com.example.inventoryapplication.data.model.Article;

public interface MainContract {

    interface Presenter {
        void insert(Article article);
        void update(Article article);
        void delete(Article article);
        void deleteAllArticles();
    }
}
