package com.example.inventoryapplication.main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryapplication.R;
import com.example.inventoryapplication.addEdit.AddEditActivity;
import com.example.inventoryapplication.data.model.Article;
import com.example.inventoryapplication.adapter.ArticleAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_ARTICLE_REQUEST = 1;
    public static final int EDIT_ARTICLE_REQUEST = 2;

    private MainPresenter mainPresenter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        FloatingActionButton buttonAddArticle = findViewById(R.id.button_add);
        buttonAddArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                startActivityForResult(intent, ADD_ARTICLE_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        ArticleAdapter adapter = new ArticleAdapter();
        recyclerView.setAdapter(adapter);

        mainPresenter =  new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(MainPresenter.class);
        mainPresenter.getAllArticles().observe(this, articles -> adapter.setArticles(articles));


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete this article?");
                // add the buttons
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mainPresenter.delete(adapter.getArticleAt(viewHolder.getAdapterPosition()));
                        Toast.makeText(MainActivity.this, "Article deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        Toast.makeText(getApplicationContext(),
                                "Article not deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new ArticleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Article article) {
                Intent intent = new Intent(com.example.inventoryapplication.main.MainActivity.this, AddEditActivity.class);
                intent.putExtra(AddEditActivity.EXTRA_ID, article.getId());
                intent.putExtra(AddEditActivity.EXTRA_NAME, article.getName());
                intent.putExtra(AddEditActivity.EXTRA_PRICE, article.getPrice());
                intent.putExtra(AddEditActivity.EXTRA_QUANTITY, article.getQuantity());
                intent.putExtra(AddEditActivity.EXTRA_SUPPLIER, article.getSupplier());
                intent.putExtra(AddEditActivity.EXTRA_IMAGE, article.getImage());
                startActivityForResult(intent, EDIT_ARTICLE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_ARTICLE_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(AddEditActivity.EXTRA_NAME);
            int price = data.getIntExtra(AddEditActivity.EXTRA_PRICE, 1);
            int quantity = data.getIntExtra(AddEditActivity.EXTRA_QUANTITY, 1);
            String supplier = data.getStringExtra(AddEditActivity.EXTRA_SUPPLIER);
            String image = data.getStringExtra(AddEditActivity.EXTRA_IMAGE);

            Article article = new Article(name, price, quantity, supplier, image);
            mainPresenter.insert(article);

            Toast.makeText(this, "Article saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_ARTICLE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Article can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = data.getStringExtra(AddEditActivity.EXTRA_NAME);
            int price = data.getIntExtra(AddEditActivity.EXTRA_PRICE, 1);
            int quantity = data.getIntExtra(AddEditActivity.EXTRA_QUANTITY, 1);
            String supplier = data.getStringExtra(AddEditActivity.EXTRA_SUPPLIER);
            String image = data.getStringExtra(AddEditActivity.EXTRA_IMAGE);

            Article article = new Article(name, price, quantity, supplier, image);
            article.setId(id);
            mainPresenter.update(article);

            Toast.makeText(this, "Article updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Article not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_articles:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete all?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mainPresenter.deleteAllArticles();
                        Toast.makeText(com.example.inventoryapplication.main.MainActivity.this, "All articles deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),
                                "Articles not deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}