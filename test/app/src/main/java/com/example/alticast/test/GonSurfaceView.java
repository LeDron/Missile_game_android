package com.example.alticast.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.alticast.test.GonSurfaceThread;

import java.util.ArrayList;

public class GonSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private GonSurfaceThread thread;
    private ArrayList<String> wordList;

    public GonSurfaceView(Context context) {
        super(context);
        init();
    }

    public GonSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GonSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        getHolder().addCallback(this);
        thread = new GonSurfaceThread(getHolder(), this);

        setFocusable(true); // make sure we get key events
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

    }

    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }
    public GonSurfaceThread getThread(){return thread;}

    public void listupdate(){
        wordList=thread.getWordList();

    }
    public ArrayList<String> getWordList(){
        return wordList;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (getHolder()) {

            //add line_missile at list or vector
            thread.getPlanet().addMissiles(thread.getPlanetSurfaceSize(),(int)event.getX(),(int)event.getY());

            return true;
        }

    }
}