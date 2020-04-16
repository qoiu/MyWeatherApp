package com.geekbrains.myweather;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ThermometrView extends View {

    public ThermometrView(Context context) {
        super(context);
    }

    public ThermometrView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ThermometrView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ThermometrView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initAttr(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ThermometrView,
                0,0);
    }
}
