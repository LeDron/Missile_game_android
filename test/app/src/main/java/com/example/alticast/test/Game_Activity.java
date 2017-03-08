package com.example.alticast.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by alticast on 17. 3. 6.
 */
public class Game_Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(new GameView(this));
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
    @Override
    protected void onDestroy()
    {
        finish();
        super.onDestroy();
    }
}
