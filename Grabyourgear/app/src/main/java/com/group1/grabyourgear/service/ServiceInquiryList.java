package com.group1.grabyourgear.service;

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
import com.group1.grabyourgear.customer.CustomerMyBookingsActivity;
import com.group1.grabyourgear.utils.Adapter_InquiryView;
import com.group1.grabyourgear.models.Inquiry;
import com.group1.grabyourgear.utils.Adapter_MyBookingView;
import com.group1.grabyourgear.utils.FirebaseHelper_Inquiries;

import java.util.List;

public class ServiceInquiryList extends AppCompatActivity {

    RecyclerView recyclerView;
    Adapter_InquiryView adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_service_inquiry_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.rvInquiryList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseHelper_Inquiries.loadAllInquiries(new FirebaseHelper_Inquiries.InquiryListCallback() {
            @Override
            public void onSuccess(List<Inquiry> inquiryList) {
                adapter = new Adapter_InquiryView(inquiryList, ServiceInquiryList.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(ServiceInquiryList.this,
                        "Failed to load inquiries: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}