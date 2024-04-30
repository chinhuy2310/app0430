package com.example.foodapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.adapter.PostImageAdapter;
import com.example.foodapp.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostDetail extends AppCompatActivity {
    private ImageView imageViewAvatar;
    private TextView textViewUsername, textViewDateTime, textViewTitle, textViewContent, txtRecipe;
    private RecyclerView recyclerViewImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_detail);

        // Ánh xạ các thành phần UI từ layout
        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewDateTime = findViewById(R.id.textViewDateTime);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewContent = findViewById(R.id.textViewContent);
        txtRecipe = findViewById(R.id.txtRecipe);
        recyclerViewImages = findViewById(R.id.recyclerViewImages);

        //hiện nút back
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back); // Đặt icon back tại đây
        }

        // Lấy dữ liệu bài đăng từ Intent
        Post post = (Post) getIntent().getSerializableExtra("POST_DETAIL");

        // Kiểm tra xem post có null hay không trước khi hiển thị
        if (post != null) {
            // Hiển thị dữ liệu của bài đăng trên layout
            displayPostDetails(post);
        } else {
            // Xử lý trường hợp post là null
        }
    }

    private void displayPostDetails(Post post) {
        String avatarUrl = post.getAvatarUrl();
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            Picasso.get().load(avatarUrl).into(imageViewAvatar);
        }else{
            Picasso.get().load(R.drawable.user_icon2).into(imageViewAvatar);
        }
        textViewUsername.setText(post.getUsername());
        textViewDateTime.setText(post.getDate());
        textViewTitle.setText(post.getTitle());
        textViewContent.setText(post.getContent());

        // Hiển thị "Recipe" nếu bài đăng là công thức nấu ăn
        if (post.getIsRecipe() == 1) {
            txtRecipe.setVisibility(View.VISIBLE);
        } else {
            txtRecipe.setVisibility(View.GONE);
        }

        // Hiển thị danh sách ảnh nếu có
        List<String> imageUrls = post.getImageUrls();
        if (imageUrls != null && !imageUrls.isEmpty()) {
            recyclerViewImages.setVisibility(View.VISIBLE);
            recyclerViewImages.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewImages.setAdapter(new PostImageAdapter(this, imageUrls));
        } else {
            recyclerViewImages.setVisibility(View.GONE);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
           finish(); // Quay lại Fragment trước đó
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
