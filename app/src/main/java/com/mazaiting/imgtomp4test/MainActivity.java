package com.mazaiting.imgtomp4test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.googlecode.javacv.FFmpegFrameRecorder;
import com.googlecode.javacv.FrameRecorder;
import com.googlecode.javacv.cpp.opencv_core;

import java.io.File;

import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;

public class MainActivity extends AppCompatActivity {
    public static final String IMAGE_TYPE = ".png";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void start(View view) {
        imageToMp4();
    }

    /**
     * 图片转视频
     */
    private void imageToMp4() {
        // 生成的新文件名
        String newFileName = "test_" + System.currentTimeMillis() + ".mp4";
        // 保存的路径
        String temp = null;
        final double frameRate = 5;
        try {
            temp = new FileUtils().getSDCardRoot() + "ScreenRecord"
                    + File.separator + newFileName;
        } catch (FileUtils.NoSdcardException e1) {
            e1.printStackTrace();
        }
        final String savePath = temp;
        new Thread() {
            @Override
            public void run() {
                Log.d("test", "开始将图片转成视频啦...frameRate=" + frameRate);
                try {
                    // 临时文件
                    String tempFilePath = new FileUtils().getSDCardRoot()
                            + "ScreenRecord/temp" + File.separator;
                    Log.i("test", "tempFilePath=" + tempFilePath);
                    Bitmap testBitmap = BitmapFactory.decodeFile(tempFilePath
                            + "head1" + MainActivity.IMAGE_TYPE);
                    FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(
                            savePath, testBitmap.getWidth(),
                            testBitmap.getHeight());

                    recorder.setFormat("mp4");
                    recorder.setFrameRate(frameRate);// 录像帧率
                    recorder.start();

                    int index = 0;
                    while (index < 8) {
                        opencv_core.IplImage image = cvLoadImage(tempFilePath
                                + "head" + index
                                + MainActivity.IMAGE_TYPE);
                        recorder.record(image);
                        index++;
                    }
                    Log.d("test", "录制完成....");
                    recorder.stop();
                } catch (FileUtils.NoSdcardException | FrameRecorder.Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }



}
