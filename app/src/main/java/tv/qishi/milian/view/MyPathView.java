package tv.qishi.milian.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

/**
 *
 * Created by hxj on 2017/11/3.
 */

public class MyPathView extends View {
    private Paint paint;
    private Path path;
    private Paint paintPoint;
    private int width;
    private int height;

    private int count = 0;
    private int size = 0;

    private boolean isAdd=true;

    public MyPathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(30);
        paint.setStyle(Paint.Style.STROKE);



        paintPoint = new Paint();
        paintPoint.setStrokeWidth(10);
        paintPoint.setColor(Color.GREEN);
        paintPoint.setStyle(Paint.Style.FILL);

        path = new Path();

        @SuppressLint("HandlerLeak")
        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x23:
                        count += 5;
                        if (count >= 80) {
                            count = 0;
                        }
                        if (isAdd) {
                            size++;
                            if (size > 10) {
                                isAdd = false;
                            }
                        } else {
                            size--;
                            if (size <= -10) {
                                isAdd = true;
                            }
                        }
                        invalidate();
                        sendEmptyMessageDelayed(0x23, 100);
                        break;
                }


            }
        };
        mHandler.sendEmptyMessageDelayed(0x23, 1000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.reset();

        path.moveTo(count, 100);
        for (int i = 0; i < 10; i++) {
            path.rQuadTo(20, 20, 40, 0);
            path.rQuadTo(20, -20, 40, 0);

        }

        canvas.drawPath(path, paint);
        canvas.drawRect(200, 0, 400, 200, paint);

    }


    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}