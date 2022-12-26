package com.example.surfaceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new SpriteSurfaceView(this));
    }
}

class SpriteSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private Resources resources;
    Bitmap bitmap, spriteBitmap;

    private SpriteSurfaceThread thread;

    float currentX = 0, currentY = 0;
    ArrayList<Sprite> sprites = new ArrayList<Sprite>();


    public SpriteSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
        resources = getResources();
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.sprites);
        setFocusable(true);

        sprites.add(0, new Sprite(this, bitmap, currentX, currentY));
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        thread = new SpriteSurfaceThread(this, getHolder());
        thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        thread.setRunning(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int j = 0;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float touchX = event.getX();
            float touchY = event.getY();

            for(j = 0; j < sprites.size(); j++){
                sprites.get(j).setTarget(touchX, touchY);
            }
            sprites.add(j, new Sprite(this, bitmap, touchX, touchY));
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawARGB(1,0, 0, 0);

        for(int j = 0; j < sprites.size(); j++){
            sprites.get(j).draw(canvas);
        }
    }
}

class SpriteSurfaceThread extends Thread {
    private final long DELAY = 30;
    private boolean running = true;

    private SpriteSurfaceView view;
    private SurfaceHolder holder;
    private long startTime = 0;

    public SpriteSurfaceThread (SpriteSurfaceView view, SurfaceHolder holder) {
        this.view = view;
        this.holder = holder;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        while (running) {
            Canvas canvas = null;

            long currentTime = System.currentTimeMillis();
            long deltaTime = currentTime - startTime;

            if (deltaTime > DELAY) {
                startTime = currentTime;
                canvas = holder.lockCanvas();

                synchronized (holder) {
                    this.view.draw(canvas);
                }

                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
