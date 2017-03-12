package com.shtainyky.converterlab.activities.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class DrawCanvas extends View {
    Paint paint;
    Paint fontPaint;
    public DrawCanvas(Context context) {
        super(context);
        paint = new Paint();
        fontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public DrawCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        fontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        paint.setTextSize(50);
        paint.setColor(Color.RED);
        canvas.drawText("Bank", 100, 100, paint);
//        super.onDraw(canvas);
//        Paint pBackground = new Paint();
//        pBackground.setColor(Color.WHITE);
//        canvas.drawRect(0, 0, 512, 512, pBackground);
//        Paint pText = new Paint();
//        pText.setColor(Color.BLACK);
//        pText.setTextSize(20);
//        canvas.drawText("Sample Text", 100, 100, pText);


    }

}