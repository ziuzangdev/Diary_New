package com.project.diary.Model.Painting;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DrawView extends View {

    public int width;
    public  int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint   mBitmapPaint;

    private Paint drawPaint;
    Context context;
    private Paint circlePaint;
    private Path circlePath;
    Boolean eraserOn = false;
    Boolean newAdded = false;
    Boolean allClear = false;
    private Path drawPath;

    private ArrayList<Bitmap> bitmap = new ArrayList<>();
    private ArrayList<Bitmap> undoBitmap = new ArrayList<>();

    public DrawView(Context c) {
        super(c);
        init(context);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context c){
        context=c;
        drawPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        circlePaint = new Paint();
        circlePath = new Path();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(4f);
        drawPaint = new Paint();
        drawPaint.setAntiAlias(true);
        drawPaint.setDither(true);
        drawPaint.setColor(Color.BLACK);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        drawPaint.setStrokeWidth(20);
    }
    public void setDrawPaint(Paint drawPaint) {
        this.drawPaint = drawPaint;
    }

    public Paint getDrawPaint() {
        return drawPaint;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if(mBitmap==null) {
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);

        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(drawPath, drawPaint);
        canvas.drawPath( circlePath,  circlePaint);
    }

    public void onClickEraser(boolean isEraserOn)
    {
        if (isEraserOn) {
            eraserOn = true;
            drawPaint.setColor(getResources().getColor(android.R.color.transparent));
            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        }
        else {
            eraserOn = false;
            drawPaint.setColor(drawPaint.getColor());
            drawPaint.setXfermode(null);
        }
    }
    public Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }
    @WorkerThread
    public String saveDraw() {
        try {
            requestPermission();
            String resultPath;
            String path = getContext().getExternalCacheDir().getAbsolutePath().toString();
            OutputStream fOut = null;
            Integer counter = 0;
            File file = new File(path, String.valueOf(System.currentTimeMillis())+".jpeg");
            fOut = new FileOutputStream(file);
            Bitmap pictureBitmap = getBitmapFromView(DrawView.this);
            pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            resultPath = file.getAbsolutePath();
            fOut.flush();
            fOut.close();
            return resultPath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(((Activity)getContext()), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        while (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){}
    }
    public void onClickUndo () {
        if(newAdded) {
            bitmap.add(mBitmap.copy(mBitmap.getConfig(), mBitmap.isMutable()));
            newAdded=false;
        }
        if (bitmap.size()>1)
        {
            undoBitmap.add(bitmap.remove(bitmap.size()-1));
            mBitmap= bitmap.get(bitmap.size()-1).copy(mBitmap.getConfig(),mBitmap.isMutable());
            mCanvas = new Canvas(mBitmap);
            invalidate();
            if(bitmap.size()==1)
                allClear=true;
        }
        else
        {

        }
        //toast the user
    }

    public void onClickRedo (){
        if (undoBitmap.size()>0)
        {
            bitmap.add(undoBitmap.remove(undoBitmap.size()-1));
            mBitmap= bitmap.get(bitmap.size()-1).copy(mBitmap.getConfig(),mBitmap.isMutable());
            mCanvas = new Canvas(mBitmap);
            invalidate();
        }
        else
        {

        }
        //toast the user
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
            float touchX = event.getX();
            float touchY = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    newAdded = true;
                    if(!allClear)
                        bitmap.add(mBitmap.copy(mBitmap.getConfig(),mBitmap.isMutable()));
                    else allClear=false;

                    drawPath.moveTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (eraserOn) {
                        drawPath.lineTo(touchX, touchY);
                        mCanvas.drawPath(drawPath, drawPaint);
                        drawPath.reset();
                        drawPath.moveTo(touchX, touchY);
                    } else {
                        drawPath.lineTo(touchX, touchY);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    mCanvas.drawPath(drawPath, drawPaint);
                    drawPath.reset();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    return false;

                default:
                    return false;
            }

            invalidate();
            return true;
    }
}

