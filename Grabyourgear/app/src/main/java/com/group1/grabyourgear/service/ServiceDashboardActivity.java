package com.group1.grabyourgear.service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.group1.grabyourgear.R;
import com.group1.grabyourgear.supplier.SupplierAddEquipmentActivity;
import com.group1.grabyourgear.utils.BaseActivity;
import com.group1.grabyourgear.utils.UserManager;

public class ServiceDashboardActivity extends BaseActivity {
    LinearLayout lyViewInquiries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_service_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lyViewInquiries = findViewById(R.id.lyViewInquiries);

        setHeaderTitle("Service Dashboard");

        lyViewInquiries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ServiceInquiryList.class);
                startActivity(intent);
            }
        });

        if(UserManager.getInstance().isApproved()) {
            // show functionality
        }
        else {
            // show not approved message
            Toast.makeText(getApplicationContext(), "Account pending approval", Toast.LENGTH_LONG).show();
        }
    }
}