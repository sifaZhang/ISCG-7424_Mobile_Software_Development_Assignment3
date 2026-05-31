package com.group1.grabyourgear.admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group1.grabyourgear.R;
import com.group1.grabyourgear.models.Users;
import com.group1.grabyourgear.utils.Adapter_AdminSupplierApplicationView;
import com.group1.grabyourgear.utils.Adapter_AdminUserView;
import com.group1.grabyourgear.utils.BaseActivity;
import com.group1.grabyourgear.utils.FirebaseHelper_Users;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminUserListActivity extends BaseActivity {

    RecyclerView rvUsers;

    Adapter_AdminUserView adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_user_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setHeaderTitle("User Management");

        rvUsers = findViewById(R.id.rv_admin_user_view);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));

        FirebaseHelper_Users.loadAllUsers(new FirebaseHelper_Users.UserListCallback() {
            @Override
            public void onSuccess(List<Users> usersList) {
                List<Users> uList = new ArrayList<>();

                for (Users u : usersList) {
                    if (!Objects.equals(u.getRole(), "supplier")) {
                        uList.add(u);
                    }
                }

                adapter = new Adapter_AdminUserView(
                        AdminUserListActivity.this,
                        uList);

                rvUsers.setAdapter(adapter);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AdminUserListActivity.this,
                        "User list retrieval failed",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void refreshAdapter() {
        FirebaseHelper_Users.loadAllUsers(new FirebaseHelper_Users.UserListCallback() {
            @Override
            public void onSuccess(List<Users> usersList) {
                List<Users> uList = new ArrayList<>();

                for (Users u : usersList) {
                    if (!Objects.equals(u.getRole(), "supplier")) {
                        uList.add(u);
                    }
                }

                adapter = new Adapter_AdminUserView(
                        AdminUserListActivity.this,
                        uList);

                rvUsers.setAdapter(adapter);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AdminUserListActivity.this,
                        "User list retrieval failed",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}