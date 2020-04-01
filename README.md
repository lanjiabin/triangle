# Android自定义进度条显示音量

<a name="eaexi"></a>
# 一、效果：
![音量.gif](https://cdn.nlark.com/yuque/0/2020/gif/292956/1585705743328-07a1453f-b0d7-4214-9e5d-976d9add3920.gif#align=left&display=inline&height=402&name=%E9%9F%B3%E9%87%8F.gif&originHeight=402&originWidth=256&size=188287&status=done&style=none&width=256)<br />

<a name="WlMpV"></a>
# 二、代码地址
[triangle](https://github.com/lanjiabin/triangle)
<a name="DjPUK"></a>
# 三、自定义view
Triangle.java
```java
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class Triangle extends View {
    private Paint mRadarPaint, mValuePaint;
    private int mCenterX; //中心X
    private int mCenterY; //中心Y
    private Path mLinePath; //外部容器的形状
    private Path mValuePath; //内部填充的形状
    private double mPercent;

    public Triangle(Context context) {
        super(context);
    }

    public Triangle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Triangle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // 中心坐标
        mCenterX = w / 2;
        mCenterY = h / 2;
        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setPercent(double percent){
        mPercent=percent;
        init();
        invalidate();
    }

    public void init() {

        // 绘制外部容器
        mRadarPaint = new Paint();
        mRadarPaint.setAntiAlias(true);
        mRadarPaint.setStrokeWidth(2);
        mRadarPaint.setStyle(Paint.Style.STROKE);
        mRadarPaint.setColor(Color.BLACK);

        // 绘制填充内容
        mValuePaint = new Paint();
        mValuePaint.setStrokeWidth(2);
        mValuePaint.setColor(Color.BLUE);
        mValuePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        //外部容器路径
        mLinePath = new Path();

        //内部填充物路径
        mValuePath = new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制外部容器
        drawLines(canvas);

        //绘制内部填充
        drawRegion(canvas, mPercent);
    }

    //绘制外部容器，顺时针 o-a-b-c
    public void drawLines(Canvas canvas) {
        mLinePath.reset();

        float xo = mCenterX;
        float yo = mCenterY;
        mLinePath.moveTo(xo, yo);

        float xa = (float) (mCenterX + 160);
        float ya = (float) (mCenterY);
        mLinePath.lineTo(xa, ya);

        float xb = (float) (mCenterX + 160);
        float yb = (float) (mCenterY - 80);
        mLinePath.lineTo(xb, yb);

        float xc = (float) (mCenterX);
        float yc = (float) (mCenterY - 15);
        mLinePath.lineTo(xc, yc);

        mLinePath.close();
        canvas.drawPath(mLinePath, mRadarPaint);

    }


    //绘制覆盖图层，顺时针 O-Q-P-C
    public void drawRegion(Canvas canvas, double Percent) {

        //直线CB 与QH 求出交点坐标P,
        // 其中H点=(是以B点的Y坐标，Q点的X坐标)，其实就是QP的延长线。
        double lineCX = mCenterX;
        double lineCY = mCenterY - 15;

        double lineBX = mCenterX + 160;
        double lineBY = mCenterY - 80;

        double lineQX = Percent * 160 + mCenterX;
        double lineQY = mCenterY;

        double lineHX = lineQX;
        double lineHY = lineBY;

        //求出交点P的坐标
        double[] result = reckon(lineCX, lineCY, lineBX, lineBY, lineQX, lineQY, lineHX, lineHY);

        double px;
        double py;

        px = result[0];
        py = result[1];


        float xo = mCenterX;
        float yo = mCenterY;

        float xq = (float) lineQX;
        float yq = (float) (mCenterY);

        float xp = (float) px;
        float yp = (float) py;

        float xc = (float) lineCX;
        float yc = (float) lineCY;

        mValuePath.moveTo(xo, yo);
        mValuePath.lineTo(xq, yq);
        mValuePath.lineTo(xp, yp);
        mValuePath.lineTo(xc, yc);

        mValuePath.close();
        canvas.drawPath(mValuePath, mValuePaint);

    }

    //求出两直线相交的坐标
    public double[] reckon(double x0, double y0,
                           double x1, double y1,
                           double x2, double y2,
                           double x3, double y3) {
        double[] result = new double[2];
        double a, b, c, d, e, f;

        a = (y0 - y1) * (y3 - y2) * x0;
        b = (y3 - y2) * (x1 - x0) * y0;
        c = (y1 - y0) * (y3 - y2) * x2;
        d = (x2 - x3) * (y1 - y0) * y2;
        e = (x1 - x0) * (y3 - y2);
        f = (y0 - y1) * (x3 - x2);

        double resultY = (a + b + c + d) / (e + f);
        double resultX = x2 + (x3 - x2) * (resultY - y2) / (y3 - y2);

        result[0] = resultX;
        result[1] = resultY;

        return result;
    }
}

```

<br />

<a name="nQ7vG"></a>
# 四、界面使用


```java
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".MainActivity">
    <com.lanjiabin.triangle.Triangle
        android:id="@+id/triangle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />

</LinearLayout>
```

<br />

<a name="TEJLp"></a>
# 五、Activity使用
```java
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;

public class MainActivity extends Activity {
    private Triangle mTriangle;
    private AudioManager mAudioManager;
    VolumeReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public void initView() {
        setContentView(R.layout.activity_main);
        mTriangle = findViewById(R.id.triangle);

        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        double percent = (double) currentVolume / maxVolume;

        mTriangle.setPercent(percent);

        mReceiver = new VolumeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        this.registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private class VolumeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
                mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
                int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                double percent = (double) currentVolume / maxVolume;
                mTriangle.setPercent(percent);
            }
        }
    }
}

```

<br />

