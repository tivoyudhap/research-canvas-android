package id.kivo.researchpath;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CanvasView extends View {

    private static final float TOLERANCE = 5;
    public int width;
    public int height;
    Context context;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;
    private float mX, mY;
    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private OnFinishDraw listener;
    private ArrayList<PathPointModel> list = new ArrayList<>();

    public CanvasView(Context c) {
        super(c);
        context = c;

        // we set a new Path
        mPath = new Path();

        // and we set a new Paint with the desired attributes
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);
    }

    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // your Canvas will draw onto the defined Bitmap
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw the mPath with the mPaint on the canvas when onDraw
        canvas.drawPath(mPath, mPaint);
        mCanvas.drawPath(mPath, mPaint);
    }

    // when ACTION_DOWN start touch according to the x,y values
    private void startTouch(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;

        list.add(new PathPointModel(x, y));
    }

    // when ACTION_MOVE move touch according to the x,y values
    private void moveTouch(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
            list.add(new PathPointModel(x, y));
        }
    }

    public void setDrawing(List<PathPointModel> list) {
        for (int a = 1; a < list.size(); a++) {
            PathPointModel modelBefore = list.get(a - 1);
            PathPointModel modelAfter = list.get(a);

            if (a == 1) {
                mPath.moveTo(modelBefore.getX(), modelBefore.getY());
            } else if (a == list.size() - 1) {
                mPath.lineTo(modelBefore.getX(), modelBefore.getY());
            } else {
                mPath.quadTo(modelBefore.getX(), modelBefore.getY(), (modelAfter.getX() + modelBefore.getX()) / 2, (modelAfter.getY() + modelBefore.getY()) / 2);
            }
        }
    }

    public byte[] getByteArray() {
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void clearCanvas() {
        mPath.reset();
        invalidate();
    }

    public ArrayList<PathPointModel> getList() {
        return list;
    }

    public void setListener(OnFinishDraw listener) {
        this.listener = listener;
    }

    public String getPath() {
        return Base64.encodeToString(getByteArray(), Base64.DEFAULT);
    }

    // when ACTION_UP stop touch
    private void upTouch() {
        mPath.lineTo(mX, mY);
        listener.drawing(mBitmap);
    }

    //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;
        }
        return true;
    }
}

