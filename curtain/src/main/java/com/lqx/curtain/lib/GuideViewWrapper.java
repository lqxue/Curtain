package com.lqx.curtain.lib;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentActivity;

/**
 *
 * @author lqx
 */
public class GuideViewWrapper implements IGuide {

    private static final int MAX_CHILD_COUNT = 2;

    private static final int GUIDE_ID = 0x3;

    private FrameLayout contentView;

    private CurtainInflate.CallBack callBack;

    private View topView = null;

    private GuideView guideView;

    private View decorView;

    public void show() {
        FragmentActivity activity = (FragmentActivity) guideView.getContext();
        guideView.setId(GUIDE_ID);
        this.contentView = new FrameLayout(activity);
        this.contentView.addView(guideView);
        if (topView != null) {
            updateTopView();
        }
        contentView.setPadding(0, getStatusBarHeight(activity),0,0);
        decorView = activity.getWindow().getDecorView();
        if (decorView instanceof FrameLayout){
            ((FrameLayout) decorView).removeView(contentView);
            ((FrameLayout) decorView).addView(contentView);
        }
        if (null != callBack) {
            callBack.onShow(this);
        }
    }

    /**
     * 获取状态栏的高度
     */
    private int getStatusBarHeight(Context context){
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
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
        if (decorView instanceof FrameLayout){
            ((FrameLayout) decorView).removeView(contentView);
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
