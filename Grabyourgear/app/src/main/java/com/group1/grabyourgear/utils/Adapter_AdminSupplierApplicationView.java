package com.group1.grabyourgear.utils;

import android.content.Context;
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
import com.group1.grabyourgear.admin.AdminReviewSuppliersActivity;
import com.group1.grabyourgear.models.Users;

import java.util.List;

public class Adapter_AdminSupplierApplicationView extends RecyclerView.Adapter<Adapter_AdminSupplierApplicationView.AdminApplicationViewHolder> {
    private Context context;

    public List<Users> applicationsList;

    private OnApproveClickListener approveClickListener;
    private OnDenyClickListener denyClickListener;

    public interface OnApproveClickListener {
        void onApprove(Users application);
    }

    public interface OnDenyClickListener {
        void onDeny(Users application);
    }

    public Adapter_AdminSupplierApplicationView(Context context, List<Users> applicationsList,
                                                OnApproveClickListener approveClickListener,
                                                OnDenyClickListener denyClickListener)
    {
        this.context = context;
        this.applicationsList = applicationsList;
        this.approveClickListener = approveClickListener;
        this.denyClickListener = denyClickListener;
    }

    @NonNull
    @Override
    public AdminApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_supplier_application,
                parent, false);

        return new AdminApplicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminApplicationViewHolder holder, int position) {
        Users application = applicationsList.get(position);

        holder.tvName.setText(application.getName());
        holder.tvUserName.setText(application.getUsername());
        holder.tvEmail.setText(application.getEmail());
        holder.tvPhone.setText(application.getPhone());
        holder.tvAddress.setText(application.getAddress());

        // Load avatar
        Glide.with(context)
                .load(application.getAvatar())
                .placeholder(R.drawable.placeholder_avatar)
                .into(holder.imgAvatar);

        // Approve button
        holder.btnApprove.setOnClickListener(view -> {
            // Logic needs to be added, need to focus on just showing the list
            if (approveClickListener != null) {
                approveClickListener.onApprove(application);
            }
        });

        // Deny button
        holder.btnDeny.setOnClickListener(view -> {
            if (denyClickListener != null) {
                denyClickListener.onDeny(application);
            }
        });
    }

    @Override
    public int getItemCount() { return applicationsList.size(); }


    public static class AdminApplicationViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView tvStatus, tvName, tvUserName, tvEmail, tvPhone, tvAddress;
        Button btnApprove, btnDeny;

        public AdminApplicationViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAvatar = itemView.findViewById(R.id.img_appl_avatar);
            tvName = itemView.findViewById(R.id.tv_appl_name);
            tvUserName = itemView.findViewById(R.id.tv_appl_username);
            tvEmail = itemView.findViewById(R.id.tv_appl_email);
            tvPhone = itemView.findViewById(R.id.tv_appl_phone);
            tvAddress = itemView.findViewById(R.id.tv_appl_address);

            btnApprove = itemView.findViewById(R.id.btn_appl_approve);
            btnDeny = itemView.findViewById(R.id.btn_appl_deny);

        }
    }

    public void delete(int position){
        applicationsList.remove(position);
        notifyItemRemoved(position);
    }
}
