package com.example.inventoryapplication.addEdit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.inventoryapplication.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEditActivity extends AppCompatActivity implements AddEditContract.View {
    public static final int REQUEST_IMAGE_ARTICLE = 1;

    public static final String EXTRA_ID =
            "com.example.inventoryapp.view.EXTRA_ID";
    public static final String EXTRA_NAME =
            "com.example.inventoryapp.view.EXTRA_NAME";
    public static final String EXTRA_PRICE =
            "com.example.inventoryapp.view.EXTRA_PRICE";
    public static final String EXTRA_QUANTITY =
            "com.example.inventoryapp.view.EXTRA_QUANTITY";
    public static final String EXTRA_SUPPLIER =
            "com.example.inventoryapp.view.EXTRA_SUPPLIER";
    public static final String EXTRA_IMAGE =
            "com.example.inventoryapp.view.EXTRA_IMAGE";

    private AddEditPresenter addEditPresenter;

    private EditText editName;
    private EditText editPrice;
    private NumberPicker editQuantity;
    private EditText editSupplier;
    private ImageView editImage;
    private String imagePath;

    File imageFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        editName = findViewById(R.id.edit_name);
        editPrice = findViewById(R.id.edit_price);

        editQuantity = findViewById(R.id.edit_quantity);
        editQuantity.setMinValue(1);
        editQuantity.setMaxValue(100);

        editSupplier = findViewById(R.id.edit_supplier);
        editImage = findViewById(R.id.edit_image);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Article");
            editName.setText(intent.getStringExtra(EXTRA_NAME));
            editPrice.setText(String.valueOf(intent.getIntExtra(EXTRA_PRICE, 1)));
            editQuantity.setValue(intent.getIntExtra(EXTRA_QUANTITY, 1));
            editSupplier.setText(intent.getStringExtra(EXTRA_SUPPLIER));

            imagePath = intent.getStringExtra(EXTRA_IMAGE);
            Bitmap map = BitmapFactory.decodeFile(imagePath);
            editImage.setImageBitmap(map);
        } else {
            setTitle("Add Article");
        }
    }

    @Override
    public void saveArticle() {
        String name = editName.getText().toString();
        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Please insert name of the article", Toast.LENGTH_SHORT).show();
            return;
        }

        int price = 0;
        try {
            price = Integer.parseInt(editPrice.getText().toString());
        }catch (NumberFormatException e){
            Toast.makeText(this, "Please insert price of the article", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity = editQuantity.getValue();
        String supplier = editSupplier.getText().toString();
        String image = imagePath;

        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, name);
        data.putExtra(EXTRA_PRICE, price);
        data.putExtra(EXTRA_QUANTITY, quantity);
        data.putExtra(EXTRA_SUPPLIER, supplier);
        data.putExtra(EXTRA_IMAGE, image);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_article_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_article:
                saveArticle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setImagePath(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            try {
                imageFile = getImagePath();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (imageFile != null) {
                Uri imageUrl = FileProvider.getUriForFile(this,
                        "com.example.android.fileProvider", imageFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUrl);
                Glide.with(this).load(imageFile).into(editImage);
                startActivityForResult(cameraIntent, REQUEST_IMAGE_ARTICLE);
            }
        }
    }

    private File getImagePath() throws IOException {
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String name = "jpg_" + time + "_";
        File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(name, ".jpg", directory);
        imagePath = imageFile.getAbsolutePath();
        return imageFile;
    }
}