package com.group1.grabyourgear.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.group1.grabyourgear.R;
import com.group1.grabyourgear.common.AppConstants;
import com.group1.grabyourgear.models.Users;
import com.group1.grabyourgear.utils.BaseActivity;
import com.group1.grabyourgear.utils.CloudinaryUploader;
import com.group1.grabyourgear.utils.FirebaseHelper_Users;

import java.util.Objects;

public class AdminEditUserActivity extends BaseActivity {

    String userID, userRole;

    private CloudinaryUploader uploader;

    EditText txtName, txtUsername, txtEmail, txtPhone, txtAddress;

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
        txtEmail = findViewById(R.id.txt_admin_edit_email);
        txtPhone = findViewById(R.id.txt_admin_edit_phone);
        txtAddress = findViewById(R.id.txt_admin_edit_address);
        imgAvatar = findViewById(R.id.img_admin_edit_avatar);

        FirebaseHelper_Users.loadUserInfo(userID, new FirebaseHelper_Users.UserCallback() {
            @Override
            public void onSuccess(Users user) {
                txtName.setText(user.getName());
                txtUsername.setText(user.getUsername());
                txtEmail.setText(user.getEmail());
                txtPhone.setText(user.getPhone());
                txtAddress.setText(user.getAddress());

                Glide.with(getApplicationContext())
                        .load(user.getAvatar())
                        .placeholder(R.drawable.placeholder_avatar)
                        .into(imgAvatar);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Could not load user information!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}