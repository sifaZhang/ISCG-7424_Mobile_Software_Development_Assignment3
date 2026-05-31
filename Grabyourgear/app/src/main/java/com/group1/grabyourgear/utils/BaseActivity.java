package com.group1.grabyourgear.utils;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group1.grabyourgear.R;
import com.group1.grabyourgear.admin.AdminDashboardActivity;
import com.group1.grabyourgear.auth.LoginActivity;
import com.group1.grabyourgear.auth.ProfileActivity;
import com.group1.grabyourgear.common.AppConstants;
import com.group1.grabyourgear.customer.CustomerDashboardActivity;
import com.group1.grabyourgear.customer.CustomerMyBookingsActivity;
import com.group1.grabyourgear.models.Users;
import com.group1.grabyourgear.service.ServiceDashboardActivity;
import com.group1.grabyourgear.service.ServiceFAQActivity;
import com.group1.grabyourgear.service.ServiceInquiryActivity;
import com.group1.grabyourgear.supplier.SupplierDashboardActivity;

public abstract class BaseActivity extends AppCompatActivity {

    protected ImageView imgHeaderAvatar, imgHeaderLogo;
    protected TextView tvHeaderTitle, tvHeaderUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        setupHeader();

        View root = findViewById(android.R.id.content);
        root.setBackgroundColor(Color.parseColor(AppConstants.BK_COLOR));
    }

    protected void setupHeader() {
        imgHeaderAvatar = findViewById(R.id.imgHeaderAvatar);
        tvHeaderTitle = findViewById(R.id.tvHeaderTitle);
        imgHeaderLogo = findViewById(R.id.imgHeaderLogo);
        tvHeaderUsername = findViewById(R.id.tvHeaderUsername);

        if (imgHeaderAvatar == null || tvHeaderTitle == null || imgHeaderLogo == null || tvHeaderUsername == null) {
            return;
        }

        setupAvatarClick();
        loadUserAvatar();
        setupLogoClicke();
        setupUsername();
    }

    private void setupUsername() {
        Users user = UserManager.getInstance().getUser();
        if (user != null) {
            tvHeaderUsername.setText(user.getUsername());
        }
    }

    private void setupLogoClicke() {
        imgHeaderLogo.setOnClickListener(v -> {
            Users user = UserManager.getInstance().getUser();
            if (user != null && user.getRole() != null) {
                goToDashboard(user.getRole());
            }
        });
    }

    private void setupAvatarClick() {
        imgHeaderAvatar.setOnClickListener(v -> {
            UserPrefs prefs = new UserPrefs(this);
            if (prefs.isLoggedIn()) {
                showAvatarMenu(v);
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        });
    }

    protected void goToDashboard(String role) {
        switch (role) {
            case AppConstants.Role.CUSTOMER:
                startActivity(new Intent(this, CustomerDashboardActivity.class));
                finish();
                break;
            case AppConstants.Role.SUPPLIER:
                startActivity(new Intent(this, SupplierDashboardActivity.class));
                finish();
                break;
            case AppConstants.Role.SERVICE:
                startActivity(new Intent(this, ServiceDashboardActivity.class));
                finish();
                break;
            case AppConstants.Role.ADMIN:
                startActivity(new Intent(this, AdminDashboardActivity.class));
                finish();
                break;
            default:
                Toast.makeText(this, "Unknown role: " + role, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void showAvatarMenu(View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenuInflater().inflate(R.menu.avatar_menu, popup.getMenu());

        Users currentUser = UserManager.getInstance().getUser();

        if(currentUser == null) {
            Toast.makeText(this, "User session expired. Please log in again.", Toast.LENGTH_SHORT).show();

            UserPrefs prefs = new UserPrefs(this);
            prefs.clear();

            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        if(UserManager.getInstance().getUser().getRole().equals(AppConstants.Role.CUSTOMER)){
            popup.getMenu().findItem(R.id.menu_myBookings).setVisible(true);
        }
        else{
            popup.getMenu().findItem(R.id.menu_myBookings).setVisible(false);
        }

        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            } else if (id == R.id.menu_logout) {
                UserPrefs prefs = new UserPrefs(this);
                prefs.clear();
                UserManager.getInstance().clear();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
                return true;
            } else if(id == R.id.menu_myBookings) {
                startActivity(new Intent(this, CustomerMyBookingsActivity.class));
                return true;
            } else if(id == R.id.menu_faq) {
                startActivity(new Intent(this, ServiceFAQActivity.class));
                return true;
            }else if(id == R.id.menu_inquiries) {
                startActivity(new Intent(this, ServiceInquiryActivity.class));
                return true;
            }

            return false;
        });

        popup.show();
    }

    private void loadUserAvatar() {
        Users user = UserManager.getInstance().getUser();
        if(user == null || user.getAvatar() == null || user.getAvatar().isEmpty())
            return;

        Glide.with(this)
                .load(user.getAvatar())
                .placeholder(R.drawable.placeholder_avatar)
                .into(imgHeaderAvatar);
    }

    protected void setHeaderTitle(String title) {
        if (tvHeaderTitle != null) {
            tvHeaderTitle.setText(title);
        }
    }
}
