package com.example.windows10gamer.beautimusic.utilities;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Windows 10 Gamer on 21/10/2017.
 */

public class NonScrollImageView extends android.support.v7.widget.AppCompatImageView {
    public NonScrollImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean requestRectangleOnScreen(Rect rectangle, boolean immediate) {
        return false;
    }
}
