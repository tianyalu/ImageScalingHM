package com.sty.image.scaling;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnLoadImage;
    private Button btnImageCopyActivity;
    private ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setListeners();
    }

    private void initViews(){
        btnLoadImage = findViewById(R.id.btn_load_image);
        btnImageCopyActivity = findViewById(R.id.btn_image_copy_activity);
        ivImage = findViewById(R.id.iv_image);
    }

    private void setListeners(){
        btnLoadImage.setOnClickListener(this);
        btnImageCopyActivity.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_load_image:
                loadAndScaleImage();
                break;
            case R.id.btn_image_copy_activity:
                Intent intent = new Intent(MainActivity.this, ImageCopyActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void loadAndScaleImage(){
        //1.获取手机分辨率 获取windowManager实例
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        int screenHeight = wm.getDefaultDisplay().getHeight();
        Log.i("Tag", "手机屏幕 width:" + screenWidth + "  height:" + screenHeight);

//        Point point = new Point();
//        wm.getDefaultDisplay().getSize(point);
//        Log.i("Tag", "手机屏幕 width:" + point.x + "  height:" + point.y);

        //2.把dog.jpg转换成bitmap
        //创建bitmap工厂的配置参数
        BitmapFactory.Options options = new BitmapFactory.Options();
        //返回一个null 没有bitmap 不去真正解析位图，但是能返回图片的一些新信息（宽高）
        options.inJustDecodeBounds = true;
        String fileStr = Environment.getExternalStorageDirectory() + "/sty/dog.jpg";
        Log.i("Tag", "file:" + fileStr);
        Bitmap bitmap = BitmapFactory.decodeFile(fileStr, options);

        //3.获取图片的宽和高
        int imgWidth = options.outWidth;
        int imgHeight = options.outHeight;
        Log.i("Tag", "图片的width：" + imgWidth + "  height:" + imgHeight);

        //4.计算缩放比
        int scale = 1;
        int scaleX = imgWidth / screenWidth;
        int scaleY = imgHeight / screenHeight;
        if(scaleX > scaleY && scaleX > scale){
            scale = scaleX;
        }
        if(scaleY > scaleX && scaleY > scale){
            scale = scaleY;
        }
        Log.i("Tag", "缩放比：" + scale);

        //5.根据缩放比显示图片
        options.inSampleSize = scale;

        //6.开始真正地解析位图
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(fileStr, options);

        //7.把bitmap显示到控件上
        ivImage.setImageBitmap(bitmap);
    }
}
