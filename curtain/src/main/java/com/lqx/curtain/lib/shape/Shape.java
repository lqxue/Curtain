package com.lqx.curtain.lib.shape;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.lqx.curtain.lib.HollowInfo;

public interface Shape {

    /**
     * draw any shape you want
     */
    void drawShape(Canvas canvas, Paint paint, HollowInfo info);

}
