package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.myapplication.data.DatabaseHelper;
import com.example.myapplication.model.User;

public class LoginActivity extends AppCompatActivity {
    TextView signUpTextView,findPassword,buttonLogin;
    EditText editTextUsername, editTextPassword;

    DatabaseHelper databaseHelper;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
        editTextUsername = findViewById(R.id.taikhoan);
        editTextPassword = findViewById(R.id.matkhau);
        buttonLogin = findViewById(R.id.loginbutton);
        signUpTextView = findViewById(R.id.signUpTextView);
        findPassword = findViewById(R.id.findPasswordText);


        databaseHelper = new DatabaseHelper(this);

        //로그인되어 있으면 메인 화면으로 이동합니다
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Login();
            }
        });

        editTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
//                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    Login();
                    return true;
                }
                return false;
            }
        });
        //chuyển sang màn hình  sign up khi nhấn text sign up ở cuối màn hình
        //"화면 하단의 'Sign up' 텍스트를 누르면 '가입' 화면으로 전환됩니다."
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        //chuyển đến màn hình tìm mật khẩu khi nhấn "forgot your password"
        // 'forgot your password?'를 클릭하면 비밀번호 찾기 화면으로 이동합니다
        findPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FindPassword.class);
                startActivity(intent);
            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
    public void Login() {
        // Get username and password input
        String useraccname = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Check if username and password are not empty
        if (!useraccname.isEmpty() && !password.isEmpty()) {
            // Check if username and password are valid
            if (databaseHelper.checkUser(useraccname, password)) {
                // Login successful
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                User user = databaseHelper.getUserByUseraccname(useraccname);
                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                } else {
                    Log.e("LoginActivity", "User not found for username: " + useraccname);
                }
                finish();
            } else {
                // Login failed
                Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Empty username or password
            Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
        }
    }

}
