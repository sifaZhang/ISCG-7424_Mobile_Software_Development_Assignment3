package com.group1.grabyourgear.supplier;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group1.grabyourgear.R;
import com.group1.grabyourgear.common.AppConstants;
import com.group1.grabyourgear.common.FirebaseNodes;
import com.group1.grabyourgear.models.Equipment;
import com.group1.grabyourgear.utils.BaseActivity;
import com.group1.grabyourgear.utils.CategoryRepository;

import java.text.DecimalFormat;

public class SupplierEquipmentDetailActivity extends BaseActivity {

    private ImageView imgProduct;
    private TextView tvTitle, tvRating, tvLocation, tvCategory, tvPrice, tvDiscount, tvStatus, tvDescription;
    private Button btnPrimary, btnSecondary, btnBack, btnComplete;

    private String equipmentId, mode, bookingId, bookingStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_supplier_equipment_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setHeaderTitle("Equipment Details");

        imgProduct = findViewById(R.id.imgProductSupplierDetail);
        tvTitle = findViewById(R.id.tvTitleSupplierDetail);
        tvRating = findViewById(R.id.tvRatingSupplierDetail);
        tvLocation = findViewById(R.id.tvLocationSupplierDetail);
        tvCategory = findViewById(R.id.tvCategorySupplierDetail);
        tvPrice = findViewById(R.id.tvPriceSupplierDetail);
        tvDiscount = findViewById(R.id.tvDiscountSupplierDetail);
        tvStatus = findViewById(R.id.tvStatusSupplierDetail);
        tvDescription = findViewById(R.id.tvDescriptionSupplierDetail);

        btnPrimary = findViewById(R.id.btnPrimaryDetail);
        btnSecondary = findViewById(R.id.btnSecondaryDetail);
        btnBack = findViewById(R.id.btnBackDetail);
        btnComplete = findViewById(R.id.btnComplete);
        btnComplete.setVisibility(View.GONE);

        equipmentId = getIntent().getStringExtra(AppConstants.IntenParamer.EQUIPMENT_ID);
        mode = getIntent().getStringExtra("mode");
        bookingId = getIntent().getStringExtra("bookingId");
        bookingStatus = getIntent().getStringExtra("bookingStatus");

        if(equipmentId == null || equipmentId.isEmpty()) {
            Toast.makeText(this, "No equipment selected.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBookingStatus(FirebaseNodes.BookingStatus.COMPLETED);
            }
        });

        loadEquipmentDetails();
        setupButtons();
    }

    private void loadEquipmentDetails() {
        FirebaseDatabase.getInstance()
                .getReference(FirebaseNodes.EQUIPMENT)
                .child(equipmentId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Equipment equipment = snapshot.getValue(Equipment.class);

                        if(equipment == null) {
                            Toast.makeText(SupplierEquipmentDetailActivity.this,
                                    "Equipment not found",
                                    Toast.LENGTH_SHORT)
                                    .show();
                            finish();
                            return;
                        }

                        DecimalFormat df = new DecimalFormat("0.00");

                        tvTitle.setText(equipment.getName());
                        tvRating.setText(Html.fromHtml("<b>⭐ Rating:</b> " + equipment.getRating() + "/5", Html.FROM_HTML_MODE_LEGACY));
                        tvLocation.setText(Html.fromHtml("<b>📍 Location:</b> " + equipment.getLocation(), Html.FROM_HTML_MODE_LEGACY));
                        tvCategory.setText(Html.fromHtml("<b>🪴 Category:</b> " + CategoryRepository.getInstance().getCategoryName(equipment.getCategoryId()), Html.FROM_HTML_MODE_LEGACY));
                        tvPrice.setText(Html.fromHtml("<b>💰 Price:</b> " + df.format(equipment.getPricePerDay()) + " / day", Html.FROM_HTML_MODE_LEGACY));
                        tvDiscount.setText(Html.fromHtml("<b>💵 Discount:</b> " + Math.round(equipment.getDiscount()) + "% OFF", Html.FROM_HTML_MODE_LEGACY));
                        if("equipment".equals(mode)) {
                            tvStatus.setText(Html.fromHtml("<b>📣 Status:</b> " + equipment.getStatus(), Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            tvStatus.setText(Html.fromHtml("<b>📣 Status:</b> " + bookingStatus, Html.FROM_HTML_MODE_LEGACY));
                        }
                        tvDescription.setText(equipment.getDescription());

                        Glide.with(SupplierEquipmentDetailActivity.this)
                                .load(equipment.getImageUrl())
                                .placeholder(R.drawable.placeholder_general)
                                .into(imgProduct);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SupplierEquipmentDetailActivity.this,
                                "Failed to load equipment",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    private void setupButtons() {
        if("booking".equals(mode)) {
            setupBookingButtons();
        } else {
            setupEquipmentButtons();
        }
    }

    private void setupEquipmentButtons() {
        btnPrimary.setVisibility(View.VISIBLE);
        btnSecondary.setVisibility(View.VISIBLE);

        btnPrimary.setText("Edit");
        btnSecondary.setText("Delete");

        btnPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SupplierEditEquipmentActivity.class);
                intent.putExtra(AppConstants.IntenParamer.EQUIPMENT_ID, equipmentId);
                startActivity(intent);
            }
        });

        btnSecondary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(SupplierEquipmentDetailActivity.this)
                        .setTitle("Delete Equipment")
                        .setMessage("Are you sure you want to delete this equipment?")
                        .setPositiveButton("Delete", (dialog, which) -> {

                            FirebaseDatabase.getInstance()
                                    .getReference(FirebaseNodes.EQUIPMENT)
                                    .child(equipmentId)
                                    .removeValue()
                                    .addOnSuccessListener(unused -> {
                                        deleteRelatedBookings(equipmentId, () -> {
                                            Toast.makeText(
                                                    SupplierEquipmentDetailActivity.this,
                                                    "Equipment and related bookings deleted",
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                            finish();
                                        });
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(SupplierEquipmentDetailActivity.this, "Delete failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            }
        });
    }
    private void deleteRelatedBookings(String equipmentId, Runnable onComplete) {
        FirebaseDatabase.getInstance()
                .getReference(FirebaseNodes.BOOKINGS)
                .get()
                .addOnSuccessListener(dataSnapshot -> {
                    for(DataSnapshot bookingSnapshot : dataSnapshot.getChildren()) {
                        String bookingEquipmentId = bookingSnapshot
                                .child(FirebaseNodes.BookingsFields.EQUIPMENT_ID)
                                .getValue(String.class);

                        if(equipmentId.equals(bookingEquipmentId)) {
                            bookingSnapshot.getRef().removeValue();
                        }
                    }

                    onComplete.run();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(
                            SupplierEquipmentDetailActivity.this,
                            "Equipment deleted, but bookings could not be checked.",
                            Toast.LENGTH_SHORT
                    ).show();

                    onComplete.run();
                });
    }

    private void setupBookingButtons() {
        boolean isPending = FirebaseNodes.BookingStatus.PENDING.equalsIgnoreCase(bookingStatus);
        boolean isConfirmed = FirebaseNodes.BookingStatus.CONFIRMED.equalsIgnoreCase(bookingStatus);

        if (isConfirmed){
            //confirmed
            btnComplete.setVisibility(View.VISIBLE);
        }

        if(!isPending) {
            btnPrimary.setVisibility(View.GONE);
            btnSecondary.setVisibility(View.GONE);
            return;
        }

        //pending
        btnPrimary.setVisibility(View.VISIBLE);
        btnSecondary.setVisibility(View.VISIBLE);

        btnPrimary.setText("Confirm");
        btnSecondary.setText("Reject");

        btnPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBookingStatus(FirebaseNodes.BookingStatus.CONFIRMED);
            }
        });

        btnSecondary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBookingStatus(FirebaseNodes.BookingStatus.REJECTED);
            }
        });
    }

    private void updateBookingStatus(String newStatus) {
        if(bookingId == null || bookingId.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Booking ID is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseDatabase.getInstance()
                .getReference(FirebaseNodes.BOOKINGS)
                .child(bookingId)
                .child(FirebaseNodes.BookingsFields.STATUS)
                .setValue(newStatus)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getApplicationContext(), "Booking " + newStatus, Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(equipmentId != null && !equipmentId.isEmpty()) {
            loadEquipmentDetails();
        }
    }
}