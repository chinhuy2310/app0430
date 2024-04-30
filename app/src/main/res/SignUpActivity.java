package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodapp.data.DatabaseHelper;

public class SignUpActivity extends AppCompatActivity {
    EditText usernameEditText,accnameEditText,passwordEditText,emailEditText,confirmPasswordEditText;
    DatabaseHelper dbHelper;
    TextView loginTextView, signUpButton;
    ImageView eyeIcon1, eyeIcon2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        // 레이아웃에서 뷰들을 찾아와 연결합니다
        // Ánh xạ các view từ layout
        usernameEditText = findViewById(R.id.createUsername);
        accnameEditText = findViewById(R.id.createAccname);
        passwordEditText = findViewById(R.id.createPW);
        confirmPasswordEditText = findViewById(R.id.createPW2);
        emailEditText = findViewById(R.id.createEmail);
        signUpButton = findViewById(R.id.SignUpbutton);
        loginTextView = findViewById(R.id.logintext);
        eyeIcon1 = findViewById(R.id.eye_icon1);
        eyeIcon2 = findViewById(R.id.eye_icon2);


        dbHelper = new DatabaseHelper(this);


        //chuyển sang login khi nhấn text login
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo intent để chuyển từ SignUpActivity sang LoginActivity
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Đóng SignUpActivity sau khi chuyển sang LoginActivity
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        // Xử lý sự kiện khi nhấn vào nút Sign Up
        // 가입 버튼 클릭 시 이벤트 처리
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ EditText
                // EditText로부터 데이터를 가져옵니다
                String username = usernameEditText.getText().toString();
                String accname = accnameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                String email = emailEditText.getText().toString();

                if (username.isEmpty() || accname.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Kiểm tra xem tên tài khoản đã tồn tại hay chưa
                // 사용자 이름이 이미 존재하는지 확인합니다
                if (dbHelper.isUsernameExists(accname)) {
                    Toast.makeText(SignUpActivity.this, "Account id already exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Kiểm tra xem mật khẩu và xác nhận mật khẩu có khớp nhau không
                // 비밀번호와 비밀번호 확인이 일치하는지 확인합니다
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }


                // Thêm tài khoản vào cơ sở dữ liệu
                // 데이터베이스에 사용자 정보를 추가합니다
                DatabaseHelper dbHelper = new DatabaseHelper(SignUpActivity.this);
                dbHelper.insertUser(username, accname, email, password, "", "", 0);

                // Hiển thị thông báo khi tài khoản được tạo thành công
                // 사용자가 성공적으로 가입되었음을 알리는 메시지를 표시합니다
                Toast.makeText(SignUpActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();

                // Đóng hoạt động và quay trở lại màn hình đăng nhập
                // 활동을 종료하고 로그인 화면으로 돌아갑니다
                finish();
            }
        });
        // Xử lý sự kiện khi nhấn vào icon eye trong password
        eyeIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordEditText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    eyeIcon1.setImageResource(R.drawable.ic_eye);
                } else {
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    eyeIcon1.setImageResource(R.drawable.ic_eyex);
                }
            }
        });

        // Xử lý sự kiện khi nhấn vào icon eye trong confirm password
        eyeIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmPasswordEditText.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    confirmPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    eyeIcon2.setImageResource(R.drawable.ic_eye);
                } else {
                    confirmPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    eyeIcon2.setImageResource(R.drawable.ic_eyex);
                }
            }
        });
    }
}