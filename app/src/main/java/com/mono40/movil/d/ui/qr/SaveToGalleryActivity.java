package com.mono40.movil.d.ui.qr;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.mono40.movil.R;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SaveToGalleryActivity extends AppCompatActivity {

    public static final String EXTRA_BITMAP_URI = "EXTRA_BITMAP_URI";
    private static final int FILE_EXPORT_REQUEST_CODE = 900;
    Bitmap qrBitmap;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_to_gallery);

        imageUri = Uri.parse(getIntent().getStringExtra(EXTRA_BITMAP_URI));

        try {
            qrBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            openSaveToGallery();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openSaveToGallery() {
        Intent exportIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        exportIntent.addCategory(Intent.CATEGORY_OPENABLE);
        exportIntent.setType("image/png");
        String filename = "MyQrCode.png";
        exportIntent.putExtra(Intent.EXTRA_TITLE, filename);
        startActivityForResult(exportIntent, FILE_EXPORT_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("com.tpago.mobile", "OnActivityResult = ResultCode" + resultCode);
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }

        switch (requestCode) {
            case FILE_EXPORT_REQUEST_CODE:
                Log.d("com.tpago.mobile", "OnActivityResult = " + data);
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        Maybe.fromCallable(() -> {
                            try {
                                OutputStream os = this.getContentResolver().openOutputStream(uri);
                                if (os != null) {
                                    qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                                    os.flush();
                                    os.close();
                                }
                            } catch (IOException e) {
                                Log.e("com.tpago.mobile", e.getMessage(), e);
                            }
                            return null;
                        })
                                .doOnError(new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        Log.e("com.tpago.mobile", throwable.getMessage(), throwable);
                                    }
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe();
                    }
                }
                finish();
                break;
        }
    }
}
