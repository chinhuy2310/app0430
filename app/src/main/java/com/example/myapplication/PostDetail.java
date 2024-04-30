package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.adapter.CommentAdapter;
import com.example.myapplication.adapter.PostImageAdapter;
import com.example.myapplication.data.DatabaseHelper;
import com.example.myapplication.model.Comment;

import com.example.myapplication.model.Post;
import com.example.myapplication.model.User;
import com.squareup.picasso.Picasso;


import java.util.List;

public class PostDetail extends AppCompatActivity {
    private NestedScrollView netscrollview;
    private ImageButton buttonLike;
    private ImageView imageViewAvatar,sendComment;
    private TextView textViewUsername, textViewDateTime, textViewTitle, textViewContent, txtRecipe,likeCountTextView,commentCountTextView;
    private RecyclerView recyclerViewImages, recyclerViewComments;
    private EditText writecomment;
    private DatabaseHelper databaseHelper;
    private User user;
    private int postId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_detail);

        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("user")) {
            user = (User) intent.getSerializableExtra("user");
//            if (user != null) {
//                Log.e("userid dang nhap", "tại trang nội dung: " + user.getUserId());
//            } else {
//                Log.e("PostDetail", "User is null");
//            }
        } else {
            // Xử lý trường hợp Intent không chứa "user"
            Log.e("PostDetail", "Intent does not contain 'user' extra");
        }
        Post post = (Post) getIntent().getSerializableExtra("POST_DETAIL");
        postId = intent.getIntExtra("POST_ID", -1);

        // Ánh xạ các thành phần UI từ layout
        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewDateTime = findViewById(R.id.textViewDateTime);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewContent = findViewById(R.id.textViewContent);
        txtRecipe = findViewById(R.id.txtRecipe);
        netscrollview = findViewById(R.id.netscrollview);
        recyclerViewImages = findViewById(R.id.recyclerViewImages);
        recyclerViewComments = findViewById(R.id.recyclerViewComments);
        writecomment = findViewById(R.id.writeComment);
        sendComment = findViewById(R.id.sendComment);

        likeCountTextView = findViewById(R.id.likeCountTextView);
        commentCountTextView = findViewById(R.id.commentCountTextView);

        likeCountTextView.setText(String.valueOf(databaseHelper.getLikeCountForPost(postId)));
        commentCountTextView.setText(String.valueOf(databaseHelper.getCommentCountForPost(postId)));
        // Ánh xạ nút like
        buttonLike = findViewById(R.id.buttonLike);
        buttonLike.setImageResource(post.isLiked() ? R.drawable.ic_liked : R.drawable.like);
        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLiked = !post.isLiked(); // Đảo ngược trạng thái like
                // Gọi phương thức để thay đổi trạng thái like trong cơ sở dữ liệu
                databaseHelper.toggleLike(user.getUserId(), postId, isLiked);
                // Cập nhật trạng thái like của bài đăng
                post.setLiked(isLiked);
                // Cập nhật icon của nút like
                buttonLike.setImageResource(isLiked ? R.drawable.ic_liked : R.drawable.like);
                // Cập nhật số lượng like trên giao diện
                int likeCount = databaseHelper.getLikeCountForPost(postId);
                likeCountTextView.setText(String.valueOf(likeCount));
            }
        });

        boolean scrollToComment = getIntent().getBooleanExtra("SCROLL_TO_COMMENT", false);
        if (scrollToComment) {
            recyclerViewComments.post(new Runnable() {
                @Override
                public void run() {
                    // Cuộn RecyclerView đến phần comment
                    scrollToComments();
                    // Hiển thị bàn phím để nhập comment
                    writecomment.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(writecomment, InputMethodManager.SHOW_IMPLICIT);
                }
            });
        }
        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi phương thức để xử lý việc gửi comment
                sendComment();
            }
        });
        writecomment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No need to implement anything here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No need to implement anything here
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Kiểm tra nếu text đã được nhập vào EditText
                if (s.toString().trim().isEmpty()) {
                    sendComment.setVisibility(View.GONE);
                } else {

                    sendComment.setVisibility(View.VISIBLE);
                }
            }
        });

        //hiện nút back
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back); // Đặt icon back tại đây
        }

        // Lấy dữ liệu bài đăng từ Intent

//        Log.e("postid:  ", String.valueOf(postId));
        // Kiểm tra xem post có null hay không trước khi hiển thị
        if (post != null) {
            // Hiển thị dữ liệu của bài đăng trên layout
            displayPostDetails(post);
            displayComments(post.getComments());
        } else {
            // Xử lý trường hợp post là null
        }
    }
    public void sendComment() {
        if (user != null) {
            String commentContent = writecomment.getText().toString().trim();
            if (!commentContent.isEmpty()) {
                // Thêm comment vào cơ sở dữ liệu
                long commentId = databaseHelper.addComment(user.getUserId(), postId, commentContent);
                if (commentId != -1) {
                    Toast.makeText(this, "Comment added successfully", Toast.LENGTH_SHORT).show();
                    commentCountTextView.setText(String.valueOf(databaseHelper.getCommentCountForPost(postId)));
                    // Làm mới danh sách comment để hiển thị comment mới thêm vào
                    writecomment.setText("");
                    hideKeyboard();
                    writecomment.clearFocus();
                    List<Comment> newComments = databaseHelper.getCommentsForPost(postId);
                    displayComments(newComments);
                } else {
                    // Hiển thị thông báo lỗi nếu không thêm được comment vào cơ sở dữ liệu
                    Toast.makeText(this, "Failed to add comment", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Xử lý trường hợp loggedInUser là null
            Log.e("PostDetail", "loggedInUser is null");
        }
    }
    private void scrollToComments() {
        if (recyclerViewComments != null) {
            int totalHeight = netscrollview.getChildAt(0).getHeight();
            int recyclerViewHeight = recyclerViewComments.getHeight();
            if(totalHeight >recyclerViewHeight){
              int scrollPosition = totalHeight - recyclerViewHeight ;
             netscrollview.scrollTo(0, scrollPosition);
            }
            else {
                netscrollview.scrollTo(0, recyclerViewComments.getTop());
            }
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

    private void displayComments(List<Comment> comments) {
        // Hiển thị danh sách các comment trong RecyclerView
        if (comments != null && !comments.isEmpty()) {
            recyclerViewComments.setVisibility(View.VISIBLE);
            recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewComments.setAdapter(new CommentAdapter(this, comments));
        } else {
            recyclerViewComments.setVisibility(View.GONE);
        }
    }
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
