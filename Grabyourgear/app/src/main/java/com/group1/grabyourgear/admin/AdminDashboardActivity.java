package com.group1.grabyourgear.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group1.grabyourgear.R;
import com.group1.grabyourgear.common.FirebaseNodes;
import com.group1.grabyourgear.models.Users;
import com.group1.grabyourgear.utils.BaseActivity;
import com.group1.grabyourgear.utils.FirebaseHelper_Users;
import com.group1.grabyourgear.utils.UserManager;

import java.util.List;
import java.util.Objects;

public class AdminDashboardActivity extends BaseActivity {

    TextView tvPendingApplications;

    LinearLayout lySupplierApplications, lyUserManager, lySupplierManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setHeaderTitle("Admin Dashboard");

//        if(UserManager.getInstance().isApproved()) {
//            // show functionality
//        } else {
//            // show not approved message
//        }

        tvPendingApplications = findViewById(R.id.tv_pending_applications);

        // Need to get count of pending suppliers for the count thing
        FirebaseHelper_Users.loadAllUsers(new FirebaseHelper_Users.UserListCallback() {
            @Override
            public void onSuccess(List<Users> usersList) {
                int userCount = 0;
                for (Users u : usersList) {
                    if (Objects.equals(u.getRole(), "supplier") && !u.isApproved()) {
                        userCount++;
                    }
                }

                tvPendingApplications.setText("" + userCount);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AdminDashboardActivity.this,
                        "Supplier count retrieval failed",
                        Toast.LENGTH_SHORT).show();
            }
        });

        lySupplierApplications = findViewById(R.id.ly_supplier_applications);
        lySupplierApplications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminReviewSuppliersActivity.class);
                startActivity(intent);
            }
        });

        lyUserManager = findViewById(R.id.ly_user_mgmt);
        lyUserManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminUserListActivity.class);
                startActivity(intent);
            }
        });

        lySupplierManager = findViewById(R.id.ly_supplier_mgmt);
        lySupplierManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminSupplierListActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        FirebaseHelper_Users.loadAllUsers(new FirebaseHelper_Users.UserListCallback() {
            @Override
            public void onSuccess(List<Users> usersList) {
                int userCount = 0;
                for (Users u : usersList) {
                    if (Objects.equals(u.getRole(), "supplier") && !u.isApproved()) {
                        userCount++;
                    }
                }

                tvPendingApplications.setText("" + userCount);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(AdminDashboardActivity.this,
                        "Supplier count retrieval failed",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}