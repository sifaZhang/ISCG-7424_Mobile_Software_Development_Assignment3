package com.group1.grabyourgear.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.group1.grabyourgear.R;
import com.group1.grabyourgear.common.AppConstants;
import com.group1.grabyourgear.models.Users;
import com.group1.grabyourgear.utils.BaseActivity;
import com.group1.grabyourgear.utils.FirebaseHelper_Users;
import com.group1.grabyourgear.utils.UserManager;
import com.group1.grabyourgear.utils.UserPrefs;

import java.util.Objects;

public class LoginActivity extends BaseActivity {

    private Button btLogin;
    private EditText etEmail, etPassword;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setHeaderTitle("Login");

        btLogin = findViewById(R.id.btnLogin);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvRegister = findViewById(R.id.tvLogin);

        imgHeaderAvatar.setVisibility(View.INVISIBLE);
        imgHeaderLogo.setVisibility(View.INVISIBLE);

        tvRegister.setText(Html.fromHtml("Don't have an account? <u>Register</u>",
                Html.FROM_HTML_MODE_LEGACY));

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseHelper_Users helper = new FirebaseHelper_Users();

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                String uid = auth.getCurrentUser().getUid();

                                helper.loadUserInfo(uid, new FirebaseHelper_Users.UserCallback() {
                                    @Override
                                    public void onSuccess(Users user) {
                                        // 保存用户资料
                                        UserPrefs prefs = new UserPrefs(LoginActivity.this);
                                        prefs.saveLogin(user.getUid());
                                        UserManager.getInstance().setUser(user);

                                        // Check if the user is approved, if not show an appropriate message
                                        // based on the role of the user and do not go to the dashboard.
                                        if(user.isApproved()) {
                                            Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();

                                            goToDashboard(user.getRole());
                                        } else {
                                            // Make sure the user cannot get to the dashboard anyway by leaving the app and re-opening it
                                            FirebaseAuth.getInstance().signOut();
                                            UserManager.getInstance().clear();
                                            if(Objects.equals(user.getRole(), AppConstants.Role.SUPPLIER)) {
                                                Toast.makeText(LoginActivity.this, "Account not yet approved.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(LoginActivity.this, "Account has been banned.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(LoginActivity.this, "Load user info error", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } else {
                                Toast.makeText(LoginActivity.this, "Email or Password is incorrect", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}