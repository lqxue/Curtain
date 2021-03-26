package com.lqx.curtain.lib;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 * @author cd5160866
 */
public class GuideViewGroup implements IGuide {

    private static final int MAX_CHILD_COUNT = 2;

    private static final int GUIDE_ID = 0x3;

    private FrameLayout contentView;

    private CurtainInflate.CallBack callBack;

    private View topView = null;

    private GuideView guideView;
    private View contentParent;

    public void show() {
        FragmentActivity activity = (FragmentActivity) guideView.getContext();
        guideView.setId(GUIDE_ID);
        this.contentView = new FrameLayout(activity);
        this.contentView.addView(guideView);
        if (topView != null) {
            updateTopView();
        }
        contentParent = activity.getWindow().getDecorView();
        if (contentParent instanceof FrameLayout){
            ((FrameLayout) contentParent).removeView(contentView);
            ((FrameLayout) contentParent).addView(contentView);
        }
        if (null != callBack) {
            callBack.onShow(this);
        }
    }

    public void setCallBack(CurtainInflate.CallBack callBack) {
        this.callBack = callBack;
    }

    public void setTopView(View topView) {
        this.topView = topView;
    }

    public void setGuideView(GuideView guideView) {
        this.guideView = guideView;
    }

    void updateContent() {
        contentView.removeAllViews();
        contentView.addView(guideView);
        updateTopView();
    }

    @Override
    public void updateHollows(HollowInfo... hollows) {
        GuideView guideView = contentView.findViewById(GUIDE_ID);
        if (null != guideView) {
            guideView.setHollowInfo(hollows);
        }
    }

    @Override
    public void updateTopView(View topView) {
        if (null == contentView) {
            return;
        }
        setTopView(topView);
        updateTopView();
    }

    @Override
    public <T extends View> T findViewByIdInTopView(int id) {
        if (null == contentView) {
            return null;
        }
        return contentView.findViewById(id);
    }

    @Override
    public void dismissGuide() {
        if (contentParent instanceof FrameLayout){
            ((FrameLayout) contentParent).removeView(contentView);
        }
        if (null != callBack) {
            callBack.onDismiss(this);
        }
    }

    private void updateTopView() {
        if (contentView.getChildCount() == MAX_CHILD_COUNT) {
            contentView.removeViewAt(1);
        }
        contentView.addView(topView);
    }
}
