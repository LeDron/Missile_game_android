package com.example.alticast.test;

import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by alticast on 17. 3. 6.
 */
public class Game_Activity extends AppCompatActivity {


    private int width;
    private int height;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    GonSurfaceView gameView;
    private ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);


      setContentView(R.layout.game_main);

        gameView = (GonSurfaceView)findViewById(R.id.gameView);
        gameView.getThread().setHandler(handler);
        list = (ListView)findViewById(R.id.listview);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
        list.setAdapter(adapter);


        adapter.notifyDataSetChanged();


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String word =adapter.getItem(position);
                World w =gameView.getThread().getPlanet();
                Vector<Line_Opponents> oppo =w.getOppo();

                for(int i=0;i<oppo.size();i++)
                {
                    if(oppo.get(i).getWord() == word)
                    {
                        gameView.onTouchEvent( MotionEvent.obtain(SystemClock.uptimeMillis(),
                                SystemClock.uptimeMillis(),
                                MotionEvent.ACTION_DOWN,
                                (float)oppo.get(i).getXCoor(),
                                (float)oppo.get(i).getYCoor(),
                                0 ) );
                        System.out.println(11111111);
                        //w.addMissiles(gameView.getThread().getPlanetSurfaceSize(), (int) oppo.get(i).getXCoor(), (int) oppo.get(i).getYCoor());
                    }
                }
            }
        });
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

     Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){

            if(msg.what==0)
            {
                listItems.clear();
                gameView.listupdate();
                if(gameView.getWordList() !=null) {
                    for (int i = 0; i < gameView.getWordList().size(); i++) {

                        listItems.add(gameView.getWordList().get(i));
                    }

                    adapter.notifyDataSetChanged();
                }
            }

        }
    };



}
