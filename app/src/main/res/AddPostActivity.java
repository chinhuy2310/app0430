package com.example.foodapp;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodapp.data.DatabaseHelper;

import java.util.ArrayList;

public class AddPostActivity extends AppCompatActivity {

    private CheckBox checkBoxIsRecipe;
    private EditText editTextTitle;
    private EditText editTextContent;
    private Button buttonChooseImage;
    private RecyclerView imageViewSelected;
    private Button buttonSubmit;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_post);

        // Khởi tạo đối tượng DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Ánh xạ các view từ layout
        checkBoxIsRecipe = findViewById(R.id.checkBoxIsRecipe);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        buttonChooseImage = findViewById(R.id.buttonChooseImage);
        imageViewSelected = findViewById(R.id.imageViewSelected);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Thiết lập layout manager cho RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        imageViewSelected.setLayoutManager(layoutManager);

        // Thiết lập nút back trên ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back); // Đặt icon back tại đây
        }

        // Xử lý sự kiện khi nhấn nút Gửi bài viết
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin từ các trường nhập trên giao diện
                String title = editTextTitle.getText().toString().trim();
                String content = editTextContent.getText().toString().trim();
                boolean isRecipe = checkBoxIsRecipe.isChecked();

                // Kiểm tra xem tiêu đề và nội dung có rỗng không
                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(AddPostActivity.this, "Vui lòng điền đầy đủ tiêu đề và nội dung", Toast.LENGTH_SHORT).show();
                    return;
                }


                long postId = databaseHelper.insertPost(2, 1, isRecipe ? 1 : 0, title, content, new ArrayList<String>());
                if (postId != -1) {
                    Toast.makeText(AddPostActivity.this, "Đã gửi bài viết thành công", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(AddPostActivity.this, Menu2Fragment.class);
                    finish(); // Đóng Activity sau khi gửi bài viết thành công
//                    startActivity(intent);
                } else {
                    Toast.makeText(AddPostActivity.this, "Đã xảy ra lỗi khi gửi bài viết", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            showCancelConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showCancelConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Cancel Post");
        builder.setMessage("Do you want to cancel this post?");
        builder.setPositiveButton("Continue Writing", (dialogInterface, i) -> {
            // Do nothing, continue writing
        });
        builder.setNegativeButton("Cancel Post", (dialogInterface, i) -> {
            finish(); // Đóng Activity nếu người dùng chọn hủy bài viết
        });
        builder.show();
    }

}
