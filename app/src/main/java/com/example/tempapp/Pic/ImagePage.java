package com.example.tempapp.Pic;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.example.tempapp.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/* 이미지 촬영 & S3 전송 */



public class ImagePage extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "ImagePage";

    CognitoCachingCredentialsProvider credentialsProvider;
    AmazonS3 s3;
    TransferUtility transferUtility;

    Button uploadBtn, selectBtn;
    ImageView imageview;
    File f;

    private String userChoosenTask;
    Uri imageUri;
    String imagePath;
    private Uri mImageUri;
    private int PICTURE_CHOICE = 1;
    private int REQUEST_CAMERA = 2;
    static final int SELECT_FILE = 3;

    String mCurrentPhotoPath;
    final static int TAKE_PICTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    String userId = com.example.tempapp.Login.LoginActivity.etId.getText().toString();      // 사용자 id


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        uploadBtn = (Button) findViewById(R.id.uploadBtn);
        selectBtn = (Button) findViewById(R.id.selectBtn);
        imageview = (ImageView) findViewById(R.id.iv_result);

        uploadBtn.setOnClickListener(this);
        selectBtn.setOnClickListener(this);


        // Amazon Cognito 인증 공급자를 초기화합니다
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-northeast-2:935db374-9c46-4a29-96ad-e90a9313fc39", // 자격 증명 풀 ID
                Regions.AP_NORTHEAST_2 // 리전
        );

        s3 = new AmazonS3Client(credentialsProvider);

        transferUtility = new TransferUtility(s3, getApplicationContext());
        s3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        s3.setEndpoint("s3.ap-northeast-2.amazonaws.com");

        transferUtility = new TransferUtility(s3, getApplicationContext());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 서버전송 버튼 클릭
            case R.id.uploadBtn:
                if (f != null)  {
                    TransferObserver observer = transferUtility.upload(
                            "test-pic-bow",
                            f.getName(),
                            f
                    );
                    // php 연결
                    String link = "http://52.78.99.175/image.php";
                    PHPRequest_pic request = new PHPRequest_pic(link);
                    request.PHPTest(userId, f.getName());

                    Toast.makeText(this, "Complete!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Select a image", Toast.LENGTH_LONG).show();
                }
                break;

            // 이미지 선택 버튼 클릭
            case R.id.selectBtn:
                selectImage();
                break;
        }
    }
    /* 이미지 선택 버튼 클릭 */
    private void selectImage() {

        Log.d(TAG, "select Image");
        final CharSequence[] items = {"촬영하기", "사진 가져오기",
                "취소"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("사진가져오기");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(ImagePage.this);

                if (items[item].equals("촬영하기")) {
                    userChoosenTask = "촬영하기";
                    if (result)
                        dispatchTakePictureIntent();


                } else if (items[item].equals("사진 가져오기")) {
                    userChoosenTask = "사진 가져오기";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("취소")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    /* 갤러리에서 이미지 선택 */
    private void galleryIntent() {
        Log.d(TAG, "Gallery Intent");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    /* 사진 촬영 */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                f = createImageFile();  // 이미지파일 생성
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.v(TAG, "Can't create file to take picture!");
                Toast.makeText(this, "Failed to take a picture.", Toast.LENGTH_SHORT);
            }
            // Continue only if the File was successfully created
            if (f != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.tempapp.fileprovider",
                        f);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    /* 이미지파일 생성 */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        try {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO: {
                    if (resultCode == RESULT_OK) {
                        File file = new File(mCurrentPhotoPath);
                        Bitmap bitmap = MediaStore.Images.Media
                                .getBitmap(getContentResolver(), Uri.fromFile(file));
                        if (bitmap != null) {
                            ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
                            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                    ExifInterface.ORIENTATION_UNDEFINED);

                            Bitmap rotatedBitmap = null;
                            switch (orientation) {
                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    rotatedBitmap = rotateImage(bitmap, 90);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    rotatedBitmap = rotateImage(bitmap, 180);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    rotatedBitmap = rotateImage(bitmap, 270);
                                    break;

                                case ExifInterface.ORIENTATION_NORMAL:
                                default:
                                    rotatedBitmap = bitmap;

                            }
                            imageview.setImageBitmap(rotatedBitmap);
                        }
                    }
                    break;
                }
                case SELECT_FILE: {
                    Log.d(TAG, "onActivityResult, SELECT_FILE");
                    onSelectFromGalleryResult(intent, SELECT_FILE);
                }
            }

        } catch (Exception error) {
            error.printStackTrace();
        }
    }


    // 이미지 선택
    private void onSelectFromGalleryResult(Intent data, int imagetype) {

        Log.d(TAG, "onSelectFromGalleryResult");
        Bitmap bm = null;
        imageUri = data.getData();
        if (Build.VERSION.SDK_INT < 11) {
            imagePath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, imageUri);
            Log.d(TAG, Build.VERSION.SDK_INT + "");
        } else if (Build.VERSION.SDK_INT < 19) {
            Log.d(TAG, Build.VERSION.SDK_INT + "");
            imagePath = RealPathUtil.getRealPathFromURI_API11to18(this, imageUri);
        } else {
            Log.d(TAG, Build.VERSION.SDK_INT + "");
            imagePath = RealPathUtil.getRealPathFromURI_API19(this, imageUri);
        }
        Log.d(TAG, imagePath);


        try {
            bm = getResizedBitmap(decodeUri(data.getData()), getResources().getDimensionPixelSize(R.dimen.idcard_pic_height), getResources().getDimensionPixelSize(R.dimen.idcard_pic_width));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("오류");
        }

        if (imagetype == SELECT_FILE) {
            f = new File(imagePath);
            imageview.setImageBitmap(bm);
        }
    }


    public static Bitmap getResizedBitmap(Bitmap image, int newHeight, int newWidth) {
        int width = image.getWidth();
        int height = image.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        if (Build.VERSION.SDK_INT <= 19) {
            matrix.postRotate(90);
        }
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;
    }


    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                this.getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(
                this.getContentResolver().openInputStream(selectedImage), null, o2);
    }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


}
