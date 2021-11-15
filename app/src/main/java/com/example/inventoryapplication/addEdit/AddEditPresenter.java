package com.example.inventoryapplication.addEdit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.inventoryapplication.repository.ArticleRepository;

public class AddEditPresenter extends AndroidViewModel implements AddEditContract.Presenter {

    private ArticleRepository repository;

    public AddEditPresenter(@NonNull Application application) {
        super(application);

        repository = new ArticleRepository(application);
    }
}
