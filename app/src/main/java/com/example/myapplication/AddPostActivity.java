package com.example.myapplication;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.SelectedImageAdapter;
import com.example.myapplication.adapter.spinnerAdapter;
import com.example.myapplication.data.DatabaseHelper;
import com.example.myapplication.model.ItemData;
import com.example.myapplication.model.User;

import java.util.ArrayList;
import java.util.List;

public class AddPostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private CheckBox checkBoxIsRecipe;
    private EditText editTextTitle;
    private EditText editTextContent;
    private Button buttonChooseImage;
    private RecyclerView imageViewSelected;
    private Button buttonSubmit;
    private User loggedInUser;
    private Spinner spinner;
    private boolean isSpinnerOpen = false;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_post);

        // Khởi tạo đối tượng DatabaseHelper
        databaseHelper = new DatabaseHelper(this);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("user")) {
            loggedInUser = (User) intent.getSerializableExtra("user");
            Log.e("userid dang nhap","trang addpost :"+loggedInUser.getUserId());
        }

        // Ánh xạ các view từ layout
        checkBoxIsRecipe = findViewById(R.id.checkBoxIsRecipe);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        buttonChooseImage = findViewById(R.id.buttonChooseImage);
        imageViewSelected = findViewById(R.id.imageViewSelected);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        spinner = findViewById(R.id.spinner);
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Lúc bắt đầu chạm vào Spinner, kiểm tra và ẩn bàn phím nếu cần
                        if (isSpinnerOpen==false) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            editTextContent.clearFocus();
                            editTextTitle.clearFocus();
                            isSpinnerOpen = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        // Khi nhả chạm, cập nhật trạng thái của Spinner
                        isSpinnerOpen = false;
                        break;
                }
                return false;
            }
        });
        buttonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Cho phép chọn nhiều ảnh
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
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
        List<ItemData> itemList = createItemgridList();
        spinnerAdapter adapter = new spinnerAdapter(this, itemList);
        spinner.setAdapter(adapter);

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
                    Toast.makeText(AddPostActivity.this, "Please fill in the title and content completely", Toast.LENGTH_SHORT).show();
                    return;
                }

                int userId = loggedInUser.getUserId();
//                Log.e("usid", String.valueOf(userId));

                // Lấy ID của mục đã chọn từ Spinner
                ItemData selectedItem = (ItemData) spinner.getSelectedItem();
                int groupPostId = selectedItem.getGroupId(); // Lấy ID của mục đã chọn
                long postId = databaseHelper.insertPost(userId, groupPostId, isRecipe ? 1 : 0, title, content, new ArrayList<String>());
                if (postId != -1) {
                    Toast.makeText(AddPostActivity.this, "sent successfully", Toast.LENGTH_SHORT).show();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                // Nếu người dùng đã chọn nhiều ảnh
                int count = data.getClipData().getItemCount();
                List<Uri> imageUris = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    imageUris.add(imageUri);
                    // Xử lý ảnh được chọn ở đây, ví dụ: hiển thị nó lên ImageView
                    // Đồng thời, bạn cũng có thể lưu đường dẫn của ảnh vào cơ sở dữ liệu nếu cần
                    // Ví dụ:
                    // String imagePath = imageUri.getPath();
                    // databaseHelper.insertImage(imagePath);
                }
                displaySelectedImages(imageUris);
            } else if (data.getData() != null) {
                // Nếu người dùng chỉ chọn một ảnh
                Uri imageUri = data.getData();
                List<Uri> imageUris = new ArrayList<>();
                imageUris.add(imageUri);
                // Xử lý ảnh được chọn ở đây, ví dụ: hiển thị nó lên ImageView
                // Đồng thời, bạn cũng có thể lưu đường dẫn của ảnh vào cơ sở dữ liệu nếu cần
                // Ví dụ:
                // String imagePath = imageUri.getPath();
                // databaseHelper.insertImage(imagePath);
                displaySelectedImages(imageUris);
            }
        }
    }
    private void displaySelectedImages(List<Uri> imageUris) {
        List<String> imagePaths = new ArrayList<>();
        for (Uri uri : imageUris) {
            // Lấy đường dẫn của ảnh và thêm vào danh sách
            String imagePath = uri.toString();
            imagePaths.add(imagePath);
        }
        if (!imagePaths.isEmpty()) {
            // Tạo adapter và thiết lập cho RecyclerView
            SelectedImageAdapter adapter1 = new SelectedImageAdapter(this, imagePaths);
            adapter1.setOnListEmptyListener(new SelectedImageAdapter.OnListEmptyListener() {
                @Override
                public void onListEmpty() {
                    imageViewSelected.setVisibility(View.GONE);
                }
            });
            imageViewSelected.setAdapter(adapter1);
            imageViewSelected.setVisibility(View.VISIBLE);
        } else {
            imageViewSelected.setVisibility(View.GONE);
        }
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
    private List<ItemData> createItemgridList() {
        List<ItemData> itemgridList = new ArrayList<>();
        itemgridList.add(new ItemData(R.drawable.meat, "고기",1));
        itemgridList.add(new ItemData(R.drawable.seafood, "생선",2));
        itemgridList.add(new ItemData(R.drawable.cereal, "곡류",3));
        itemgridList.add(new ItemData(R.drawable.vegetable, "채소",4));
//        itemgridList.add(new ItemData(R.drawable.botmi, "간식"));
        itemgridList.add(new ItemData(R.drawable.dessert, "디저트",5));
        itemgridList.add(new ItemData(R.drawable.cooking, " 끓임",6));
        itemgridList.add(new ItemData(R.drawable.deep_fried, "튀김",7));
        itemgridList.add(new ItemData(R.drawable.soup, " 국",8));
        itemgridList.add(new ItemData(R.drawable.grill, "구워",9));
        itemgridList.add(new ItemData(R.drawable.fried, "볶음",10));
        itemgridList.add(new ItemData(R.drawable.smoothie, "스무티",11));
        itemgridList.add(new ItemData(R.drawable.ic_delete, "Item 12",12));
        itemgridList.add(new ItemData(R.drawable.ic_delete, "Item 13",13));
        itemgridList.add(new ItemData(R.drawable.ic_delete, "Item 14",14));
        // Add data for other items here
        return itemgridList;
    }


}
