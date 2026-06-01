package com.group1.grabyourgear.utils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group1.grabyourgear.common.FirebaseNodes;
import com.group1.grabyourgear.models.Inquiry;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper_Inquiries {
    private static final DatabaseReference INQUIRY_REF =
            FirebaseDatabase.getInstance().getReference(FirebaseNodes.INQUIRIES);

    public interface InquiryListCallback {
        void onSuccess(List<Inquiry> inquiryList);
        void onFailure(Exception e);
    }

    public static void loadAllInquiries(InquiryListCallback callback) {
        INQUIRY_REF.get().addOnSuccessListener(snapshot -> {

            List<Inquiry> list = new ArrayList<>();
            for (DataSnapshot child : snapshot.getChildren()) {
                Inquiry item = child.getValue(Inquiry.class);
                if (item != null) {
                    item.setId(child.getKey());
                    list.add(item);
                }
            }

            callback.onSuccess(list);

        }).addOnFailureListener(callback::onFailure);
    }
}
