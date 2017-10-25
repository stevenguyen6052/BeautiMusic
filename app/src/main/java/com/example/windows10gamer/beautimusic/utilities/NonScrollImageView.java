package com.example.windows10gamer.beautimusic.utilities;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

public class NonScrollImageView extends android.support.v7.widget.AppCompatImageView {
    public NonScrollImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean requestRectangleOnScreen(Rect rectangle, boolean immediate) {
        return false;
    }
}
