package com.lqx.curtain.lib;

import android.view.View;

import androidx.annotation.IdRes;

/**
 * @author cd5160866
 */
public interface IGuide {

    void updateHollows(HollowInfo... hollows);

    void updateTopView(View view);

    <T extends View> T findViewByIdInTopView(@IdRes int id);

    void dismissGuide();

}
