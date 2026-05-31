package com.group1.grabyourgear.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group1.grabyourgear.R;
import com.group1.grabyourgear.admin.AdminEditUserActivity;
import com.group1.grabyourgear.admin.AdminSupplierListActivity;
import com.group1.grabyourgear.models.Users;

import java.util.List;

public class Adapter_AdminSupplierView extends RecyclerView.Adapter<Adapter_AdminSupplierView.AdminSupplierViewHolder> {

    private Context context;

    private List<Users> supplierList;

    public Adapter_AdminSupplierView(Context context, List<Users> userList) {
        this.context = context;
        this.supplierList = userList;
    }

    @NonNull
    @Override
    public Adapter_AdminSupplierView.AdminSupplierViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_supplier,
                parent, false);

        return new Adapter_AdminSupplierView.AdminSupplierViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_AdminSupplierView.AdminSupplierViewHolder holder, int position) {
        Users user = supplierList.get(position);

        holder.tvName.setText(user.getName());
        holder.tvUserName.setText(user.getUsername());
        holder.tvEmail.setText(user.getEmail());
        holder.tvPhone.setText(user.getPhone());
        holder.tvAddress.setText(user.getAddress());

        Glide.with(context)
                .load(user.getAvatar())
                .placeholder(R.drawable.placeholder_avatar)
                .into(holder.imgAvatar);

        //TODO: Button handlers
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdminEditUserActivity.class);
                intent.putExtra("ROLE", user.getRole());
                intent.putExtra("UID", user.getUid());
                context.startActivity(intent);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Users user = supplierList.get(holder.getBindingAdapterPosition());

                FirebaseHelper_Users.updateApprovalStatus(user.getUid(), false,
                        new FirebaseHelper_Users.UpdateCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(context, "Supplier banned",
                                        Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(context, "Supplier could not be banned",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                notifyItemRemoved(holder.getBindingAdapterPosition());
                // This works for refreshing the adapter from an onClick in the viewholder.
                // Do what u gotta do.
                AdminSupplierListActivity activity = (AdminSupplierListActivity) context;
                activity.refreshAdapter();
            }
        });
    }

    @Override
    public int getItemCount() { return supplierList.size(); }

    public static class AdminSupplierViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;

        TextView tvName, tvUserName, tvEmail, tvPhone, tvAddress;

        Button btnEdit, btnDelete;

        public AdminSupplierViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAvatar = itemView.findViewById(R.id.img_admin_supp_avatar);
            tvName = itemView.findViewById(R.id.tv_admin_supp_name);
            tvUserName = itemView.findViewById(R.id.tv_admin_supp_username);
            tvEmail = itemView.findViewById(R.id.tv_admin_supp_email);
            tvPhone = itemView.findViewById(R.id.tv_admin_supp_phone);
            tvAddress = itemView.findViewById(R.id.tv_admin_supp_address);

            btnEdit = itemView.findViewById(R.id.btn_admin_supp_edit);
            btnDelete = itemView.findViewById(R.id.btn_admin_supp_delete);
        }
    }
}
