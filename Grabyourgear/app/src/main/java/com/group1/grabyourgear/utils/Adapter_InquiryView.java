package com.group1.grabyourgear.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group1.grabyourgear.R;
import com.group1.grabyourgear.models.Inquiry;

import java.util.List;

public class Adapter_InquiryView extends RecyclerView.Adapter<Adapter_InquiryView.InquiryViewHolder>{
    private List<Inquiry> inquiryList;
    private Context context;

    public Adapter_InquiryView(List<Inquiry> inquiryList, Context context){
        this.inquiryList = inquiryList;
        this.context = context;
    }

    @NonNull
    @Override
    public InquiryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_inquiry, parent, false);
        return new InquiryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InquiryViewHolder holder, int position) {
        Inquiry item = inquiryList.get(position);

        holder.tvInquiryContact.setText(item.getContact());
        holder.tvInquiryDetails.setText(item.getDetails());
    }

    @Override
    public int getItemCount() {
        return inquiryList.size();
    }

    public static class InquiryViewHolder extends RecyclerView.ViewHolder{
        TextView tvInquiryContact, tvInquiryDetails;

        public InquiryViewHolder(@NonNull View itemView){
            super(itemView);

            tvInquiryContact = itemView.findViewById(R.id.tvInquiryContact);
            tvInquiryDetails = itemView.findViewById(R.id.tvInquiryDetails);
        }
    }
}
