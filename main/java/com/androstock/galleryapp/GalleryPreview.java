package com.androstock.galleryapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * Created by SHAJIB on 25/12/2015.
 */
public class GalleryPreview extends AppCompatActivity {

    public static final String TAG = GalleryPreview.class.getSimpleName();
    public static boolean fin = false;
    ImageView GalleryPreviewImg, shareImage_img, deleteImage_img;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.gallery_preview);
        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        GalleryPreviewImg = (ImageView) findViewById(R.id.GalleryPreviewImg);
        shareImage_img = (ImageView) findViewById(R.id.img_share_image);
        deleteImage_img = (ImageView) findViewById(R.id.img_delete_image);
        Glide.with(GalleryPreview.this)
                .load(new File(path)) // Uri of the picture
                .into(GalleryPreviewImg);

        shareImage_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "shareImageClicked...");
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/.*");
                Log.e(TAG, "path for selected image" + path);
                Uri uri = Uri.fromFile(new File(path));
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(intent, "Share Image!"));
            }
        });

        deleteImage_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "deleteImageClicked....");
                File filedelete = new File(path);
                if (filedelete.exists()) {
                    if (filedelete.delete()) {
                        Log.e(TAG, "Image is Deleted...");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            final Uri contentUri = Uri.fromFile(new File(path));
                            scanIntent.setData(contentUri);
                            sendBroadcast(scanIntent);
                        } else {
                            final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
                            sendBroadcast(intent);
                        }
                        finish();
                        fin = true;
                    } else {
                        Log.e(TAG, "Image is not Deleted...");
                    }
                }
            }
        });
    }
}
