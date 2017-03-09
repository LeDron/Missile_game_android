package com.example.alticast.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Vector;

public class GonSurfaceThread extends Thread {
    private SurfaceHolder myThreadSurfaceHolder;
    private boolean myThreadRun = false;



    private ArrayList<String> wordList=null;
    private int Width, Height;
    private double HealthCons=1;
    private Paint redPaint;
    private Paint bluePaint;
    private Paint GrayPaint;
    private Paint BlackPaint;
    private Paint GreenPaint;
    private World planet;
    private String Score;
    private int HealthBarSize;
    private int exW;
    private int exH;
    private ArrayList<String> word_data;
    private Handler mHandler;

    InputStream inputStream ;


    //img variable in game
    private Bitmap explosionImg;
    private Bitmap[] explosionSplit;
    private Bitmap backGroundImg;
    private Bitmap missileDishImgR;
    private Bitmap planetSurface;
    private int planetSurfaceSize ;
    private Bitmap healthBar;
    private Bitmap missile;
    private Bitmap missileDishImgL;
    private Bitmap wordMissile;

    boolean playGame = true;

    boolean imageSet = true;

    public GonSurfaceThread(SurfaceHolder surfaceHolder, GonSurfaceView surfaceView) {
        myThreadSurfaceHolder = surfaceHolder;

        String data =null;
        inputStream = surfaceView.getResources().openRawResource(R.raw.word_data);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int x;
        try {
            x = inputStream.read();
            while(x !=-1){
                byteArrayOutputStream.write(x);
                x = inputStream.read();
            }

            data = new String(byteArrayOutputStream.toByteArray(),"MS949");
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] data2 = data.split("\n");
        word_data = new ArrayList<>();
        for(int c=0;c<data2.length;c++)
        {
            if(data2[c] != " "){
                word_data.add(data2[c]);
            }

        }
        missileDishImgR = BitmapFactory.decodeResource(surfaceView.getResources(), R.drawable.missile_dish);
        backGroundImg = BitmapFactory.decodeResource(surfaceView.getResources(), R.drawable.nightsky);
        explosionImg = BitmapFactory.decodeResource(surfaceView.getResources(), R.drawable.explosion_sprite);
        planetSurface = BitmapFactory.decodeResource(surfaceView.getResources(), R.drawable.planet_surface101);
        healthBar = BitmapFactory.decodeResource(surfaceView.getResources(),R.drawable.health_bar);
        missile = BitmapFactory.decodeResource(surfaceView.getResources(),R.drawable.missilesammo101);


        explosionSplit = new Bitmap[12];

        exW=explosionImg.getWidth()/3 -90;
        exH=explosionImg.getHeight()/4-30;

        for(int i=0;i<12;i++)
        {
            int k=i%3;
            int j=(int) i/3;
            int a=0;
            if(i>=9)
            {
                a=30;
            }
            explosionSplit[i]=Bitmap.createBitmap(explosionImg,k*explosionImg.getWidth()/3+90,j*exH,exW,exH+a);
            explosionSplit[i]=Bitmap.createScaledBitmap(explosionSplit[i],explosionSplit[i].getWidth()/2,explosionSplit[i].getHeight()/2,true);
        }



    }
    public World getPlanet(){return planet;}
    public int getPlanetSurfaceSize(){return planetSurfaceSize;}
    public void setRunning(boolean b) {
        myThreadRun = b;
    }

    @Override
    public void run() {
        while(myThreadRun){
            Canvas c = null;

            try{
                c = myThreadSurfaceHolder.lockCanvas(null);
                synchronized (myThreadSurfaceHolder){
                    //myThreadSurfaceView.draw(c);
                    if(imageSet)
                    {
                        Width = c.getWidth();
                        Height=c.getHeight();

                        planet = new World(Width, Height,word_data);

                        GreenPaint = new Paint();
                        redPaint = new Paint();
                        bluePaint = new Paint();
                        GrayPaint = new Paint();
                        BlackPaint = new Paint();
                        BlackPaint.setColor(Color.BLACK);
                        GrayPaint.setColor(Color.DKGRAY);
                        redPaint.setColor(Color.RED);
                        bluePaint.setColor(Color.BLUE);
                        GreenPaint.setColor(Color.GREEN);
                        redPaint.setStrokeWidth(20);
                        bluePaint.setStrokeWidth(20);
                        GrayPaint.setStrokeWidth(80);
                        BlackPaint.setStrokeWidth(80);
                        GreenPaint.setTextSize(Width/10);

                        planetSurfaceSize=(int)(Height/5.5);
                        Score ="Score: ";
                        HealthBarSize = Height/24;
                        planetSurface = Bitmap.createScaledBitmap(planetSurface,Width , planetSurfaceSize, true);
                        healthBar = Bitmap.createScaledBitmap(healthBar,Width,HealthBarSize,true);
                        missile = Bitmap.createScaledBitmap(missile,Width/30,HealthBarSize,true);
                        missileDishImgR = Bitmap.createScaledBitmap(missileDishImgR,2*HealthBarSize,2*HealthBarSize,true);



                        Matrix sideInversion = new Matrix();
                        sideInversion.setScale(-1, 1);
                        missileDishImgL = Bitmap.createBitmap(missileDishImgR, 0, 0,
                                missileDishImgR.getWidth(), missileDishImgR.getHeight(), sideInversion, false);

                        imageSet=false;
                    }
                    calc();

                    c = Draw(c);
                    Vector<Line_Opponents> tempVector=planet.getOppo();
                    ArrayList<String> tempList = new ArrayList<>();

                    for(int i=0;i<tempVector.size();i++)
                    {

                        tempList.add(tempVector.get(i).getWord());
                    }


                    ArrayList<String> old = wordList;

                    setwordList(tempList);
                    if(wordList !=null) {
                        int tempNum =0;
                        for (int i = 0; i < wordList.size(); i++) {
                            if(old ==null)
                            {
                                tempNum ++;
                            }
                            else if (wordList.get(i) != old.get(i))
                            {
                               tempNum ++;
                            }
                        }
                        if(tempNum>0)
                        {
                            mHandler.sendEmptyMessage(0);
                        }
                    }
                }
            }
            finally{
                if (c != null) {
                    myThreadSurfaceHolder.unlockCanvasAndPost(c);

                }

            }
        }
    }
    public void setHandler(Handler h)
    {
        mHandler=h;
    }

    public void setwordList(ArrayList<String> temp)
    {
       wordList=temp;
    }

    public ArrayList<String> getWordList(){

        return wordList;}
    private void calc ()
    {
        // TODO Auto-generated method stub

        if(playGame==true){//if the boolean controlling the playing of the game is true then

            setAllFalse();//calls the method which sets all of the booleans in the game to false
            playGame=true;//sets playGame to true again so that the game can continue

            if(planet.getGameWon()==true){//if a level has been completed then
                setAllFalse();//sets all booleans false
                if(planet.getLevel()!=5){//if the game is on any other level besides 5 then
                    //       levelCompleteScreen=true;//call on the screen meant for all of the other levels besides 5
                }
                else if(planet.getLevel()==5){//if the game is on level 5 then
                    //       levelCompleteScreenSettings=true;///call on the screen meant for level 5
                }
            }

            //calls the "updateScreenSize" method from the planet class
            //planet.updateScreenSize(getWidth(), getHeight());

            //calls the "updateBackGround" method from the planet class
            planet.updateBackGround();

            //decides whether the "missileDishLightingSpritesIndex" should be updated or reset to zero
/*            if (missileDishLightingTimer == 0){

                missileDishLightingTimer = missileDishLightingTimerTime;

                if (missileDishLightingSpritesIndex < (missileDishLightingSprites.length - 1))
                    missileDishLightingSpritesIndex++;
                else
                    missileDishLightingSpritesIndex = 0;
            }
            else if (missileDishLightingTimer > 0)
                missileDishLightingTimer--;
*/


            //calls the "updateMissiles" method from the planet class
            planet.updateMissiles();
            //calls the "updateExplosions" method from the planet class
            planet.updateExplosions();

            if(!planet.getDied()){
                planet.updateEnemyMissiles(planetSurfaceSize);
            }
            //resets the variables if the player had died
            else{
                setAllFalse();
                //drawGameOverScreen=true;
                //planet.setDied(false);

                //calls the "updateScore" method
                //          updateScore();

                //calls the "reset" method from the planet class
                planet.reset(planet.getLevel());
            }
            //calls the "updateHealth" method from the planet class
            HealthCons=planet.updateHealth();
            //calls the "collisions" method from the planet class
            planet.collisions();
            planet.updateScore();

            Score = "Score: " + planet.getShownScore();

        }
        ////////////////////////////////////////////////////////////////////////////
    }
    private void setAllFalse(){//method that sets all of the booleans inhttps://github.com/KamDongYun/Missile_game_android.git the game to false, so as to avoid boolean confusion

    }

    private Canvas Draw(Canvas canvas)
    {
        canvas.drawBitmap(backGroundImg, (int) planet.getXBackGround(), (int) planet.getYBackGround()+planetSurface.getHeight(), null);

        canvas.drawBitmap(planetSurface, 0, Height - planetSurfaceSize, null);


        for(int i=0; i<planet.getMissiles().size();i++)
        {
            Line_Missiles tempMissile = planet.getMissiles().get(i);

            canvas.drawLine(Width/2,Height-planetSurfaceSize,(int)tempMissile.getXCurrent(),(int)tempMissile.getYCurrent(),bluePaint);
        }




        for (int i = 0 ; i < planet.getExplosions().size () ; i++)
        {
            Explosions tempExplosions = (Explosions) planet.getExplosions().get (i);

            int num = planet.getExplosionUpdateCount().get(i);
            int explosionSpritesIndex = 0;

            for (int j = (planet.getExplosionsSpeed()/explosionSplit.length); j<=planet.getExplosionsSpeed(); j += (planet.getExplosionsSpeed()/explosionSplit.length)) {
                if (num <= j) {

                    canvas.drawBitmap(explosionSplit[explosionSpritesIndex], (int) tempExplosions.getXCurrent() - explosionSplit[explosionSpritesIndex].getWidth() / 2 + 90, (int) tempExplosions.getYCurrent() - explosionSplit[explosionSpritesIndex].getHeight() / 2, null);

                }
                else if (explosionSpritesIndex < (explosionSplit.length - 1))
                {explosionSpritesIndex++;}

            }
        }
        for(int x=0;x<planet.getOppo().size();x++) {//for loop runs through the line opponents vector and draws every line in the vector
            Line_Opponents temp = (Line_Opponents) planet.getOppo().get(x);
            //canvas.drawLine((int) temp.getStartXCoor(), (int) temp.getStartYCoor(), (int) temp.getXCoor(), (int) temp.getYCoor(), redPaint);
            // canvas.drawBitmap(wordMissile,(int)(temp.getXCoor()-wordMissile.getWidth()/2),(int)(temp.getYCoor()-wordMissile.getHeight()/2),null);

            canvas.drawText(temp.getWord(),(int)temp.getXCoor(),(int)temp.getYCoor(),GreenPaint);

        }



        canvas.drawBitmap(healthBar,0,Height-healthBar.getHeight(),null);
        canvas.drawLine(Width,Height-healthBar.getHeight()/2,(int)(healthBar.getWidth()*HealthCons),Height-healthBar.getHeight()/2,GrayPaint);
        canvas.drawLine(0,Height-healthBar.getHeight()-HealthBarSize/2,Width,Height-healthBar.getHeight()-HealthBarSize/2,BlackPaint);




        for(int i=0; i<planet.getCurrNumOfMissiles();i++)
        {
            canvas.drawBitmap(missile,i*missile.getWidth(),Height-healthBar.getHeight()-HealthBarSize,null);
        }




        canvas.drawBitmap(missileDishImgR,Width/2 ,Height-healthBar.getHeight()-HealthBarSize-missileDishImgR.getHeight(),null);
        canvas.drawBitmap(missileDishImgL,Width/2 -missileDishImgR.getWidth(),Height-healthBar.getHeight()-HealthBarSize-missileDishImgR.getHeight(),null);

        canvas.drawText(Score,0,Width/10,GreenPaint);
        return canvas;
    }
}