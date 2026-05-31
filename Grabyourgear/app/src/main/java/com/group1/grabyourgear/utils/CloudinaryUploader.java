package com.group1.grabyourgear.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.group1.grabyourgear.common.CloudinaryConfig;

import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;

public class CloudinaryUploader {

    private final Activity activity;
    private final ActivityResultLauncher<Intent> launcher;
    private UploadCallback callback;

    private final Cloudinary cloudinary;

    public CloudinaryUploader(Activity activity, ActivityResultLauncher<Intent> launcher) {
        this.activity = activity;
        this.launcher = launcher;

        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CloudinaryConfig.CLOUD_NAME,
                "api_key", CloudinaryConfig.API_KEY,
                "api_secret", CloudinaryConfig.API_SECRET
        ));
    }

    /** 选择图片 */
    public void pickImage(UploadCallback callback) {
        this.callback = callback;

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        launcher.launch(intent);
    }

    /** 新 API 的回调处理 */
    public void handleResult(Intent data) {
        if (data == null) return;

        Uri imageUri = data.getData();
        if (imageUri == null) {
            if (callback != null) {
                callback.onError(new Exception("No image selected"));
            }
            return;
        }

        uploadToCloudinary(imageUri);
    }

    /** 上传到 Cloudinary */
    private void uploadToCloudinary(Uri uri) {
        try {
            InputStream inputStream = activity.getContentResolver().openInputStream(uri);
            File tempFile = File.createTempFile("upload", ".jpg", activity.getCacheDir());
            FileOutputStream out = new FileOutputStream(tempFile);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }

            inputStream.close();
            out.close();

            new Thread(() -> {
                try {
                    var result = cloudinary.uploader().upload(tempFile, ObjectUtils.emptyMap());
                    String url = result.get("secure_url").toString();

                    activity.runOnUiThread(() -> callback.onSuccess(url));

                } catch (Exception e) {
                    activity.runOnUiThread(() -> callback.onError(e));
                }
            }).start();

        } catch (Exception e) {
            callback.onError(e);
        }
    }
}
