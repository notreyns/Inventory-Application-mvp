package com.example.inventoryapplication.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.inventoryapplication.data.db.ArticleDao;
import com.example.inventoryapplication.data.db.ArticleDatabase;
import com.example.inventoryapplication.data.model.Article;

import java.util.List;

public class ArticleRepository {
    private ArticleDao articleDao;
    private LiveData<List<Article>> allArticles;

    public ArticleRepository(Application application) {
        ArticleDatabase articleDatabase = ArticleDatabase.getInstance(application);

        articleDao = articleDatabase.articleDao();
        allArticles = articleDao.getAllArticles();
    }

    public void insert(Article article) {
        new InsertArticleAsyncTask(articleDao).execute(article);
    }

    public void update(Article article) {
        new UpdateArticleAsyncTask(articleDao).execute(article);
    }

    public void delete(Article article) {
        new DeleteArticleAsyncTask(articleDao).execute(article);
    }

    public void deleteAllArticles() {
        new DeleteAllArticleAsyncTask(articleDao).execute();
    }

    public LiveData<List<Article>> getAllArticles() {
        return allArticles;
    }

    private static class InsertArticleAsyncTask extends AsyncTask<Article, Void, Void> {
        private ArticleDao articleDao;

        private InsertArticleAsyncTask(ArticleDao articleDao) {
            this.articleDao = articleDao;
        }

        @Override
        protected Void doInBackground(Article... articles) {
            articleDao.insert(articles[0]);
            return null;
        }
    }

    private static class UpdateArticleAsyncTask extends AsyncTask<Article, Void, Void> {
        private ArticleDao articleDao;

        private UpdateArticleAsyncTask(ArticleDao articleDao) {
            this.articleDao = articleDao;
        }

        @Override
        protected Void doInBackground(Article... articles) {
            articleDao.update(articles[0]);
            return null;
        }
    }

    private static class DeleteArticleAsyncTask extends AsyncTask<Article, Void, Void> {
        private ArticleDao articleDao;

        private DeleteArticleAsyncTask(ArticleDao articleDao) {
            this.articleDao = articleDao;
        }

        @Override
        protected Void doInBackground(Article... articles) {
            articleDao.delete(articles[0]);
            return null;
        }
    }

    private static class DeleteAllArticleAsyncTask extends AsyncTask<Void, Void, Void> {
        private ArticleDao articleDao;

        private DeleteAllArticleAsyncTask(ArticleDao articleDao) {
            this.articleDao = articleDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            articleDao.deleteAllArticles();
            return null;
        }
    }
}
