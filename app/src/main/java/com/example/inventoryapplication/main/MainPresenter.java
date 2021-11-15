package com.example.inventoryapplication.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.inventoryapplication.data.model.Article;
import com.example.inventoryapplication.repository.ArticleRepository;

import java.util.List;

public class MainPresenter extends AndroidViewModel implements MainContract.Presenter {

    private ArticleRepository repository;
    private LiveData<List<Article>> allArticles;

    public MainPresenter(@NonNull Application application) {
        super(application);

        repository = new ArticleRepository(application);
        allArticles = repository.getAllArticles();
    }

    @Override
    public void insert(Article article) {
        repository.insert(article);
    }

    @Override
    public void update(Article article) {
        repository.update(article);
    }

    @Override
    public void delete(Article article) {
        repository.delete(article);
    }

    @Override
    public void deleteAllArticles() {
        repository.deleteAllArticles();
    }

    public LiveData<List<Article>> getAllArticles() {
        return allArticles;
    }
}
