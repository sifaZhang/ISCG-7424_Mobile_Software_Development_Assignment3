package com.group1.grabyourgear.common;

public class FirebaseNodes {

    public static final double FEATURED_DISCOUNT = 20;
    public static final String USERS = "users";
    public static class UserFields {
        public static final String UID = "uid";
        public static final String FULLNAME = "name";
        public static final String USERNAME = "username";
        public static final String EMAIL = "email";
        public static final String PHONE = "phone";
        public static final String ADDRESS = "address";
        public static final String ROLE = "role";
        public static final String AVATAR = "avatar";
        public static final String IS_APPROVED = "isApproved";
    }


    public static final String CATEGORIES = "categories";
    public static class CATEGORIESFields {
        public static final String ID = "ctId";
        public static final String NAME = "name";
    }

    public static final String EQUIPMENT = "equipment";
    public static class EquipmentFields {
        public static final String ID = "eqId";
        public static final String NAME = "name";
        public static final String CATEGORY_ID = "categoryId";
        public static final String SUPPLIER_ID = "supplierId";
        public static final String PRICE_PER_DAY = "pricePerDay";
        public static final String DISCOUNT = "discount";
        public static final String DESCRIPTION = "description";
        public static final String IMAGE_URL = "imageUrl";
        public static final String LOCATION = "location";
        public static final String RATING = "rating";
        public static final String STATUS = "status";
        public static final String IS_LOCKED = "isLocked";
    }

    public static class EquipmentStatus {
        public static final String AVAILABLE = "available";
        public static final String UNAVAILABLE = "unavailable";
        public static final String MAINTENANCE = "maintenance";
    }

    public static class BookingStatus {
        public static final String PENDING = "pending";       // 用户已下单，等待商家确认
        public static final String CONFIRMED = "confirmed";   // 商家已确认
        public static final String CANCELLED = "cancelled";   // 用户取消
        public static final String REJECTED = "rejected";     // 商家拒绝
        public static final String COMPLETED = "completed";   // 已完成租借
    }

    public static final String BOOKINGS = "bookings";

    public static class BookingsFields {
        public static final String ID = "id";
        public static final String EQUIPMENT_ID = "equipmentId";
        public static final String USER_ID = "userId";
        public static final String SUPPLIER_ID = "supplierId";
        public static final String START_DATE = "startDate";
        public static final String END_DATE = "endDate";
        public static final String TOTAL_PRICE = "totalPrice";
        public static final String STATUS = "status";
        public static final String TIMESTAMP = "timestamp";
    }



    public static final String FAQ = "faq";

    public static final String INQUIRIES = "inquiries";

    public static class InquiriesFields {
        public static final String ID = "id";
        public static final String CONTACT = "contact";
        public static final String DETAILS = "details";

    }

    public static final String FEEDBACK = "feedback";

    public static class FeedbackFields {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String PHONE = "phone";
        public static final String DESCRIPTION = "description";
    }
}
