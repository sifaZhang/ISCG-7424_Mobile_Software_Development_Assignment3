package com.group1.grabyourgear.service;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group1.grabyourgear.R;
import com.group1.grabyourgear.auth.ProfileActivity;
import com.group1.grabyourgear.common.AppConstants;
import com.group1.grabyourgear.common.FirebaseNodes;
import com.group1.grabyourgear.models.Inquiry;
import com.group1.grabyourgear.utils.CloudinaryUploader;
import com.group1.grabyourgear.utils.UploadCallback;
import com.group1.grabyourgear.utils.UserManager;

public class ServiceInquiryActivity extends AppCompatActivity {

    private EditText etContact, etDetails;
    private Button btnSendInquiry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_service_inquiry);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etContact = findViewById(R.id.etContact);
        etDetails = findViewById(R.id.etDetails);
        btnSendInquiry = findViewById(R.id.btnSendInquiry);

        btnSendInquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendInquiry();
            }
        });
    }

    private void sendInquiry(){
        String contact = etContact.getText().toString().trim();
        String details = etDetails.getText().toString().trim();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("inquiries");
        String id = ref.push().getKey();

        Inquiry inquiry = new Inquiry(
                id,
                contact,
                details
        );

        ref.child(id).setValue(inquiry)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Inquiry sent", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }
}