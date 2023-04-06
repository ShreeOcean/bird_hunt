package com.sandipbhattacharya.birdhunt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class GameView extends View {

    int birdX, birdY;
    int touchX, touchY;
    int screenWidth, screenHeight;
    int birdFrame = 0, birdDirectionX = 1, birdDirectionY = 1,
    birdSpeedX =20, birdSpeedY = 20, deadBirdDropSpeed = 3;
    int tempX=0, tempY = 0;
    int points = 0;
    Handler handler;
    Runnable runnable;
    Random random;
    long UPDATE_MILLIS = 30;
    Bitmap bg, birdDead, bullet;
    Bitmap[] bird = new Bitmap[6];
    boolean birdAlive = true;
    Rect rect;
    int dist = 0;
    boolean resetState = true;
    int bulletsRemaining = 20;
    MediaPlayer mpPoints;
    Context context;

    public GameView(Context context) {
        super(context);
        this.context = context;
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        birdDead = BitmapFactory.decodeResource(getResources(), R.drawable.bird_dead);
        bullet = BitmapFactory.decodeResource(getResources(), R.drawable.bullet);
        bird[0] = BitmapFactory.decodeResource(getResources(), R.drawable.bird1);
        bird[1] = BitmapFactory.decodeResource(getResources(), R.drawable.bird2);
        bird[2] = BitmapFactory.decodeResource(getResources(), R.drawable.bird3);
        bird[3] = BitmapFactory.decodeResource(getResources(), R.drawable.bird4);
        bird[4] = BitmapFactory.decodeResource(getResources(), R.drawable.bird5);
        bird[5] = BitmapFactory.decodeResource(getResources(), R.drawable.bird6);
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        handler = new Handler();
        random = new Random();
        birdX = random.nextInt(screenWidth);
        birdY = random.nextInt(screenHeight);
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        rect = new Rect(0, 0, screenWidth, screenHeight);
        mpPoints = MediaPlayer.create(context, R.raw.points);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bg, null, rect, null);
        for(int i=0; i< bulletsRemaining; i++){
            canvas.drawBitmap(bullet, i*bullet.getWidth(), 10, null);
        }
        if(birdAlive) {
            if (resetState) {
                tempX = birdX;
                tempY = birdY;
                resetState = false;
            }
            birdX = birdX + birdSpeedX * birdDirectionX;
            birdY = birdY + birdSpeedY * birdDirectionY;
            dist = 100 + random.nextInt(600);
            if (Math.abs(birdX - tempX) >= dist || Math.abs(birdY - tempY) >= dist) {
                if (birdDirectionX > 0) {
                    if (Math.random() < 0.5)
                        birdDirectionX = -1;
                } else {
                    if (Math.random() < 0.5)
                        birdDirectionX = 1;
                }
                if (birdDirectionY > 0) {
                    if (Math.random() < 0.5)
                        birdDirectionY = -1;
                } else {
                    if (Math.random() < 0.5)
                        birdDirectionY = 1;
                }
                resetBirdSpeed();
                resetState = true;
            }
            if (birdX >= screenWidth - bird[0].getWidth()) {
                birdDirectionX = -1;
                resetBirdSpeed();
            }
            if (birdX <= 0) {
                birdDirectionX = 1;
                resetBirdSpeed();
            }
            if (birdY <= 0) {
                birdDirectionY = 1;
                resetBirdSpeed();
            }
            if (birdY >= screenHeight - bird[0].getHeight()) {
                birdDirectionY = -1;
                resetBirdSpeed();
            }
            birdFrame++;
            if (birdFrame > 5) {
                birdFrame = 0;
            }
            canvas.drawBitmap(bird[birdFrame], birdX, birdY, null);
        }else
        {
            birdY += deadBirdDropSpeed;
            deadBirdDropSpeed *= 1.5;
            canvas.drawBitmap(birdDead, birdX, birdY, null);
            if(birdY >= screenHeight){
                birdAlive = true;
                deadBirdDropSpeed = 3;
                birdX = random.nextInt(screenWidth);
                birdY = random.nextInt(screenHeight);
            }
        }
        handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchX = (int)event.getX();
        touchY = (int)event.getY();
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN){
            checkCollision(touchX, touchY);
            bulletsRemaining--;
            if(bulletsRemaining < 1){
                Intent intent = new Intent(getContext(), GameOver.class);
                intent.putExtra("points", points);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        }
        return true;
    }

    private void checkCollision(int touchX, int touchY) {
        if(touchX >= birdX
                && touchX <= (birdX + bird[0].getWidth())
                && touchY >= birdY
                && touchY<= (birdY + bird[0].getHeight())){
            points++;
            birdAlive = false;
            if(mpPoints != null){
                mpPoints.start();
            }
        }
    }

    private void resetBirdSpeed() {
        birdSpeedX = 10 + random.nextInt(20);
        birdSpeedY = 10 + random.nextInt(20);
    }
}
