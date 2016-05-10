package com.yun.radarview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by dell on 2016/5/6.
 */
public class RadarView extends View {
    private int radius = 0;
    private Paint paint;
    private ValueAnimator valueAnimator;
    private boolean isDraw = true;
    private String lock = "0";

    public RadarView(Context context) {
        super(context);
        init();
    }

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        valueAnimator = ValueAnimator.ofFloat(0, 360);
        valueAnimator.setDuration(4000);
        valueAnimator.setRepeatCount(Integer.MAX_VALUE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                synchronized (this) {
                    lock = animation.getAnimatedValue().toString();
                    invalidate();
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getContext().getResources().getColor(R.color.round_color));
        if (radius == 0) {
            radius = (getWidth() > getHeight() ? getHeight() : getWidth()) / 2;
        }
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius / 3 * 2, paint);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius / 3, paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getContext().getResources().getColor(R.color.radar));
        paint.setAlpha(180);
        synchronized (lock) {
            RectF rectF = new RectF(getWidth() / 2 - radius, getHeight() / 2 - radius, getWidth() / 2 + radius, getHeight() / 2 + radius);
            float startAngle = Float.valueOf(lock);
            for (int i = 1; i < 5; i++) {
                float angle = startAngle + i * 10;
                if (angle + 10 >= 360) {
                    angle = angle - 350;
                }
                Log.v("angle", angle + "");
                canvas.drawArc(rectF, angle, angle + 10, true, paint);
            }

        }

    }

    public void start() {
        valueAnimator.start();
    }
}
