package com.example.alticast.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class animantionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animantion);

        // ThreadTemp thread = new ThreadTemp();
        TextView t1 = (TextView) findViewById(R.id.one);
        TextView t2 = (TextView) findViewById(R.id.two);
        TextView t3 = (TextView) findViewById(R.id.three);
        TextView t4 = new TextView(this);
        t4.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.animation);
        t4.setText("rrrrrrrrrr");
        t4.setTextSize(100);
        t1.setText("one");
        t2.setText("two");
        t3.setText("Three");
        layout.addView(t4);
        TranslateAnimation anim = new TranslateAnimation(t1.getX(), t1.getX() + 1000, t1.getY(), t1.getY() + 1000);
        anim.setFillAfter(true);
        anim.setDuration(5000);
        t4.startAnimation(anim);
        t1.startAnimation(anim);
        t2.startAnimation(anim);
        // platy();

    }


}
