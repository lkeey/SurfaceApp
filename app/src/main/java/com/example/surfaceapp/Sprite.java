package com.example.surfaceapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceView;

public class Sprite {
    private final int SPEED_FACTOR = 50;

    SpriteSurfaceView surfaceView;
    Bitmap image;
    float x,y;
    Paint paint;
    int columns = 3, rows = 4;
    int currentFrame = 0, direction = 0;
    int frameWidth, frameHeight;
    float targetX, targetY, speedX = 0, speedY = 0;

    public Sprite(SpriteSurfaceView surfaceView,
                Bitmap bitmap,
                float x,
                float y) {
        this.surfaceView = surfaceView;
        this.image = bitmap;
        this.x = x;
        this.y = y;

        frameWidth = image.getWidth() / columns;
        frameHeight = image.getHeight() / rows;

    }

    public void draw(Canvas canvas) {
        x += speedX;
        y += speedY;

        checkWall();

        currentFrame = ++currentFrame % columns;

        int cutX = currentFrame * frameWidth;
        int cutY = currentFrame * frameHeight;

        Rect src = new Rect(cutX, cutY, cutX + frameWidth, cutY + frameHeight);
        Rect dst = new Rect((int) x, (int) y, (int) x + frameWidth, (int) y + frameHeight);

        canvas.drawBitmap(image, src, dst, paint);
    }

    public void setTarget(float targetX, float targetY) {
        this.targetX = targetX;
        this.targetY = targetY;

        speedX = (targetX - x) / surfaceView.getWidth() * SPEED_FACTOR;
        speedY = (targetY - y) / surfaceView.getWidth() * SPEED_FACTOR;

        if (speedX / speedY > 1) {
            direction = 3;
        } else {
            if (speedY / speedX < 1 && speedY / speedX > -1) {
                direction = 0;
            } else if (speedY / speedX < -1) {
                direction = 1;
            }
        }
    }

    private void checkWall() {
        if (x <= 0 || x + frameWidth >= surfaceView.getWidth()) {
            speedX = - speedX;
        }

        if (y <= 0 || y + frameHeight >= surfaceView.getHeight()) {
            speedY = - speedY;
        }
    }
}
