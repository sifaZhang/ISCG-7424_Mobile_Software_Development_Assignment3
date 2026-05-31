package com.group1.grabyourgear.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.Firebase;
import com.group1.grabyourgear.R;
import com.group1.grabyourgear.auth.ProfileActivity;
import com.group1.grabyourgear.common.AppConstants;
import com.group1.grabyourgear.common.FirebaseNodes;
import com.group1.grabyourgear.models.Users;
import com.group1.grabyourgear.utils.BaseActivity;
import com.group1.grabyourgear.utils.CloudinaryUploader;
import com.group1.grabyourgear.utils.FirebaseHelper_Users;
import com.group1.grabyourgear.utils.UploadCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AdminEditUserActivity extends BaseActivity {

    String userID, userRole, strAvatarUrl;

    private CloudinaryUploader uploader;

    EditText txtName, txtUsername, txtPhone, txtAddress;

    TextView tvChangeAvatar;

    Button btnSave, btnCancel;

    ImageView imgAvatar;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            uploader.handleResult(data);
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_edit_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();

        userRole = intent.getStringExtra("ROLE");
        userID = intent.getStringExtra("UID");

        if(Objects.equals(userRole, AppConstants.Role.SUPPLIER)) {
            setHeaderTitle("Edit Supplier");
        }
        else if (Objects.equals(userRole, AppConstants.Role.CUSTOMER)){
            setHeaderTitle("Edit Customer");
        }
        else {
            setHeaderTitle("Edit User");
        }

        txtName = findViewById(R.id.txt_admin_edit_name);
        txtUsername = findViewById(R.id.txt_admin_edit_username);
        txtPhone = findViewById(R.id.txt_admin_edit_phone);
        txtAddress = findViewById(R.id.txt_admin_edit_address);
        imgAvatar = findViewById(R.id.img_admin_edit_avatar);

        btnSave = findViewById(R.id.btn_admin_edit_save);
        btnCancel = findViewById(R.id.btn_admin_edit_cancel);

        tvChangeAvatar = findViewById(R.id.tv_admin_edit_avatar);

        strAvatarUrl = "";

        FirebaseHelper_Users.loadUserInfo(userID, new FirebaseHelper_Users.UserCallback() {
            @Override
            public void onSuccess(Users user) {
                txtName.setText(user.getName());
                txtUsername.setText(user.getUsername());
                txtPhone.setText(user.getPhone());
                txtAddress.setText(user.getAddress());
                strAvatarUrl = user.getAvatar();

                showImage(strAvatarUrl);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Could not load user information!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserEdits();
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        uploader = new CloudinaryUploader(this, imagePickerLauncher);

        tvChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploader.pickImage(new UploadCallback() {
                    @Override
                    public void onSuccess(String imageUrl) {
                        showImage(imageUrl);
                        strAvatarUrl = imageUrl;
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(AdminEditUserActivity.this, "Upload failed：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showImage(String url) {
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder_avatar)
                .into(imgAvatar);
    }

    private void saveUserEdits() {
        String fullName = txtName.getText().toString().trim();
        String username = txtUsername.getText().toString().trim();
        String phone = txtPhone.getText().toString().trim();
        String address = txtAddress.getText().toString().trim();

        if(fullName.isEmpty() || username.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill out all fields.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseHelper_Users.loadUserInfo(userID, new FirebaseHelper_Users.UserCallback() {
            @Override
            public void onSuccess(Users user) {
                Map<String, Object> updates = new HashMap<>();
                updates.put(FirebaseNodes.UserFields.FULLNAME, fullName);
                updates.put(FirebaseNodes.UserFields.USERNAME, username);
                updates.put(FirebaseNodes.UserFields.PHONE, phone);
                updates.put(FirebaseNodes.UserFields.ADDRESS, address);
                if (strAvatarUrl != null) {
                    updates.put(FirebaseNodes.UserFields.AVATAR, strAvatarUrl);
                }

                FirebaseHelper_Users.updateUserProfile(userID, updates,
                        new FirebaseHelper_Users.UpdateCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(getApplicationContext(),
                                        userRole + " updated successfully!",
                                        Toast.LENGTH_SHORT).show();

                                user.setName(fullName);
                                user.setUsername(username);
                                user.setPhone(phone);
                                user.setAddress(address);
                                user.setAvatar(strAvatarUrl);
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(getApplicationContext(),
                                        userRole + " could not be updated. Error: " +
                                                e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getApplicationContext(), userRole + " not found!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}