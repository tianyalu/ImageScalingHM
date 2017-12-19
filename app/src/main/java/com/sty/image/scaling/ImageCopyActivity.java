package com.sty.image.scaling;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class ImageCopyActivity extends AppCompatActivity{

    private ImageView ivSrc;
    private ImageView ivCopy;
    private ImageView ivRotate;
    private ImageView ivScale;
    private ImageView ivTranslate;
    private ImageView ivScaleTranslate;

    private Bitmap srcBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_copy);

        initViews();
    }

    private void initViews(){
        //1.找到要显示的控件
        ivSrc = findViewById(R.id.iv_src);
        ivCopy = findViewById(R.id.iv_copy);
        ivRotate = findViewById(R.id.iv_rotate);
        ivScale = findViewById(R.id.iv_scale);
        ivTranslate = findViewById(R.id.iv_translate);
        ivScaleTranslate = findViewById(R.id.iv_scale_translate);

        //2.把tomcat.png 转换成bitmap并显示到iv_src控件上
        srcBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.tomcat);
        //2.1 测试 修改原图 -->原图不可以修改
        //srcBitmap.setPixel(20, 30, Color.RED);
        ivSrc.setImageBitmap(srcBitmap);


        copyImage();
        rotateImage();
        scaleImage();
        translateImage();
        scaleTranslateImage();
    }

    private void copyImage(){
        //3.拷贝原图
        //3.1 创建模板
        Bitmap copyBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());
        //3.2 以copyBitmap为模板创建画布
        Canvas canvas = new Canvas(copyBitmap);
        //3.3 创建一个画笔
        Paint paint = new Paint();
        //3.4参考srcBitmap原图作画
        canvas.drawBitmap(srcBitmap, new Matrix(), paint);

        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++) {
                //该方法一次修改一个像素值
                copyBitmap.setPixel(20 + i, 30 + j, Color.RED);
            }
        }

        //4.把copyBitmap显示到iv_copy上
        ivCopy.setImageBitmap(copyBitmap);
    }

    private void rotateImage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int degrees = 0;
                for(int i = 0; i < 100; i++) {
                    //3.1 创建模板
                    final Bitmap copyBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());
                    //3.2 以copyBitmap为模板创建画布
                    Canvas canvas = new Canvas(copyBitmap);
                    //3.3 创建一个画笔
                    Paint paint = new Paint();
                    //3.4参考srcBitmap原图作画
                    Matrix matrix = new Matrix();

                    //4.对图片进行旋转
                    matrix.setRotate(degrees, srcBitmap.getWidth()/2, srcBitmap.getHeight()/2);
                    canvas.drawBitmap(srcBitmap, matrix, paint);

                    runOnUiThread(new Runnable() { //更新UI在主线程中执行
                        @Override
                        public void run() {
                            //5.把copyBitmap显示到iv_rotate上
                            ivRotate.setImageBitmap(copyBitmap);
                        }
                    });

                    SystemClock.sleep(1 * 1000);
                    degrees += 5;
                }
            }
        }).start();
    }

    private void scaleImage(){
        //3.1 创建模板
        Bitmap copyBitmap = Bitmap.createBitmap(srcBitmap.getWidth()/2, srcBitmap.getHeight()/2, srcBitmap.getConfig());
        //3.2 以copyBitmap为模板创建画布
        Canvas canvas = new Canvas(copyBitmap);
        //3.3 创建一个画笔
        Paint paint = new Paint();
        //3.4参考srcBitmap原图作画
        Matrix matrix = new Matrix();

        //4.对图片进行缩放处理
        matrix.setScale(0.5f, 0.5f);
        canvas.drawBitmap(srcBitmap, matrix, paint);

        //5.把copyBitmap显示到iv_scale上
        ivScale.setImageBitmap(copyBitmap);
    }

    private void translateImage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int dx = 0;
                for(int i = 0; i < 250; i++) {
                    //3.1 创建模板
                    final Bitmap copyBitmap = Bitmap.createBitmap(srcBitmap.getWidth() + 250, srcBitmap.getHeight(), srcBitmap.getConfig());
                    //3.2 以copyBitmap为模板创建画布
                    Canvas canvas = new Canvas(copyBitmap);
                    //3.3 创建一个画笔
                    Paint paint = new Paint();
                    //3.4参考srcBitmap原图作画
                    Matrix matrix = new Matrix();

                    //4.对图片进行位移
                    matrix.setTranslate(dx, 0);
                    canvas.drawBitmap(srcBitmap, matrix, paint);

                    runOnUiThread(new Runnable() { //更新UI在主线程中执行
                        @Override
                        public void run() {
                            //5.把copyBitmap显示到iv_rotate上
                            ivTranslate.setImageBitmap(copyBitmap);
                        }
                    });

                    SystemClock.sleep(10);
                    dx += 1 ;
                }
            }
        }).start();
    }

    /**
     * 做一个倒影的效果/类似的可以实现镜面效果
     */
    private void scaleTranslateImage(){
        //3.1 创建模板
        Bitmap copyBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());
        //3.2 以copyBitmap为模板创建画布
        Canvas canvas = new Canvas(copyBitmap);
        //3.3 创建一个画笔
        Paint paint = new Paint();
        //3.4参考srcBitmap原图作画
        Matrix matrix = new Matrix();

        //4.对图片进行缩放处理
        matrix.setScale(1.0f, -1.0f);
       // matrix.setTranslate(0, srcBitmap.getHeight());
        //post是在上一次修改的基础上进行修改，set每次都是新的变化，会覆盖上一次的修改
        matrix.postTranslate(0, srcBitmap.getHeight());
        canvas.drawBitmap(srcBitmap, matrix, paint);

        //5.把copyBitmap显示到iv_scale上
        ivScaleTranslate.setImageBitmap(copyBitmap);
    }
}
