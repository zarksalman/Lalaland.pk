package com.lalaland.ecommerce.views.customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

import com.lalaland.ecommerce.R;

public class AViewFlipper extends ViewFlipper {

    Paint paint = new Paint();

    public AViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        float margin = 8;
        float radius = 16;
        float cx = width;
        float temp = (radius + margin) * 2 * getChildCount() / 2;

        // positioning horizontal
        cx = cx / 2.2f;

//        marginBottom
        float cy = height - 32;

        canvas.save();

        for (int i = 0; i < getChildCount(); i++) {
            if (i == getDisplayedChild()) {
                paint.setColor(getResources().getColor(R.color.colorAccent));
                canvas.drawCircle(cx, cy, radius, paint);

            } else {
                paint.setColor(getResources().getColor(R.color.colorMediumGray));
                canvas.drawCircle(cx, cy, radius, paint);
            }
            cx += 2 * (radius + margin);
        }
        canvas.restore();
    }
}