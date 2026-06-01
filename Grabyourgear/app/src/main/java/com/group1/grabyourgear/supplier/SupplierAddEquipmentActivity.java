package com.group1.grabyourgear.supplier;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group1.grabyourgear.R;
import com.group1.grabyourgear.auth.ProfileActivity;
import com.group1.grabyourgear.common.AppConstants;
import com.group1.grabyourgear.common.FirebaseNodes;
import com.group1.grabyourgear.models.Equipment;
import com.group1.grabyourgear.utils.BaseActivity;
import com.group1.grabyourgear.utils.CloudinaryUploader;
import com.group1.grabyourgear.utils.UploadCallback;
import com.group1.grabyourgear.utils.UserManager;

import java.util.Arrays;
import java.util.List;

public class SupplierAddEquipmentActivity extends BaseActivity {

    private EditText etName, etDescription, etPrice, etDiscount, etLocation;
    private Spinner spinnerCategory;
    private Button btnSaveImg;
    private TextView tvUploadImgAddEquip;
    private ImageView imgEquip;
    private String imgUrl;
    private CloudinaryUploader uploaderEquipImg;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            uploaderEquipImg.handleResult(data);
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_supplier_add_equipment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setHeaderTitle("Add Equipment");

        etName = findViewById(R.id.etEquipName);
        etDescription = findViewById(R.id.etEquipDescription);
        etPrice = findViewById(R.id.etEquipPrice);
        etDiscount = findViewById(R.id.etEquipDiscount);
        etLocation = findViewById(R.id.etEquipLocation);
        spinnerCategory = findViewById(R.id.spCategoryAddEquip);
        btnSaveImg = findViewById(R.id.btnSaveAddEquip);
        tvUploadImgAddEquip = findViewById(R.id.tvEquipImageUpload);
        imgEquip = findViewById(R.id.imgEquipment);

        fillCategory();

        uploaderEquipImg = new CloudinaryUploader(this, imagePickerLauncher);

        btnSaveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEquipment();
            }
        });

        tvUploadImgAddEquip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploaderEquipImg.pickImage(new UploadCallback() {
                    @Override
                    public void onSuccess(String imageUrl) {
                        showImage(imageUrl);
                        imgUrl = imageUrl;
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getApplicationContext(), "Upload failed：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void saveEquipment() {
        String name = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String discount = etDiscount.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String selectedCategoryName = spinnerCategory.getSelectedItem().toString();
        String categoryId = getCategoryIdFromName(selectedCategoryName);
        String imageUrl = imgUrl;

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser == null) {
            Toast.makeText(this, "Not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        String supplierID = firebaseUser.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("equipment");
        String id = ref.push().getKey();

        Equipment equipment = new Equipment(
                id,
                name,
                categoryId,
                supplierID,
                Double.parseDouble(price),
                Double.parseDouble(discount),
                description,
                imageUrl,
                location,
                0.0,
                FirebaseNodes.EquipmentStatus.AVAILABLE,
                false,
                0
        );

        ref.child(id).setValue(equipment)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Equipment added", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }

    private void showImage(String url) {
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder_avatar)
                .into(imgEquip);
    }

    private void fillCategory()
    {
        List<String> category = Arrays.asList(AppConstants.CurrentCategory.ALL, AppConstants.CurrentCategory.CONSTRUCTION, AppConstants.CurrentCategory.ELECTRONIC, AppConstants.CurrentCategory.OFFICE, AppConstants.CurrentCategory.VEHICLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                category
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setSelection(0);
    }

    private String getCategoryIdFromName(String categoryName) {
        switch (categoryName) {
            case AppConstants.CurrentCategory.VEHICLE:
                return "cat04";
            case AppConstants.CurrentCategory.OFFICE:
                return "cat01";
            case AppConstants.CurrentCategory.CONSTRUCTION:
                return "cat03";
            case AppConstants.CurrentCategory.ELECTRONIC:
                return "cat02";
            default:
                return "Unknown Category";
        }
    }
}