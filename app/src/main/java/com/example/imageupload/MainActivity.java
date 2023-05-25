package com.example.imageupload;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.imageupload.db.DbManager;
import com.example.imageupload.utils.CommonUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button btnImageUpload;

    private ImageView image_iv;
    private String mCurrentPhotoPath, timeStamp, imageFileName;

    private ArrayList<String> imageNameList = new ArrayList<String>();;
    private ArrayList<Bitmap> imageList = new ArrayList<Bitmap>();

    private final static int REQUEST_TAKE_PHOTO = 1;

    private final int MY_PERMISSION_REQUEST = 1000;
    private static final int DELETE_IMAGE_ALERT_DIALOG = 900;

    private DbManager myDB;

    private Cursor cursor;

    private Gallery gallery;
    private ImageAdapter adapter;
    private TypedArray typedArray;

    private int imgPosition;

    private final static String IMAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Download";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image_iv = findViewById(R.id.image_iv);
        btnImageUpload = findViewById(R.id.btnImageUpload);
        btnImageUpload.setOnClickListener(mClickListener);

        adapter = new ImageAdapter(getBaseContext());

        gallery = (Gallery) findViewById(R.id.gallery_g);
        gallery.setOnItemClickListener(galleryListener);
        gallery.setOnItemLongClickListener(gLongClickListener);

        myDB = DbManager.getInstance(this);

        checkPermission();

        ImageGallay();

    }

    private final View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();

            if(id == R.id.btnImageUpload){

                dispatchTakePictureIntent();

            }
        }
    };

    private void checkPermission(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES}, MY_PERMISSION_REQUEST);
            } else {

            }
        } else {
            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_REQUEST:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "권한 허용", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        try {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO:

                    insertImage();
                    break;
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    private void ImageGallay() {

        if (imageNameList.size() > 0) {
            imageNameList.clear();
        }
        if (imageList.size() > 0) {
            imageList.clear();
        }

        /*cursor = myDB.selectQuery(CommonUtils.getRawQuery(getBaseContext(), R.raw.select_image), new String[]{
                CommonUtils.fixNull(getIntent().getStringExtra("ASSET_NO"), "")
        });*/

        cursor = myDB.selectQuery(CommonUtils.getRawQuery(getBaseContext(), R.raw.select_image), "Test");

        while (cursor.moveToNext()) {

        @SuppressLint("Range")
        String imageName = CommonUtils.fixNull(cursor.getString(cursor.getColumnIndex("ORIGNL_FILE_NM")), "");
        Log.i("FileImageName", ""+imageName);
        if (!CommonUtils.isNull(imageName)) {
            String path = (IMAGE_PATH + "/" + imageName).trim();
            Log.i("FilePath", ""+path);
            if (CommonUtils.existFile(path)) {
                imageNameList.add(imageName);
            }
        }
    }

        if (imageNameList.size() > 0) {
            for (String name : imageNameList) {
                String path = (IMAGE_PATH + "/" + name).trim();
                Bitmap b = BitmapFactory.decodeFile(path);
                Log.i("FileBitmap", ""+b);
                imageList.add(b);
            }
        }

        if(imageList.size() > 0){
            setMainImage(imageList.get(0));

        }
        gallery.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void setMainImage(Bitmap bitmap) {
        image_iv.setImageBitmap(bitmap);
    }

    private class ImageAdapter extends BaseAdapter {

        int mGalleryItemBackground;
        private Context context;

        public ImageAdapter(Context context) {
            this.context = context;

            typedArray = context.obtainStyledAttributes(R.styleable.GalleryTheme);
            mGalleryItemBackground = typedArray.getResourceId(R.styleable.GalleryTheme_android_galleryItemBackground, 0);
            typedArray.recycle();
        }

        public int getCount() {
            return imageList.size();
        }

        public Object getItem(int pos) {
            return pos;
        }

        public long getItemId(int pos) {
            return pos;
        }

        public View getView(int pos, View cv, ViewGroup parent) {
            ImageView iv = new ImageView(context);

            iv.setImageBitmap(imageList.get(pos));
            iv.setLayoutParams(new Gallery.LayoutParams(185, 130));
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setBackgroundResource(mGalleryItemBackground);

            return iv;
        }

    }

    private AdapterView.OnItemClickListener galleryListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            setMainImage(imageList.get(position));
        }
    };

    private AdapterView.OnItemLongClickListener gLongClickListener = new AdapterView.OnItemLongClickListener() {
        public boolean onItemLongClick(AdapterView parent, View v, int position, long id) {

            imgPosition = position;

            showDialog(DELETE_IMAGE_ALERT_DIALOG);

            return false;
        };
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder b = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        b.setTitle("Image Delete");

        switch (id) {
            case DELETE_IMAGE_ALERT_DIALOG:
                b.setMessage("\n사진을 삭제하시겠습니까?\n");
                b.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteImage(imageNameList.get(imgPosition));
                    }
                });
                b.setNegativeButton("닫기", null);
                return b.create();
        }
        return super.onCreateDialog(id);
    }

    private void deleteImage(String image) {
        String uploadDiv = "";

        Cursor cursor = myDB.selectQuery(CommonUtils.getRawQuery(getBaseContext(), R.raw.select_image_upload_div), new String[] {
                image
        });
        while (cursor.moveToNext()) {
            uploadDiv = CommonUtils.fixNull(cursor.getString(0), "");
        }
        myDB.updateQuery(CommonUtils.getRawQuery(getBaseContext(), R.raw.update_delete_image), new String[] {
                "N",
                "Y",
                CommonUtils.fixNull(image, "")
        });

        refresh();

    }

    private void insertImage(){

        try {
            File file = new File(IMAGE_PATH, imageFileName);

            Bitmap bitmap;

            if (android.os.Build.VERSION.SDK_INT >= 29) {
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), Uri.fromFile(file));
                try {
                    bitmap = ImageDecoder.decodeBitmap(source);
                    if (bitmap != null) {
                        setMainImage(bitmap);
                        OutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {

        }

        /*myDB.insertData("Test", timeStamp, "", imageFileName, imageFileName,
                        "jpg", "Y", "Y");*/

        myDB.updateQuery(CommonUtils.getRawQuery(getBaseContext(), R.raw.insert_image), new String[] {
                "Test",
                timeStamp,
                "",
                imageFileName,
                imageFileName,
                "jpg",
                "Y",
                "Y"
        });

        refresh();
    }

    private File createImageFile() throws IOException {
        timeStamp = getCurrentDateTime();
        imageFileName = "Test" + "_" + timeStamp + ".jpg";

        File file = new File(IMAGE_PATH, imageFileName);

        mCurrentPhotoPath = file.getAbsolutePath();
        Log.i("PhotoUri", ""+mCurrentPhotoPath);

        return file;
    }

    public static String getCurrentDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
        return sdf.format(new Date());
    }

    // 카메라 인텐트 실행하는 부분
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, getApplication().getPackageName(), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }

        }
    }

    private void refresh() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

}