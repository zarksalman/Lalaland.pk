package com.lalaland.ecommerce.views.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import kotlin.jvm.internal.Intrinsics;

public final class CustomScrollView extends ScrollView {
    private final GestureDetector mGestureDetector;

    public boolean onInterceptTouchEvent(@Nullable MotionEvent ev) {
        GestureDetector var10000 = this.mGestureDetector;
        if (var10000 == null) {
            Intrinsics.throwNpe();
        }

        return var10000.onTouchEvent(ev) ? true : super.onInterceptTouchEvent(ev);
    }

    public CustomScrollView(@NotNull Context context, @NotNull AttributeSet attrs) {
        super(context, attrs);
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(attrs, "attrs");
        this.mGestureDetector = new GestureDetector(context, new CustomScrollView.YScrollDetector());
        this.setFadingEdgeLength(0);
    }

    public final class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        public boolean onScroll(@NotNull MotionEvent e1, @NotNull MotionEvent e2, float distanceX, float distanceY) {
            Intrinsics.checkParameterIsNotNull(e1, "e1");
            Intrinsics.checkParameterIsNotNull(e2, "e2");
            return Math.abs(distanceY) > Math.abs(distanceX);
        }
    }
}